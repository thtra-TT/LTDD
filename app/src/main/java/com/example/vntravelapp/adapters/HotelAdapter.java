package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.fragments.DetailFragment;
import com.example.vntravelapp.models.Hotel;
import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotels;

    public HotelAdapter(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotels.get(position);
        holder.tvName.setText(hotel.getName());
        holder.tvLocation.setText(hotel.getLocation());
        holder.tvDescription.setText(hotel.getDescription());
        holder.tvPrice.setText(hotel.getPrice());
        holder.tvRating.setText("★ " + hotel.getRating());
        holder.tvReviews.setText("(" + hotel.getReviewCount() + " đánh giá)");

        if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(hotel.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivHotel);
        } else {
            holder.ivHotel.setImageResource(hotel.getImageRes());
        }

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            DetailFragment fragment = DetailFragment.newInstance(
                    hotel.getName(),
                    hotel.getLocation(),
                    hotel.getPrice(),
                    hotel.getDescription(),

                    "", // itinerary ❗
                    "", // included ❗
                    "", // excluded ❗

                    hotel.getImageRes(),
                    hotel.getImageUrl(),
                    new ArrayList<>(),
                    null,
                    hotel.getRating(),
                    hotel.getReviewCount()
            );
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public void updateHotels(List<Hotel> items) {
        this.hotels = items;
        notifyDataSetChanged();
    }

    static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHotel;
        TextView tvName, tvLocation, tvDescription, tvPrice, tvRating, tvReviews;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHotel = itemView.findViewById(R.id.ivHotelImage);
            tvName = itemView.findViewById(R.id.tvHotelName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviews = itemView.findViewById(R.id.tvReviews);
        }
    }
}
