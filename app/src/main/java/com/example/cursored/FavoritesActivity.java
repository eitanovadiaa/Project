package com.example.cursored;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.cursored.model.FavoriteCar;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyView;
    private FavoriteAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference favoritesRef;
    private List<FavoriteCar> favoritesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Favorite Cars");
        }

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(userId);

        // Initialize views
        recyclerView = findViewById(R.id.favoritesRecyclerView);
        emptyView = findViewById(R.id.emptyView);

        // Setup RecyclerView
        favoritesList = new ArrayList<>();
        adapter = new FavoriteAdapter(favoritesList, this::removeFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load favorites
        loadFavorites();
    }

    private void loadFavorites() {
        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoritesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FavoriteCar favoriteCar = snapshot.getValue(FavoriteCar.class);
                    if (favoriteCar != null) {
                        favoriteCar.setId(snapshot.getKey());
                        favoritesList.add(favoriteCar);
                    }
                }
                
                adapter.notifyDataSetChanged();
                updateEmptyView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FavoritesActivity.this, 
                    "Error loading favorites: " + databaseError.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFavorite(FavoriteCar favoriteCar) {
        if (favoriteCar.getId() != null) {
            favoritesRef.child(favoriteCar.getId()).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, 
                    "Removed from favorites", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this,
                    "Failed to remove: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void updateEmptyView() {
        if (favoritesList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 