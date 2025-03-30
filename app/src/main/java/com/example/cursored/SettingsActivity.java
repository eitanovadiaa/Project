package com.example.cursored;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    
    private Switch notificationsSwitch;
    private Switch darkModeSwitch;
    private Switch locationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        locationSwitch = findViewById(R.id.locationSwitch);

        }

    }


