package com.example.vntravelapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.vntravelapp.fragments.HomeFragment;
import com.example.vntravelapp.fragments.MapFragment;
import com.example.vntravelapp.fragments.ProfileFragment;
import com.example.vntravelapp.fragments.TripFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private int pendingTripTab = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottomNavigation);

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String role = pref.getString("saved_role", "BUYER");

        if ("SELLER".equals(role)) {
            bottomNav.getMenu().findItem(R.id.nav_trip).setVisible(false);
        }

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();

            } else if (id == R.id.nav_map) {
                selectedFragment = new MapFragment();

            } else if (id == R.id.nav_trip) {
                SharedPreferences tripPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String tripRole = tripPref.getString("saved_role", "BUYER");

                if ("SELLER".equals(tripRole)) {
                    Toast.makeText(this, "Người bán không có mục chuyến đi", Toast.LENGTH_SHORT).show();
                    return false;
                }

                TripFragment tripFragment = new TripFragment();
                if (pendingTripTab >= 0) {
                    Bundle args = new Bundle();
                    args.putInt(TripFragment.ARG_INITIAL_TAB, pendingTripTab);
                    tripFragment.setArguments(args);
                    pendingTripTab = -1;
                }
                selectedFragment = tripFragment;

            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment current = getCurrentFragment();
            handleBottomNavVisibility(current);
        });
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        handleBottomNavVisibility(fragment);
    }

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

    public void hideBottomNav() {
        bottomNav.setVisibility(View.GONE);
    }

    public void showBottomNav() {
        bottomNav.setVisibility(View.VISIBLE);
    }

    public void selectTab(int menuItemId) {
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(menuItemId);
        }
    }

    public void openTripsTab(int tabIndex) {
        pendingTripTab = tabIndex;
        selectTab(R.id.nav_trip);
    }
}