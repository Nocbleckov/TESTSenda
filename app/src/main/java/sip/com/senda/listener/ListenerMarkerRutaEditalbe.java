package sip.com.senda.listener;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import sip.com.senda.R;
import sip.com.senda.activities.MapaActivity;
import sip.com.senda.adapters.AdaptadorRutaEditable;
import sip.com.senda.objetos.Punto;
import sip.com.senda.objetos.Stuff;

/**
 * Created by DESARROLLO on 23/12/15.
 */
public class ListenerMarkerRutaEditalbe implements OnMarkerClickListener{

    private MapaActivity view;
    private Button botonAceptar;
    private ArrayList<Punto> direcciones;
    private ArrayList<Punto> puntosDirecciones;
    private AdaptadorRutaEditable adaptadorRutaEditable;
    private Context context;

    public ListenerMarkerRutaEditalbe(Context contex,ArrayList<Punto> direcciones,AdaptadorRutaEditable adaptadorRutaEditable,ArrayList<Punto>puntosDirecciones){
        this.direcciones = direcciones;
        this.adaptadorRutaEditable = adaptadorRutaEditable;
        this.context = contex;
        this.puntosDirecciones = puntosDirecciones;

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();
        Punto puntoTemp = esEste(marker.getTitle());
        /*puntoTemp.setCoordenada(marker.getPosition());
        puntoTemp.setDireccion(marker.getTitle());*/
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        agregarItems(puntoTemp);
        return true;
    }

    public Punto esEste(String title){
        Punto temp = null;
        for (int i = 0;i<puntosDirecciones.size();i++){
            if(puntosDirecciones.get(i).getDireccion().equals(title)){
                return puntosDirecciones.get(i);
            }
        }
        return temp;
    }

    public boolean existe(String direccion){
        for(int i = 0;i<direcciones.size();i++){
            if(direcciones.get(i).getDireccion().equals(direccion)){
                return true;
            }
        }
        return false;
    }

    public void agregarItems(Punto direccion){
        if(!existe(direccion.getDireccion())) {
            Vibrator v = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
            v.vibrate(33);
            direcciones.add(direccion);
            adaptadorRutaEditable.notifyDataSetChanged();
            //Stuff.toastCsmCntx(context, "El punto se agrego a la ruta").show();
        }else{
            //Stuff.toastCsmCntx(context,"El punto ya ha sido Seleccionado").show();
        }
    }
}
