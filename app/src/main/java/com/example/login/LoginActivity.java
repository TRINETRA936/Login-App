package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText userIdInput, passwordInput;
    Button loginBtn;
    TextView registerLink, haveAccountMsg;

    UserDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userIdInput = findViewById(R.id.userIdInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.registerLink);
        haveAccountMsg = findViewById(R.id.haveAccountMsg);

        dbHelper = new UserDBHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String savedUserId = sharedPreferences.getString("userId", null);
        if (savedUserId != null) {
            userIdInput.setText(savedUserId);
        }

        loginBtn.setOnClickListener(v -> {
            String userId = userIdInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "User ID and password cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the hashed password from the database
            String savedHashedPassword = dbHelper.getPassword(userId);

            if (savedHashedPassword != null) {
                // Hash the entered password and compare
                String inputHashed = Hash.hashPassword(password);

                if (savedHashedPassword.equals(inputHashed)) {
                    // Save userId to preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId", userId);
                    editor.apply();

                    // Proceed to welcome screen
                    Intent intent = new Intent(this, WelcomeActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                } else {
                    haveAccountMsg.setText("Invalid username or password.");
                    haveAccountMsg.setVisibility(TextView.VISIBLE);
                    Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
                }
            } else {
                haveAccountMsg.setText("Invalid username or password.");
                haveAccountMsg.setVisibility(TextView.VISIBLE);
                Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
            }
        });

        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }
}
