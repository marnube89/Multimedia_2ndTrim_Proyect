package com.example.gestiontaller.views.administrative;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.data_classes.CarPart;
import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.adapters.Stock_Item_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Administrative_CheckPartsStock extends AppCompatActivity {
    private DatabaseReference database;
    private ArrayList<CarPart> available = new ArrayList<CarPart>();
    private ArrayList<CarPart> notAvailable = new ArrayList<CarPart>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_check_parts_stock);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        ListView partsAvailable = findViewById(R.id.partsAvailable);
        ListView partsNotAvailable = findViewById(R.id.partsNotAvailable);

        database.child("carParts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    CarPart partTemp = new CarPart((HashMap<String, Object>) postSnapshot.getValue());
                    if(partTemp.getUnitsAvailable()<=0){
                        notAvailable.add(partTemp);
                    }else {
                        available.add(partTemp);
                    }
                }
                Stock_Item_Adapter availableAdaptor = new Stock_Item_Adapter(Administrative_CheckPartsStock.this, available);
                Stock_Item_Adapter notAvailableAdaptor = new Stock_Item_Adapter(Administrative_CheckPartsStock.this, notAvailable);
                partsAvailable.setAdapter(availableAdaptor);
                partsNotAvailable.setAdapter(notAvailableAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}