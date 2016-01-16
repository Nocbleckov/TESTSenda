package sip.com.senda.objetos;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESARROLLO on 24/11/15.
 */
public class Ruta {

    private String distancia,duracion,direccionIni,direccionFin,polyLine;
    private String id;
    private ArrayList<String> intrucciones ;
    private List<LatLng> puntos;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDireccionIni() {
        return direccionIni;
    }

    public void setDireccionIni(String direccionIni) {
        this.direccionIni = direccionIni;
    }

    public String getDireccionFin() {
        return direccionFin;
    }

    public void setDireccionFin(String direccionFin) {
        this.direccionFin = direccionFin;
    }

    public String getPolyLine() {
        return polyLine;
    }

    public void setPolyLine(String polyLine) {
        this.polyLine = polyLine/*.replaceAll("\\\\", "\\")*/;
        Log.wtf("POLICODE",polyLine);
        this.puntos = PolyUtil.decode(polyLine);

    }

    public ArrayList<String> getIntrucciones() {
        return intrucciones;
    }

    public void setIntrucciones(ArrayList<String> intrucciones) {
        this.intrucciones = intrucciones;
    }

    public List<LatLng> getPuntos() {
        return puntos;
    }

}
