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
import com.example.vntravelapp.models.ProfileTrip;

import java.util.List;

public class ProfileTripAdapter extends RecyclerView.Adapter<ProfileTripAdapter.TripViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(ProfileTrip trip);
    }

    private final List<ProfileTrip> trips;
    private final OnTripClickListener listener;

    public ProfileTripAdapter(List<ProfileTrip> trips, OnTripClickListener listener) {
        this.trips = trips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        ProfileTrip trip = trips.get(position);
        holder.tvTitle.setText(trip.getTitle());
        holder.tvSubtitle.setText(trip.getLocation());
        holder.tvMeta.setText(trip.getDate());
        holder.tvStatus.setText(trip.getStatus());
        holder.tvPrice.setText(trip.getPrice());

        if (trip.getImageUrl() == null || trip.getImageUrl().trim().isEmpty()) {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(trip.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivImage);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTripClick(trip);
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void updateItems(List<ProfileTrip> newItems) {
        trips.clear();
        trips.addAll(newItems);
        notifyDataSetChanged();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvMeta;
        TextView tvStatus;
        TextView tvPrice;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProfileTripImage);
            tvTitle = itemView.findViewById(R.id.tvProfileTripTitle);
            tvSubtitle = itemView.findViewById(R.id.tvProfileTripSubtitle);
            tvMeta = itemView.findViewById(R.id.tvProfileTripMeta);
            tvStatus = itemView.findViewById(R.id.tvProfileTripStatus);
            tvPrice = itemView.findViewById(R.id.tvProfileTripPrice);
        }
    }
}

