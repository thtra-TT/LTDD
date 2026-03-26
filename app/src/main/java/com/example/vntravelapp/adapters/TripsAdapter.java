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
import com.example.vntravelapp.models.TripItem;

import java.util.ArrayList;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {

    public interface TripActionListener {
        void onViewDetail(TripItem item);
        void onCancel(TripItem item);
        void onReview(TripItem item);
        void onRebook(TripItem item);
        void onFavorite(TripItem item);
    }

    private final List<TripItem> items = new ArrayList<>();
    private final TripActionListener listener;

    public TripsAdapter(TripActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_card, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvLocation.setText(item.getLocation());
        holder.tvDate.setText(item.getDate());
        holder.tvStatus.setText(item.getStatus());
        holder.tvPrice.setText(item.getPrice());

        if (item.getReason() == null || item.getReason().trim().isEmpty()) {
            holder.tvReason.setVisibility(View.GONE);
        } else {
            holder.tvReason.setVisibility(View.VISIBLE);
            holder.tvReason.setText("Lý do: " + item.getReason());
        }

        String imageUrl = item.getImageUrl();
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivImage);
        }

        holder.btnPrimary.setOnClickListener(v -> {
            if (listener != null) listener.onViewDetail(item);
        });
        holder.btnSecondary.setOnClickListener(v -> {
            if (listener != null) {
                String status = item.getStatus();
                if ("Sắp đi".equals(status)) {
                    listener.onCancel(item);
                } else if ("Đã đi".equals(status)) {
                    listener.onReview(item);
                } else if ("Đã hủy".equals(status)) {
                    listener.onRebook(item);
                }
            }
        });
        holder.btnTertiary.setOnClickListener(v -> {
            if (listener != null) listener.onFavorite(item);
        });

        if ("Sắp đi".equals(item.getStatus())) {
            holder.btnPrimary.setText("Xem chi tiết");
            holder.btnSecondary.setText("Hủy chuyến");
            holder.btnSecondary.setVisibility(View.VISIBLE);
            holder.btnTertiary.setVisibility(View.GONE);
        } else if ("Đã đi".equals(item.getStatus())) {
            holder.btnPrimary.setText("Xem lại");
            holder.btnSecondary.setText("Viết review");
            holder.btnSecondary.setVisibility(View.VISIBLE);
            holder.btnTertiary.setText("Yêu thích");
            holder.btnTertiary.setVisibility(View.VISIBLE);
        } else {
            holder.btnPrimary.setText("Xem chi tiết");
            holder.btnSecondary.setText("Đặt lại");
            holder.btnSecondary.setVisibility(View.VISIBLE);
            holder.btnTertiary.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<TripItem> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    public List<TripItem> getItems() {
        return new ArrayList<>(items);
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvLocation;
        TextView tvDate;
        TextView tvStatus;
        TextView tvPrice;
        TextView tvReason;
        Button btnPrimary;
        Button btnSecondary;
        Button btnTertiary;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivTripImage);
            tvTitle = itemView.findViewById(R.id.tvTripTitle);
            tvLocation = itemView.findViewById(R.id.tvTripLocation);
            tvDate = itemView.findViewById(R.id.tvTripDate);
            tvStatus = itemView.findViewById(R.id.tvTripStatus);
            tvPrice = itemView.findViewById(R.id.tvTripPrice);
            tvReason = itemView.findViewById(R.id.tvTripReason);
            btnPrimary = itemView.findViewById(R.id.btnTripPrimary);
            btnSecondary = itemView.findViewById(R.id.btnTripSecondary);
            btnTertiary = itemView.findViewById(R.id.btnTripTertiary);
        }
    }
}

