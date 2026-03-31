package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.models.Tour;

import java.util.ArrayList;
import java.util.List;

public class SellerTourAdapter extends RecyclerView.Adapter<SellerTourAdapter.SellerTourViewHolder> {

    public interface OnSellerTourActionListener {
        void onEdit(Tour tour);
        void onDelete(Tour tour);
    }

    private final List<Tour> items = new ArrayList<>();
    private final OnSellerTourActionListener listener;

    public SellerTourAdapter(List<Tour> data, OnSellerTourActionListener listener) {
        if (data != null) items.addAll(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SellerTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_tour, parent, false);
        return new SellerTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerTourViewHolder holder, int position) {
        Tour tour = items.get(position);

        holder.tvTitle.setText(tour.getTitle());
        holder.tvLocation.setText(tour.getLocation());
        holder.tvPrice.setText(tour.getPrice());

        String imageUrl = tour.getPrimaryImageUrl();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .centerCrop()
                    .into(holder.ivTourImage);
        } else {
            holder.ivTourImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(tour);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(tour);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<Tour> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class SellerTourViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTourImage;
        TextView tvTitle, tvLocation, tvPrice;
        Button btnEdit, btnDelete;

        public SellerTourViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTourImage = itemView.findViewById(R.id.ivSellerTourImage);
            tvTitle = itemView.findViewById(R.id.tvSellerTourTitle);
            tvLocation = itemView.findViewById(R.id.tvSellerTourLocation);
            tvPrice = itemView.findViewById(R.id.tvSellerTourPrice);
            btnEdit = itemView.findViewById(R.id.btnEditTour);
            btnDelete = itemView.findViewById(R.id.btnDeleteTour);
        }
    }
}