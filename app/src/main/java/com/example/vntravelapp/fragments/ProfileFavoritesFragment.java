package com.example.vntravelapp.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.FavoritesAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.FavoriteItem;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.Tour;

import java.util.List;

public class ProfileFavoritesFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvFavorites;
    private TextView tvEmpty;
    private FavoritesAdapter adapter;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);
        db = new DatabaseHelper(getContext());

        TextView tvTitle = view.findViewById(R.id.tvToolbarTitle);
        ImageView ivBack = view.findViewById(R.id.ivToolbarBack);
        if (tvTitle != null) tvTitle.setText(R.string.profile_title_favorites);
        if (ivBack != null) ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        swipeRefreshLayout = view.findViewById(R.id.swipeProfileList);
        rvFavorites = view.findViewById(R.id.rvProfileList);
        tvEmpty = view.findViewById(R.id.tvProfileEmpty);
        tvEmpty.setText(R.string.profile_empty_favorites);

        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoritesAdapter(new FavoritesAdapter.FavoriteActionListener() {
            @Override
            public void onOpen(FavoriteItem item) {
                openFavoriteDetail(item);
            }

            @Override
            public void onRemove(FavoriteItem item) {
                db.removeFavorite(item.getTitle(), item.getItemType());
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
        List<FavoriteItem> list = db.getFavoriteItems();
        adapter.submitList(list);
        tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void openFavoriteDetail(FavoriteItem item) {
        if ("Hotel".equals(item.getItemType())) {
            Hotel hotel = db.getHotelById(item.getItemId());
            if (hotel == null) {
                Toast.makeText(getContext(), "Không tìm thấy chi tiết khách sạn", Toast.LENGTH_SHORT).show();
                return;
            }
            DetailFragment fragment = DetailFragment.newInstanceWithItem(DetailFragment.ITEM_TYPE_HOTEL, hotel.getId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return;
        }

        Tour tour = db.getTourById(item.getItemId());
        if (tour == null) {
            Toast.makeText(getContext(), "Không tìm thấy chi tiết tour", Toast.LENGTH_SHORT).show();
            return;
        }
        DetailFragment fragment = DetailFragment.newInstanceWithItem(DetailFragment.ITEM_TYPE_TOUR, tour.getId());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

