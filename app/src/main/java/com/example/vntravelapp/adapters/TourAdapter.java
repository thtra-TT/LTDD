package com.example.vntravelapp.adapters;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vntravelapp.AddEditTourActivity;
import com.example.vntravelapp.R;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.fragments.DetailFragment;
import com.example.vntravelapp.models.Tour;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {

    private List<Tour> tourList;
    private Handler videoHandler = new Handler(Looper.getMainLooper());
    private DatabaseHelper dbHelper;
    private Set<String> favoriteTitles = new HashSet<>();

    private boolean isSeller;
    private String sellerEmail;
    public TourAdapter(List<Tour> tourList, boolean isSeller, String sellerEmail) {
        this.tourList = tourList;
        this.isSeller = isSeller;
        this.sellerEmail = sellerEmail;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tour, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        LinearLayout sellerLayout = holder.itemView.findViewById(R.id.layoutSellerActions);
        Button btnEdit = holder.itemView.findViewById(R.id.btnEditTour);
        Button btnDelete = holder.itemView.findViewById(R.id.btnDeleteTour);

        if (isSeller) {
            sellerLayout.setVisibility(View.VISIBLE);
        } else {
            sellerLayout.setVisibility(View.GONE);
        }

        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(holder.itemView.getContext());
            favoriteTitles = new HashSet<>(dbHelper.getFavoriteTitles("Tour"));
        }
        Tour tour = tourList.get(position);
        holder.tvTitle.setText(tour.getTitle());
        holder.tvLocation.setText(tour.getLocation());
        holder.tvDuration.setText(tour.getDuration());
        holder.tvPrice.setText(tour.getPrice());
        holder.tvRating.setText(String.format(Locale.getDefault(), "⭐ %.1f", tour.getRating()));
        holder.tvReviews.setText(String.format(Locale.getDefault(), "(%d đánh giá)", tour.getReviewCount()));

        // Hiển thị số lượt đặt
        int books = tour.getBookCount();
        if (books >= 1000) {
            holder.tvBookCount.setText(String.format(Locale.getDefault(), "• %.1fk đã đặt", books / 1000.0f));
        } else {
            holder.tvBookCount.setText(String.format(Locale.getDefault(), "• %d đã đặt", books));
        }

        // Xử lý trạng thái hiệu lực
        boolean isActive = tour.isActive();
        if (!isActive) {
            holder.vInactiveOverlay.setVisibility(View.VISIBLE);
            holder.tvTourStatus.setVisibility(View.VISIBLE);
            holder.tvTourStatus.setText(tour.getStatusMessage());
            holder.itemView.setAlpha(0.7f);

            // Áp dụng hiệu ứng màu xám cho ảnh
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.ivTourImage.setColorFilter(filter);
        } else {
            holder.vInactiveOverlay.setVisibility(View.GONE);
            holder.tvTourStatus.setVisibility(View.GONE);
            holder.itemView.setAlpha(1.0f);
            holder.ivTourImage.setColorFilter(null);
        }

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AddEditTourActivity.class);
            intent.putExtra("mode", "edit");
            intent.putExtra("tour_id", tour.getId());
            holder.itemView.getContext().startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(holder.itemView.getContext());
            boolean success = db.deleteSellerTour(tour.getId());

            if (success) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    tourList.remove(pos);
                    notifyItemRemoved(pos);
                }
                Toast.makeText(holder.itemView.getContext(), "Đã xóa tour", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý Badge thông minh
        String badge = tour.getBadge();
        if (badge != null && !badge.trim().isEmpty()) {
            holder.tvBadge.setVisibility(View.VISIBLE);
            holder.tvBadge.setText(badge);
            // Có thể đổi màu badge dựa trên text
            if (badge.equalsIgnoreCase("HOT")) {
                holder.tvBadge.setTextColor(0xFFFF5722);
            } else if (badge.equalsIgnoreCase("BEST SELLER")) {
                holder.tvBadge.setTextColor(0xFFE91E63);
            } else {
                holder.tvBadge.setTextColor(0xFF2196F3);
            }
        } else {
            // Tự động gán badge nếu không có
            if (tour.getBookCount() > 2000) {
                holder.tvBadge.setVisibility(View.VISIBLE);
                holder.tvBadge.setText("BEST SELLER");
                holder.tvBadge.setTextColor(0xFFE91E63);
            } else if (tour.getRating() >= 4.9) {
                holder.tvBadge.setVisibility(View.VISIBLE);
                holder.tvBadge.setText("TOP RATED");
                holder.tvBadge.setTextColor(0xFF4CAF50);
            } else {
                holder.tvBadge.setVisibility(View.GONE);
            }
        }

        boolean hasVideo = tour.getVideoUrl() != null && !tour.getVideoUrl().trim().isEmpty();
        boolean canPreviewVideo = hasVideo && isDirectVideoUrl(tour.getVideoUrl()) && isActive;

        String primaryImageUrl = tour.getPrimaryImageUrl();

        if (primaryImageUrl != null && !primaryImageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(primaryImageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .centerCrop()
                    .into(holder.ivTourImage);
        } else if (tour.getImageResId() != 0) {
            holder.ivTourImage.setImageResource(tour.getImageResId());
        }

        if (canPreviewVideo) {
            holder.ivPlayIcon.setVisibility(View.VISIBLE);
            videoHandler.postDelayed(() -> {
                if (holder.getAdapterPosition() == position) {
                    holder.vvTourPreview.setVideoURI(Uri.parse(tour.getVideoUrl()));
                    holder.vvTourPreview.setOnPreparedListener(mp -> {
                        mp.setLooping(true);
                        mp.setVolume(0, 0);
                        holder.vvTourPreview.start();
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
                }
            }, 800);
        } else {
            holder.vvTourPreview.setVisibility(View.GONE);
            holder.ivTourImage.setVisibility(View.VISIBLE);
            holder.ivPlayIcon.setVisibility(hasVideo && isActive ? View.VISIBLE : View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(() -> {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                DetailFragment fragment = DetailFragment.newInstanceWithItem(DetailFragment.ITEM_TYPE_TOUR, tour.getId());
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        });

        boolean isFavorite = favoriteTitles.contains(tour.getTitle());
        updateFavoriteIcon(holder.ivFavorite, isFavorite);
        holder.ivFavorite.setOnClickListener(v -> {
            boolean wasFavorite = favoriteTitles.contains(tour.getTitle());
            if (wasFavorite) {
                dbHelper.removeFavorite(tour.getTitle(), "Tour");
                favoriteTitles.remove(tour.getTitle());
                Toast.makeText(holder.itemView.getContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addFavorite(tour.getTitle(), "Tour");
                favoriteTitles.add(tour.getTitle());
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

    private static boolean isDirectVideoUrl(String url) {
        if (url == null) return false;
        String trimmed = url.trim();
        if (trimmed.isEmpty()) return false;
        String lower = trimmed.toLowerCase();
        if (lower.contains("youtube.com") || lower.contains("youtu.be")) return false;
        String path = null;
        try { path = Uri.parse(trimmed).getPath(); } catch (Exception ignored) {}
        String target = path != null ? path.toLowerCase() : lower;
        return target.endsWith(".mp4") || target.endsWith(".m3u8") || target.endsWith(".webm") || target.endsWith(".3gp");
    }

    @Override
    public void onViewRecycled(@NonNull TourViewHolder holder) {
        super.onViewRecycled(holder);
        holder.vvTourPreview.stopPlayback();
        holder.vvTourPreview.setVisibility(View.GONE);
        holder.ivTourImage.setVisibility(View.VISIBLE);
        holder.ivPlayIcon.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() { return tourList.size(); }

    public void updateData(List<Tour> newTours) {
        this.tourList.clear();
        this.tourList.addAll(newTours);
        notifyDataSetChanged();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTourImage, ivPlayIcon, ivFavorite;
        VideoView vvTourPreview;
        TextView tvTitle, tvLocation, tvDuration, tvPrice, tvRating, tvReviews, tvBadge, tvTourStatus, tvBookCount;
        View vInactiveOverlay;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTourImage = itemView.findViewById(R.id.ivTourImage);
            vvTourPreview = itemView.findViewById(R.id.vvTourPreview);
            ivPlayIcon = itemView.findViewById(R.id.ivPlayIcon);
            ivFavorite = itemView.findViewById(R.id.ivFavoriteTour);
            tvTitle = itemView.findViewById(R.id.tvTourTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviews = itemView.findViewById(R.id.tvReviews);
            tvBadge = itemView.findViewById(R.id.tvBadge);
            tvTourStatus = itemView.findViewById(R.id.tvTourStatus);
            tvBookCount = itemView.findViewById(R.id.tvBookCount);
            vInactiveOverlay = itemView.findViewById(R.id.vInactiveOverlay);
        }
    }
}
