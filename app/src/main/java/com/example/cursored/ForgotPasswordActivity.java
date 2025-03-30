package com.example.cursored;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailInput;
    private Button resetButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        resetButton = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBar);

        // Setup reset button click listener
        resetButton.setOnClickListener(v -> performPasswordReset());
    }

    private void performPasswordReset() {
        String email = emailInput.getText().toString().trim();

        // Validate input
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        resetButton.setEnabled(false);

        // Send password reset email
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    resetButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this,
                                "Password reset email sent. Please check your email.",
                                Toast.LENGTH_LONG).show();
                        finish(); // Return to login screen
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this,
                                "Failed to send reset email: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
} 