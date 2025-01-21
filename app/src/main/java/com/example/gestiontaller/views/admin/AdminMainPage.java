package com.example.gestiontaller.views.admin;

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

import com.example.gestiontaller.adapters.UsersAdapter;
import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.User;
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
    private UsersAdapter adapter;

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

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        employees = findViewById(R.id.employeeList);
        adapter = new UsersAdapter(AdminMainPage.this, users);
        employees.setAdapter(adapter);

        //Se actualizan los datos del adaptador
        adapterUpdate();

        ImageButton createUserBt = findViewById(R.id.createUser_Button);
        createUserBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Cambio de actividad para crear un usuario
                Intent gotoCreateEmployee = new Intent(AdminMainPage.this, Admin_createAndEditUser.class);
                startActivity(gotoCreateEmployee);
            }
        });

        ImageButton modUserBt = findViewById(R.id.editUser_Button);
        modUserBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> selectedUsers = getSelectedUsers();

                //Se cambiara de actividad y se aplicara el estado de isModding true para poder modificar el usuario
                if(selectedUsers.size()==1){
                    Intent gotoCreateEmployee = new Intent(AdminMainPage.this, Admin_createAndEditUser.class);
                    gotoCreateEmployee.putExtra("user", selectedUsers.get(0));
                    gotoCreateEmployee.putExtra("isModding", true);
                    startActivity(gotoCreateEmployee);
                }else{

                    //En caso de seleccionar mas de un usuario se le avisara al admin de una accion ilegal
                    new MaterialAlertDialogBuilder(AdminMainPage.this)
                            .setTitle("Advertencia")
                            .setMessage("No se pueden editar varios usuarios a la vez")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).show();
                }
            }
        });

        ImageButton deleteUserBt = findViewById(R.id.deleteUser_Button);
        FirebaseApp.initializeApp(this);
        deleteUserBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> selectedUsers = getSelectedUsers();
                if(!selectedUsers.isEmpty()){

                    //Se mostrara el dialogo de confirmacion de borrado de usuarios
                    deleteDialog(selectedUsers).show();
                }
            }
        });
    }



    /**
     * Actualiza los datos del adaptador que muestra los empleados
     */
    private void adapterUpdate(){
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    User userTemp = new User((HashMap<String, Object>) postSnapshot.getValue());

                    //Se recuperaran todos los usuarios que no sean ni admin ni cliente
                    if(userTemp.getJobRol()>1){
                        if(userTemp.isActiveAccount()){
                            users.add(userTemp);
                        }
                    }
                }

                //Se actualizan los datos del adaptador
                adapter.notifyDataSetChanged();

                //Se mostrara este textView en caso de no haber usuarios disponibles
                TextView noUsersText = findViewById(R.id.noUserText);
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
    }

    /**
     * Devuelve una lista con los usuarios seleccionados en el ListView
     * @return lista de usuarios
     */
    private ArrayList<User> getSelectedUsers(){
        ArrayList<User> temp = new ArrayList<User>();

        //Se comprobara si el checkbox de los items estan seleccionados y si lo estan se añadiran a la lista de usuarios
        for (int i = 0; i < users.size(); i++) {
            View viewTemp = employees.getChildAt(i);
            CheckBox isSelected = viewTemp.findViewById(R.id.selectedCheckBox);
            if(isSelected.isChecked()){
                temp.add(users.get(i));
            }
        }
        return temp;
    }

    /**
     * Crea un dialogo de confirmacion que avisa que se van a borrar n usuarios y tras confirmar, los borra
     * @param selectedUsers lista de usuarios seleccionados
     * @return devuelve un nuevo dialogo
     */
    private MaterialAlertDialogBuilder deleteDialog(ArrayList<User> selectedUsers){

        //Dialogo de confirmacion de borrado de cuentas
       return new MaterialAlertDialogBuilder(AdminMainPage.this)
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
                            //"Borrado" de datos, en realidad se inavilita la cuenta
                            u.setActiveAccount(false);
                            database.child("users").child(u.getUid()).setValue(u);
                        }
                    }
                });

    }
}