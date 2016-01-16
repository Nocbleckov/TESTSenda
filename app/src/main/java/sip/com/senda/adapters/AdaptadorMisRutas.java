package sip.com.senda.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sip.com.senda.R;
import sip.com.senda.objetos.Ruta;

/**
 * Created by DESARROLLO on 28/12/15.
 */
public class AdaptadorMisRutas extends ArrayAdapter<Ruta>{

    private final Context context;
    private final ArrayList<Ruta> values ;

    public AdaptadorMisRutas(Context context, ArrayList<Ruta> values){
        super(context,-1,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adaptador_misrutas,parent,false);
        TextView textNombre = (TextView)rowView.findViewById(R.id.textNombreRuta_AdaptadorMisRutas);
        textNombre.setText("Ruta: "+position);

        return rowView;
    }


}
