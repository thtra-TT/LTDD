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
import com.example.vntravelapp.models.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    public interface FavoriteActionListener {
        void onOpen(FavoriteItem item);
        void onRemove(FavoriteItem item);
    }

    private final List<FavoriteItem> items = new ArrayList<>();
    private final FavoriteActionListener listener;

    public FavoritesAdapter(FavoriteActionListener listener) {
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
        FavoriteItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvLocation.setText(item.getLocation());
        holder.tvType.setText(item.getItemType());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOpen(item);
        });
        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) listener.onRemove(item);
        });

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<FavoriteItem> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvLocation;
        TextView tvType;
        TextView btnRemove;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivFavoriteImage);
            tvTitle = itemView.findViewById(R.id.tvFavoriteTitle);
            tvLocation = itemView.findViewById(R.id.tvFavoriteLocation);
            tvType = itemView.findViewById(R.id.tvFavoriteType);
            btnRemove = itemView.findViewById(R.id.tvRemoveFavorite);
        }
    }
}

