package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {

    // Firebase Instance
    private FirebaseAuth mAuth;

    // Components
    TextView heading;
    EditText reg_name, reg_phone, reg_email, reg_password;
    Button register_btn;
    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // Firebase Initialization
        mAuth = FirebaseAuth.getInstance();

        heading = (TextView) findViewById(R.id.heading);
        reg_name = (EditText) findViewById(R.id.reg_name);
        reg_phone = (EditText) findViewById(R.id.reg_phone);
        reg_email = (EditText) findViewById(R.id.reg_email);
        reg_password = (EditText) findViewById(R.id.reg_password);
        register_btn = (Button) findViewById(R.id.register_btn);
        back_btn = (ImageButton) findViewById(R.id.back_btn);


        // If header is clicked forward to login page
        heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent heading_intent = new Intent(RegisterUser.this, MainActivity.class);
                startActivity(heading_intent);
            }
        });

        // If back btn clicked move to login page
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_btn_Intent = new Intent(RegisterUser.this, MainActivity.class);
                startActivity(back_btn_Intent);
            }
        });

        // Register btn clicked
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser() {

        // Obtaining the input as text then converting to string, then trim it and pass to the variable.
        String name = reg_name.getText().toString().trim();
        String phone = reg_phone.getText().toString().trim();
        String email = reg_email.getText().toString().trim();
        String password = reg_password.getText().toString().trim();

        // Validations
        if (name.isEmpty()){
            reg_name.setError("Name is required");
            reg_name.requestFocus();  // focus back on the name field
            return;
        }

        if (phone.isEmpty()){
            reg_phone.setError("Phone number is required");
            reg_phone.requestFocus();
            return;
        }

        if (phone.length() != 10){
            reg_phone.setError("Phone number should have 10 digits");
            reg_phone.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phone).matches()){
            reg_phone.setError("Invalid phone number");
            reg_phone.requestFocus();
            return;
        }

        if (email.isEmpty()){
            reg_email.setError("Email is required");
            reg_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){  // Checks if the email address matches the pattern
            reg_email.setError("Invalid Email");
            reg_email.requestFocus();
            return;
        }

        if (password.isEmpty()){
            reg_password.setError("Password is required");
            reg_password.requestFocus();
            return;
        }

        if (password.length() < 6){
            reg_password.setError("Password should have more than 6 characters");
            reg_password.requestFocus();
        }

        // Setting visibility of progress bar
//        progressBar.setVisible(View.VISIBLE);

        // Firebase Authentication (Creating user with email and password by passing above created variables as parameters)
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // Check if user has been registered
                        if(task.isSuccessful()){



                            // If so create user object
                            User user = new User(name,phone,email);

                            // Send user object to real-time db in Firebase
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
//                                        progressBar.setVisibility(View.GONE);

                                        // Then redirect to the login page
                                    }

                                    else{
                                        Toast.makeText(RegisterUser.this, "Failed to register", Toast.LENGTH_LONG).show();
//                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(RegisterUser.this, "Failed to register user", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }




}