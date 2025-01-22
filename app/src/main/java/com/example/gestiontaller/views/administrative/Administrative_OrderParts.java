package com.example.gestiontaller.views.administrative;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.data_classes.CarPart;
import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Administrative_OrderParts extends AppCompatActivity {
    private DatabaseReference database;
    private final Object[] retailers = new Object[1];
    private long priceUd;
    private int uds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_order_parts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        AutoCompleteTextView piece = findViewById(R.id.pieceTextEdit);
        TextInputEditText units = findViewById(R.id.unitsNumberTextEdit);
        AutoCompleteTextView supplier = findViewById(R.id.supplierTextEdit);
        TextView priceTittle = findViewById(R.id.unitPriceTittle);
        TextView total = findViewById(R.id.totalNumber);

        //Carga y actualiza el adaptador de los proveedores
        database.child("retailer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> retailerTemp = new ArrayList<String>();
                for(DataSnapshot postShot : snapshot.getChildren()){
                    if(postShot!=null){
                        retailerTemp.add((String) postShot.child("name").getValue());

                    }
                }
                ArrayAdapter<String> adapterRetailer = new ArrayAdapter<String>(Administrative_OrderParts.this, android.R.layout.simple_spinner_dropdown_item, retailerTemp);
                supplier.setAdapter(adapterRetailer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Selecciona y actualiza los datos del proveedor
        HashMap<String, CarPart> carPartArrayList = new HashMap<String, CarPart>();
        supplier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                piece.setText("");
                String selectedRetailer = supplier.getText().toString();
                database.child("carParts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> carPartNames = new ArrayList<String>();
                        CarPart temPart;
                        for(DataSnapshot postShot : snapshot.getChildren()){
                            temPart = new CarPart( (HashMap<String, Object>)postShot.getValue());
                            Log.i("selectedRetailer", selectedRetailer);
                            if(temPart.getRetailerName().equals(selectedRetailer)){
                                carPartNames.add(temPart.getName());
                                carPartArrayList.put(postShot.getKey(), temPart);
                                //it just works
                            }
                        }

                        ArrayAdapter<String> adapterParts = new ArrayAdapter<String>(Administrative_OrderParts.this, android.R.layout.simple_spinner_dropdown_item, carPartNames);
                        piece.setAdapter(adapterParts);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        final CarPart[] partSelected = new CarPart[1];
        final String[] partId = new String[1];
        //Cuando seleccionas una pieza, los campos referentes a precio se actualizan
        piece.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                carPartArrayList.forEach((s, carPart) -> {
                    if(((String)adapterView.getItemAtPosition(i)).equals(carPart.getName())){
                    partSelected[0] = carPart;
                    partId[0] = s;
                    priceUd = carPart.getPrice();
                    priceTittle.setText("Precio Ud.: "+priceUd+"€");

                }
                });
            }
        });

        //Actualizara el texto a medida que se vaya escribiendo
        units.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(units.getText().toString().isEmpty()){
                    units.setText("0");
                }
                uds = Integer.parseInt(units.getText().toString());
                total.setText(Long.toString(uds*priceUd)+"€");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Confirma y actualiza las piezas compradas
        AppCompatButton confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!supplier.getText().toString().isEmpty() && !units.getText().toString().isEmpty() && !piece.getText().toString().isEmpty()){
                    partSelected[0].setUnitsAvailable(((long) uds)+ partSelected[0].getUnitsAvailable());
                    database.child("carParts").child(partId[0]).setValue(partSelected[0]);
                    finish();
                }else{
                    Snackbar.make(findViewById(R.id.main), "No se pueden dejar campos vacios", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //cancela la operacion
        AppCompatButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }
}