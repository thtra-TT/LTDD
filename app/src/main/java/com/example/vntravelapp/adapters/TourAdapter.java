package com.example.vntravelapp.adapters;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.fragments.DetailFragment;
import com.example.vntravelapp.models.Tour;
import java.util.ArrayList;
import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {

    private List<Tour> tourList;
    private Handler videoHandler = new Handler(Looper.getMainLooper());

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

        if (tour.getBadge() != null && !tour.getBadge().trim().isEmpty()) {
            holder.tvBadge.setVisibility(View.VISIBLE);
            holder.tvBadge.setText(tour.getBadge());
        } else {
            holder.tvBadge.setVisibility(View.GONE);
        }

        boolean hasVideo = tour.getVideoUrl() != null && !tour.getVideoUrl().trim().isEmpty();
        boolean canPreviewVideo = hasVideo && isDirectVideoUrl(tour.getVideoUrl());

        String primaryImageUrl = tour.getPrimaryImageUrl();

        // 1. Load Thumbnail trước
        if (primaryImageUrl != null && !primaryImageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(primaryImageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .centerCrop()
                    .into(holder.ivTourImage);
        } else if (tour.getImageResId() != 0) {
            holder.ivTourImage.setImageResource(tour.getImageResId());
        }

        // 2. Xử lý Video Preview (Autoplay & Loop)
        if (canPreviewVideo) {
            holder.ivPlayIcon.setVisibility(View.VISIBLE);

            // Trì hoãn load video một chút để tránh lag khi scroll nhanh (Lazy load)
            videoHandler.postDelayed(() -> {
                if (holder.getAdapterPosition() == position) {
                    holder.vvTourPreview.setVideoURI(Uri.parse(tour.getVideoUrl()));
                    holder.vvTourPreview.setOnPreparedListener(mp -> {
                        mp.setLooping(true);
                        mp.setVolume(0, 0); // Mute video
                        holder.vvTourPreview.start();

                        // Hiệu ứng Fade out ảnh, Fade in video khi video đã sẵn sàng
                        Animation fadeOut = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_out);
                        fadeOut.setDuration(1000);
                        holder.ivTourImage.startAnimation(fadeOut);
                        holder.ivTourImage.setVisibility(View.GONE);

                        holder.vvTourPreview.setVisibility(View.VISIBLE);
                        Animation fadeIn = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in);
                        fadeIn.setDuration(1000);
                        holder.vvTourPreview.startAnimation(fadeIn);
                        holder.ivPlayIcon.setVisibility(View.GONE);
                    });
                    holder.vvTourPreview.setOnErrorListener((mp, what, extra) -> {
                        holder.vvTourPreview.setVisibility(View.GONE);
                        holder.ivTourImage.setVisibility(View.VISIBLE);
                        holder.ivPlayIcon.setVisibility(View.VISIBLE);
                        return true;
                    });
                }
            }, 800);
        } else {
            holder.vvTourPreview.setVisibility(View.GONE);
            holder.ivTourImage.setVisibility(View.VISIBLE);
            holder.ivPlayIcon.setVisibility(hasVideo ? View.VISIBLE : View.GONE);
        }

        // 3. Hiệu ứng Click/Interaction
        holder.itemView.setOnClickListener(v -> {
            // Animation nhẹ khi click
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(() -> {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                DetailFragment fragment = DetailFragment.newInstance(
                        tour.getTitle(), tour.getLocation(), tour.getPrice(),
                        tour.getDescription(), tour.getItinerary(),
                        tour.getIncluded(), tour.getExcluded(),
                        tour.getImageResId(), primaryImageUrl,
                        new ArrayList<>(tour.getImageUrls()),
                        tour.getVideoUrl(),
                        tour.getRating(), tour.getReviewCount()
                );

                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        });
    }

    private static boolean isDirectVideoUrl(String url) {
        if (url == null) {
            return false;
        }
        String trimmed = url.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        String lower = trimmed.toLowerCase();
        if (lower.contains("youtube.com") || lower.contains("youtu.be")) {
            return false;
        }
        String path = null;
        try {
            path = Uri.parse(trimmed).getPath();
        } catch (Exception ignored) {
            path = null;
        }
        String target = path != null ? path.toLowerCase() : lower;
        return target.endsWith(".mp4") || target.endsWith(".m3u8") || target.endsWith(".webm") || target.endsWith(".3gp");
    }

    @Override
    public void onViewRecycled(@NonNull TourViewHolder holder) {
        super.onViewRecycled(holder);
        // Dừng video khi item bị trôi khỏi màn hình để tiết kiệm tài nguyên
        holder.vvTourPreview.stopPlayback();
        holder.vvTourPreview.setVisibility(View.GONE);
        holder.ivTourImage.setVisibility(View.VISIBLE);
        holder.ivPlayIcon.setVisibility(View.GONE);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TourViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.vvTourPreview.stopPlayback();
        holder.vvTourPreview.setVisibility(View.GONE);
        holder.ivTourImage.setVisibility(View.VISIBLE);
        holder.ivPlayIcon.setVisibility(View.GONE);
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
        ImageView ivTourImage, ivPlayIcon;
        VideoView vvTourPreview;
        TextView tvTitle, tvLocation, tvDuration, tvPrice, tvRating, tvReviews, tvBadge;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTourImage = itemView.findViewById(R.id.ivTourImage);
            vvTourPreview = itemView.findViewById(R.id.vvTourPreview);
            ivPlayIcon = itemView.findViewById(R.id.ivPlayIcon);
            tvTitle = itemView.findViewById(R.id.tvTourTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviews = itemView.findViewById(R.id.tvReviews);
            tvBadge = itemView.findViewById(R.id.tvBadge);
        }
    }
}
