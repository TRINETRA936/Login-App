package com.example.login;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    TextView welcomeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeMsg = findViewById(R.id.welcomeText);
        String userId = getIntent().getStringExtra("userId");

        welcomeMsg.setText("Welcome, " + userId + "!");
    }
}

