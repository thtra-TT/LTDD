package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.ProfileTourAdapter;
import com.example.vntravelapp.adapters.TripsPagerAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Tour;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class TripFragment extends Fragment {

    public static final String ARG_INITIAL_TAB = "initial_tab";

    private TripsPagerAdapter pagerAdapter;
    private DatabaseHelper db;
    private ProfileTourAdapter recommendationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        db = new DatabaseHelper(getContext());

        TabLayout tabLayout = view.findViewById(R.id.tabLayoutTrips);
        ViewPager2 viewPager = view.findViewById(R.id.vpTrips);
        EditText edtSearch = view.findViewById(R.id.edtTripSearch);

        pagerAdapter = new TripsPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Sắp đi");
            if (position == 1) tab.setText("Đã đi");
            if (position == 2) tab.setText("Đã hủy");
            if (position == 3) tab.setText("Yêu thích");
        }).attach();

        int initialTab = getArguments() != null ? getArguments().getInt(ARG_INITIAL_TAB, 0) : 0;
        if (initialTab >= 0 && initialTab < pagerAdapter.getItemCount()) {
            viewPager.setCurrentItem(initialTab, false);
        }

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                Fragment fragment = getChildFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());
                if (fragment instanceof TripsTabFragment) {
                    ((TripsTabFragment) fragment).filter(s.toString());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        RecyclerView rvRecommendations = view.findViewById(R.id.rvTripRecommendations);
        rvRecommendations.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendationAdapter = new ProfileTourAdapter(new ArrayList<>(), this::openTourDetail);
        rvRecommendations.setAdapter(recommendationAdapter);
        loadRecommendations();

        return view;
    }

    private void loadRecommendations() {
        List<Tour> tours = db.getRecommendedTours(6);
        recommendationAdapter.updateItems(tours);
    }

    private void openTourDetail(Tour tour) {
        DetailFragment fragment = DetailFragment.newInstanceWithItem(DetailFragment.ITEM_TYPE_TOUR, tour.getId());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}