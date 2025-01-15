package com.example.gestiontaller.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.aux_classes.AdminUsersAdapter;
import com.example.gestiontaller.R;
import com.example.gestiontaller.aux_classes.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminMainPage extends AppCompatActivity {
    private DatabaseReference database;
    private final User[] userSelected = new User[1];
    private ArrayList<User> users = new ArrayList<User>();
    private ListView employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        employees = findViewById(R.id.employeeList);
        AdminUsersAdapter adapter = new AdminUsersAdapter(AdminMainPage.this, users);
        employees.setAdapter(adapter);
        TextView noUsersText = findViewById(R.id.noUserText);
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    User userTemp = new User((HashMap<String, Object>) postSnapshot.getValue());
                    if(userTemp.getJobRol()>1){
                        if(userTemp.isActiveAccount()){
                            users.add(userTemp);
                        }

                    }

                }
                adapter.notifyDataSetChanged();
                Log.i("len", Integer.toString(users.size()));

                if(users.size()==0){
                    employees.setVisibility(View.GONE);
                    noUsersText.setVisibility(View.VISIBLE);
                }else {
                    employees.setVisibility(View.VISIBLE);
                    noUsersText.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }

        });





        ImageButton createUserBt = findViewById(R.id.createUser_Button);
        createUserBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoCreateEmployee = new Intent(AdminMainPage.this, Admin_createAndEditUser.class);
                startActivity(gotoCreateEmployee);
            }
        });

        ImageButton modUserBt = findViewById(R.id.editUser_Button);
        modUserBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> selectedUsers = getSelectedUsers();
                if(selectedUsers.size()==1){
                    Intent gotoCreateEmployee = new Intent(AdminMainPage.this, Admin_createAndEditUser.class);
                    gotoCreateEmployee.putExtra("user", selectedUsers.get(0));
                    gotoCreateEmployee.putExtra("isModding", true);
                    startActivity(gotoCreateEmployee);
                }else{
                    new MaterialAlertDialogBuilder(AdminMainPage.this)
                            .setTitle("Advertencia")
                            .setMessage("No se pueden editar varios usuarios a la vez")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).show();                }

            }
        });
        ImageButton deleteUserBt = findViewById(R.id.deleteUser_Button);
        FirebaseApp.initializeApp(this);
        deleteUserBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> selectedUsers = getSelectedUsers();
                if(selectedUsers.size()>0){
                    new MaterialAlertDialogBuilder(AdminMainPage.this)
                            .setTitle("Borrado")
                            .setMessage("Deseas elminar " + selectedUsers.size() + " usuario(s)?")
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for(User u : selectedUsers){
                                        //Borrado de datos
                                        u.setActiveAccount(false);
                                        database.child("users").child(u.getUid()).setValue(u);
                                    }
                                }
                            })
                            .show();

                }
            }
        });
    }

    public ArrayList<User> getSelectedUsers(){
        ArrayList<User> temp = new ArrayList<User>();
        for (int i = 0; i < users.size(); i++) {
            View viewTemp = employees.getChildAt(i);
            CheckBox isSelected = viewTemp.findViewById(R.id.selectedCheckBox);
            if(isSelected.isChecked()){
                temp.add(users.get(i));
            }
        }
        return temp;
    }
}