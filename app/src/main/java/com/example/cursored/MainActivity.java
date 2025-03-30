package com.example.cursored;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.cursored.api.CarApiService;
import com.example.cursored.model.Car;
import com.example.cursored.model.ModelResponse;
import com.example.cursored.model.CarModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner brandSpinner;
    private Spinner engineSizeSpinner;
    private Spinner budgetSpinner;
    private Spinner yearSpinner;
    private Spinner seatsSpinner;
    private Spinner transmissionSpinner;
    private Button findButton;
    private TextView resultText;
    private CardView resultCard;
    private CarApiService apiService;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private TextView userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Not signed in, launch the Login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set user info in navigation drawer header
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_header_username);
        TextView navEmail = headerView.findViewById(R.id.nav_header_email);
        if (currentUser != null) {
            navUsername.setText(currentUser.getDisplayName());
            navEmail.setText(currentUser.getEmail());
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup navigation item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (itemId == R.id.nav_about) {
                startActivity(new Intent(this, AboutActivity.class));
            } else if (itemId == R.id.nav_logout) {
                signOut();
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Initialize views
        brandSpinner = findViewById(R.id.brandSpinner);
        engineSizeSpinner = findViewById(R.id.engineSizeSpinner);
        budgetSpinner = findViewById(R.id.budgetSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        seatsSpinner = findViewById(R.id.seatsSpinner);
        transmissionSpinner = findViewById(R.id.transmissionSpinner);
        findButton = findViewById(R.id.findButton);
        resultText = findViewById(R.id.resultText);
        resultCard = findViewById(R.id.resultCard);

        // Setup Retrofit with Car Query API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.carqueryapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(CarApiService.class);

        // Setup spinners
        setupSpinners();

        // Setup button click listener
        findButton.setOnClickListener(v -> findMatchingCar());
    }

    private void signOut() {
        mAuth.signOut();
        // Navigate to LoginActivity
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setupSpinners() {
        // Setup brand spinner
        String[] brands = {"toyota", "honda", "ford", "bmw", "mercedes", "volkswagen", "audi", "chevrolet"};
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, brands);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);

        // Setup year spinner
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        String[] years = new String[currentYear - 1940 + 1];
        for (int i = 0; i <= currentYear - 1940; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Setup seats spinner
        String[] seats = {"2 seats", "4 seats", "5 seats", "6 seats", "7 seats", "8+ seats"};
        ArrayAdapter<String> seatsAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, seats);
        seatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsSpinner.setAdapter(seatsAdapter);

        // Setup transmission spinner
        String[] transmissions = {"Automatic", "Manual", "Any"};
        ArrayAdapter<String> transmissionAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, transmissions);
        transmissionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transmissionSpinner.setAdapter(transmissionAdapter);

        // Setup engine size spinner
        String[] engineSizes = {"1.0L", "1.4L", "1.6L", "2.0L", "2.5L", "3.0L", "4.0L", "5.0L", "6.2L"};
        ArrayAdapter<String> engineAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, engineSizes);
        engineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        engineSizeSpinner.setAdapter(engineAdapter);

        // Setup budget spinner
        String[] budgets = {
                "Under $10,000",
                "$10,000 - $20,000",
                "$20,000 - $30,000",
                "$30,000 - $50,000",
                "$50,000 - $75,000",
                "Over $75,000"
        };
        ArrayAdapter<String> budgetAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, budgets);
        budgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(budgetAdapter);
    }

    private void findMatchingCar() {
        String selectedBrand = brandSpinner.getSelectedItem().toString();
        String selectedEngineSize = engineSizeSpinner.getSelectedItem().toString();
        String selectedBudget = budgetSpinner.getSelectedItem().toString();
        int selectedYear = Integer.parseInt(yearSpinner.getSelectedItem().toString());
        String selectedSeats = seatsSpinner.getSelectedItem().toString();
        String selectedTransmission = transmissionSpinner.getSelectedItem().toString();

        // Show loading state
        resultCard.setVisibility(View.GONE);

        // Get models for the selected brand and year
        Call<ModelResponse> call = apiService.getModels(
            "getModels", // cmd parameter
            selectedBrand.toLowerCase(), // make parameter
            selectedYear,
            1 // sold_in_us parameter
        );

        call.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                Log.d("API_RESPONSE", "Response code: " + response.code());
                if (response.body() != null) {
                    Log.d("API_RESPONSE", "Response body: " + response.body().getModels());
                }

                if (response.isSuccessful() && response.body() != null &&
                        response.body().getModels() != null &&
                        !response.body().getModels().isEmpty()) {

                    List<CarModel> models = response.body().getModels();
                    CarModel randomModel = models.get((int)(Math.random() * models.size()));

                    String resultString = String.format(
                            "Suggested Car:\nBrand: %s\nModel: %s\nYear: %d\n" +
                                    "Seats: %s\nTransmission: %s\nEngine Size: %s\nBudget Range: %s\n",
                            selectedBrand.toUpperCase(),
                            randomModel.getModelName(),
                            selectedYear,
                            selectedSeats,
                            selectedTransmission,
                            selectedEngineSize,
                            selectedBudget
                    );

                    resultText.setText(resultString);
                    resultCard.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this,
                            "No models found for " + selectedBrand + " in " + selectedYear,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Log.e("API_ERROR", "API call failed", t);
                Toast.makeText(MainActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}