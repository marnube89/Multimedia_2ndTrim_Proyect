package com.example.gestiontaller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gestiontaller.R;
import com.example.gestiontaller.data_classes.RepairTask;

import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<RepairTask> {
    private ArrayList<RepairTask> data;


    /**
     * Constructor
     * @param context contexto de donde se crea el adaptador
     * @param objects ArrayList con los datos a mostrar
     */
    public TasksAdapter(@NonNull Context context, @NonNull ArrayList<RepairTask> objects) {
        super(context, 0, objects);
        data = objects;
    }

    /**
     * Muestra los datos colocados en un layout personalizado
     * @param position es la posicion del elemento
     * @param convertView
     * @param parent es la vista padre
     * @return devuelve una vista, la cual se colocara en cada elemento del ListView
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mostrado = LayoutInflater.from(getContext());
        View element = mostrado.inflate(R.layout.task_item, parent, false);

        TextView taskNum = element.findViewById(R.id.taskNumber);
        taskNum.setText(getContext().getString(R.string.task_number) + ": " + data.get(position).getTaskNumber());
        TextView date = element.findViewById(R.id.startDate);
        date.setText(getContext().getString(R.string.date) + ": " + data.get(position).getStartDate());
        TextView chiefMechanic = element.findViewById(R.id.chiefMechanic);
        chiefMechanic.setText(getContext().getString(R.string.chief_mechanic) + ": " + data.get(position).getChiefMechanic());

        return element;
    }
}
