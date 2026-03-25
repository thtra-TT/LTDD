package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.HomeSearchAdapter;
import com.example.vntravelapp.adapters.TourAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.Tour;
import com.example.vntravelapp.utils.SearchUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TourAdapter tourAdapter;
    private TourAdapter popularAdapter;
    private HomeSearchAdapter searchAdapter;
    private List<Tour> allTours = new ArrayList<>();
    private List<Tour> popularTours = new ArrayList<>();
    private List<Hotel> allHotels = new ArrayList<>();
    private List<SearchIndexItem> searchIndex = new ArrayList<>();
    private List<LocationIndexItem> locationIndex = new ArrayList<>();
    
    private TextView tvNoResults;
    private TextView tvFeaturedTitle;
    private RecyclerView rvResults;
    private RecyclerView rvPopular;
    private View rlPopularHeader;
    private boolean isSearching = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dbHelper = new DatabaseHelper(getContext());

        // UI References
        tvNoResults = view.findViewById(R.id.tvNoResults);
        tvFeaturedTitle = view.findViewById(R.id.tvFeaturedTitle);
        rvResults = view.findViewById(R.id.rvTours);
        rvPopular = view.findViewById(R.id.rvPopularTours);
        rlPopularHeader = view.findViewById(R.id.rlPopularHeader);

        // Setup Categories
        setupCategory(view.findViewById(R.id.catTour), "Tour", android.R.drawable.ic_menu_directions);
        setupCategory(view.findViewById(R.id.catHotel), "Khách sạn", android.R.drawable.ic_menu_myplaces);
        setupCategory(view.findViewById(R.id.catTicket), "Vé", android.R.drawable.ic_menu_agenda);
        setupCategory(view.findViewById(R.id.catCombo), "Combo", android.R.drawable.ic_menu_save);

        // Click Listeners
        view.findViewById(R.id.catHotel).setOnClickListener(v -> switchFragment(new HotelFragment()));
        view.findViewById(R.id.catTicket).setOnClickListener(v -> switchFragment(new TicketFragment()));
        view.findViewById(R.id.catCombo).setOnClickListener(v -> switchFragment(new ComboFragment()));

        // Data Loading
        allTours = dbHelper.getAllTours(); // Now sorted by bookCount and rating
        popularTours = dbHelper.getPopularTours();
        allHotels = dbHelper.getAllHotels();
        buildSearchIndex(allTours, allHotels);

        // Setup Main RecyclerView (All Tours)
        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvResults.setHasFixedSize(false);
        rvResults.setNestedScrollingEnabled(false);
        tourAdapter = new TourAdapter(new ArrayList<>(allTours));
        rvResults.setAdapter(tourAdapter);

        // Setup Popular RecyclerView (Horizontal)
        LinearLayoutManager popularLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPopular.setLayoutManager(popularLayoutManager);
        popularAdapter = new TourAdapter(new ArrayList<>(popularTours)) {
            @Override
            public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                // Adjust width for horizontal items
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                holder.itemView.setLayoutParams(lp);
            }
        };
        rvPopular.setAdapter(popularAdapter);

        searchAdapter = new HomeSearchAdapter();

        EditText etSearch = view.findViewById(R.id.etSearchHome);
        etSearch.setOnClickListener(v -> switchFragment(new SearchFragment()));

        return view;
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

    private void buildSearchIndex(List<Tour> tours, List<Hotel> hotels) {
        searchIndex.clear();
        Map<String, String> locations = new LinkedHashMap<>();

        for (Tour tour : tours) {
            String title = tour.getTitle() == null ? "" : tour.getTitle();
            String location = tour.getLocation() == null ? "" : tour.getLocation();
            searchIndex.add(new SearchIndexItem(
                SearchIndexType.TOUR,
                tour,
                null,
                SearchUtils.normalize(title),
                SearchUtils.normalize(location)
            ));
            addLocation(locations, location);
        }

        for (Hotel hotel : hotels) {
            String title = hotel.getName() == null ? "" : hotel.getName();
            String location = hotel.getLocation() == null ? "" : hotel.getLocation();
            searchIndex.add(new SearchIndexItem(
                SearchIndexType.HOTEL,
                null,
                hotel,
                SearchUtils.normalize(title),
                SearchUtils.normalize(location)
            ));
            addLocation(locations, location);
        }

        locationIndex = new ArrayList<>();
        for (Map.Entry<String, String> entry : locations.entrySet()) {
            if (!entry.getKey().isEmpty()) {
                locationIndex.add(new LocationIndexItem(entry.getValue(), entry.getKey()));
            }
        }
    }

    private void addLocation(Map<String, String> locations, String display) {
        if (display == null || display.trim().isEmpty()) {
            return;
        }
        String normalized = SearchUtils.normalize(display);
        if (!normalized.isEmpty() && !locations.containsKey(normalized)) {
            locations.put(normalized, display.trim());
        }
    }

    private enum SearchIndexType {
        TOUR,
        HOTEL
    }

    private static class SearchIndexItem {
        final SearchIndexType type;
        final Tour tour;
        final Hotel hotel;
        final String titleNormalized;
        final String locationNormalized;

        SearchIndexItem(SearchIndexType type, Tour tour, Hotel hotel, String titleNormalized, String locationNormalized) {
            this.type = type;
            this.tour = tour;
            this.hotel = hotel;
            this.titleNormalized = titleNormalized;
            this.locationNormalized = locationNormalized;
        }
    }

    private static class LocationIndexItem {
        final String display;
        final String normalized;

        LocationIndexItem(String display, String normalized) {
            this.display = display;
            this.normalized = normalized;
        }
    }
}
