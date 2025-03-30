package com.example.cursored;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        // Setup register button click listener
        registerButton.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validate input
        if (name.isEmpty()) {
            nameInput.setError("Name is required");
            nameInput.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            confirmPasswordInput.requestFocus();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser user = mAuth.getCurrentUser();
                        
                        // Update user profile with display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileTask -> {
                                    progressBar.setVisibility(View.GONE);
                                    if (profileTask.isSuccessful()) {
                                        // Profile updated successfully
                                        Toast.makeText(RegisterActivity.this,
                                                "Registration successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        // If profile update fails, still proceed but show warning
                                        Toast.makeText(RegisterActivity.this,
                                                "Registration successful but failed to set display name",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });
                    } else {
                        // If registration fails, display a message to the user.
                        progressBar.setVisibility(View.GONE);
                        registerButton.setEnabled(true);
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
} 