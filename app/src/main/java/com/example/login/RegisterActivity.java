package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText userIdInput, passwordInput, name;
    Button registerBtn;
    TextView doYouHaveAccount;

    UserDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userIdInput = findViewById(R.id.registerUsername);
        passwordInput = findViewById(R.id.registerPassword);
        name = findViewById(R.id.registerName);
        registerBtn = findViewById(R.id.registerBtn);
        doYouHaveAccount = findViewById(R.id.doYouHaveAccount);
        dbHelper = new UserDBHelper(this);

        registerBtn.setOnClickListener(v -> {
            String userId = userIdInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String nameText = name.getText().toString().trim();

            // 1. Check for empty fields
            if (userId.isEmpty() || password.isEmpty() || nameText.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Check userId and password length
            if (userId.length() < 4) {
                Toast.makeText(this, "User ID must be at least 4 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8) {
                Toast.makeText(this, "Password must be at least 8 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Check if password contains at least one symbol
            if (!password.matches(".*[!@#$%^&*()].*")) {
                Toast.makeText(this, "Password must contain at least one symbol (!@#$%^&*()).", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. Check if user already exists
            if (dbHelper.checkUserExists(userId)) {
                Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 5. Hash the password before storing
            String hashedPassword = Hash.hashPassword(password);
            boolean success = dbHelper.registerUser(userId, nameText, hashedPassword);

            if (success) {
                Toast.makeText(this, "Registered! Now login.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });

        doYouHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
