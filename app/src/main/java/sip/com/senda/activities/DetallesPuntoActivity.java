package sip.com.senda.activities;

import android.app.Activity;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import sip.com.senda.R;
import sip.com.senda.listener.ListenerLocation;
import sip.com.senda.objetos.Punto;

public class DetallesPuntoActivity extends Activity {

    TextView direccionCompleta,numeroCalle,colonia,localidad,municipio,estado,codigoPostal,estatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_punto);

        Punto punto =(Punto)getIntent().getExtras().get("punto");
        String idUsuario = getIntent().getStringExtra("idUsuario");

        direccionCompleta = (TextView)findViewById(R.id.textDireccionCompleta_DetallesPunto);
        numeroCalle = (TextView)findViewById(R.id.textNumeroCalle_DetallesPunto);
        colonia = (TextView)findViewById(R.id.textColonia_DetallesPunto);
        localidad = (TextView)findViewById(R.id.textLocalidad_DetallesPunto);
        municipio = (TextView)findViewById(R.id.textMunicipio_DetallesPunto);
        estado = (TextView)findViewById(R.id.textEstado_DetallesPunto);
        codigoPostal = (TextView)findViewById(R.id.textCodigoPostal_DetallesPunto);
        estatus = (TextView)findViewById(R.id.textEstatus_DetallesPunto);

        cargaInicio(punto);
        cercania(punto,idUsuario);

    }


    public void cargaInicio(Punto punto){
        direccionCompleta.setText(punto.getDireccion());
        numeroCalle.setText(punto.getCalle()+" ,Nº. "+punto.getNumero());
        colonia.setText(punto.getColonia());
        localidad.setText(punto.getLocalidad());
        municipio.setText(punto.getMunicipio());
        estado.setText(punto.getEstado());
        codigoPostal.setText(punto.getCodigoPostal());
        estatus.setText(punto.getEstatus());
    }

    public void cercania(Punto punto,String idUsuario){

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new ListenerLocation(this,punto,DetallesPuntoActivity.this,getCurrentFocus(),idUsuario,locationManager);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 0, listener);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 0, listener);
        }

    }

}
