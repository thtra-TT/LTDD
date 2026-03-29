package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.BusTripAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.BusTrip;
import java.util.ArrayList;
import java.util.List;

public class BusTripListFragment extends Fragment {

    private String departure, destination, date;
    private int passengers;
    private DatabaseHelper db;
    private BusTripAdapter adapter;
    private List<BusTrip> tripList = new ArrayList<>();

    public static BusTripListFragment newInstance(String dep, String dest, String date, int passengers) {
        BusTripListFragment f = new BusTripListFragment();
        Bundle args = new Bundle();
        args.putString("dep", dep);
        args.putString("dest", dest);
        args.putString("date", date);
        args.putInt("pass", passengers);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            departure = getArguments().getString("dep");
            destination = getArguments().getString("dest");
            date = getArguments().getString("date");
            passengers = getArguments().getInt("pass");
        }
        db = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_trip_list, container, false);

        view.findViewById(R.id.ivBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        TextView tvTitle = view.findViewById(R.id.tvHeaderTitle);
        tvTitle.setText(departure + " → " + destination);

        RecyclerView rv = view.findViewById(R.id.rvBusTrips);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        tripList = db.searchBusTrips(departure, destination, date, passengers);
        
        adapter = new BusTripAdapter(tripList, trip -> {
            // Chuyển sang BookingFragment
            String title = trip.getBusCompany() + " (" + trip.getDepartureTime() + ")";
            BookingFragment bookingFragment = BookingFragment.newInstance(
                    title,
                    trip.getPrice(),
                    trip.getDeparture() + " - " + trip.getDestination(),
                    "", // No image for bus yet
                    android.R.drawable.ic_menu_directions,
                    trip.getDate(),
                    trip.getDate()
            );
            
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, bookingFragment)
                    .addToBackStack(null)
                    .commit();
        });
        
        rv.setAdapter(adapter);

        view.findViewById(R.id.tvNoBusResults).setVisibility(tripList.isEmpty() ? View.VISIBLE : View.GONE);

        return view;
    }
}
