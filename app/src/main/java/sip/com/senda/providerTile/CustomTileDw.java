package sip.com.senda.providerTile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import sip.com.senda.activities.RegionDwl;

/**
 * Created by DESARROLLO on 07/01/16.
 */
public class CustomTileDw {
    private Bitmap imagen;
    private String url ="http://mt0.google.com/vt/lyrs=y&hl=es&x=%d&y=%d&z=%d&scale=4&s=Galileo";
    private Context context;
    private int x , y, zoom;

    private Dialog dialog;
    private RegionDwl regionDwl;
    private ProgressBar progressBar;
    private TextView progressLabel;
    private double tamañoImagen;

    public CustomTileDw(int x,int y,int zoom,Context context,RegionDwl regionDwl,ProgressBar progressBar,TextView progressLabel,Dialog dialog){
        this.x = x;
        this.y = y;
        this.dialog = dialog;
        this.regionDwl = regionDwl;
        this.zoom = zoom;
        this.context = context;
        this.url = String.format(url,x,y,zoom);
        this.progressBar = progressBar;
        this.progressLabel = progressLabel;
        if(x<=coordMax(zoom) && y<=coordMax(zoom) && x>=0 && y>=0){
            tamañoImagen = tamaño();
            new OnBackDescarga(url).execute();
        }
    }

    private int coordMax(int zoom){
        int max =(int) Math.sqrt(Math.pow(4,zoom)) -1 ;
        return max;
    }

    private File crearRuta(int x,int zoom){

        boolean succes = false;

        File ruta = new File(Environment.getExternalStorageDirectory()+"/map/"+zoom+"/"+x);

        if(!ruta.exists()){
            succes = ruta.mkdirs();
        }

        return  ruta;
    }

    @Override
    public String toString() {
        return "CustomTileDw{" +
                "zoom=" + zoom +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomTileDw that = (CustomTileDw) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        return zoom == that.zoom;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + zoom;
        return result;
    }

    public  double tamaño(){
        try {
            URL direccion = new URL(url) ;
            URLConnection urlConnection = direccion.openConnection();
            urlConnection.connect();
            double tamañoImagen = urlConnection.getContentLength();
            if(tamañoImagen == 1555){
                tamañoImagen = 0;
            }
            Log.wtf("TAMAÑO_?",""+tamañoImagen);
            regionDwl.aumentarAcumualdor(tamañoImagen);
            return tamañoImagen;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tamañoImagen;
    }

    private class OnBackDescarga extends AsyncTask<String,Void,Bitmap>{
        String url;

        public OnBackDescarga(String url){
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap btm = null;
            try{
                InputStream iS = new URL(url).openStream();
                btm = BitmapFactory.decodeStream(iS);

                File imagen = new File(crearRuta(x,zoom)+"/"+y+".jpeg");
                imagen.createNewFile();
                publishProgress();
                FileOutputStream os = new FileOutputStream(imagen);
                btm.compress(Bitmap.CompressFormat.JPEG,100,os);
                os.close();

            }catch(Exception e){
                e.printStackTrace();
            }

            return btm;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            double meta = regionDwl.getAcumulador()/1024;
            double max = regionDwl.getActual((tamañoImagen / 1024));

            int prc = (int)((max*100)/meta);

            progressBar.setProgress(prc);

            String metaS = new DecimalFormat("#.####").format(meta);
            String maxS = new DecimalFormat("#.####").format(max);

            progressLabel.setText(maxS+ " KB " + "/" +metaS+" KB");

            if(progressBar.getProgress() == 100){
                regionDwl.setActual(0);
                regionDwl.setAcumulador(0);
                dialog.dismiss();
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            //regionDwl.aumentarAcumualdor();
        }
    }

}
