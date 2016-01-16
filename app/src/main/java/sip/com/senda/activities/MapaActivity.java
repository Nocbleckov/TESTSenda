package sip.com.senda.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.kml.KmlLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sip.com.senda.R;
import sip.com.senda.fragments.FragmentMisRutas;
import sip.com.senda.fragments.FragmentRutaEditable;
import sip.com.senda.listener.ListenerMarkers;
import sip.com.senda.objetos.Conexion;
import sip.com.senda.objetos.Punto;
import sip.com.senda.objetos.Ruta;
import sip.com.senda.objetos.Stuff;
import sip.com.senda.objetos.Usuario;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public SupportMapFragment mapFragment;
    private boolean estado = false;
    private Usuario usuario;
    private ListenerMarkers listenerMarkers;
    private boolean visible = false;
    private ArrayList<Punto> puntosMapa;
    private ArrayList<Marker> marcas;
    private ArrayList<Ruta> misRutas;
    FragmentRutaEditable fragmentRutaEditable;
    FragmentMisRutas fragmentMisRutas;
    PolylineOptions pol;
    Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        LayoutInflater inf = LayoutInflater.from(this);
        usuario = (Usuario) getIntent().getExtras().get("usuario");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //
        fragmentRutaEditable = new FragmentRutaEditable();
        fragmentMisRutas = new FragmentMisRutas();
        //

        View cstAction = inf.inflate(R.layout.csmactionbar_layout, null);
        ImageButton ib = (ImageButton) cstAction.findViewById(R.id.iconoMenu);

        marcas = new ArrayList<>();

        android.support.v7.app.ActionBar a = getSupportActionBar();
        if (a != null) {
            a.setTitle("");
            a.setCustomView(cstAction);
            a.setDisplayShowCustomEnabled(true);
        }

        startDrawer(this, this);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }

    public void setPoli(PolylineOptions pol, Polyline polyline) {
        this.pol = pol;
        this.polyline = polyline;
    }

    public void cambiarColorMarcas() {
        for (int i = 0; i < marcas.size(); i++) {
            marcas.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void setPuntosMapa(ArrayList<Punto> puntosMapa) {
        this.puntosMapa = puntosMapa;
    }

    public void pintarRuta(String cadenaCode) {
        pol = null;
        if (polyline != null) {
            polyline.remove();
        }
        List<LatLng> puntos = PolyUtil.decode(cadenaCode);
        pol = new PolylineOptions();
        pol.addAll(puntos);
        pol.width(8);
        pol.color(Color.argb(150, 80, 190, 160));
    }

    public void cambiarListener() {
        listenerMarkers = new ListenerMarkers(getBaseContext(), usuario, this);
        listenerMarkers.setPuntos(this.puntosMapa);
        mMap.setOnMarkerClickListener(listenerMarkers);
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void startDrawer(final Context context, final MapaActivity mapaActivity) {
        navigationView = (NavigationView) findViewById(R.id.navigationView_Frag);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.misRutas:
                        //Toast.makeText(context, "Mis Puntos", Toast.LENGTH_SHORT).show();
                        /*navigationView.inflateMenu(R.menu.menu_rutas);
                        mMap.clear();
                        eliminarFragment();
                        listenerMarkers = new ListenerMarkers(getBaseContext(), usuario, mapaActivity);
                        new OnBackBuscarPuntos().execute();*/
                        iniMisRutas(fragmentMisRutas.getVisible());
                        //item.setChecked(false);
                        break;
                    case R.id.miInfo:
                        //Toast.makeText(context, "Mi Info", Toast.LENGTH_SHORT).show();
                        eliminarFragment();
                        break;
                    case R.id.editarRuta:
                        //editadoRuta(!item.isChecked());
                        editadoRuta(fragmentRutaEditable.getVisible());
                        break;
                    case R.id.guardarMapas:

                        Intent i =  new Intent(MapaActivity.this,RegionDwl.class);
                        startActivity(i);

                        break;
                }

                return false;
            }
        });

        navigationView.getHeaderCount();
        View header = navigationView.getHeaderView(0);

        TextView nombreUsuarioTex = (TextView) header.findViewById(R.id.nombreUsuario);
        TextView correoUsuarioTex = (TextView) header.findViewById(R.id.correoUsuario);
        CircleImageView imgenUsu = (CircleImageView) header.findViewById(R.id.imagenPerfil);

        imgenUsu.setImageBitmap(usuario.getfoto(imgenUsu));
        nombreUsuarioTex.setText(usuario.getNombre());
        correoUsuarioTex.setText(usuario.getEstado());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                estado = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                estado = false;
            }
        };

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void buscarPuntos(Ruta ruta){
        listenerMarkers = new ListenerMarkers(getBaseContext(), usuario, this);
        new OnBackBuscarPuntos(ruta).execute();
    }

    public void colocarMarca(Punto punto, Float color) {
        //MarkerOptions markTemp = new MarkerOptions();
        //markTemp.position(punto.getCoordenada());
        //markTemp.title(punto.getDireccion());
        //markTemp.icon(BitmapDescriptorFactory.defaultMarker(color));
        //mMap.addMarker(markTemp);
        Marker marcaTemp = mMap.addMarker(new MarkerOptions().position(punto.getCoordenada()).title(punto.getDireccion()).icon(BitmapDescriptorFactory.defaultMarker(color)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto.getCoordenada()));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        marcas.add(marcaTemp);
    }

    public void eliminarFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentRutaEditable);
        fragmentTransaction.remove(fragmentMisRutas);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        fragmentRutaEditable.setVisible(false);
        fragmentMisRutas.setVisible(false);

        visible = false;
    }

    public void editadoRuta(boolean a) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!a) {
            fragmentRutaEditable.setmMap(this.mMap, polyline, pol, this, puntosMapa,fragmentMisRutas);
            fragmentTransaction.replace(R.id.frame, fragmentRutaEditable);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            fragmentTransaction.remove(fragmentRutaEditable);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            visible = false;
            fragmentRutaEditable.setVisible(false);
        }
        fragmentTransaction.commit();
    }

    public void iniMisRutas(boolean a) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!a) {
            fragmentMisRutas.iniFrag(this.mMap, this, fragmentRutaEditable,usuario);
            fragmentTransaction.replace(R.id.frame_misRutas, fragmentMisRutas);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            fragmentTransaction.remove(fragmentMisRutas);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentMisRutas.setVisible(false);
        }
        fragmentTransaction.commit();
    }

    public class OnBackBuscarPuntos extends AsyncTask<String, Integer, ArrayList<Punto>> {

        private Conexion con;
        private Ruta ruta;

        public OnBackBuscarPuntos(Ruta ruta){
            marcas.clear();
            this.ruta = ruta;
        }

        @Override
        protected ArrayList<Punto> doInBackground(String... params) {
            ArrayList<Punto> puntos;
            HashMap<String, String> parametros = new HashMap<String, String>();

            if (usuario.getIdPerfil().equals("1")) {
                parametros.put("numPeticion", "2");
            } else {
                parametros.put("numPeticion", "3");
                parametros.put("idRuta",ruta.getId());
                new onBackPuntosBrigada().execute();
            }
            try {

                con = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                con.setParametros(parametros);
                con.executar(Conexion.metodoPeticion.POST);
                String respuesta = con.getRespuesta();
                if (Stuff.existe(respuesta)) {
                    puntos = Stuff.obtenerPuntos(respuesta);
                    return puntos;
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Punto> puntos) {
            super.onPostExecute(puntos);

            if (puntos != null) {
                listenerMarkers.agregarPuntos(puntos);
                new OnBackColocarPuntos(puntos, BitmapDescriptorFactory.HUE_AZURE).execute();
            }

        }
    }


    public class onBackPuntosBrigada extends AsyncTask<String, Integer, ArrayList<Punto>> {

        private Conexion con;

        @Override
        protected ArrayList<Punto> doInBackground(String... params) {
            ArrayList<Punto> puntosBrigada;
            HashMap<String, String> parametros = new HashMap<>();
            parametros.put("numPeticion", "4");
            parametros.put("idBrigada", usuario.getIdBrigada());
            parametros.put("idUsuario", usuario.getIdUsuario());

            try {
                con = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                con.setParametros(parametros);
                con.executar(Conexion.metodoPeticion.POST);
                String respuesta = con.getRespuesta();

                if (Stuff.existe(respuesta)) {
                    puntosBrigada = Stuff.obtenerPuntos(respuesta);
                    return puntosBrigada;
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Punto> puntos) {
            super.onPostExecute(puntos);
            if (puntos != null) {
                listenerMarkers.agregarPuntos(puntos);
                new OnBackColocarPuntos(puntos, BitmapDescriptorFactory.HUE_RED).execute();
            }
        }
    }


    public class OnBackColocarPuntos extends AsyncTask<String, Punto, String> {

        private ArrayList<Punto> puntos;
        private Float color;

        public OnBackColocarPuntos(ArrayList<Punto> puntos, Float color) {
            this.puntos = puntos;
            this.color = color;
            mMap.setOnMarkerClickListener(listenerMarkers);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                for (int i = 0; i < puntos.size(); i++) {
                    Punto puntoTmp = puntos.get(i);
                    Log.wtf("PUNTO", puntos.get(i).toString());

                    Thread.sleep(2);

                    publishProgress(puntoTmp);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Punto... values) {
            super.onProgressUpdate(values);
            colocarMarca(values[0], color);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                pintarRuta(puntos.get(0).getCadenaRuta());
                polyline = mMap.addPolyline(pol);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}