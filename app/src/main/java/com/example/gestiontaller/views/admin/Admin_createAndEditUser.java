package com.example.gestiontaller.views.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_createAndEditUser extends AppCompatActivity {
private DatabaseReference database;
private FirebaseAuth auth;

//Variable que le indica al programa si se esta creando o editando un usuario
private boolean isModding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        Intent temp = getIntent();
        isModding = temp.getBooleanExtra("isModding", false);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_create_and_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);


        TextInputEditText fullname = findViewById(R.id.fullName_TextField);
        TextInputLayout fullNameLayout = findViewById(R.id.fullName_textField);
        TextInputEditText mail = findViewById(R.id.mail_TextField);
        TextInputLayout mailLayout = findViewById(R.id.mail_textField);
        TextInputEditText tlf = findViewById(R.id.tlf_TextField);
        TextInputLayout tlfLayout = findViewById(R.id.tlf_textField);
        TextInputEditText pass = findViewById(R.id.pass_TextField);
        TextInputLayout passLayout = findViewById(R.id.pass_textField);
        AutoCompleteTextView rol = findViewById(R.id.jobRol_Text);


        //Mostrara los datos del usuario seleccionado si se esta editando al mismo
        if(isModding){
            User dataRetrieved = (User) temp.getSerializableExtra("user");
            fullname.setText(dataRetrieved.getFullName());
            mail.setText(dataRetrieved.getMail());
            tlf.setText(dataRetrieved.getTlfNumber().toString());
            pass.setText(dataRetrieved.getPassword());

            if(dataRetrieved.getJobRol()==2){
                rol.setText("Administrativo");
            } else if (dataRetrieved.getJobRol()==3) {
                rol.setText("Mecanico");
            } else if (dataRetrieved.getJobRol()==4) {
                rol.setText("Jefe Mecanico");
            }
        }

        AppCompatButton confirm = findViewById(R.id.addNewUserBt);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Se asigna un valor numerico para identificar el rol de este empleado (2-administrativo por defecto)
                Integer rolNumber = 2;
                if(rol.getText().toString().equals("Mecanico")){
                    rolNumber = 4;
                }else if(rol.getText().toString().equals("Jefe Mecanico")){
                    rolNumber = 3;
                }

                //Comprobacion de campos vacios
                if(!(fullname.getText().toString().isEmpty()) && !(mail.getText().toString().isEmpty()) && !(tlf.getText().toString().isEmpty()) && !(pass.getText().toString().isEmpty()) && !(rol.getText().toString().isEmpty())){

                    if(!isModding){

                        //Recoge los datos de los campos
                        User userTemp = new User(fullname.getText().toString(), mail.getText().toString(), Long.parseLong(tlf.getText().toString()), rolNumber, pass.getText().toString());
                        createUser(userTemp);
                    }else{

                        //Se crea un usuario con los nuevos datos
                        User moddedUser = new User(fullname.getText().toString(), mail.getText().toString(), Long.parseLong(tlf.getText().toString()), rolNumber, pass.getText().toString());

                        //Se recupera el usuario seleccionado anteriormente
                        User userTemp = (User) temp.getSerializableExtra("user");

                        //Se actualizan los datos del usuario editado
                        database.child("users").child(userTemp.getUid()).setValue(moddedUser);
                    }

                }else{
                    //Validacion de datos para campos vacios
                    if(fullname.getText().toString().isEmpty()){
                        fullNameLayout.setError("Campo Vacio");
                    }else{
                        fullNameLayout.setErrorEnabled(false);
                    }
                    if(mail.getText().toString().isEmpty()){
                        mailLayout.setError("Campo Vacio");
                    }else{
                        mailLayout.setErrorEnabled(false);
                    }
                    if(pass.getText().toString().isEmpty()){
                        passLayout.setError("Campo Vacio");
                    }else{
                        passLayout.setErrorEnabled(false);
                    }
                    if(tlf.getText().toString().isEmpty()){
                        tlfLayout.setError("Campo Vacio");
                    }else{
                        tlfLayout.setErrorEnabled(false);
                    }

                }
            }
        });
        AppCompatButton cancel = findViewById(R.id.cancelOperation);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    /**
     * Se crean credenciales de usuario a partir del usuario pasado y se almacenan sus datos en la base de datos
     * @param userTemp objeto usuario a agregar
     */
    private void createUser(User userTemp){
        //Si se consigue completar la creacion de credenciales, se almacenara el usuario
        auth.createUserWithEmailAndPassword(userTemp.getMail(), userTemp.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userTemp.setUid(auth.getCurrentUser().getUid());
                    //Se añaden los datos del usuario a la base de datos
                    database.child("users").child(userTemp.getUid()).setValue(userTemp);
                }else{
                    Snackbar.make(findViewById(R.id.main), "Usuario ya creado o parametros mal formulados", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}