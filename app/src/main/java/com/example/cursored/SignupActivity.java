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
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button signupButton;
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
        signupButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        // Setup signup button
        signupButton.setOnClickListener(v -> performSignup());


    }

    private void performSignup() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty()) {
            nameInput.setError("Name is required");
            return;
        }
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Valid email is required");
            return;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);

                if (task.isSuccessful()) {
                    // Sign up success, navigate to main activity
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this,
                        "Registration failed: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
                }
            });
    }
} 