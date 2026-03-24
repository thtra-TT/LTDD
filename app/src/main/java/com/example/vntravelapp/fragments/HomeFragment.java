package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
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
    private HomeSearchAdapter searchAdapter;
    private List<Tour> allTours = new ArrayList<>();
    private List<Hotel> allHotels = new ArrayList<>();
    private List<SearchIndexItem> searchIndex = new ArrayList<>();
    private List<LocationIndexItem> locationIndex = new ArrayList<>();
    private TextView tvNoResults;
    private TextView tvFeaturedTitle;
    private TextView tvViewAll;
    private RecyclerView rvResults;
    private boolean isSearching = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dbHelper = new DatabaseHelper(getContext());

        // Setup Categories
        setupCategory(view.findViewById(R.id.catTour), "Tour", android.R.drawable.ic_menu_directions);
        setupCategory(view.findViewById(R.id.catHotel), "Khách sạn", android.R.drawable.ic_menu_myplaces);
        setupCategory(view.findViewById(R.id.catTicket), "Vé", android.R.drawable.ic_menu_agenda);
        setupCategory(view.findViewById(R.id.catCombo), "Combo", android.R.drawable.ic_menu_save);

        // Add Click Listeners
        view.findViewById(R.id.catHotel).setOnClickListener(v -> switchFragment(new HotelFragment()));
        view.findViewById(R.id.catTicket).setOnClickListener(v -> switchFragment(new TicketFragment()));
        view.findViewById(R.id.catCombo).setOnClickListener(v -> switchFragment(new ComboFragment()));

        // Setup Tours RecyclerView from SQLite
        rvResults = view.findViewById(R.id.rvTours);
        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));

        allTours = dbHelper.getAllTours();
        allHotels = dbHelper.getAllHotels();
        buildSearchIndex(allTours, allHotels);

        tourAdapter = new TourAdapter(new ArrayList<>(allTours));
        searchAdapter = new HomeSearchAdapter();
        rvResults.setAdapter(tourAdapter);

        tvNoResults = view.findViewById(R.id.tvNoResults);
        tvFeaturedTitle = view.findViewById(R.id.tvFeaturedTitle);

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

    private void filterSearch(String query) {
        String normalized = SearchUtils.normalize(query);
        if (normalized.isEmpty()) {
            if (isSearching) {
                rvResults.setAdapter(tourAdapter);
                isSearching = false;
            }
            tvFeaturedTitle.setVisibility(View.VISIBLE);
            tvViewAll.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
            return;
        }

        SearchResults results = runSearch(normalized);
        if (!isSearching) {
            rvResults.setAdapter(searchAdapter);
            isSearching = true;
        }

        searchAdapter.submitSections(results.sections);
        tvFeaturedTitle.setVisibility(View.GONE);
        tvViewAll.setVisibility(View.GONE);
        tvNoResults.setVisibility(results.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private SearchResults runSearch(String normalizedQuery) {
        List<ScoredTour> scoredTours = new ArrayList<>();
        List<ScoredHotel> scoredHotels = new ArrayList<>();

        for (SearchIndexItem item : searchIndex) {
            double score = scoreItem(normalizedQuery, item);
            if (score < 65.0) {
                continue;
            }
            if (item.type == SearchIndexType.TOUR) {
                scoredTours.add(new ScoredTour(item.tour, score));
            } else if (item.type == SearchIndexType.HOTEL) {
                scoredHotels.add(new ScoredHotel(item.hotel, score));
            }
        }

        List<ScoredLocation> scoredLocations = new ArrayList<>();
        for (LocationIndexItem location : locationIndex) {
            double score = SearchUtils.matchScore(normalizedQuery, location.normalized);
            if (score >= 75.0) {
                scoredLocations.add(new ScoredLocation(location.display, score));
            }
        }

        Collections.sort(scoredTours, (a, b) -> Double.compare(b.score, a.score));
        Collections.sort(scoredHotels, (a, b) -> Double.compare(b.score, a.score));
        Collections.sort(scoredLocations, (a, b) -> Double.compare(b.score, a.score));

        List<HomeSearchAdapter.SearchSection> sections = new ArrayList<>();

        if (!scoredLocations.isEmpty()) {
            List<HomeSearchAdapter.SearchRow> rows = new ArrayList<>();
            for (ScoredLocation entry : scoredLocations) {
                rows.add(new HomeSearchAdapter.LocationRow(entry.location, entry.score));
            }
            sections.add(new HomeSearchAdapter.SearchSection("Địa điểm", rows));
        }

        if (!scoredTours.isEmpty()) {
            List<HomeSearchAdapter.SearchRow> rows = new ArrayList<>();
            for (ScoredTour entry : scoredTours) {
                rows.add(new HomeSearchAdapter.TourRow(entry.tour, entry.score));
            }
            sections.add(new HomeSearchAdapter.SearchSection("Tour", rows));
        }

        if (!scoredHotels.isEmpty()) {
            List<HomeSearchAdapter.SearchRow> rows = new ArrayList<>();
            for (ScoredHotel entry : scoredHotels) {
                rows.add(new HomeSearchAdapter.HotelRow(entry.hotel, entry.score));
            }
            sections.add(new HomeSearchAdapter.SearchSection("Khách sạn", rows));
        }

        return new SearchResults(sections);
    }

    private double scoreItem(String normalizedQuery, SearchIndexItem item) {
        double locationScore = SearchUtils.matchScore(normalizedQuery, item.locationNormalized);
        double titleScore = SearchUtils.matchScore(normalizedQuery, item.titleNormalized);

        double best = Math.max(titleScore, locationScore * 1.15);
        if (locationScore >= 90.0) {
            best += 10.0;
        } else if (locationScore >= 80.0) {
            best += 5.0;
        }

        if (titleScore >= 90.0) {
            best += 5.0;
        }

        return best;
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

    private static class SearchResults {
        final List<HomeSearchAdapter.SearchSection> sections;

        SearchResults(List<HomeSearchAdapter.SearchSection> sections) {
            this.sections = sections;
        }

        boolean isEmpty() {
            return sections == null || sections.isEmpty();
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

    private static class ScoredTour {
        final Tour tour;
        final double score;

        ScoredTour(Tour tour, double score) {
            this.tour = tour;
            this.score = score;
        }
    }

    private static class ScoredHotel {
        final Hotel hotel;
        final double score;

        ScoredHotel(Hotel hotel, double score) {
            this.hotel = hotel;
            this.score = score;
        }
    }

    private static class ScoredLocation {
        final String location;
        final double score;

        ScoredLocation(String location, double score) {
            this.location = location;
            this.score = score;
        }
    }
}
