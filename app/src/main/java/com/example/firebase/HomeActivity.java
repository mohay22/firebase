package com.example.firebase;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome, tvAge, tvCity;
    private EditText etUserText; // Reference to EditText
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI elements
        tvWelcome = findViewById(R.id.tvWelcome);
        tvAge = findViewById(R.id.tvAge);
        tvCity = findViewById(R.id.tvCity);
        etUserText = findViewById(R.id.etUserText); // Initialize EditText

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get the current user's ID
        userId = mAuth.getCurrentUser().getUid();

        // Fetch user data from Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    try {
                        if (documentSnapshot.exists()) {
                            // Fetch first name, last name, and city
                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String city = documentSnapshot.getString("city");

                            // Check if first name, last name, and city are not null
                            if (firstName != null && lastName != null && city != null) {
                                // Update the tvWelcome TextView with the full name directly in the UI
                                tvWelcome.setText("Your first name: " + firstName + "\nYour Last name: " + lastName);

                                // Update the tvCity TextView with the city
                                tvCity.setText("Your city: " + city);  // Display the city
                            } else {
                                tvWelcome.setText("Name or city data not available.");
                            }

                            // Fetch and display the saved text
                            String savedText = documentSnapshot.getString("savedText");
                            if (savedText != null) {
                                etUserText.setText(savedText); // Populate the EditText with saved text
                            }

                            // Fetch and display the dateOfBirth as a number (timestamp)
                            Long dateOfBirth = documentSnapshot.getLong("dateOfBirth");

                            if (dateOfBirth != null) {
                                // Convert the Unix timestamp to Date object (assuming it's in milliseconds)
                                Date dob = new Date(dateOfBirth);  // No multiplication by 1000 needed

                                // Get current date
                                Calendar dobCalendar = Calendar.getInstance();
                                dobCalendar.setTime(dob);

                                int yearOfBirth = dobCalendar.get(Calendar.YEAR);
                                int monthOfBirth = dobCalendar.get(Calendar.MONTH);
                                int dayOfBirth = dobCalendar.get(Calendar.DAY_OF_MONTH);

                                Calendar currentCalendar = Calendar.getInstance();
                                int currentYear = currentCalendar.get(Calendar.YEAR);
                                int currentMonth = currentCalendar.get(Calendar.MONTH);
                                int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

                                // Calculate the age in years, months, and days
                                int ageInYears = currentYear - yearOfBirth;
                                int ageInMonths = currentMonth - monthOfBirth;
                                int ageInDays = currentDay - dayOfBirth;

                                // Adjust the months and days if needed
                                if (ageInDays < 0) {
                                    // If the current day is less than the birth day, adjust months
                                    ageInDays += currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                    ageInMonths--;
                                }
                                if (ageInMonths < 0) {
                                    // If the current month is less than the birth month, adjust years
                                    ageInMonths += 12;
                                    ageInYears--;
                                }

                                // Display age in tvAge TextView
                                if (ageInYears > 0) {
                                    tvAge.setText("Your age: " + ageInYears + " years");
                                } else if (ageInMonths > 0) {
                                    tvAge.setText("Your age: " + ageInMonths + " months");
                                } else {
                                    tvAge.setText("Your age: " + ageInDays + " days");
                                }

                            } else {
                                tvAge.setText("Date of birth not available.");
                            }
                        } else {
                            Log.d(TAG, "Document does not exist");
                            tvWelcome.setText("Error fetching user data.");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception caught: ", e);
                        tvWelcome.setText("Error: " + e.getMessage());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user data", e);
                    tvWelcome.setText("Error fetching user data.");
                });

        // Button to save data
        Button btnSave = findViewById(R.id.btnSave); // Save button

        btnSave.setOnClickListener(v -> {
            String textToSave = etUserText.getText().toString().trim(); // Get the text from EditText

            if (!textToSave.isEmpty()) {
                // Save the text in Firestore
                db.collection("users").document(userId).update("savedText", textToSave)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(HomeActivity.this, "Text saved successfully to firebase", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Text saved successfully");
                        })
                        .addOnFailureListener(e -> Log.e(TAG, "Error saving text", e));
            }
        });
    }
}
