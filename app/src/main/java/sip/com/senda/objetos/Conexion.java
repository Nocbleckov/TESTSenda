package sip.com.senda.objetos;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by DESARROLLO on 23/11/15.
 */
public class Conexion {


    private URL url;
    private HttpURLConnection conexion;
    private String parametros;


    private int codigoRespuesta;
    private String mensaje;
    private String respuesta;

    public String getMensaje(){
        return mensaje;
    }

    public int getCodigoRespuesta(){
        return codigoRespuesta;
    }

    public String getRespuesta(){
        return respuesta;
    }

    public void setParametros(Map<String,String> parametros){

        StringBuilder parametrosB = new StringBuilder();
        final char FINPARAMETRO = '&';
        final char IGUAL = '=';

        if(parametros != null) {

            boolean primerParametro = true;
            for (String NombreParametro : parametros.keySet() ) {

                if(!primerParametro){
                    parametrosB.append(FINPARAMETRO);
                }
                parametrosB.append(NombreParametro).append(IGUAL).append(parametros.get(NombreParametro));
                primerParametro = false;
            }

        }

        this.parametros = parametrosB.toString();
    }

    public String getParametros(){
        return parametros;
    }

    public enum metodoPeticion{
        GET,POST
    }

    public Conexion(String urlStg) throws Exception {

        this.url = new URL(urlStg);
        conexion = (HttpURLConnection)url.openConnection();

    }

    public void executar(metodoPeticion metodo) throws Exception{

        switch (metodo){

            case GET:

                if (parametros != null) {
                    url = new URL( url.toString()+"?"+parametros);
                    Log.wtf("URL",url.toString());
                    conexion = (HttpURLConnection) url.openConnection();
                }

                conexion.setRequestMethod("GET");


                break;
            case POST:
                conexion.setRequestMethod("POST");

                conexion.setDoOutput(true);
                conexion.setDoInput(true);
                conexion.setInstanceFollowRedirects(false);

                conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conexion.setRequestProperty("charset", "utf-8");
                conexion.setRequestProperty("Content-Length", "" + Integer.toString(parametros.getBytes().length));
                conexion.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(conexion.getOutputStream());
                wr.writeBytes(parametros);
                wr.flush();
                wr.close();

                break;
        }

        this.codigoRespuesta = conexion.getResponseCode();
        this.mensaje = conexion.getResponseMessage();

        InputStream in = conexion.getInputStream();

        this.respuesta = convertStreamToString(in);

        conexion.disconnect();
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
