package sip.com.senda.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

import sip.com.senda.R;
import sip.com.senda.objetos.Stuff;
import sip.com.senda.providerTile.CustomMapTileProvider;
import sip.com.senda.providerTile.CustomTileDw;

public class RegionDwl extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ListView listamisMapas;
    private Button guardarMapa, prueba;
    private TileOverlay tileOverlay;
    private CustomMapTileProvider ctmT;
    private TileOverlayOptions tileOption;
    private Marker[] marcas;
    private double acumulador;
    private double actual;
    private Dialog dialog;
    private ProgressDialog pd;
    private int numMarcas;
    private int zoomInicial;
    private int zoomIncremento;
    private LatLng[] posiciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_dwl);
        acumulador = 0;
        actual = 0;
        numMarcas = 0;

        marcas = new Marker[4];
        posiciones = new LatLng[4];
        guardarMapa = (Button) findViewById(R.id.botonGuardarMapas_RegionDwl);
        prueba = (Button) findViewById(R.id.botonPruebaMapas_RegionDwl);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDwl);
        mapFragment.getMapAsync(this);

        dialog = Stuff.dialogProgressBar(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng inicio = new LatLng(23.477, -99.144);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(inicio));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(2));

        mMap.setOnMapLongClickListener(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void aumentarAcumualdor(double peso) {
        acumulador = acumulador + peso;
        //Log.wtf("Acumulador", "" + acumulador);
    }


    public double getAcumulador() {
        return this.acumulador;
    }

    public double getActual(double cantidad) {
        double temp = actual + cantidad;
        this.actual = temp;
        return actual;
    }

    public void setAcumulador(double acumulador) {
        this.acumulador = acumulador;
        Log.wtf("TOTAL",""+acumulador);
    }

    public void setActual(double actual) {
        this.actual = actual;
    }

    public void onClickGuardarMapa(View view) {
        if ( elementosMarcas()< 4) {
            AlertDialog dialog = new AlertDialog.Builder(view.getContext()).setTitle("Error").setMessage("Debe delimitar el area a guardar con 4 marcas").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } else {
            zoomInicial = (int)mMap.getCameraPosition().zoom;
            zoomIncremento = zoomInicial;
            borrarMarcas();
            /*LatLng centro = marca.getPosition();
            Point punto = Stuff.cnvLtLnToTileCoord(centro, zoom, 256);
            Point nuevo = new Point(punto.x - 4, punto.y - 4);
            new OnBackIniciarDw(nuevo, zoom, this, this).execute();*/
            new OnBackIniciarDw(zoomIncremento,this,this).execute();
        }
    }

    public void onClickPruebas(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
        CustomMapTileProvider customMapTileProvider = new CustomMapTileProvider(getResources().getAssets());
        tileOverlayOptions.tileProvider(customMapTileProvider);
        mMap.addTileOverlay(tileOverlayOptions);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Marker marca = mMap.addMarker(new MarkerOptions().position(latLng));
        marca.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));


        if(elementosMarcas()<4){
            marcas[numMarcas] = marca;
            posiciones[numMarcas] = marca.getPosition();
        }else{

            if(numMarcas>=4){
                numMarcas = 0;
            }
            marcas[numMarcas].remove();
            posiciones[numMarcas] = marca.getPosition();
            marcas[numMarcas] = marca;
        }
        Vibrator v = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        v.vibrate(33);


        Log.w("POS", "" + latLng.toString());
        numMarcas = numMarcas + 1;
    }


    private int elementosMarcas(){
        int temp = 0;
        for(int i = 0;i<marcas.length;i++){
            if(marcas[i]!=null){
                temp = temp +1;
            }
        }
        return temp;
    }

    private void normalizarPoligono(int zoom,int title_size,ProgressBar progressBar,TextView progressLabel){

        int[] xs = new int[4];
        int[] ys = new int[4];

        for(int i = 0;i<marcas.length;i++){
            LatLng latLng = posiciones[i];
            Point temp = Stuff.cnvLtLnToTileCoord(latLng,zoom,title_size);
            Log.wtf("PuntosOrg",temp.toString());
            xs[i]=temp.x;
            ys[i]=temp.y;
        }

        MenorMayor xS = ordenarArray(xs);
        MenorMayor yS = ordenarArray(ys);

        Point[] puntos=  getAristas(xS, yS,zoom);

        Log.wtf("Xs", xS.toString());
        Log.wtf("Ys",yS.toString());
        Log.wtf("ZOOM",""+zoom);

        for(int i = puntos[0].x;i<=puntos[2].x;i++){
            for(int y = puntos[0].y;y<=puntos[3].y;y++){
                Log.wtf("PUNTO",i+","+y);
                CustomTileDw ctTemp = new CustomTileDw(i,y,zoom,getBaseContext(),this,progressBar,progressLabel,dialog);
            }
        }

        if(zoomIncremento<zoomInicial+3){
            zoomIncremento = zoomIncremento + 1;
            normalizarPoligono(zoomIncremento,256,progressBar,progressLabel);
        }

    }


    public Point[] getAristas(MenorMayor x ,MenorMayor y,int zoom){

        Point[] puntos= new Point[4];

        puntos[0] = new Point(x.getMenor(),y.getMenor());

        puntos[1] = new Point(x.getMayor(),y.getMayor());

        puntos[2] = new Point(x.getMayor(),y.getMenor());

        puntos[3] = new Point(x.getMenor(),y.getMayor());

        //new OnBackIniciarDw(puntos,zoom,this,this).execute();

        return puntos;
    }

    public void borrarMarcas(){
        for(int i = 0; i<marcas.length;i++){
            marcas[i].remove();
        }
    }

    public MenorMayor ordenarArray(int[] n) {
        int aux;

        MenorMayor menorMayor;

        for (int i = 0; i < n.length - 1; i++) {
            for (int x = i + 1; x < n.length; x++) {
                if (n[x] < n[i]) {
                    aux = n[i];
                    n[i] = n[x];
                    n[x] = aux;
                }
            }
        }

        menorMayor =  new MenorMayor(n[0],n[n.length-1]);

        return menorMayor;
    }

    private class OnBackIniciarDw extends AsyncTask<String, Boolean, Boolean> {

        private Context context;
        //private Point[] puntos;
        private int zoom;
        private RegionDwl regionDwl;
        private ProgressBar progressBar;
        private TextView progressLabel;

        public OnBackIniciarDw(int zoom, RegionDwl regionDwl, Context context) {
            //this.puntos = puntos;
            this.zoom = zoom;
            this.regionDwl = regionDwl;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) dialog.findViewById(R.id.progresbarr_CustomDialogPB);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressLabel = (TextView) dialog.findViewById(R.id.labelProgreso_CustomDialogPB);
            progressLabel.setText("0 / Calculando...");

            dialog.show();
            pd = ProgressDialog.show(context, "Calculando informacion ..", "Esto puede tardar un poco..", true);
        }

        @Override
        protected Boolean doInBackground(String... params) {


            /*for(int i = puntos[0].x;i<=puntos[2].x;i++){
                for(int y = puntos[0].y;y<=puntos[3].y;y++){
                    Log.wtf("PUNTO",i+","+y);
                    CustomTileDw ctTemp = new CustomTileDw(i,y,zoom,getBaseContext(),regionDwl,progressBar,progressLabel,dialog);
                }
            }*/


            normalizarPoligono(zoomIncremento,256,progressBar,progressLabel);

            return null;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pd.dismiss();
            String maxS = new DecimalFormat("#.####").format((acumulador / 1024));
            progressLabel.setText("0 KB /" + maxS + " KB");
            Log.wtf("Acumulador", "" + acumulador);
        }
    }

    private class MenorMayor{
        int menor;
        int mayor;

        public MenorMayor(int menor,int mayor){
            this.mayor = mayor;
            this.menor = menor;
        }

        public int getMayor() {
            return mayor;
        }

        public int getMenor() {
            return menor;
        }

        @Override
        public String toString() {
            return "MenorMayor{" +
                    "menor=" + menor +
                    ", mayor=" + mayor +
                    '}';
        }
    }

}
