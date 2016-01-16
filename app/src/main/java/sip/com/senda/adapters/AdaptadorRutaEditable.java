package sip.com.senda.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sip.com.senda.R;
import sip.com.senda.objetos.Punto;

/**
 * Created by DESARROLLO on 23/12/15.
 */
public class AdaptadorRutaEditable extends ArrayAdapter<Punto>{

    private final Context context;
    private final ArrayList<Punto> values;


    public AdaptadorRutaEditable(Context context, ArrayList<Punto> values) {
        super(context,-1,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adaptador_rutaeditable,parent,false);
        TextView direccionCompleta = (TextView)rowView.findViewById(R.id.textDireccionCompleta_Adaptador);
        direccionCompleta.setText(values.get(position).getDireccion());
        return  rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}
