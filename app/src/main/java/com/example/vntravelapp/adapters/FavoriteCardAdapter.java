package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.models.Tour;

import java.util.ArrayList;
import java.util.List;

public class FavoriteCardAdapter extends RecyclerView.Adapter<FavoriteCardAdapter.FavoriteViewHolder> {

    public interface FavoriteActionListener {
        void onOpen(Tour tour);
        void onRemove(Tour tour);
    }

    private final List<Tour> items = new ArrayList<>();
    private final FavoriteActionListener listener;

    public FavoriteCardAdapter(FavoriteActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_card, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Tour tour = items.get(position);
        holder.tvTitle.setText(tour.getTitle());
        holder.tvLocation.setText(tour.getLocation());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOpen(tour);
        });
        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) listener.onRemove(tour);
        });

        Glide.with(holder.itemView.getContext())
                .load(tour.getPrimaryImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<Tour> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvLocation;
        TextView btnRemove;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivFavoriteImage);
            tvTitle = itemView.findViewById(R.id.tvFavoriteTitle);
            tvLocation = itemView.findViewById(R.id.tvFavoriteLocation);
            btnRemove = itemView.findViewById(R.id.tvRemoveFavorite);
        }
    }
}

