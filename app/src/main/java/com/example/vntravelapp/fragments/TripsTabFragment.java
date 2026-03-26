package com.example.vntravelapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vntravelapp.HomeActivity;
import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.TripsAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.TripItem;
import com.example.vntravelapp.models.Tour;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class TripsTabFragment extends Fragment {

    public static final String ARG_TYPE = "tab_type";
    public static final int TYPE_UPCOMING = 0;
    public static final int TYPE_COMPLETED = 1;
    public static final int TYPE_CANCELLED = 2;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvTrips;
    private TripsAdapter adapter;
    private DatabaseHelper db;
    private List<TripItem> allItems = new ArrayList<>();

    private View containerList;
    private View containerMap;
    private View containerTimeline;
    private LinearLayout llTimeline;
    private LinearLayout llVisited;
    private TextView tvEmpty;
    private MaterialButtonToggleGroup toggleGroup;

    private int type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips_tab, container, false);
        db = new DatabaseHelper(getContext());
        type = getArguments() != null ? getArguments().getInt(ARG_TYPE, TYPE_UPCOMING) : TYPE_UPCOMING;

        swipeRefreshLayout = view.findViewById(R.id.swipeTripsTab);
        rvTrips = view.findViewById(R.id.rvTripsTab);
        containerList = view.findViewById(R.id.containerList);
        containerMap = view.findViewById(R.id.containerMap);
        containerTimeline = view.findViewById(R.id.containerTimeline);
        llTimeline = view.findViewById(R.id.llTimelineContainer);
        llVisited = view.findViewById(R.id.llVisitedContainer);
        tvEmpty = view.findViewById(R.id.tvEmptyTripsTab);
        toggleGroup = view.findViewById(R.id.toggleViewMode);

        rvTrips.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TripsAdapter(new TripsAdapter.TripActionListener() {
            @Override
            public void onViewDetail(TripItem item) {
                openTourDetail(item.getTitle());
            }

            @Override
            public void onCancel(TripItem item) {
                showCancelDialog(item);
            }

            @Override
            public void onReview(TripItem item) {
                Toast.makeText(getContext(), "Mở form review", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRebook(TripItem item) {
                Toast.makeText(getContext(), "Đặt lại chuyến đi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFavorite(TripItem item) {
                Toast.makeText(getContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
        rvTrips.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        setupToggle();
        loadData();

        return view;
    }

    private void setupToggle() {
        if (type == TYPE_COMPLETED) {
            toggleGroup.setVisibility(View.VISIBLE);
            toggleGroup.check(R.id.btnViewList);
            toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (!isChecked) return;
                if (checkedId == R.id.btnViewList) {
                    showContainer(containerList);
                } else if (checkedId == R.id.btnViewMap) {
                    showContainer(containerMap);
                } else if (checkedId == R.id.btnViewTimeline) {
                    showContainer(containerTimeline);
                }
            });
        } else {
            toggleGroup.setVisibility(View.GONE);
            showContainer(containerList);
        }
    }

    private void showContainer(View target) {
        containerList.setVisibility(target == containerList ? View.VISIBLE : View.GONE);
        containerMap.setVisibility(target == containerMap ? View.VISIBLE : View.GONE);
        containerTimeline.setVisibility(target == containerTimeline ? View.VISIBLE : View.GONE);
    }

    private void loadData() {
        if (type == TYPE_UPCOMING) {
            allItems = db.getUpcomingTrips();
        } else if (type == TYPE_COMPLETED) {
            allItems = db.getCompletedTrips();
        } else {
            allItems = db.getCancelledTrips();
        }
        adapter.submitList(allItems);
        tvEmpty.setVisibility(allItems.isEmpty() ? View.VISIBLE : View.GONE);

        if (type == TYPE_COMPLETED) {
            renderTimeline(allItems);
            renderVisitedLocations();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.submitList(allItems);
            return;
        }
        String lower = query.toLowerCase();
        List<TripItem> filtered = new ArrayList<>();
        for (TripItem item : allItems) {
            if (item.getTitle().toLowerCase().contains(lower) || item.getLocation().toLowerCase().contains(lower)) {
                filtered.add(item);
            }
        }
        adapter.submitList(filtered);
    }

    private void renderTimeline(List<TripItem> items) {
        llTimeline.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (TripItem item : items) {
            View row = inflater.inflate(R.layout.item_timeline_row, llTimeline, false);
            ((TextView) row.findViewById(R.id.tvTimelineTitle)).setText(item.getTitle());
            ((TextView) row.findViewById(R.id.tvTimelineMeta)).setText(item.getDate() + " • " + item.getLocation());
            llTimeline.addView(row);
        }
    }

    private void renderVisitedLocations() {
        llVisited.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (String location : db.getVisitedLocations()) {
            View chip = inflater.inflate(R.layout.item_profile_chip, llVisited, false);
            ((TextView) chip.findViewById(R.id.tvChipText)).setText(location);
            llVisited.addView(chip);
        }
        View btnOpenMap = getView() != null ? getView().findViewById(R.id.btnOpenMap) : null;
        if (btnOpenMap != null) {
            btnOpenMap.setOnClickListener(v -> {
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).selectTab(R.id.nav_map);
                }
            });
        }
    }

    private void showCancelDialog(TripItem item) {
        EditText edtReason = new EditText(getContext());
        edtReason.setHint("Lý do hủy");
        new AlertDialog.Builder(getContext())
                .setTitle("Hủy chuyến")
                .setView(edtReason)
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    String reason = edtReason.getText().toString().trim();
                    db.cancelTrip(item.getTitle(), item.getDate(), reason);
                    loadData();
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void openTourDetail(String title) {
        Tour tour = db.getTourByTitle(title);
        if (tour == null) {
            Toast.makeText(getContext(), "Không tìm thấy chi tiết tour", Toast.LENGTH_SHORT).show();
            return;
        }
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
}
