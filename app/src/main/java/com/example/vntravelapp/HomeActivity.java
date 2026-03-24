package com.example.vntravelapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.vntravelapp.fragments.HomeFragment;
import com.example.vntravelapp.fragments.MapFragment;
import com.example.vntravelapp.fragments.ProfileFragment;
import com.example.vntravelapp.fragments.TripFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottomNavigation);

        // Load Home ngay lần đầu
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Xử lý chọn tab bottom nav
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_map) {
                selectedFragment = new MapFragment();
            } else if (id == R.id.nav_trip) {
                selectedFragment = new TripFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            loadFragment(selectedFragment);
            return true;
        });

        // ✅ Lắng nghe mỗi khi back stack thay đổi
        // — bắt được cả khi fragment con tự replace hoặc popBackStack
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment current = getCurrentFragment();
            handleBottomNavVisibility(current);
        });
    }

    // ── Lấy fragment đang hiển thị ───────────────────────────────────────────
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
    }

    // ── Load fragment + xử lý ẩn/hiện ───────────────────────────────────────
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        handleBottomNavVisibility(fragment);
    }

    // ── Chỉ hiển thị bottom nav ở 4 trang chính ──────────────────────────────
    private void handleBottomNavVisibility(Fragment fragment) {
        if (fragment == null) return;

        if (fragment instanceof HomeFragment
                || fragment instanceof MapFragment
                || fragment instanceof TripFragment
                || fragment instanceof ProfileFragment) {
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            bottomNav.setVisibility(View.GONE);
        }
    }

    // ── Cho fragment con gọi trực tiếp nếu cần ───────────────────────────────
    public void hideBottomNav() {
        bottomNav.setVisibility(View.GONE);
    }

    public void showBottomNav() {
        bottomNav.setVisibility(View.VISIBLE);
    }
}