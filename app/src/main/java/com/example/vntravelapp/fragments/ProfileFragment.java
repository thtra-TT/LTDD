package com.example.vntravelapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.vntravelapp.LoginActivity;
import com.example.vntravelapp.R;

public class ProfileFragment extends Fragment {

    private TextView tvUserName, tvUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        // Hiển thị thông tin người dùng từ SharedPreferences
        loadUserInfo();

        // Group 1
        setupOption(view.findViewById(R.id.optFavorite), "Yêu thích", "12 tour", android.R.drawable.ic_menu_myplaces);
        setupOption(view.findViewById(R.id.optHistory), "Lịch sử chuyến đi", "8 chuyến", android.R.drawable.ic_menu_recent_history);
        setupOption(view.findViewById(R.id.optPayment), "Phương thức thanh toán", "3 thẻ", android.R.drawable.ic_menu_agenda);
        setupOption(view.findViewById(R.id.optAI), "Trợ lý AI", "Sẵn sàng hỗ trợ", android.R.drawable.stat_notify_chat);

        // Group 2
        setupOption(view.findViewById(R.id.optSettings), "Cài đặt", "Thông báo, bảo mật...", android.R.drawable.ic_menu_preferences);

        // Nút Đăng xuất
        View logoutView = view.findViewById(R.id.optLogout);
        setupOption(logoutView, "Đăng xuất", "Thoát tài khoản", android.R.drawable.ic_lock_power_off);
        if (logoutView != null) {
            logoutView.setOnClickListener(v -> handleLogout());
        }

        return view;
    }

    private void loadUserInfo() {
        if (getActivity() != null) {
            SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String name = pref.getString("saved_username", "Người dùng");
            String email = pref.getString("saved_email", "example@email.com");

            if (tvUserName != null) tvUserName.setText(name);
            if (tvUserEmail != null) tvUserEmail.setText(email);
        }
    }

    private void handleLogout() {
        if (getActivity() != null) {
            // Xóa trạng thái đăng nhập
            SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

            // Chuyển về màn hình Login
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void setupOption(View view, String title, String sub, int iconRes) {
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
    }
}
