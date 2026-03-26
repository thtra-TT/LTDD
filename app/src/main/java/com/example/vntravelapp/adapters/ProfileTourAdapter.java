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

import java.util.List;

public class ProfileTourAdapter extends RecyclerView.Adapter<ProfileTourAdapter.TourViewHolder> {

    public interface OnTourClickListener {
        void onTourClick(Tour tour);
    }

    private final List<Tour> tours;
    private final OnTourClickListener listener;

    public ProfileTourAdapter(List<Tour> tours, OnTourClickListener listener) {
        this.tours = tours;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_card, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tours.get(position);
        holder.tvTitle.setText(tour.getTitle());
        holder.tvSubtitle.setText(tour.getLocation());
        holder.tvMeta.setText(tour.getPrice() + " • " + tour.getRating() + "⭐");
        if (tour.getBadge() == null || tour.getBadge().trim().isEmpty()) {
            holder.tvBadge.setVisibility(View.GONE);
        } else {
            holder.tvBadge.setText(tour.getBadge());
            holder.tvBadge.setVisibility(View.VISIBLE);
        }

        Glide.with(holder.itemView.getContext())
                .load(tour.getPrimaryImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTourClick(tour);
        });
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    public void updateItems(List<Tour> newItems) {
        tours.clear();
        tours.addAll(newItems);
        notifyDataSetChanged();
    }

    public Tour getItem(int position) {
        if (position < 0 || position >= tours.size()) return null;
        return tours.get(position);
    }

    static class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvMeta;
        TextView tvBadge;

        TourViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProfileCardImage);
            tvTitle = itemView.findViewById(R.id.tvProfileCardTitle);
            tvSubtitle = itemView.findViewById(R.id.tvProfileCardSubtitle);
            tvMeta = itemView.findViewById(R.id.tvProfileCardMeta);
            tvBadge = itemView.findViewById(R.id.tvProfileCardBadge);
        }
    }
}
