package com.example.cursored;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private EditText nicknameInput;
    private Button changePhotoButton;
    private Button saveButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        nicknameInput = findViewById(R.id.nicknameInput);
        changePhotoButton = findViewById(R.id.changePhotoButton);
        saveButton = findViewById(R.id.saveButton);
        progressBar = findViewById(R.id.progressBar);

        // Load current user data
        loadUserProfile();

        // Setup button listeners
        changePhotoButton.setOnClickListener(v -> openImagePicker());
        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Load nickname
            nicknameInput.setText(user.getDisplayName());

            // Load profile picture
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                    .load(user.getPhotoUrl())
                    .circleCrop()
                    .placeholder(R.drawable.default_profile)
                    .into(profileImageView);
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK 
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this)
                .load(imageUri)
                .circleCrop()
                .into(profileImageView);
        }
    }

    private void saveProfile() {
        String nickname = nicknameInput.getText().toString().trim();
        if (nickname.isEmpty()) {
            nicknameInput.setError("Nickname is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        if (imageUri != null) {
            // Upload new profile picture
            StorageReference imageRef = storageRef.child("profile_pictures/" + user.getUid());
            imageRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        updateProfile(nickname, downloadUri);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        saveButton.setEnabled(true);
                        Toast.makeText(ProfileActivity.this, 
                            "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            // Just update nickname
            updateProfile(nickname, user.getPhotoUrl());
        }
    }

    private void updateProfile(String nickname, Uri photoUrl) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setDisplayName(nickname)
            .setPhotoUri(photoUrl)
            .build();

        user.updateProfile(profileUpdates)
            .addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                saveButton.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, 
                        "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, 
                        "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 