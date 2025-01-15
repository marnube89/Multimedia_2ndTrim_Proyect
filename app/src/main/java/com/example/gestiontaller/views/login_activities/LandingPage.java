package com.example.gestiontaller.views.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingPage extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(currentUser!=null){
            Log.e("firebase", "usuarioNo");
        }else{
            Log.v("firebase", "usuarioSi");
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button signIn_bt = findViewById(R.id.signIn_button);
        signIn_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn_Activity();
            }
        });

        Button singUp_bt = findViewById(R.id.signUp_button);
        singUp_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp_Activity();
            }
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

    }

    private void signIn_Activity(){
        Intent signIn = new Intent(LandingPage.this, SignInPage.class);
        startActivity(signIn);
    }

    private void singUp_Activity(){
        Intent singUp = new Intent(LandingPage.this, SingUpPage.class);
        startActivity(singUp);
    }
}