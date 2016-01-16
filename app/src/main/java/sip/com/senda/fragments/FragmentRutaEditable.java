package sip.com.senda.fragments;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import sip.com.senda.R;
import sip.com.senda.activities.MapaActivity;
import sip.com.senda.adapters.AdaptadorRutaEditable;
import sip.com.senda.listener.ListenerMarkerRutaEditalbe;
import sip.com.senda.objetos.Conexion;
import sip.com.senda.objetos.Punto;
import sip.com.senda.objetos.RequestDireccion;
import sip.com.senda.objetos.Ruta;
import sip.com.senda.objetos.Stuff;

/**
 * Created by DESARROLLO on 23/12/15.
 */
public class FragmentRutaEditable extends Fragment {

    private int id;
    private FragmentMisRutas fragmentMisRutas;
    GoogleMap mMap;
    ListView listaRutaEditable;
    Button botonAceptar;
    AdaptadorRutaEditable adaptadorRutaEditable;
    MapaActivity mapaActivity;
    ArrayList<Punto> direcciones;
    ArrayList<Punto> puntosDireccion;
    Polyline polyline;
    PolylineOptions pol;
    boolean visible = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rutaeditable,container,false);

        direcciones = new ArrayList<>();
        botonAceptar = (Button)view.findViewById(R.id.botonAceptarRuta_FragmentRuta);
        listaRutaEditable = (ListView)view.findViewById(R.id.listaRutaEditable_FragmentRuta);
        adaptadorRutaEditable =  new AdaptadorRutaEditable(getContext(),direcciones);
        listaRutaEditable.setAdapter(adaptadorRutaEditable);
        ListenerMarkerRutaEditalbe listenerMarkerRutaEditalbe =  new ListenerMarkerRutaEditalbe(getContext(),direcciones,adaptadorRutaEditable,puntosDireccion);
        mMap.setOnMarkerClickListener(listenerMarkerRutaEditalbe);

        mapaActivity.setVisible(true);
        this.visible = true;

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPuntos();
            }
        });

        return view;
    }

    public  void setId(int id){
        this.id = id;
    }

    public void enviarPuntos(){
        mapaActivity.cambiarListener();
        new OnBackRequest(direcciones).execute();
        dismiss();
    }


    public void dismiss(){
        mapaActivity.setVisible(false);
        this.visible = false;
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    public void setmMap(GoogleMap mMap,Polyline polyline,PolylineOptions pol,MapaActivity mapaActivity,ArrayList<Punto> puntosDireccion,FragmentMisRutas fragmentMisRutas){
        this.mMap = mMap;
        this.polyline = polyline;
        this.pol = pol;
        this.mapaActivity = mapaActivity;
        this.puntosDireccion = puntosDireccion;
        this.fragmentMisRutas = fragmentMisRutas;
        if(fragmentMisRutas.getVisible()){
            FragmentManager fragmentManager = mapaActivity.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentMisRutas);
            fragmentTransaction.commit();
            fragmentMisRutas.setVisible(false);
        }
    }

    public void limpiar(){
        pol = null;
        polyline.remove();
        pol = new PolylineOptions();
        pol.color(Color.argb(150,115,60,125));
        pol.width(8);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public boolean getVisible(){
        return this.visible;
    }

    private  class OnBackRequest extends AsyncTask<Boolean,Ruta,Ruta>{

        private ArrayList<Punto> direcciones;
        private RequestDireccion peticion;
        private Conexion con;

        public OnBackRequest(ArrayList<Punto> direcciones){
            this.direcciones = direcciones;
            buildRequest();
        }
        public void buildRequest(){
            peticion = new RequestDireccion();
            peticion.setKey("AIzaSyCN9dweEHH0yQXVVLyuCTxa_Es1Vk0gzJY");
            peticion.setMode("walking");
            for(int i = 0;i<direcciones.size();i++){

                LatLng latLngTmp = new LatLng(Double.parseDouble(direcciones.get(i).getLatitud()),Double.parseDouble(direcciones.get(i).getLongitud()));

                if(i == 0){
                    peticion.setOrigin(latLngTmp);
                }else if(i == direcciones.size()-1){
                    peticion.setDestination(latLngTmp);
                }else{
                    peticion.addWaypoint(latLngTmp);
                }
            }
        }
        @Override
        protected Ruta doInBackground(Boolean... params) {

            try{
                con = new Conexion("https://maps.googleapis.com/maps/api/directions/json?");
                con.setParametros(peticion.getParametros());
                con.executar(Conexion.metodoPeticion.GET);
                String respuesta = con.getRespuesta();
                Ruta ruta = Stuff.obtenerRuta(respuesta);
                return  ruta;
            }catch (Exception e){
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(Ruta ruta) {
            super.onPostExecute(ruta);
            if(ruta != null){
                limpiar();
                mapaActivity.cambiarColorMarcas();
                pol.addAll(ruta.getPuntos());
                polyline = mMap.addPolyline(pol);
                mapaActivity.setPoli(pol, polyline);
            }
        }
    }

}
