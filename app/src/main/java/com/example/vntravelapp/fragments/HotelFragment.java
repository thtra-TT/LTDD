package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.HotelAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.utils.SearchUtils;
import java.util.ArrayList;
import java.util.List;

public class HotelFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private HotelAdapter adapter;
    private List<Hotel> allHotels = new ArrayList<>();
    private TextView tvNoResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel, container, false);
        dbHelper = new DatabaseHelper(getContext());

        view.findViewById(R.id.ivBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        RecyclerView rvHotels = view.findViewById(R.id.rvHotels);
        rvHotels.setLayoutManager(new LinearLayoutManager(getContext()));

        allHotels = dbHelper.getAllHotels();
        adapter = new HotelAdapter(new ArrayList<>(allHotels));
        rvHotels.setAdapter(adapter);

        tvNoResults = view.findViewById(R.id.tvNoResultsHotel);
        EditText etSearch = view.findViewById(R.id.etSearchHotel);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHotels(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void filterHotels(String query) {
        String normalized = SearchUtils.normalize(query);
        if (normalized.isEmpty()) {
            adapter.updateHotels(new ArrayList<>(allHotels));
            tvNoResults.setVisibility(View.GONE);
            return;
        }

        List<Hotel> filtered = new ArrayList<>();
        for (Hotel hotel : allHotels) {
            String name = SearchUtils.normalize(hotel.getName());
            String location = SearchUtils.normalize(hotel.getLocation());
            double score = Math.max(
                SearchUtils.matchScore(normalized, name),
                SearchUtils.matchScore(normalized, location) * 1.1
            );
            if (score >= 65.0) {
                filtered.add(hotel);
            }
        }

        adapter.updateHotels(filtered);
        tvNoResults.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
