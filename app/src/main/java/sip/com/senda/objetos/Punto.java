package sip.com.senda.objetos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by DESARROLLO on 03/12/15.
 */
public class Punto implements Parcelable {

    private String referencias, pais, numero, municipio, longitud, localidad, latitud, idPunto, idAccion, estatus, estado, direccion, colonia, codigoPostal, calle,cadenaRuta;
    private LatLng coordenada;

    public Punto() {

    }

    public Punto(String referencias, String pais, String numero, String municipio, String longitud, String localidad, String latitud, String idPunto, String estatus, String estado, String direccion, String colonia, String codigoPostal, String calle,String cadenaRuta) {
        this.referencias = referencias;
        this.pais = pais;
        this.numero = numero;
        this.municipio = municipio;
        this.longitud = longitud;
        this.localidad = localidad;
        this.latitud = latitud;
        this.idPunto = idPunto;
        this.idAccion = idAccion;
        this.estatus = estatus;
        this.estado = estado;
        this.direccion = direccion;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.cadenaRuta = cadenaRuta;
        latLng(latitud, longitud);
    }

    protected Punto(Parcel in) {
        referencias = in.readString();
        pais = in.readString();
        numero = in.readString();
        municipio = in.readString();
        longitud = in.readString();
        localidad = in.readString();
        latitud = in.readString();
        idPunto = in.readString();
        idAccion = in.readString();
        estatus = in.readString();
        estado = in.readString();
        direccion = in.readString();
        colonia = in.readString();
        codigoPostal = in.readString();
        calle = in.readString();
        cadenaRuta = in.readString();
        //coordenada = in.readParcelable(LatLng.class.getClassLoader());
        coordenada =(LatLng) in.readValue(LatLng.class.getClassLoader());
    }

    public static final Creator<Punto> CREATOR = new Creator<Punto>() {
        @Override
        public Punto createFromParcel(Parcel in) {
            return new Punto(in);
        }

        @Override
        public Punto[] newArray(int size) {
            return new Punto[size];
        }
    };

    public void latLng(String lat, String lng) {
        try {
            this.coordenada = new LatLng(Float.parseFloat(lat), Float.parseFloat(lng));
        } catch (Exception e) {
            e.printStackTrace();
            this.coordenada = new LatLng(0, 0);
        }
    }

    public void setCoordenada(LatLng coordenada){
        this.coordenada = coordenada;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getReferencias() {
        return referencias;
    }

    public String getPais() {
        return pais;
    }

    public String getNumero() {
        return numero;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getIdPunto() {
        return idPunto;
    }

    public String getIdAccion() {
        return idAccion;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getEstado() {
        return estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getColonia() {
        return colonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getCalle() {
        return calle;
    }

    public LatLng getCoordenada() {
        return coordenada;
    }

    public String getCadenaRuta(){
        return cadenaRuta;
    }

    public double LatDouble(){
        double lat = Double.parseDouble(latitud);
        return  lat;
    }

    public double LngDouble(){
        double lng = Double.parseDouble(longitud);
        return lng;
    }

    @Override
    public String toString() {
        return "Punto{" +
                "coordenada=" + coordenada + "lat= " + latitud + "lng= " + longitud +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(referencias);
        dest.writeString(pais);
        dest.writeString(numero);
        dest.writeString(municipio);
        dest.writeString(longitud);
        dest.writeString(localidad);
        dest.writeString(latitud);
        dest.writeString(idPunto);
        dest.writeString(idAccion);
        dest.writeString(estatus);
        dest.writeString(estado);
        dest.writeString(direccion);
        dest.writeString(colonia);
        dest.writeString(codigoPostal);
        dest.writeString(calle);
        dest.writeString(cadenaRuta);
        dest.writeValue(coordenada);
    }
}
