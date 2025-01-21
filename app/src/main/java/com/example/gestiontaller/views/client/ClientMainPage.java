package com.example.gestiontaller.views.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gestiontaller.R;
import com.example.gestiontaller.adapters.RepairListAdapter;
import com.example.gestiontaller.data_classes.CarInShop;
import com.example.gestiontaller.data_classes.RepairJob;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.local_database.UserDbHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClientMainPage extends AppCompatActivity {
    private DatabaseReference database;
    private User currentUser;
    private Set<String> clientCars = new HashSet<>();
    private ArrayList<RepairJob> repairs = new ArrayList<>();
    private RepairListAdapter adapter ;
    private UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new UserDbHelper(this);

        currentUser = (User) getIntent().getSerializableExtra("user");

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        ListView repairList = findViewById(R.id.repairList);
        adapter = new RepairListAdapter(this, repairs);
        repairList.setAdapter(adapter);
        loadAdapter();

        repairList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent gotoDetails = new Intent(ClientMainPage.this, Client_RepairDetails.class);
                gotoDetails.putExtra("repair", adapter.getItem(i));
                startActivity(gotoDetails);
            }
        });

        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.profile_info){
                    Intent gotoProfileInfo = new Intent(ClientMainPage.this, Client_Profile.class);
                    gotoProfileInfo.putExtra("user", currentUser);
                    startActivity(gotoProfileInfo);
                }
                if(item.getItemId() == R.id.contact){

                    //dialogo de seleccion de tipo de contacto
                    //intent implicito de mail
                    //intent implicito de telefono
                }
                if(item.getItemId() == R.id.signOut){
                    dbHelper.deleteUser(currentUser);
                    finish();
                }
                return true;
            }
        });
        ImageButton profile = findViewById(R.id.profileBtn);
        DrawerLayout drawerLayout = findViewById(R.id.main);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(nav);
            }
        });



    }

    private void loadAdapter(){
        database.child("carInShop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    CarInShop temp = new CarInShop((HashMap<String, Object>) postSnapshot.getValue());
                    if(temp.getClient().equals(currentUser.getFullName())){
                        clientCars.add(temp.getLicensePlate());
                    }
                }
                loadRepairs(clientCars);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadRepairs(Set<String> cars){
        database.child("repairJobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Iterator<String> i = cars.iterator();
                    RepairJob temp = new RepairJob((HashMap<String, Object>) postSnapshot.getValue());

                    if(i.hasNext()){
                        i.forEachRemaining(s -> {
                            if(s.equals(temp.getCar())){
                                repairs.add(temp);
                            }
                        });
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}