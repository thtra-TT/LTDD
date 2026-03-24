package com.example.vntravelapp.adapters;

import android.graphics.Color;
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
import com.example.vntravelapp.models.Trip;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> tripList;

    public TripAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.tvTitle.setText(trip.getTitle());
        holder.tvLocation.setText(trip.getLocation());
        holder.tvDate.setText(trip.getDate());
        holder.tvStatus.setText(trip.getStatus());
        holder.tvBookingCode.setText("Mã đặt chỗ: " + trip.getBookingCode());
        holder.tvPrice.setText(trip.getPrice());

        if (trip.getStatus().equals("Đã xác nhận")) {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#FBC02D"));
        }

        if (trip.isCanPayNow()) {
            holder.btnAction.setText("Thanh toán ngay");
            holder.btnAction.setBackgroundColor(Color.BLACK);
            holder.btnAction.setTextColor(Color.WHITE);
        } else {
            holder.btnAction.setText("Xem chi tiết");
        }

        if (trip.getImageResId() != 0) {
            holder.ivTripImage.setImageResource(trip.getImageResId());
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(trip.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivTripImage);
        }
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTripImage;
        TextView tvTitle, tvLocation, tvDate, tvStatus, tvBookingCode, tvPrice;
        Button btnAction;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTripImage = itemView.findViewById(R.id.ivTripImage);
            tvTitle = itemView.findViewById(R.id.tvTripTitle);
            tvLocation = itemView.findViewById(R.id.tvTripLocation);
            tvDate = itemView.findViewById(R.id.tvTripDate);
            tvStatus = itemView.findViewById(R.id.tvTripStatus);
            tvBookingCode = itemView.findViewById(R.id.tvBookingCode);
            tvPrice = itemView.findViewById(R.id.tvTripPrice);
            btnAction = itemView.findViewById(R.id.btnTripAction);
        }
    }
}