package com.example.vntravelapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.TourAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.Tour;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TourAdapter tourAdapter;
    private TourAdapter popularAdapter;
    private TourAdapter nearYouAdapter;
    private List<Tour> allTours = new ArrayList<>();
    private List<Tour> popularTours = new ArrayList<>();
    private List<Tour> nearYouTours = new ArrayList<>();
    private List<Tour> allDestinations = new ArrayList<>();
    
    private TextView tvNoResults;
    private TextView tvNearYouTitle;
    private RecyclerView rvResults;
    private RecyclerView rvPopular;
    private RecyclerView rvNearYou;
    private View rlNearYouHeader;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dbHelper = new DatabaseHelper(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // UI References
        tvNoResults = view.findViewById(R.id.tvNoResults);
        tvNearYouTitle = view.findViewById(R.id.tvNearYouTitle);
        rvResults = view.findViewById(R.id.rvTours);
        rvPopular = view.findViewById(R.id.rvPopularTours);
        rvNearYou = view.findViewById(R.id.rvNearYou);
        rlNearYouHeader = view.findViewById(R.id.rlNearYouHeader);

        // Setup Categories
        setupCategory(view.findViewById(R.id.catTour), "Tour", android.R.drawable.ic_menu_directions);
        setupCategory(view.findViewById(R.id.catHotel), "Khách sạn", android.R.drawable.ic_menu_myplaces);
        setupCategory(view.findViewById(R.id.catCombo), "Combo", android.R.drawable.ic_menu_save);

        // Click Listeners
        view.findViewById(R.id.catHotel).setOnClickListener(v -> switchFragment(new HotelFragment()));
        view.findViewById(R.id.catCombo).setOnClickListener(v -> switchFragment(new ComboFragment()));

        // Data Loading
        allTours = dbHelper.getAllTours(); // Chỉ lấy type = 'Tour'
        popularTours = dbHelper.getPopularTours();
        allDestinations = dbHelper.getAllDestinations(); // Lấy type = 'Destination'

        // Setup Main RecyclerView (All Tours)
        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvResults.setNestedScrollingEnabled(false);
        tourAdapter = new TourAdapter(new ArrayList<>(allTours));
        rvResults.setAdapter(tourAdapter);

        // Setup Popular RecyclerView
        rvPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularAdapter = new TourAdapter(new ArrayList<>(popularTours)) {
            @Override
            public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                holder.itemView.setLayoutParams(lp);
            }
        };
        rvPopular.setAdapter(popularAdapter);

        // Setup Near You RecyclerView
        rvNearYou.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        nearYouAdapter = new TourAdapter(new ArrayList<>(nearYouTours)) {
            @Override
            public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                holder.itemView.setLayoutParams(lp);
            }
        };
        rvNearYou.setAdapter(nearYouAdapter);

        EditText etSearch = view.findViewById(R.id.etSearchHome);
        etSearch.setOnClickListener(v -> switchFragment(new SearchFragment()));

        checkLocationPermissionAndFetch();

        return view;
    }

    private void checkLocationPermissionAndFetch() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                getLastLocation();
            }
        }
    }

    private void getLastLocation() {
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                Location refLocation = location;
                if (refLocation == null) {
                    refLocation = new Location("default");
                    refLocation.setLatitude(10.7769);
                    refLocation.setLongitude(106.7009);
                }
                updateNearYouTours(refLocation);
            }).addOnFailureListener(e -> {
                Location defaultLocation = new Location("default");
                defaultLocation.setLatitude(10.7769);
                defaultLocation.setLongitude(106.7009);
                updateNearYouTours(defaultLocation);
            });
        } catch (SecurityException e) {
            Location defaultLocation = new Location("default");
            defaultLocation.setLatitude(10.7769);
            defaultLocation.setLongitude(106.7009);
            updateNearYouTours(defaultLocation);
        }
    }

    private void updateNearYouTours(Location userLocation) {
        List<Tour> nearbyDestinations = new ArrayList<>();
        
        for (Tour dest : allDestinations) {
            if (dest.getLatitude() == 0 && dest.getLongitude() == 0) continue;
            
            float[] results = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), 
                                   dest.getLatitude(), dest.getLongitude(), results);
            
            if (results[0] <= 500000) {
                nearbyDestinations.add(dest);
            }
        }

        if (nearbyDestinations.isEmpty() && !allDestinations.isEmpty()) {
            nearbyDestinations.addAll(allDestinations);
        }

        Collections.sort(nearbyDestinations, (t1, t2) -> {
            float[] d1 = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), t1.getLatitude(), t1.getLongitude(), d1);
            float[] d2 = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), t2.getLatitude(), t2.getLongitude(), d2);
            return Float.compare(d1[0], d2[0]);
        });

        nearYouTours = nearbyDestinations;

        if (!nearYouTours.isEmpty()) {
            rlNearYouHeader.setVisibility(View.VISIBLE);
            rvNearYou.setVisibility(View.VISIBLE);
            nearYouAdapter.updateData(nearYouTours);
            if (tvNearYouTitle != null) {
                tvNearYouTitle.setText("📍 Điểm du lịch gần bạn");
            }
        } else {
            rlNearYouHeader.setVisibility(View.GONE);
            rvNearYou.setVisibility(View.GONE);
        }
    }

    private void switchFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        }
    }

    private void setupCategory(View view, String name, int iconRes) {
        TextView tv = view.findViewById(R.id.tvCategoryName);
        ImageView iv = view.findViewById(R.id.ivCategoryIcon);
        tv.setText(name);
        iv.setImageResource(iconRes);
    }
}
