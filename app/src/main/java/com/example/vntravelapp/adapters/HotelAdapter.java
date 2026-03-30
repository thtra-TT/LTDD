package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.fragments.DetailFragment;
import com.example.vntravelapp.models.Hotel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotels;
    private DatabaseHelper dbHelper;
    private Set<String> favoriteTitles = new HashSet<>();

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
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(holder.itemView.getContext());
            favoriteTitles = new HashSet<>(dbHelper.getFavoriteTitles("Hotel"));
        }
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
            DetailFragment fragment = DetailFragment.newInstanceWithItem(DetailFragment.ITEM_TYPE_HOTEL, hotel.getId());
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        boolean isFavorite = favoriteTitles.contains(hotel.getName());
        updateFavoriteIcon(holder.ivFavorite, isFavorite);
        holder.ivFavorite.setOnClickListener(v -> {
            boolean wasFavorite = favoriteTitles.contains(hotel.getName());
            if (wasFavorite) {
                dbHelper.removeFavorite(hotel.getName(), "Hotel");
                favoriteTitles.remove(hotel.getName());
                Toast.makeText(holder.itemView.getContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addFavorite(hotel.getName(), "Hotel");
                favoriteTitles.add(hotel.getName());
                Toast.makeText(holder.itemView.getContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon(holder.ivFavorite, !wasFavorite);
            v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(120)
                    .withEndAction(() -> v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(120));
        });
    }

    private void updateFavoriteIcon(ImageView view, boolean isFavorite) {
        int resId = isFavorite ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline;
        view.setImageResource(resId);
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
        ImageView ivFavorite;
        TextView tvName, tvLocation, tvDescription, tvPrice, tvRating, tvReviews;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHotel = itemView.findViewById(R.id.ivHotelImage);
            ivFavorite = itemView.findViewById(R.id.ivFavoriteHotel);
            tvName = itemView.findViewById(R.id.tvHotelName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviews = itemView.findViewById(R.id.tvReviews);
        }
    }
}
