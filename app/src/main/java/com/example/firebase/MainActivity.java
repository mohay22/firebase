package com.example.firebase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseExample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        Log.d(TAG, "Firebase is connected successfully!");

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get references to input fields and button
        EditText etName = findViewById(R.id.etName);
        EditText etAge = findViewById(R.id.etAge);
        EditText etCity = findViewById(R.id.etCity);
        Button saveButton = findViewById(R.id.saveButton);

        // Set up the save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input from the user
                String name = etName.getText().toString().trim();
                String ageStr = etAge.getText().toString().trim();
                String city = etCity.getText().toString().trim();

                // Validate input
                if (name.isEmpty() || ageStr.isEmpty() || city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Age must be a valid number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Prepare data to save
                Map<String, Object> userData = new HashMap<>();
                userData.put("name", name);
                userData.put("age", age);
                userData.put("city", city);

                // Save data to Firestore
                db.collection("users")
                        .add(userData)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Document added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                            // Clear input fields
                            etName.setText("");
                            etAge.setText("");
                            etCity.setText("");
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
