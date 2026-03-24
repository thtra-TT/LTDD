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
import com.example.vntravelapp.models.Tour;
import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {

    private List<Tour> tourList;

    public TourAdapter(List<Tour> tourList) {
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tour, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);
        holder.tvTitle.setText(tour.getTitle());
        holder.tvLocation.setText(tour.getLocation());
        holder.tvDuration.setText(tour.getDuration());
        holder.tvPrice.setText(tour.getPrice());
        holder.tvRating.setText("⭐ " + tour.getRating());
        holder.tvReviews.setText("(" + tour.getReviewCount() + " đánh giá)");

        if (tour.getImageUrl() != null && !tour.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(tour.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivTourImage);
        } else if (tour.getImageResId() != 0) {
            holder.ivTourImage.setImageResource(tour.getImageResId());
        }

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();

            DetailFragment fragment = DetailFragment.newInstance(
                    tour.getTitle(),
                    tour.getLocation(),
                    tour.getPrice(),
                    tour.getDescription(),
                    tour.getItinerary(),
                    tour.getIncluded(),
                    tour.getExcluded(),
                    tour.getImageResId(),
                    tour.getImageUrl(),
                    tour.getRating(),
                    tour.getReviewCount()
            );

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public void updateTours(List<Tour> tours) {
        this.tourList = tours;
        notifyDataSetChanged();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTourImage;
        TextView tvTitle, tvLocation, tvDuration, tvPrice, tvRating, tvReviews;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTourImage = itemView.findViewById(R.id.ivTourImage);
            tvTitle = itemView.findViewById(R.id.tvTourTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviews = itemView.findViewById(R.id.tvReviews);
        }
    }
}
