package com.example.cursored;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cursored.model.FavoriteCar;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<FavoriteCar> favorites;
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onRemoveClick(FavoriteCar favoriteCar);
    }

    public FavoriteAdapter(List<FavoriteCar> favorites, OnFavoriteClickListener listener) {
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_favorite_car, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteCar favoriteCar = favorites.get(position);
        String carInfo = String.format(
            "Brand: %s\nModel: %s\nYear: %s\nEngine Size: %s\nBudget Range: %s",
            favoriteCar.getBrand().toUpperCase(),
            favoriteCar.getModel(),
            favoriteCar.getYear(),
            favoriteCar.getEngineSize(),
            favoriteCar.getBudget()
        );
        holder.carInfoText.setText(carInfo);
        holder.removeButton.setOnClickListener(v -> listener.onRemoveClick(favoriteCar));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView carInfoText;
        ImageButton removeButton;

        FavoriteViewHolder(View itemView) {
            super(itemView);
            carInfoText = itemView.findViewById(R.id.carInfoText);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
} 