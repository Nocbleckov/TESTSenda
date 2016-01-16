package sip.com.senda.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import sip.com.senda.objetos.Conexion;

import java.util.HashMap;

/**
 * Created by DESARROLLO on 16/12/15.
 */
public class ListenerLocalizador implements LocationListener {
    private String idUsuario;

    public ListenerLocalizador(String idUsuario){
        this.idUsuario = idUsuario;
    }

    @Override
    public void onLocationChanged(Location location) {
        new OnBackRegistroPos(location.getLatitude(),location.getLongitude()).execute();
        Log.wtf("LOG","Registra");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class OnBackRegistroPos extends AsyncTask<String, String, String> {


        private Double latitud,longitud;

        public OnBackRegistroPos(Double latitud,Double longitud){
            this.latitud = latitud;
            this.longitud = longitud;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Conexion con = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("numPeticion","6");
                parametros.put("idUsuario",idUsuario);
                parametros.put("lat",""+latitud);
                parametros.put("lng", "" + longitud);


                con.setParametros(parametros);
                con.executar(Conexion.metodoPeticion.POST);
                String respuesta =  con.getRespuesta();

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

}
