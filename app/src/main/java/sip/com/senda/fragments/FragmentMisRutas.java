package sip.com.senda.fragments;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;

import sip.com.senda.R;
import sip.com.senda.activities.MapaActivity;
import sip.com.senda.adapters.AdaptadorMisRutas;
import sip.com.senda.adapters.AdaptadorRutaEditable;
import sip.com.senda.objetos.Conexion;
import sip.com.senda.objetos.Ruta;
import sip.com.senda.objetos.Stuff;
import sip.com.senda.objetos.Usuario;

/**
 * Created by DESARROLLO on 28/12/15.
 */
public class FragmentMisRutas extends Fragment {

    private ListView listaMisRutas;
    private FragmentRutaEditable fragmentRutaEditable;
    private ArrayList<Ruta> misRutas;
    private GoogleMap mMap;
    private MapaActivity mapaActivity;
    private boolean visible;
    private Usuario usuario;
    private Polyline pol;
    private PolylineOptions polylineOptions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_misrutas,container,false);
        listaMisRutas = (ListView)view.findViewById(R.id.listaMisRutas_FragMisRutas);
        mapaActivity.setVisible(true);
        visible = true;

        new OnBackRutas().execute();

        return view;
    }

    public void cargarLista(){
        AdaptadorMisRutas adaptadorMisRutas = new AdaptadorMisRutas(getContext(),misRutas);
        listaMisRutas.setAdapter(adaptadorMisRutas);
        listaMisRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                Ruta ruta = (Ruta) listaMisRutas.getItemAtPosition(position);
                pintarRutaPuntos(ruta);
                dismiss();
            }
        });
    }

    public void dismiss(){
        this.visible = false;
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }


    public void pintarRutaPuntos(Ruta ruta){
        mapaActivity.buscarPuntos(ruta);
        mapaActivity.pintarRuta(ruta.getPolyLine());
    }

    public void iniFrag(GoogleMap mMap,MapaActivity mapaActivity,FragmentRutaEditable fragmentRutaEditable,Usuario usuario){
        this.mMap = mMap;
        this.mapaActivity = mapaActivity;
        this.usuario = usuario;
        if(fragmentRutaEditable.getVisible()){
            FragmentManager fragmentManager = mapaActivity.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentRutaEditable);
            fragmentTransaction.commit();
            fragmentRutaEditable.setVisible(false);
        }
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public boolean getVisible(){
        return  visible;
    }

    private class OnBackRutas extends AsyncTask<Boolean,Boolean,Boolean>{

        private Conexion con;
        private HashMap<String,String> parametros;

        public OnBackRutas(){

        }
        @Override
        protected Boolean doInBackground(Boolean... params) {
            try{
                parametros = new HashMap<>();
                parametros.put("numPeticion","7");
                parametros.put("idUsuario",usuario.getIdUsuario());
                con = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                con.setParametros(parametros);
                con.executar(Conexion.metodoPeticion.POST);
                String respuesta = con.getRespuesta();
                misRutas = Stuff.misRutas(respuesta);
                Log.wtf("RESPUESTA",respuesta);
                return true;

            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                cargarLista();
            }
        }
    }

}
