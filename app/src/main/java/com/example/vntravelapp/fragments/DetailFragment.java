package com.example.vntravelapp.fragments;

import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.CommentItem;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.Tour;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailFragment extends Fragment {

    public static final String ITEM_TYPE_TOUR = "Tour";
    public static final String ITEM_TYPE_HOTEL = "Hotel";

    private String title, location, price, description, imageUrl, videoUrl;
    private int imageRes;
    private float rating;
    private int reviews;
    private String itinerary, included, excluded;
    private String startDate, endDate;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private int tourId = -1;

    private static final String[][] REVIEW_POOL = {
            {"Nguyễn Minh Tuấn", "5", "Tour rất tuyệt! Hướng dẫn viên nhiệt tình, lịch trình hợp lý. Sẽ quay lại lần sau."},
            {"Trần Thị Lan", "5", "Dịch vụ tốt, khách sạn sạch sẽ, đồ ăn ngon. Rất đáng tiền!"},
            {"Phạm Văn Hùng", "4", "Trải nghiệm tốt, chỉ tiếc thời tiết không ủng hộ ngày 2. Nhìn chung rất hài lòng."},
            {"Lê Thu Hương", "5", "Mình đi cùng gia đình, các bé rất thích. Sẽ giới thiệu cho bạn bè!"},
            {"Đỗ Quang Hải", "4", "Tổ chức chuyên nghiệp, đúng giờ. Xe đưa đón thoải mái, hướng dẫn viên vui tính."},
            {"Vũ Thị Mai", "5", "Lần đầu đi tour nhưng cảm thấy rất an tâm. Đặt tour lần sau chắc chắn sẽ chọn lại."},
            {"Ngô Bá Khá", "3", "Tour ổn, nhưng một số điểm tham quan hơi vội. Hy vọng lần sau có thêm thời gian tự do."},
            {"Bùi Thanh Xuân", "5", "Tuyệt vời! Ảnh chụp được cực đẹp, kỷ niệm khó quên."},
    };

    public static DetailFragment newInstanceWithItem(String itemType, int itemId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("itemType", itemType);
        args.putInt("itemId", itemId);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstance(String title, String location, String price,
                                             String description, String itinerary,
                                             String included, String excluded,
                                             int imageRes, String imageUrl, ArrayList<String> imageUrls, String videoUrl,
                                             float rating, int reviews, String startDate, String endDate) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("location", location);
        args.putString("price", price);
        args.putString("description", description);
        args.putString("itinerary", itinerary);
        args.putString("included", included);
        args.putString("excluded", excluded);
        args.putInt("imageRes", imageRes);
        args.putString("imageUrl", imageUrl);
        args.putStringArrayList("imageUrls", imageUrls);
        args.putString("videoUrl", videoUrl);
        args.putFloat("rating", rating);
        args.putInt("reviews", reviews);
        args.putString("startDate", startDate);
        args.putString("endDate", endDate);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isTourActive() {
        return Tour.isActiveOn(startDate, endDate, null);
    }

    private String getStatusMessage() {
        return Tour.getStatusMessage(startDate, endDate, null);
    }

    private String getTimeRangeDescription() {
        return Tour.getDisplayTimeRange(startDate, endDate);
    }

    private static boolean isDirectVideoUrl(String url) {
        if (url == null) return false;
        String trimmed = url.trim();
        if (trimmed.isEmpty()) return false;
        String lower = trimmed.toLowerCase();
        if (lower.contains("youtube.com") || lower.contains("youtu.be")) return false;
        String path;
        try {
            path = Uri.parse(trimmed).getPath();
        } catch (Exception ignored) {
            path = null;
        }
        String target = path != null ? path.toLowerCase() : lower;
        return target.endsWith(".mp4")
                || target.endsWith(".m3u8")
                || target.endsWith(".webm")
                || target.endsWith(".3gp");
    }

    private static boolean isYouTubeUrl(String url) {
        if (url == null) return false;
        String lower = url.toLowerCase();
        return lower.contains("youtube.com") || lower.contains("youtu.be");
    }

    private static List<String> parseVideoUrls(String raw) {
        List<String> list = new ArrayList<>();
        if (raw == null) return list;
        String[] parts = raw.split("\\|");
        for (String part : parts) {
            if (part == null) continue;
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) list.add(trimmed);
        }
        return list;
    }

    private static String buildYouTubeEmbedUrl(String url) {
        String videoId = null;
        if (url.contains("youtu.be/")) {
            videoId = url.substring(url.lastIndexOf("/") + 1);
        } else if (url.contains("watch?v=")) {
            int start = url.indexOf("v=") + 2;
            int end = url.indexOf("&", start);
            videoId = end > start ? url.substring(start, end) : url.substring(start);
        }
        if (videoId == null || videoId.trim().isEmpty()) return url;
        return "https://www.youtube.com/embed/" + videoId + "?autoplay=1&mute=1&playsinline=1&controls=1&rel=0";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;

        int itemId = getArguments().getInt("itemId", -1);
        String itemType = getArguments().getString("itemType");
        if (itemId > 0 && itemType != null) {
            loadItemFromDatabase(itemType, itemId);
            return;
        }

        title = getArguments().getString("title");
        location = getArguments().getString("location");
        price = getArguments().getString("price");
        description = getArguments().getString("description");
        itinerary = getArguments().getString("itinerary");
        included = getArguments().getString("included");
        excluded = getArguments().getString("excluded");
        imageRes = getArguments().getInt("imageRes");
        imageUrl = getArguments().getString("imageUrl");
        ArrayList<String> urls = getArguments().getStringArrayList("imageUrls");
        imageUrls = urls == null ? new ArrayList<>() : urls;
        videoUrl = getArguments().getString("videoUrl");
        rating = getArguments().getFloat("rating");
        reviews = getArguments().getInt("reviews");
        startDate = getArguments().getString("startDate");
        endDate = getArguments().getString("endDate");
    }

    private void loadItemFromDatabase(@NonNull String itemType, int itemId) {
        DatabaseHelper db = new DatabaseHelper(requireContext());

        if (ITEM_TYPE_HOTEL.equals(itemType)) {
            Hotel hotel = db.getHotelById(itemId);
            if (hotel == null) return;

            title = hotel.getName();
            location = hotel.getLocation();
            price = hotel.getPrice();
            description = hotel.getDescription();
            itinerary = "";
            included = "";
            excluded = "";
            imageRes = hotel.getImageRes();
            imageUrl = hotel.getImageUrl();
            imageUrls = new ArrayList<>();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                imageUrls.add(imageUrl.trim());
            }
            videoUrl = null;
            rating = hotel.getRating();
            reviews = hotel.getReviewCount();
            startDate = null;
            endDate = null;
            tourId = -1;
            return;
        }

        Tour tour = db.getTourById(itemId);
        if (tour == null) return;

        title = tour.getTitle();
        location = tour.getLocation();
        price = tour.getPrice();
        description = tour.getDescription();
        itinerary = tour.getItinerary();
        included = tour.getIncluded();
        excluded = tour.getExcluded();
        imageRes = tour.getImageResId();
        imageUrl = tour.getPrimaryImageUrl();
        imageUrls = new ArrayList<>(tour.getImageUrls());
        videoUrl = tour.getVideoUrl();
        rating = tour.getRating();
        reviews = tour.getReviewCount();
        startDate = tour.getStartDate();
        endDate = tour.getEndDate();
        tourId = tour.getId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ViewPager2 vpDetailMedia = view.findViewById(R.id.vpDetailMedia);
        TabLayout tlMediaIndicator = view.findViewById(R.id.tlMediaIndicator);
        ImageView ivBack = view.findViewById(R.id.ivBack);
        TextView tvTitle = view.findViewById(R.id.tvDetailTitle);
        TextView tvLocation = view.findViewById(R.id.tvDetailLocation);
        TextView tvPrice = view.findViewById(R.id.tvDetailPrice);
        TextView tvDesc = view.findViewById(R.id.tvDetailDescription);
        TextView tvRating = view.findViewById(R.id.tvDetailRating);
        TextView tvReviews = view.findViewById(R.id.tvDetailReviews);
        TextView tvScore = view.findViewById(R.id.tvReviewScore);
        TextView tvReadMore = view.findViewById(R.id.tvReadMore);
        TextView tvTimeRange = view.findViewById(R.id.tvTourTimeRange);
        Button btnBook = view.findViewById(R.id.btnBook);

        RatingBar rbCommentRating = view.findViewById(R.id.rbCommentRating);
        EditText etCommentContent = view.findViewById(R.id.etCommentContent);
        Button btnSubmitComment = view.findViewById(R.id.btnSubmitComment);
        TextView tvReviewEmpty = view.findViewById(R.id.tvReviewEmpty);

        LinearLayout llItinerary = view.findViewById(R.id.tvItinerary);
        LinearLayout llIncluded = view.findViewById(R.id.tvIncluded);
        LinearLayout llExcluded = view.findViewById(R.id.tvExcluded);
        LinearLayout llReviews = view.findViewById(R.id.llReviews);

        tvTitle.setText(title);
        tvLocation.setText("📍 " + location);
        tvPrice.setText(price);
        tvDesc.setText(description);
        tvRating.setText("⭐ " + rating);
        tvReviews.setText(" (" + reviews + ")");
        tvScore.setText(rating + " ⭐");
        tvTimeRange.setText("Thời gian: " + getTimeRangeDescription());

        boolean active = isTourActive();
        if (!active) {
            btnBook.setEnabled(false);
            btnBook.setText(getStatusMessage());
            btnBook.setBackgroundColor(0xFF9E9E9E);
            tvDesc.setAlpha(0.6f);
        }

        List<MediaItem> mediaItems = buildMediaItems();
        MediaSlideAdapter mediaSlideAdapter = new MediaSlideAdapter(mediaItems, active);
        vpDetailMedia.setAdapter(mediaSlideAdapter);

        if (mediaItems.size() > 1) {
            new TabLayoutMediator(tlMediaIndicator, vpDetailMedia, (tab, position) -> {
            }).attach();
        } else {
            tlMediaIndicator.setVisibility(View.GONE);
        }

        tvDesc.post(() -> {
            if (tvDesc.getLineCount() > 6) {
                tvReadMore.setVisibility(View.VISIBLE);
                tvReadMore.setOnClickListener(v -> {
                    if ("Xem thêm".contentEquals(tvReadMore.getText())) {
                        tvDesc.setMaxLines(Integer.MAX_VALUE);
                        tvReadMore.setText("Rút gọn");
                    } else {
                        tvDesc.setMaxLines(6);
                        tvReadMore.setText("Xem thêm");
                    }
                });
            } else {
                tvReadMore.setVisibility(View.GONE);
            }
        });

        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        renderTimeline(llItinerary, itinerary);
        renderChecklist(llIncluded, included, 0xFF2E7D32, "✓");
        renderChecklist(llExcluded, excluded, 0xFFC62828, "✗");

        DatabaseHelper db = new DatabaseHelper(requireContext());
        if (tourId <= 0 && title != null) {
            tourId = db.getTourIdByTitle(title);
        }
        renderComments(db, llReviews, tvReviewEmpty, tvScore, tvRating, tvReviews);

        btnSubmitComment.setOnClickListener(v -> {
            if (tourId <= 0) {
                Toast.makeText(getContext(), "Không thể gửi đánh giá cho tour này", Toast.LENGTH_SHORT).show();
                return;
            }
            String content = etCommentContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập nội dung đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences pref = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
            String email = pref.getString("saved_email", "");
            int userId = db.getUserIdByEmail(email);
            if (userId <= 0) {
                Toast.makeText(getContext(), "Vui lòng đăng nhập để đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }
            float stars = rbCommentRating.getRating();
            boolean ok = db.addComment(userId, tourId, stars, content);
            if (!ok) {
                Toast.makeText(getContext(), "Không thể gửi đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }
            etCommentContent.setText("");
            rbCommentRating.setRating(5f);
            renderComments(db, llReviews, tvReviewEmpty, tvScore, tvRating, tvReviews);
            Toast.makeText(getContext(), "Đã gửi đánh giá", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void renderReviews(LinearLayout container, float avgRating) {
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, requireContext().getResources().getDisplayMetrics()
        );
        int startIdx = Math.abs(title.hashCode()) % REVIEW_POOL.length;
        int count = Math.min(4, REVIEW_POOL.length);

        for (int i = 0; i < count; i++) {
            String[] r = REVIEW_POOL[(startIdx + i) % REVIEW_POOL.length];
            String reviewer = r[0];
            int stars = Integer.parseInt(r[1]);
            String comment = r[2];

            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.VERTICAL);
            row.setBackgroundColor(i % 2 == 0 ? 0xFFF8FAFF : 0xFFFFFFFF);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            row.setPadding(0, 14 * dp, 0, 14 * dp);

            LinearLayout header = new LinearLayout(requireContext());
            header.setOrientation(LinearLayout.HORIZONTAL);
            header.setGravity(Gravity.CENTER_VERTICAL);
            header.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView avatar = new TextView(requireContext());
            avatar.setText(String.valueOf(reviewer.charAt(0)));
            avatar.setTextSize(14);
            avatar.setTypeface(null, Typeface.BOLD);
            avatar.setTextColor(0xFFFFFFFF);
            avatar.setGravity(Gravity.CENTER);
            avatar.setBackground(makeCircle(avatarColor(i)));
            LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(36 * dp, 36 * dp);
            avatarParams.setMargins(0, 0, 12 * dp, 0);
            avatar.setLayoutParams(avatarParams);

            LinearLayout nameCol = new LinearLayout(requireContext());
            nameCol.setOrientation(LinearLayout.VERTICAL);
            nameCol.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            ));

            TextView tvName = new TextView(requireContext());
            tvName.setText(reviewer);
            tvName.setTextSize(13);
            tvName.setTypeface(null, Typeface.BOLD);
            tvName.setTextColor(0xFF1A2A4A);

            TextView tvStars = new TextView(requireContext());
            tvStars.setText(buildStars(stars));
            tvStars.setTextSize(12);

            nameCol.addView(tvName);
            nameCol.addView(tvStars);
            header.addView(avatar);
            header.addView(nameCol);
            row.addView(header);

            TextView tvComment = new TextView(requireContext());
            tvComment.setText(comment);
            tvComment.setTextSize(13);
            tvComment.setTextColor(0xFF4A5568);
            tvComment.setLineSpacing(0, 1.55f);
            LinearLayout.LayoutParams cmtParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cmtParams.setMargins(48 * dp, 6 * dp, 0, 0);
            tvComment.setLayoutParams(cmtParams);
            row.addView(tvComment);

            if (i < count - 1) {
                View divider = new View(requireContext());
                divider.setBackgroundColor(0xFFEEF2FA);
                LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dp
                );
                divParams.setMargins(0, 14 * dp, 0, 0);
                divider.setLayoutParams(divParams);
                row.addView(divider);
            }
            container.addView(row);
        }
    }

    private void renderComments(DatabaseHelper db, LinearLayout container, TextView tvEmpty,
                                TextView tvScore, TextView tvRating, TextView tvReviews) {
        container.removeAllViews();
        List<CommentItem> comments = tourId > 0 ? db.getCommentsForTour(tourId) : new ArrayList<>();
        if (tvEmpty != null) {
            tvEmpty.setVisibility(comments.isEmpty() ? View.VISIBLE : View.GONE);
        }
        float avg = 0f;
        for (CommentItem item : comments) {
            avg += item.getRating();
        }
        if (!comments.isEmpty()) {
            avg = avg / comments.size();
        }
        if (tvScore != null) tvScore.setText(String.format(Locale.getDefault(), "%.1f ⭐", avg));
        if (tvRating != null) tvRating.setText("⭐ " + String.format(Locale.getDefault(), "%.1f", avg));
        if (tvReviews != null) tvReviews.setText(" (" + comments.size() + ")");

        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, requireContext().getResources().getDisplayMetrics()
        );

        for (int i = 0; i < comments.size(); i++) {
            CommentItem comment = comments.get(i);
            String reviewer = comment.getUserName();
            int stars = Math.round(comment.getRating());

            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.VERTICAL);
            row.setBackgroundColor(i % 2 == 0 ? 0xFFF8FAFF : 0xFFFFFFFF);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            row.setPadding(0, 14 * dp, 0, 14 * dp);

            LinearLayout header = new LinearLayout(requireContext());
            header.setOrientation(LinearLayout.HORIZONTAL);
            header.setGravity(Gravity.CENTER_VERTICAL);
            header.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView avatar = new TextView(requireContext());
            String initials = reviewer != null && !reviewer.isEmpty() ? reviewer.substring(0, 1) : "?";
            avatar.setText(initials);
            avatar.setTextSize(14);
            avatar.setTypeface(null, Typeface.BOLD);
            avatar.setTextColor(0xFFFFFFFF);
            avatar.setGravity(Gravity.CENTER);
            avatar.setBackground(makeCircle(avatarColor(i)));
            LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(36 * dp, 36 * dp);
            avatarParams.setMargins(0, 0, 12 * dp, 0);
            avatar.setLayoutParams(avatarParams);

            LinearLayout nameCol = new LinearLayout(requireContext());
            nameCol.setOrientation(LinearLayout.VERTICAL);
            nameCol.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            ));

            TextView tvName = new TextView(requireContext());
            tvName.setText(reviewer);
            tvName.setTextSize(13);
            tvName.setTypeface(null, Typeface.BOLD);
            tvName.setTextColor(0xFF1A2A4A);

            TextView tvStars = new TextView(requireContext());
            tvStars.setText(buildStars(stars));
            tvStars.setTextSize(12);

            nameCol.addView(tvName);
            nameCol.addView(tvStars);
            header.addView(avatar);
            header.addView(nameCol);
            row.addView(header);

            TextView tvComment = new TextView(requireContext());
            tvComment.setText(comment.getContent());
            tvComment.setTextSize(13);
            tvComment.setTextColor(0xFF4A5568);
            tvComment.setLineSpacing(0, 1.55f);
            LinearLayout.LayoutParams cmtParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cmtParams.setMargins(48 * dp, 6 * dp, 0, 0);
            tvComment.setLayoutParams(cmtParams);
            row.addView(tvComment);

            TextView tvDate = new TextView(requireContext());
            tvDate.setText(comment.getCreatedAt());
            tvDate.setTextSize(11);
            tvDate.setTextColor(0xFF9AAAC8);
            LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            dateParams.setMargins(48 * dp, 6 * dp, 0, 0);
            tvDate.setLayoutParams(dateParams);
            row.addView(tvDate);

            if (i < comments.size() - 1) {
                View divider = new View(requireContext());
                divider.setBackgroundColor(0xFFEEF2FA);
                LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dp
                );
                divParams.setMargins(0, 14 * dp, 0, 0);
                divider.setLayoutParams(divParams);
                row.addView(divider);
            }
            container.addView(row);
        }
    }

    private String buildStars(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(i < count ? "★" : "☆");
        return sb.toString();
    }

    private int avatarColor(int index) {
        int[] colors = {0xFF1565C0, 0xFF2E7D32, 0xFFB8610A, 0xFF6A1B9A};
        return colors[index % colors.length];
    }

    private void renderTimeline(LinearLayout container, String data) {
        if (data == null || data.trim().isEmpty()) return;

        String[] lines = data.split("\n");
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, requireContext().getResources().getDisplayMetrics()
        );

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String label, content;
            int colonIdx = line.indexOf(":");
            if (colonIdx > 0 && colonIdx < 15) {
                label = line.substring(0, colonIdx + 1).trim();
                content = line.substring(colonIdx + 1).trim();
            } else {
                label = "";
                content = line;
            }

            if (label.toLowerCase().startsWith("ngày")) {
                TextView dayHeader = new TextView(requireContext());
                dayHeader.setText(label + " " + content);
                dayHeader.setTextSize(15);
                dayHeader.setTypeface(null, Typeface.BOLD);
                dayHeader.setTextColor(0xFF1A2A4A);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(0, i == 0 ? 0 : 15, 0, 10);
                dayHeader.setLayoutParams(lp);
                container.addView(dayHeader);
            } else {
                LinearLayout row = new LinearLayout(requireContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                rowParams.setMargins(0, 0, 0, 10 * dp);
                row.setLayoutParams(rowParams);

                LinearLayout colLeft = new LinearLayout(requireContext());
                colLeft.setOrientation(LinearLayout.VERTICAL);
                colLeft.setGravity(Gravity.CENTER_HORIZONTAL);
                colLeft.setLayoutParams(new LinearLayout.LayoutParams(24 * dp, LinearLayout.LayoutParams.MATCH_PARENT));

                View dot = new View(requireContext());
                dot.setBackground(makeCircle(0xFF1565C0));
                dot.setLayoutParams(new LinearLayout.LayoutParams(8 * dp, 8 * dp));

                View lineView = new View(requireContext());
                lineView.setBackgroundColor(0xFFD0DCEF);
                lineView.setLayoutParams(new LinearLayout.LayoutParams(2 * dp, LinearLayout.LayoutParams.MATCH_PARENT));

                colLeft.addView(dot);
                colLeft.addView(lineView);

                LinearLayout colRight = new LinearLayout(requireContext());
                colRight.setOrientation(LinearLayout.HORIZONTAL);
                colRight.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                ));

                if (!label.isEmpty()) {
                    TextView tvTime = new TextView(requireContext());
                    tvTime.setText(label);
                    tvTime.setTextSize(13);
                    tvTime.setTypeface(null, Typeface.BOLD);
                    tvTime.setTextColor(0xFF1565C0);
                    tvTime.setLayoutParams(new LinearLayout.LayoutParams(60 * dp, LinearLayout.LayoutParams.WRAP_CONTENT));
                    colRight.addView(tvTime);
                }

                TextView tvContent = new TextView(requireContext());
                tvContent.setText(content);
                tvContent.setTextSize(13.5f);
                tvContent.setTextColor(0xFF4A5568);
                tvContent.setLineSpacing(0, 1.4f);
                tvContent.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                ));
                colRight.addView(tvContent);

                row.addView(colLeft);
                row.addView(colRight);
                container.addView(row);
            }
        }
    }

    private void renderChecklist(LinearLayout container, String data, int color, String icon) {
        if (data == null || data.trim().isEmpty()) return;

        String[] lines = data.split("\n");
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, requireContext().getResources().getDisplayMetrics()
        );

        for (String line : lines) {
            String item = line.trim();
            if (item.isEmpty()) continue;

            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.TOP);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 0, 0, 8 * dp);
            row.setLayoutParams(rowParams);

            TextView tvIcon = new TextView(requireContext());
            tvIcon.setText(icon);
            tvIcon.setTextSize(10);
            tvIcon.setTextColor(color);
            tvIcon.setPadding(0, 2 * dp, 8 * dp, 0);

            TextView tvItem = new TextView(requireContext());
            tvItem.setText(item);
            tvItem.setTextSize(13);
            tvItem.setTextColor(0xFF3D4A5C);
            tvItem.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            ));

            row.addView(tvIcon);
            row.addView(tvItem);
            container.addView(row);
        }
    }

    private android.graphics.drawable.GradientDrawable makeCircle(int color) {
        android.graphics.drawable.GradientDrawable d = new android.graphics.drawable.GradientDrawable();
        d.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        d.setColor(color);
        return d;
    }

    private List<MediaItem> buildMediaItems() {
        List<MediaItem> items = new ArrayList<>();

        for (String url : parseVideoUrls(videoUrl)) {
            if (isDirectVideoUrl(url)) {
                items.add(MediaItem.video(url));
            } else if (isYouTubeUrl(url)) {
                items.add(MediaItem.youtube(url));
            }
        }

        if (imageUrls != null) {
            for (String url : imageUrls) {
                if (url != null && !url.trim().isEmpty()) {
                    items.add(MediaItem.image(url.trim()));
                }
            }
        }

        if (items.isEmpty()) {
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                items.add(MediaItem.image(imageUrl.trim()));
            } else if (imageRes != 0) {
                items.add(MediaItem.imageRes(imageRes));
            }
        }

        if (items.isEmpty()) {
            items.add(MediaItem.imageRes(android.R.drawable.ic_menu_gallery));
        }

        return items;
    }

    private static class MediaItem {
        final MediaType type;
        final String url;
        final int imageRes;

        private MediaItem(MediaType type, String url, int imageRes) {
            this.type = type;
            this.url = url;
            this.imageRes = imageRes;
        }

        static MediaItem image(String url) {
            return new MediaItem(MediaType.IMAGE, url, 0);
        }

        static MediaItem imageRes(int imageRes) {
            return new MediaItem(MediaType.IMAGE, null, imageRes);
        }

        static MediaItem video(String url) {
            return new MediaItem(MediaType.VIDEO, url, 0);
        }

        static MediaItem youtube(String url) {
            return new MediaItem(MediaType.YOUTUBE, url, 0);
        }
    }

    private enum MediaType { IMAGE, VIDEO, YOUTUBE }

    private class MediaSlideAdapter extends RecyclerView.Adapter<MediaSlideAdapter.MediaViewHolder> {
        private final List<MediaItem> items;
        private final boolean active;

        MediaSlideAdapter(List<MediaItem> items, boolean active) {
            this.items = items == null ? new ArrayList<>() : items;
            this.active = active;
        }

        @NonNull
        @Override
        public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_slide, parent, false);
            return new MediaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public void onViewRecycled(@NonNull MediaViewHolder holder) {
            super.onViewRecycled(holder);
            holder.release();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class MediaViewHolder extends RecyclerView.ViewHolder {
            final ImageView ivMediaImage;
            final PlayerView pvMediaVideo;
            final WebView wvMediaVideo;
            final ImageView ivMediaPlay;

            ExoPlayer player;

            MediaViewHolder(@NonNull View itemView) {
                super(itemView);
                ivMediaImage = itemView.findViewById(R.id.ivMediaImage);
                pvMediaVideo = itemView.findViewById(R.id.pvMediaVideo);
                wvMediaVideo = itemView.findViewById(R.id.wvMediaVideo);
                ivMediaPlay = itemView.findViewById(R.id.ivMediaPlay);
            }

            void bind(MediaItem item) {
                release();

                ivMediaImage.setVisibility(View.GONE);
                pvMediaVideo.setVisibility(View.GONE);
                wvMediaVideo.setVisibility(View.GONE);
                ivMediaPlay.setVisibility(View.GONE);

                if (!active) {
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ivMediaImage.setColorFilter(new ColorMatrixColorFilter(matrix));
                } else {
                    ivMediaImage.setColorFilter(null);
                }

                if (item.type == MediaType.IMAGE) {
                    ivMediaImage.setVisibility(View.VISIBLE);
                    if (item.url != null && !item.url.isEmpty()) {
                        Glide.with(itemView.getContext())
                                .load(item.url)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .centerCrop()
                                .into(ivMediaImage);
                    } else if (item.imageRes != 0) {
                        ivMediaImage.setImageResource(item.imageRes);
                    } else {
                        ivMediaImage.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                    return;
                }

                if (item.type == MediaType.VIDEO && active) {
                    pvMediaVideo.setVisibility(View.VISIBLE);

                    player = new ExoPlayer.Builder(itemView.getContext()).build();
                    pvMediaVideo.setPlayer(player);

                    androidx.media3.common.MediaItem exoMediaItem =
                            androidx.media3.common.MediaItem.fromUri(item.url);

                    player.setMediaItem(exoMediaItem);
                    player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
                    player.setVolume(0f);
                    player.prepare();
                    player.play();

                    return;
                }

                if (item.type == MediaType.VIDEO) {
                    ivMediaImage.setVisibility(View.VISIBLE);
                    ivMediaPlay.setVisibility(View.VISIBLE);

                    if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                        Glide.with(itemView.getContext())
                                .load(imageUrl)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .centerCrop()
                                .into(ivMediaImage);
                    } else {
                        ivMediaImage.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                    return;
                }

                if (item.type == MediaType.YOUTUBE && active) {
                    wvMediaVideo.setVisibility(View.VISIBLE);
                    wvMediaVideo.getSettings().setJavaScriptEnabled(true);
                    wvMediaVideo.getSettings().setDomStorageEnabled(true);
                    wvMediaVideo.getSettings().setMediaPlaybackRequiresUserGesture(false);
                    wvMediaVideo.setWebViewClient(new WebViewClient());
                    wvMediaVideo.loadUrl(buildYouTubeEmbedUrl(item.url));
                    return;
                }

                if (item.type == MediaType.YOUTUBE) {
                    ivMediaImage.setVisibility(View.VISIBLE);
                    ivMediaPlay.setVisibility(View.VISIBLE);
                    ivMediaImage.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            }

            void release() {
                try {
                    if (player != null) {
                        player.stop();
                        player.release();
                        player = null;
                    }
                    pvMediaVideo.setPlayer(null);
                } catch (Exception ignored) {
                }

                try {
                    wvMediaVideo.loadUrl("about:blank");
                    wvMediaVideo.onPause();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
