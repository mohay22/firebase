package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.FirebaseApp;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword, etCity;
    private Button btnRegister;
    private TextView tvLoginLink, tvDateOfBirth;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private long selectedDateInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase before calling super.onCreate
        FirebaseApp app = FirebaseApp.initializeApp(this);
        if (app != null) {
            System.out.println("Firebase initialized: " + app.getName());
        } else {
            System.err.println("Firebase NOT initialized.");
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // Initialize FirebaseAuth and Firestore after Firebase is initialized
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCity = findViewById(R.id.etCity);
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        // On Click Listener for Register Button
        btnRegister.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String city = etCity.getText().toString().trim();

            if (selectedDateInMillis == 0) {
                Toast.makeText(RegisterActivity.this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase Authentication - create user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            db.collection("users").document(userId)
                                    .set(new User(firstName, lastName, email, selectedDateInMillis, city))
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Error saving user data!", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // On Click Listener for Login Link
        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        // On Click Listener for Date of Birth (Material DatePicker)
        tvDateOfBirth.setOnClickListener(v -> {
            // Initialize the Material DatePicker
            MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select your Date of Birth")
                    .build();

            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                // Store the selected date as milliseconds
                selectedDateInMillis = selection;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selectedDateInMillis);
                // Display the selected date in the TextView
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;  // 0-indexed
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                tvDateOfBirth.setText(day + "/" + month + "/" + year);
            });

            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });
    }

    public static class User {
        private String firstName, lastName, email, city;
        private long dateOfBirth;

        public User() {}

        public User(String firstName, String lastName, String email, long dateOfBirth, String city) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.dateOfBirth = dateOfBirth;
            this.city = city;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public long getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(long dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
