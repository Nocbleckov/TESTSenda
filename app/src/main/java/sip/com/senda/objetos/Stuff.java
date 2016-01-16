package sip.com.senda.objetos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import sip.com.senda.R;

/**
 * Created by DESARROLLO on 03/12/15.
 */
public class Stuff {

    public static boolean existe(String data) {

        Boolean existe = false;

        if (!data.equalsIgnoreCase("")) {
            JSONObject json;
            try {
                json =  new JSONObject(data);
                JSONArray usuario = json.optJSONArray("respuesta");

                for(int i = 0; i<usuario.length();i++){

                    JSONObject obj = usuario.getJSONObject(i);
                    String echo = obj.optString("echo");

                    if(echo.equals("true")){
                        existe = true;
                    }else{
                        existe = false;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  existe;
    }


    public static ArrayList<Punto> obtenerPuntos(String data){
        ArrayList<Punto> puntosTemp =  new ArrayList<Punto>();
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try{
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("puntos");

                for (int i = 0; i<jsonArray.length();i++){

                    JSONObject punto = jsonArray.getJSONObject(i);
                    String idPunto = punto.optString("idPunto");
                    String direccion = punto.optString("direccion");
                    String latitud = punto.optString("latitud");
                    String longitud = punto.optString("longitud");
                    String numero = punto.optString("numero");
                    String calle = punto.optString("calle");
                    String colonia = punto.optString("colonia");
                    String localidad = punto.optString("localidad");
                    String municipio = punto.optString("municipio");
                    String estado = punto.optString("estado");
                    String pais = punto.optString("pais");
                    String codigoPostal = punto.optString("codigoPostal");
                    String referencias = punto.optString("referencias");
                    String estatus = punto.optString("estatusPunto");
                    String cadenaRuta = punto.optString("cadenaRuta");

                    Punto puntoTemp = new Punto(referencias, pais, numero,municipio,longitud,localidad, latitud, idPunto,  estatus,  estado, direccion,  colonia, codigoPostal,calle,cadenaRuta);
                    puntosTemp.add(puntoTemp);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  puntosTemp;
    }

    public static ArrayList<Ruta> misRutas(String data){
        ArrayList<Ruta> misRutasTemp = new ArrayList<>() ;
        if(!data.equalsIgnoreCase("")){
            JSONObject JSON;
            try {
                JSON = new JSONObject(data);
                JSONArray rutas = JSON.optJSONArray("rutas");

                for(int i = 0;i<rutas.length();i++){
                    JSONObject ruta = rutas.getJSONObject(i);
                    Ruta rutaTemp = new Ruta();
                    rutaTemp.setId(ruta.optString("idRuta"));
                    rutaTemp.setPolyLine(ruta.optString("cadenaRuta"));
                    misRutasTemp.add(rutaTemp);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return misRutasTemp;
    }

    public static Ruta obtenerRuta(String data){

        Ruta rtTemp = new Ruta();
        ArrayList<String> pasosTemp = new ArrayList<String>();

        if(!data.equalsIgnoreCase("")){
            JSONObject JSON;
            try{

                JSON = new JSONObject(data);
                JSONArray rutas = JSON.optJSONArray("routes");
                JSONObject objeto = rutas.getJSONObject(0);
                JSONArray legs = objeto.getJSONArray("legs");
                JSONObject objt = legs.getJSONObject(0);

                rtTemp.setDistancia(objt.optJSONObject("distance").optString("value"));
                rtTemp.setDuracion(objt.optJSONObject("duration").optString("value"));
                rtTemp.setDireccionFin(objt.optString("end_address"));
                rtTemp.setDireccionIni(objt.optString("start_address"));
                rtTemp.setPolyLine(objeto.optJSONObject("overview_polyline").optString("points"));

                JSONArray pasos = objt.getJSONArray("steps");

                for (int i = 0;i<pasos.length();i++ ){
                    JSONObject ps = pasos.getJSONObject(i);
                    pasosTemp.add(ps.optString("html_instructions").replaceAll("\\<.*?>"," "));
                }

                rtTemp.setIntrucciones(pasosTemp);

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return  rtTemp;
    }

    public static Toast toastCsm(View view,String mnsj){

        LayoutInflater inf = LayoutInflater.from(view.getContext());
        Toast mensaje = new Toast(view.getContext());

        View mensajeLayout = inf.inflate(R.layout.custom_toast, (ViewGroup) view.findViewById(R.id.lytLayout));
        mensaje.setView(mensajeLayout);
        TextView mensjeView = (TextView) mensajeLayout.findViewById(R.id.toastMessage);
        mensjeView.setText(mnsj);
        mensaje.setDuration(Toast.LENGTH_SHORT);

        return  mensaje;
    }

    public static Toast toastCsmCntx(Context context,String mnsj){

        LayoutInflater inf = LayoutInflater.from(context);
        Toast mensaje = new Toast(context);
        View view = View.inflate(context,R.layout.activity_main,null);

        View mensajeLayout = inf.inflate(R.layout.custom_toast,(ViewGroup)view.findViewById(R.id.lytLayout));
        mensaje.setView(mensajeLayout);
        TextView mensjeView = (TextView) mensajeLayout.findViewById(R.id.toastMessage);
        mensjeView.setText(mnsj);
        mensaje.setDuration(Toast.LENGTH_SHORT);

        return  mensaje;
    }

    public static Dialog dialogCsm(Context context,String msnj){
        Dialog dialog =  new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("\t\tError");

        TextView texto = (TextView)dialog.findViewById(R.id.textMensaje_CustomDialog);
        texto.setText(msnj);

        dialog.setCancelable(false);
        /*Button boton = (Button)dialog.findViewById(R.id.botonAceptar_CustomDialog);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        return dialog;
    }

    public static Dialog dialogProgressBar(Context context){
        Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialogprobar);


        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        //dialog.setCancelable(false);
        return  dialog;
    }

    public static Point cnvLtLnToTileCoord(LatLng coordenada,int zoom,int title_size){

        double scale = 1 << zoom;
        com.google.maps.android.geometry.Point worldCoordinate = project(coordenada,title_size);

        int x = (int) Math.floor(worldCoordinate.x * scale / title_size);
        int y = (int) Math.floor(worldCoordinate.y * scale / title_size);

        Point punto = new Point(x,y);
        return punto;
    }

    public static LatLng tileToLatLng(int x,int y,int zoom){
        LatLng temp = null;

        Double lat = tileLat(y,zoom);
        Double lng = tileLng(x,zoom);

        lat = Math.abs(lat);

        temp = new LatLng(lat,lng);

        return temp;
    }

    private static com.google.maps.android.geometry.Point project(LatLng coordenada,int title_size){

        Double siny = Math.sin(coordenada.latitude * Math.PI / 180);

        siny = Math.min(Math.max(siny,-0.9999),0.9999);

        Double x =  ( title_size * (0.5 + coordenada.longitude/360));
        Double y =  ( title_size * (0.5 - Math.log((1 + siny)/(1-siny)) / (4*Math.PI)));
        com.google.maps.android.geometry.Point punto = new com.google.maps.android.geometry.Point(x,y);

        return  punto;
    }

    private static Double tileLng(double x,double zoom){
        Double lng = (x/Math.pow(2,zoom)*360-180);
        return lng;
    }

    private static Double tileLat(double y,double zoom){

        Double n = Math.PI-2*Math.PI*y/Math.pow(2,zoom);

        Double lat =(180/Math.PI*Math.atan(0.5*(Math.exp(n)-Math.exp(-n))));
        return lat;
    }

}
