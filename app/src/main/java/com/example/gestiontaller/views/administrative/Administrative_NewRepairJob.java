package com.example.gestiontaller.views.administrative;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.data_classes.CarInShop;
import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.RepairJob;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Administrative_NewRepairJob extends AppCompatActivity {
    private DatabaseReference database;
    private final int[] repairNumber = new int[1];
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_new_repair_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        //Genera un nuevo numero de reparacion
        generateRepairNumber();


        AppCompatButton confirm = findViewById(R.id.confirm);
        AutoCompleteTextView car = findViewById(R.id.carSelectorTextField);

        TextInputEditText date = findViewById(R.id.date_TextEdit);
        TextInputLayout dateLayout = findViewById(R.id.startDate_textField);

        DatePickerDialog.OnDateSetListener datePicker =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                date.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
            }
        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Administrative_NewRepairJob.this,datePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TextInputEditText desc = findViewById(R.id.description);
        TextInputLayout descLayout = findViewById(R.id.description_textField);

        final String[] chiefMechanic = {""};

        ArrayList<String> carsInShop = new ArrayList<String>();
        ArrayAdapter<String> licensePlate = new ArrayAdapter<String>(Administrative_NewRepairJob.this, android.R.layout.simple_spinner_dropdown_item, carsInShop);
        car.setAdapter(licensePlate);

        //Carga los datos del adaptador
        loadCarsInShop(carsInShop, licensePlate);

        //Confirma y guarda los datos en la base de datos
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!car.getText().toString().isEmpty() && !date.getText().toString().isEmpty() && !desc.getText().toString().isEmpty()){
                    database.child("carInShop").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot postSnapshot: snapshot.getChildren()){
                                CarInShop userTemp = new CarInShop((HashMap<String, Object>) postSnapshot.getValue());
                                if(userTemp.getLicensePlate().equals(car.getText().toString())){
                                    RepairJob repairTemp = new RepairJob(Integer.toString(repairNumber[0]), userTemp.getLicensePlate(), date.getText().toString(), userTemp.getChiefMechanic(), desc.getText().toString());
                                    database.child("repairJobs").child(repairTemp.getRepairNumber()).setValue(repairTemp);
                                    finish();
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else{
                    if(desc.getText().toString().isEmpty()){
                        descLayout.setError("Campo Vacio");
                    }else{
                        descLayout.setErrorEnabled(false);
                    }
                    if(date.getText().toString().isEmpty()){
                        dateLayout.setError("Campo Vacio");
                    }else{
                        dateLayout.setErrorEnabled(false);
                    }
                }
            }
        });

        //Cancela la operacion
        AppCompatButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Carga los datos en el adaptador
     * @param carsInShop lista de matriculas
     */
    private void loadCarsInShop(ArrayList<String> carsInShop, ArrayAdapter<String> adapter) {
        database.child("carInShop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(carsInShop.size()>0){
                    carsInShop.removeAll(carsInShop);
                }

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    CarInShop carTemp = new CarInShop((HashMap<String, Object>) postSnapshot.getValue());
                    carsInShop.add(carTemp.getLicensePlate());
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });
    }

    private void generateRepairNumber() {
        database.child("repairJobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int contador = 0;
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    contador++;
                }
                if(contador>0){
                    repairNumber[0] = contador+1;
                }else {
                    repairNumber[0] = 1;
                }

                TextView repairNumberTittle = findViewById(R.id.repairNumberTittle);
                repairNumberTittle.setText("Reparacion Nº: " + repairNumber[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}