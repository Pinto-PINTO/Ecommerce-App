package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Components
    EditText login_email, login_password;
    TextView register;
    Button login_btn;
    ProgressBar progressBar;

    // Firebase Instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Initialization
        mAuth = FirebaseAuth.getInstance();

        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        register = (TextView) findViewById(R.id.register);
        login_btn = (Button) findViewById(R.id.login_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_intent = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(register_intent);

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    private void userLogin() {

        // Obtain user credential and convert to string
        String u_email = login_email.getText().toString().trim();
        String u_password = login_password.getText().toString().trim();

        // Validation
        if(u_email.isEmpty()){
            login_email.setError("Email is required");
            login_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(u_email).matches()){
            login_email.setError("Invalid Email");
            login_email.requestFocus();
            return;
        }

        if(u_password.isEmpty()){
            login_password.setError("Password is required");
            login_password.requestFocus();
            return;
        }

        if(u_password.length() < 6){
            login_password.setError("There should be more than 6 characters");
            login_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Sign-in user with Firebase
        mAuth.signInWithEmailAndPassword(u_email,u_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // Check if user has been logged in
                if(task.isSuccessful()){

                    // Log-in successful, redirect to home page
                    Intent home_intent = new Intent(MainActivity.this,Home.class);
                    startActivity(home_intent);

                }
                else {
                    Toast.makeText(MainActivity.this, "Invalid Login Credentials, try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }



}

//https://www.youtube.com/watch?v=Z-RE1QuUWPg