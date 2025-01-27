package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase (if it's not already initialized)
        FirebaseApp.initializeApp(this);
        // Initialize UI elements
        Log.d("LoginActivity", "Firebase is connected successfully!");
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        // On Click Listener for Login Button
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Firebase authentication code to sign in user
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                           Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to Home Activity if login is successful
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish(); // Close Login Activity
                        } else {
                            // Show error message if login fails
                            Toast.makeText(LoginActivity.this, "Login failed! Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // On Click Listener for Register Link
        tvRegisterLink.setOnClickListener(v -> {
            // Navigate to the Register Activity
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
