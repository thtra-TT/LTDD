package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.FavoriteCardAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Tour;

import java.util.ArrayList;
import java.util.List;

public class TripsFavoritesFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvFavorites;
    private TextView tvEmpty;
    private FavoriteCardAdapter adapter;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips_favorites, container, false);
        db = new DatabaseHelper(getContext());

        swipeRefreshLayout = view.findViewById(R.id.swipeFavorites);
        rvFavorites = view.findViewById(R.id.rvFavorites);
        tvEmpty = view.findViewById(R.id.tvEmptyFavorites);

        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoriteCardAdapter(new FavoriteCardAdapter.FavoriteActionListener() {
            @Override
            public void onOpen(Tour tour) {
                openTourDetail(tour);
            }

            @Override
            public void onRemove(Tour tour) {
                db.removeFavorite(tour.getTitle());
                loadFavorites();
                Toast.makeText(getContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
        rvFavorites.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadFavorites);
        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        List<Tour> list = db.getFavoriteTours();
        adapter.submitList(list);
        tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
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
}

