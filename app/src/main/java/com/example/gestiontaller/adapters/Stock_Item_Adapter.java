package com.example.gestiontaller.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gestiontaller.R;
import com.example.gestiontaller.data_classes.CarPart;

import java.util.ArrayList;

public class Stock_Item_Adapter extends ArrayAdapter<CarPart> {
    private ArrayList<CarPart> data;

    /**
     * Constructor
     * @param context contexto de donde se crea el adaptador
     * @param data ArrayList con los datos a mostrar
     */
    public Stock_Item_Adapter(@NonNull Context context, ArrayList<CarPart> data) {
        super(context, 0, data);
        this.data = data;
    }

    /**
     * Muestra los datos colocados en un layout personalizado
     * @param position es la posicion del elemento
     * @param convertView
     * @param parent es la vista padre
     * @return devuelve una vista, la cual se colocara en cada elemento del ListView
     */
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mostrado = LayoutInflater.from(getContext());
        View elemento = mostrado.inflate(R.layout.stock_item, parent, false);
        TextView partName = elemento.findViewById(R.id.partName);
        partName.setText(data.get(position).getName());
        TextView partUds = elemento.findViewById(R.id.partUds);
        partUds.setText("Unidades: "+Long.toString(data.get(position).getUnitsAvailable()));
        TextView partPrice = elemento.findViewById(R.id.partPrice);
        partPrice.setText("Proveedor: "+data.get(position).getRetailerName());
        return elemento;
    }
}
