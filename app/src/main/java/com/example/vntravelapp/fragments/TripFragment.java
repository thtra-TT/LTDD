package com.example.vntravelapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.TripAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripFragment extends Fragment {

    private DatabaseHelper db;
    private List<Trip> tripList;
    private TripAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        RecyclerView rvTrips = view.findViewById(R.id.rvTrips);
        rvTrips.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new DatabaseHelper(getContext());

        tripList = new ArrayList<>();

        loadTripsFromDB();

        adapter = new TripAdapter(tripList);
        rvTrips.setAdapter(adapter);

        return view;
    }

    private void loadTripsFromDB() {
        Cursor cursor = db.getAllOrders();

        if (cursor != null) {
            while (cursor.moveToNext()) {

                // ⚠️ Sửa index theo DB của bạn
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                int people = cursor.getInt(3);

                // Fake thêm data để khớp UI
                Trip trip = new Trip(
                        title,
                        "Việt Nam",          // location (có thể lưu DB sau)
                        date,
                        "Đã đặt",           // status
                        "CODE" + date,     // booking code
                        people + " người", // giá fake
                        R.mipmap.ic_launcher,
                        false
                );

                tripList.add(trip);
            }

            cursor.close();
        }
    }
}