package com.example.vntravelapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.vntravelapp.LoginActivity;
import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.ProfileTourAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Tour;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private TextView tvUserBio;

    private RecyclerView rvRecommendations;
    private ProfileTourAdapter recommendationAdapter;

    private TextView tvBadgeExplorer;
    private TextView tvBadgeNature;
    private TextView tvRecommendHeader;

    private View skeletonContainer;
    private View cardProfileHeader;
    private View profileHeaderCover;
    private ImageView ivProfileCover;

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
        ivProfileCover = view.findViewById(R.id.ivProfileCover);
        tvBadgeExplorer = view.findViewById(R.id.tvBadgeExplorer);
        tvBadgeNature = view.findViewById(R.id.tvBadgeNature);
        tvRecommendHeader = view.findViewById(R.id.tvProfileRecommendHeader);

        initAvatarLaunchers();
        ivAvatar.setOnClickListener(v -> showAvatarPicker());
        view.findViewById(R.id.btnEditProfile).setOnClickListener(v -> showEditProfileDialog());

        setupOption(view.findViewById(R.id.optEditInfo), getString(R.string.profile_opt_edit), getString(R.string.profile_opt_edit_sub), R.drawable.ic_profile_edit, v -> showEditProfileDialog());
        setupOption(view.findViewById(R.id.optPassword), getString(R.string.profile_opt_password), getString(R.string.profile_opt_password_sub), R.drawable.ic_profile_lock, v -> showChangePasswordDialog());
        setupOption(view.findViewById(R.id.optFavorites), getString(R.string.profile_opt_favorites), getString(R.string.profile_opt_favorites_sub), R.drawable.ic_profile_favorite, v -> openProfileScreen(new ProfileFavoritesFragment()));
        setupOption(view.findViewById(R.id.optMyReviews), getString(R.string.profile_opt_reviews), getString(R.string.profile_opt_reviews_sub), R.drawable.ic_profile_review, v -> openProfileScreen(new ProfileReviewsFragment()));
        setupOption(view.findViewById(R.id.optJournal), getString(R.string.profile_opt_journal), getString(R.string.profile_opt_journal_sub), R.drawable.ic_profile_journal, v -> openProfileScreen(new ProfileJournalFragment()));
        setupOption(view.findViewById(R.id.optNotifications), getString(R.string.profile_opt_notifications), getString(R.string.profile_opt_notifications_sub), R.drawable.ic_profile_notification, v -> openProfileScreen(new ProfileNotificationsFragment()));
        setupOption(view.findViewById(R.id.optSettings), getString(R.string.profile_opt_settings), getString(R.string.profile_opt_settings_sub), R.drawable.ic_profile_settings, v -> openProfileScreen(new ProfileSettingsFragment()));
        setupOption(view.findViewById(R.id.optLogout), getString(R.string.profile_opt_logout), getString(R.string.profile_opt_logout_sub), R.drawable.ic_profile_logout, v -> handleLogout());

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
        loadProfileCover();

        return view;
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
        if (tours == null) {
            tours = new ArrayList<>();
        }
        recommendationAdapter.updateItems(tours);
        boolean hasRecommendations = !tours.isEmpty();
        if (tvRecommendHeader != null) {
            tvRecommendHeader.setVisibility(hasRecommendations ? View.VISIBLE : View.GONE);
        }
        if (rvRecommendations != null) {
            rvRecommendations.setVisibility(hasRecommendations ? View.VISIBLE : View.GONE);
        }
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
        DetailFragment fragment = DetailFragment.newInstanceWithItem(DetailFragment.ITEM_TYPE_TOUR, tour.getId());
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

        View content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null, false);
        TextInputEditText edtName = content.findViewById(R.id.edtProfileName);
        TextInputEditText edtEmail = content.findViewById(R.id.edtProfileEmail);
        TextInputEditText edtPhone = content.findViewById(R.id.edtProfilePhone);
        TextInputEditText edtBio = content.findViewById(R.id.edtProfileBio);
        if (edtName != null) edtName.setText(currentName);
        if (edtEmail != null) edtEmail.setText(currentEmail);
        if (edtPhone != null) edtPhone.setText(currentPhone);
        if (edtBio != null) edtBio.setText(currentBio);

        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.dialog_edit_profile_title))
                .setView(content)
                .setPositiveButton(getString(R.string.dialog_save), (dialog, which) -> {
                    String name = edtName != null ? edtName.getText().toString().trim() : "";
                    String email = edtEmail != null ? edtEmail.getText().toString().trim() : "";
                    String phone = edtPhone != null ? edtPhone.getText().toString().trim() : "";
                    String bio = edtBio != null ? edtBio.getText().toString().trim() : "";

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
                .setNegativeButton(getString(R.string.dialog_cancel), null)
                .show();
    }

    private void showChangePasswordDialog() {
        if (getActivity() == null) return;
        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = pref.getString("saved_email", "");

        View content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, null, false);
        TextInputEditText edtOld = content.findViewById(R.id.edtOldPassword);
        TextInputEditText edtNew = content.findViewById(R.id.edtNewPassword);

        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.dialog_change_password_title))
                .setView(content)
                .setPositiveButton(getString(R.string.dialog_update), (dialog, which) -> {
                    String oldPass = edtOld != null ? edtOld.getText().toString() : "";
                    String newPass = edtNew != null ? edtNew.getText().toString() : "";
                    if (oldPass.isEmpty() || newPass.isEmpty()) {
                        Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean ok = db.updateUserPassword(email, oldPass, newPass);
                    Toast.makeText(getContext(), ok ? "Đã đổi mật khẩu" : "Không thể đổi mật khẩu", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.dialog_cancel), null)
                .show();
    }

    private void openProfileScreen(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
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
            ivIcon.setColorFilter(getResources().getColor(R.color.profile_accent));
        }
        view.setOnClickListener(listener);
    }

    private void loadProfileCover() {
        if (ivProfileCover == null) return;
        String url = getString(R.string.profile_cover_url);
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.bg_home_header)
                .centerCrop()
                .into(ivProfileCover);
    }
}
