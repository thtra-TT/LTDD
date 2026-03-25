package com.example.vntravelapp.adapters;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import com.example.vntravelapp.models.Tour;
import java.util.ArrayList;
import java.util.List;

public class HomeSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_SECTION = 0;
    public static final int TYPE_TOUR = 1;
    public static final int TYPE_HOTEL = 2;
    public static final int TYPE_LOCATION = 3;

    private final List<SearchRow> rows = new ArrayList<>();

    public void submitSections(List<SearchSection> sections) {
        rows.clear();
        for (SearchSection section : sections) {
            if (section == null || section.items.isEmpty()) {
                continue;
            }
            rows.add(new SectionRow(section.title));
            rows.addAll(section.items);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_SECTION) {
            View view = inflater.inflate(R.layout.item_search_section, parent, false);
            return new SectionViewHolder(view);
        }
        if (viewType == TYPE_TOUR) {
            View view = inflater.inflate(R.layout.item_tour, parent, false);
            return new TourViewHolder(view);
        }
        if (viewType == TYPE_HOTEL) {
            View view = inflater.inflate(R.layout.item_hotel, parent, false);
            return new HotelViewHolder(view);
        }
        View view = inflater.inflate(R.layout.item_search_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchRow row = rows.get(position);
        if (holder instanceof SectionViewHolder) {
            ((SectionViewHolder) holder).bind((SectionRow) row);
        } else if (holder instanceof TourViewHolder) {
            ((TourViewHolder) holder).bind((TourRow) row);
        } else if (holder instanceof HotelViewHolder) {
            ((HotelViewHolder) holder).bind((HotelRow) row);
        } else if (holder instanceof LocationViewHolder) {
            ((LocationViewHolder) holder).bind((LocationRow) row);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public interface SearchRow {
        int getType();
    }

    public static class SearchSection {
        public final String title;
        public final List<SearchRow> items;

        public SearchSection(String title, List<SearchRow> items) {
            this.title = title;
            this.items = items == null ? new ArrayList<>() : items;
        }
    }

    public static class SectionRow implements SearchRow {
        public final String title;

        public SectionRow(String title) {
            this.title = title;
        }

        @Override
        public int getType() {
            return TYPE_SECTION;
        }
    }

    public static class TourRow implements SearchRow {
        public final Tour tour;
        public final double score;

        public TourRow(Tour tour, double score) {
            this.tour = tour;
            this.score = score;
        }

        @Override
        public int getType() {
            return TYPE_TOUR;
        }
    }

    public static class HotelRow implements SearchRow {
        public final Hotel hotel;
        public final double score;

        public HotelRow(Hotel hotel, double score) {
            this.hotel = hotel;
            this.score = score;
        }

        @Override
        public int getType() {
            return TYPE_HOTEL;
        }
    }

    public static class LocationRow implements SearchRow {
        public final String location;
        public final double score;

        public LocationRow(String location, double score) {
            this.location = location;
            this.score = score;
        }

        @Override
        public int getType() {
            return TYPE_LOCATION;
        }
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;

        SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSectionTitle);
        }

        void bind(SectionRow row) {
            tvTitle.setText(row.title);
        }
    }

    static class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTourImage;
        TextView tvTitle, tvLocation, tvDuration, tvPrice, tvRating, tvReviews, tvBadge, tvTourStatus;
        View vInactiveOverlay;

        TourViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTourImage = itemView.findViewById(R.id.ivTourImage);
            tvTitle = itemView.findViewById(R.id.tvTourTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviews = itemView.findViewById(R.id.tvReviews);
            tvBadge = itemView.findViewById(R.id.tvBadge);
            tvTourStatus = itemView.findViewById(R.id.tvTourStatus);
            vInactiveOverlay = itemView.findViewById(R.id.vInactiveOverlay);
        }

        void bind(TourRow row) {
            Tour tour = row.tour;
            tvTitle.setText(tour.getTitle());
            tvLocation.setText(tour.getLocation());
            tvDuration.setText(tour.getDuration());
            tvPrice.setText(tour.getPrice());
            tvRating.setText("⭐ " + tour.getRating());
            tvReviews.setText("(" + tour.getReviewCount() + " đánh giá)");

            boolean isActive = tour.isActive();
            if (!isActive) {
                if (vInactiveOverlay != null) vInactiveOverlay.setVisibility(View.VISIBLE);
                if (tvTourStatus != null) {
                    tvTourStatus.setVisibility(View.VISIBLE);
                    tvTourStatus.setText(tour.getStatusMessage());
                }
                itemView.setAlpha(0.7f);
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ivTourImage.setColorFilter(new ColorMatrixColorFilter(matrix));
            } else {
                if (vInactiveOverlay != null) vInactiveOverlay.setVisibility(View.GONE);
                if (tvTourStatus != null) tvTourStatus.setVisibility(View.GONE);
                itemView.setAlpha(1.0f);
                ivTourImage.setColorFilter(null);
            }

            if (tour.getBadge() != null && !tour.getBadge().trim().isEmpty()) {
                tvBadge.setVisibility(View.VISIBLE);
                tvBadge.setText(tour.getBadge());
            } else {
                tvBadge.setVisibility(View.GONE);
            }

            String primaryImageUrl = tour.getPrimaryImageUrl();

            if (primaryImageUrl != null && !primaryImageUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                    .load(primaryImageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(ivTourImage);
            } else if (tour.getImageResId() != 0) {
                ivTourImage.setImageResource(tour.getImageResId());
            }

            itemView.setOnClickListener(v -> {
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
                        primaryImageUrl,
                        new ArrayList<>(tour.getImageUrls()),
                        tour.getVideoUrl(),
                        tour.getRating(),
                        tour.getReviewCount(),
                        tour.getStartDate(),
                        tour.getEndDate()
                );
                activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            });
        }
    }

    static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHotel;
        TextView tvName, tvLocation, tvDescription, tvPrice, tvRating, tvReviews;

        HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHotel = itemView.findViewById(R.id.ivHotelImage);
            tvName = itemView.findViewById(R.id.tvHotelName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviews = itemView.findViewById(R.id.tvReviews);
        }

        void bind(HotelRow row) {
            Hotel hotel = row.hotel;
            tvName.setText(hotel.getName());
            tvLocation.setText(hotel.getLocation());
            tvDescription.setText(hotel.getDescription());
            tvPrice.setText(hotel.getPrice());
            tvRating.setText("★ " + hotel.getRating());
            tvReviews.setText("(" + hotel.getReviewCount() + " đánh giá)");

            if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                    .load(hotel.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(ivHotel);
            } else {
                ivHotel.setImageResource(hotel.getImageRes());
            }

            itemView.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                DetailFragment fragment = DetailFragment.newInstance(
                        hotel.getName(),
                        hotel.getLocation(),
                        hotel.getPrice(),
                        hotel.getDescription(),
                        "", // itinerary
                        "", // included
                        "", // excluded
                        hotel.getImageRes(),
                        hotel.getImageUrl(),
                        new ArrayList<>(),
                        null,
                        hotel.getRating(),
                        hotel.getReviewCount(),
                        null,
                        null
                );
                activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            });
        }
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLocation;

        LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tvLocationName);
        }

        void bind(LocationRow row) {
            tvLocation.setText(row.location);
        }
    }
}
