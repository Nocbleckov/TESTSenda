package sip.com.senda.objetos;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DESARROLLO on 24/12/15.
 */
public class RequestDireccion {

    private String mode,key;
    private HashMap<String,String> parametros ;
    LatLng origin,destination;
    private ArrayList<LatLng> waypoints;

    public RequestDireccion(){
        parametros = new HashMap<>();
        waypoints = new ArrayList<>();
    }

    public String getMode() {
        return mode;
    }

    public String getKey() {
        return key;
    }

    public HashMap<String, String> getParametros() {

        parametros.put("origin",origin.latitude+","+origin.longitude);
        parametros.put("destination",destination.latitude+","+destination.longitude);
        parametros.put("mode",mode);
        parametros.put("key",key);
        parametros.put("language","es");
        if(waypoints.size()>0){
            buildWaypoints();
        }

        return parametros;
    }


    public void buildWaypoints(){
        String waypoint = "";
        for (int i = 0;i<waypoints.size();i++){
            waypoint = waypoint + waypoints.get(i).latitude+","+waypoints.get(i).longitude+"|";
        }
        parametros.put("waypoints",waypoint);
    }

    public LatLng getOrigin() {
        return origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public ArrayList<LatLng> getWaypoints() {
        return waypoints;
    }

    public void setOrigin(LatLng origin){
        this.origin = origin;
    }

    public void setDestination(LatLng destination){
        this.destination = destination;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void addWaypoint(LatLng punto){
        this.waypoints.add(punto);
    }
}
