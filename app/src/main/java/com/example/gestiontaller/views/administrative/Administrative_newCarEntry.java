package com.example.gestiontaller.views.administrative;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

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
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.views.chief_mechanic.ChiefMechanic_CreateTask;
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

public class Administrative_newCarEntry extends AppCompatActivity {
    private DatabaseReference database;
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_new_car_entry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        AutoCompleteTextView clientTextField = findViewById(R.id.clientTextField);
        ArrayList<String> clients = new ArrayList<String>();
        ArrayAdapter<String> clientName = new ArrayAdapter<String>(Administrative_newCarEntry.this, android.R.layout.simple_spinner_dropdown_item, clients);
        clientTextField.setAdapter(clientName);

        //Carga de clientes
        loadClients(clients, clientName);

        AppCompatButton confirm = findViewById(R.id.Confirm);
        TextInputEditText car = findViewById(R.id.carTextField);
        TextInputLayout carLayout = findViewById(R.id.licensePlate_textField);
        TextInputEditText date = findViewById(R.id.dateTextField);
        TextInputLayout dateLayout = findViewById(R.id.date_textField);

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
                new DatePickerDialog(Administrative_newCarEntry.this,datePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Se guardan los datos en la base de datos
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(car.getText().toString().isEmpty()) && !(date.getText().toString().isEmpty()) && !(clientTextField.getText().toString().isEmpty())){
                    CarInShop carTemp = new CarInShop(car.getText().toString(), "", date.getText().toString(), clientTextField.getText().toString());
                    database.child("carInShop").child(carTemp.getLicensePlate()).setValue(carTemp);
                    finish();
                }else{
                    if(car.getText().toString().isEmpty()){
                        carLayout.setError("Campo Vacio");
                    }else{
                        carLayout.setErrorEnabled(false);
                    }
                    if(date.getText().toString().isEmpty()){
                        dateLayout.setError("Campo Vacio");
                    }else{
                        dateLayout.setErrorEnabled(false);
                    }
                }
            }
        });

        //Cancelar operacion
        AppCompatButton cancel = findViewById(R.id.Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Carga una lista con los clientes registrados y actualiza su adaptador
     * @param clients lista de nombres de clientes
     * @param adapter adaptador a actualizar
     */
    private void loadClients(ArrayList<String> clients, ArrayAdapter<String> adapter) {
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(clients.size()>0){
                    clients.removeAll(clients);
                }

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    User userTemp = new User((HashMap<String, Object>) postSnapshot.getValue());
                    if(userTemp.getJobRol()==0){
                        clients.add(userTemp.getFullName());
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });
    }
}