package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Components
    TextView login_email;
    TextView login_password;
    TextView register;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        register = findViewById(R.id.register);
        login_btn = findViewById(R.id.login_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_intent = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(register_intent);

            }
        });
    }
}