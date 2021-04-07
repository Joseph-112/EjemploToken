package com.example.ejemplotoken;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText etEmail;
    private EditText etContraseña;

    private Button btnRegistrar;

    private String email = "";
    private String contraseña = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etContraseña = (EditText) findViewById(R.id.etPassword);
        btnRegistrar = (Button) findViewById(R.id.button);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                contraseña = etContraseña.getText().toString();

                if(!email.isEmpty() && !contraseña.isEmpty()){
                    if(contraseña.length()>=6){
                        createAccount();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"La contraseña no puede tener menos de 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Initialize Firebase Auth

    }

    protected void createAccount(){

        mAuth.createUserWithEmailAndPassword(email,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String id = mAuth.getCurrentUser().getUid();
                    Map<String , Object> map = new HashMap<>();
                    map.put( "email" , email);
                    map.put( "Contraseña", contraseña);

                    mDatabase.child("usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(MainActivity.this,PerfilActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this,"No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,"No se pudo completar la tarea", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}