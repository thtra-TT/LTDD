package com.example.vntravelapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.vntravelapp.HomeActivity;
import com.example.vntravelapp.LoginActivity;
import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.ProfileTourAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Tour;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private TextView tvUserBio;

    private TextView tvStatTrips;
    private TextView tvStatVisited;
    private TextView tvStatFavorites;

    private RecyclerView rvRecommendations;
    private ProfileTourAdapter recommendationAdapter;

    private TextView tvBadgeExplorer;
    private TextView tvBadgeNature;

    private View skeletonContainer;
    private View cardProfileHeader;
    private View profileHeaderCover;


    private DatabaseHelper db;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private Uri pendingCameraUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        db = new DatabaseHelper(getContext());

        swipeRefreshLayout = view.findViewById(R.id.swipeProfile);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserBio = view.findViewById(R.id.tvUserBio);
        skeletonContainer = view.findViewById(R.id.skeletonContainer);
        cardProfileHeader = view.findViewById(R.id.cardProfileHeader);
        profileHeaderCover = view.findViewById(R.id.profileHeaderCover);
        tvBadgeExplorer = view.findViewById(R.id.tvBadgeExplorer);
        tvBadgeNature = view.findViewById(R.id.tvBadgeNature);


        View statTrips = view.findViewById(R.id.statTrips);
        View statVisited = view.findViewById(R.id.statVisited);
        View statFavorites = view.findViewById(R.id.statFavorites);
        tvStatTrips = statTrips.findViewById(R.id.tvStatValue);
        tvStatVisited = statVisited.findViewById(R.id.tvStatValue);
        tvStatFavorites = statFavorites.findViewById(R.id.tvStatValue);
        ((TextView) statTrips.findViewById(R.id.tvStatLabel)).setText("Chuyến đi");
        ((TextView) statVisited.findViewById(R.id.tvStatLabel)).setText("Đã đi");
        ((TextView) statFavorites.findViewById(R.id.tvStatLabel)).setText("Yêu thích");

        statTrips.setOnClickListener(v -> navigateToTripsTab(0));
        statVisited.setOnClickListener(v -> navigateToTripsTab(1));
        statFavorites.setOnClickListener(v -> navigateToTripsTab(3));

        initAvatarLaunchers();
        ivAvatar.setOnClickListener(v -> showAvatarPicker());
        view.findViewById(R.id.btnEditProfile).setOnClickListener(v -> showEditProfileDialog());

        setupOption(view.findViewById(R.id.optEditInfo), "Chỉnh sửa thông tin", "Tên, email, bio", android.R.drawable.ic_menu_edit, v -> showEditProfileDialog());
        setupOption(view.findViewById(R.id.optPassword), "Đổi mật khẩu", "Bảo mật tài khoản", android.R.drawable.ic_lock_lock, v -> showChangePasswordDialog());
        setupOption(view.findViewById(R.id.optFavorites), "Yêu thích", "Tour & khách sạn", android.R.drawable.ic_menu_myplaces, v -> navigateToTripsTab(3));
        setupOption(view.findViewById(R.id.optMyReviews), "Review của tôi", "Tour & khách sạn", android.R.drawable.star_big_on, v -> Toast.makeText(getContext(), "Sắp có", Toast.LENGTH_SHORT).show());
        setupOption(view.findViewById(R.id.optJournal), "Nhật ký du lịch", "Hành trình của bạn", android.R.drawable.ic_menu_edit, v -> Toast.makeText(getContext(), "Sắp có", Toast.LENGTH_SHORT).show());
        setupOption(view.findViewById(R.id.optNotifications), "Thông báo", "Ưu đãi & nhắc lịch", android.R.drawable.ic_dialog_email, v -> Toast.makeText(getContext(), "Sắp có", Toast.LENGTH_SHORT).show());
        setupOption(view.findViewById(R.id.optSettings), "Cài đặt", "Ngôn ngữ, giao diện", android.R.drawable.ic_menu_preferences, v -> Toast.makeText(getContext(), "Sắp có", Toast.LENGTH_SHORT).show());
        setupOption(view.findViewById(R.id.optLogout), "Đăng xuất", "Thoát tài khoản", android.R.drawable.ic_lock_power_off, v -> handleLogout());

        rvRecommendations = view.findViewById(R.id.rvProfileRecommendations);
        rvRecommendations.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendationAdapter = new ProfileTourAdapter(new ArrayList<>(), this::openTourDetail);
        rvRecommendations.setAdapter(recommendationAdapter);

        View scrollView = view.findViewById(R.id.profileScroll);
        if (scrollView instanceof androidx.core.widget.NestedScrollView) {
            ((androidx.core.widget.NestedScrollView) scrollView)
                    .setOnScrollChangeListener((androidx.core.widget.NestedScrollView.OnScrollChangeListener)
                            (v, scrollX, scrollY, oldScrollX, oldScrollY) -> updateHeaderAnimation(scrollY));
        }

        swipeRefreshLayout.setOnRefreshListener(this::loadProfileData);
        loadProfileData();

        return view;
    }

    private void navigateToTripsTab(int tabIndex) {
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).openTripsTab(tabIndex);
        }
    }

    private void initAvatarLaunchers() {
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                saveAvatarUri(uri);
                loadAvatar(uri);
            }
        });

        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success && pendingCameraUri != null) {
                saveAvatarUri(pendingCameraUri);
                loadAvatar(pendingCameraUri);
            }
        });
    }

    private void showAvatarPicker() {
        String[] items = new String[]{"Chọn từ thư viện", "Chụp ảnh"};
        new AlertDialog.Builder(getContext())
                .setTitle("Đổi avatar")
                .setItems(items, (dialog, which) -> {
                    if (which == 0) {
                        pickImageLauncher.launch("image/*");
                    } else {
                        Uri uri = createCameraUri();
                        if (uri != null) {
                            pendingCameraUri = uri;
                            takePictureLauncher.launch(uri);
                        }
                    }
                })
                .show();
    }

    private Uri createCameraUri() {
        if (getContext() == null) return null;
        try {
            File dir = new File(getContext().getCacheDir(), "avatars");
            if (!dir.exists()) dir.mkdirs();
            String fileName = "avatar_" + System.currentTimeMillis() + ".jpg";
            File file = new File(dir, fileName);
            return FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Không thể mở camera", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void loadProfileData() {
        showSkeleton(true);
        loadUserInfo();
        loadStats();
        loadRecommendations();
        showSkeleton(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showSkeleton(boolean show) {
        if (skeletonContainer != null) skeletonContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        if (cardProfileHeader != null) cardProfileHeader.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void loadUserInfo() {
        if (getActivity() == null) return;
        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String name = pref.getString("saved_username", "Người dùng");
        String email = pref.getString("saved_email", "example@email.com");
        String bio = pref.getString("saved_bio", "Đam mê khám phá Việt Nam 🇻🇳");
        String avatarUri = pref.getString("saved_avatar_uri", "");

        tvUserName.setText(name);
        tvUserEmail.setText(email);
        tvUserBio.setText(bio);

        if (avatarUri != null && !avatarUri.trim().isEmpty()) {
            loadAvatar(Uri.parse(avatarUri));
        } else {
            ivAvatar.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    private void loadAvatar(Uri uri) {
        Glide.with(this)
                .load(uri)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(ivAvatar);
    }

    private void saveAvatarUri(Uri uri) {
        if (getActivity() == null) return;
        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        pref.edit().putString("saved_avatar_uri", uri.toString()).apply();
    }

    private void loadStats() {
        int tripCount = db.getTripCount();
        int visitedCount = db.getVisitedLocationCount();
        int favoriteCount = db.getFavoriteCount();
        tvStatTrips.setText(String.valueOf(tripCount));
        tvStatVisited.setText(String.valueOf(visitedCount));
        tvStatFavorites.setText(String.valueOf(favoriteCount));

        if (tvBadgeExplorer != null) {
            String level = tripCount >= 10 ? "Explorer Pro" : (tripCount >= 5 ? "Explorer" : "Beginner");
            tvBadgeExplorer.setText(level);
        }
        if (tvBadgeNature != null) {
            tvBadgeNature.setText(visitedCount >= 3 ? "Biển / Núi" : "Chuyến mới");
        }
    }


    private void loadRecommendations() {
        List<Tour> tours = db.getRecommendedTours(8);
        recommendationAdapter.updateItems(tours);
    }

    private void updateHeaderAnimation(int scrollY) {
        if (cardProfileHeader == null || profileHeaderCover == null) return;
        float progress = Math.min(1f, scrollY / 240f);
        cardProfileHeader.setAlpha(1f - (0.3f * progress));
        cardProfileHeader.setScaleX(1f - (0.04f * progress));
        cardProfileHeader.setScaleY(1f - (0.04f * progress));
        profileHeaderCover.setAlpha(1f - (0.2f * progress));
    }

    private void openTourDetail(Tour tour) {
        DetailFragment fragment = DetailFragment.newInstance(
                tour.getTitle(),
                tour.getLocation(),
                tour.getPrice(),
                tour.getDescription(),
                tour.getItinerary(),
                tour.getIncluded(),
                tour.getExcluded(),
                tour.getImageResId(),
                tour.getPrimaryImageUrl(),
                new ArrayList<>(tour.getImageUrls()),
                tour.getVideoUrl(),
                tour.getRating(),
                tour.getReviewCount(),
                tour.getStartDate(),
                tour.getEndDate()
        );
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showEditProfileDialog() {
        if (getActivity() == null) return;
        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentName = pref.getString("saved_username", "");
        String currentEmail = pref.getString("saved_email", "");
        String currentPhone = pref.getString("saved_phone", "");
        String currentBio = pref.getString("saved_bio", "");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 0);

        EditText edtName = buildInput("Họ tên", currentName, InputType.TYPE_CLASS_TEXT, layout);
        EditText edtEmail = buildInput("Email", currentEmail, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, layout);
        EditText edtPhone = buildInput("Số điện thoại", currentPhone, InputType.TYPE_CLASS_PHONE, layout);
        EditText edtBio = buildInput("Bio", currentBio, InputType.TYPE_CLASS_TEXT, layout);

        new AlertDialog.Builder(getContext())
                .setTitle("Cập nhật hồ sơ")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();
                    String bio = edtBio.getText().toString().trim();

                    pref.edit()
                            .putString("saved_username", name.isEmpty() ? "Người dùng" : name)
                            .putString("saved_email", email.isEmpty() ? currentEmail : email)
                            .putString("saved_phone", phone)
                            .putString("saved_bio", bio.isEmpty() ? "Đam mê khám phá Việt Nam 🇻🇳" : bio)
                            .apply();

                    if (!currentEmail.equals(email)) {
                        db.updateUserProfile(currentEmail, email, name, phone);
                    } else {
                        db.updateUserProfile(currentEmail, null, name, phone);
                    }
                    loadProfileData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showChangePasswordDialog() {
        if (getActivity() == null) return;
        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = pref.getString("saved_email", "");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 0);

        EditText edtOld = buildInput("Mật khẩu cũ", "", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, layout);
        EditText edtNew = buildInput("Mật khẩu mới", "", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, layout);

        new AlertDialog.Builder(getContext())
                .setTitle("Đổi mật khẩu")
                .setView(layout)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String oldPass = edtOld.getText().toString();
                    String newPass = edtNew.getText().toString();
                    if (oldPass.isEmpty() || newPass.isEmpty()) {
                        Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean ok = db.updateUserPassword(email, oldPass, newPass);
                    Toast.makeText(getContext(), ok ? "Đã đổi mật khẩu" : "Không thể đổi mật khẩu", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private EditText buildInput(String hint, String value, int inputType, LinearLayout container) {
        EditText edt = new EditText(getContext());
        edt.setHint(hint);
        edt.setText(value);
        edt.setInputType(inputType);
        edt.setPadding(20, 20, 20, 20);
        container.addView(edt);
        return edt;
    }

    private void handleLogout() {
        if (getActivity() != null) {
            SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void setupOption(View view, String title, String sub, int iconRes, View.OnClickListener listener) {
        if (view == null) return;
        TextView tvTitle = view.findViewById(R.id.tvOptionTitle);
        TextView tvSub = view.findViewById(R.id.tvOptionSub);
        ImageView ivIcon = view.findViewById(R.id.ivOptionIcon);

        if (tvTitle != null) tvTitle.setText(title);
        if (tvSub != null) {
            if (sub == null || sub.isEmpty()) {
                tvSub.setVisibility(View.GONE);
            } else {
                tvSub.setText(sub);
                tvSub.setVisibility(View.VISIBLE);
            }
        }
        if (ivIcon != null) {
            ivIcon.setImageResource(iconRes);
            ivIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_dark));
        }
        view.setOnClickListener(listener);
    }
}
