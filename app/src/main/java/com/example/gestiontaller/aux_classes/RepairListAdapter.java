package com.example.gestiontaller.aux_classes;

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

import java.util.ArrayList;

public class RepairListAdapter extends ArrayAdapter<RepairJob> {
    private ArrayList<RepairJob> data;

    public RepairListAdapter(@NonNull Context context, ArrayList<RepairJob> data) {
        super(context, 0, data);
        this.data = data;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mostrado = LayoutInflater.from(getContext());
        View elemento = mostrado.inflate(R.layout.stock_item, parent, false);
        TextView partName = elemento.findViewById(R.id.partName);
        partName.setText("Numero de reparacion: "+data.get(position).getRepairNumber());
        TextView partUds = elemento.findViewById(R.id.partUds);
        partUds.setText("Mecanico Jefe a cargo: "+data.get(position).getChiefMechanic());
        TextView partPrice = elemento.findViewById(R.id.partPrice);
        partPrice.setText("Matricula: "+data.get(position).getCar());
        return elemento;
    }
}
