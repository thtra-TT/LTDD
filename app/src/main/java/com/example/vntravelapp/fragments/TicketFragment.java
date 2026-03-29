package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.BusTripAdapter;
import com.example.vntravelapp.adapters.TicketOfferAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.BusTrip;
import com.example.vntravelapp.models.TicketOffer;
import com.example.vntravelapp.utils.SearchUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TicketFragment extends Fragment {

    private DatabaseHelper db;
    private TicketOfferAdapter offerAdapter;
    private BusTripAdapter busTripAdapter;
    
    private List<TicketOffer> allOffers = new ArrayList<>();
    private List<BusTrip> tripResults = new ArrayList<>();
    
    private TextView tvNoResults, tvNoBusResults;
    private View layoutBusResults;

    private Spinner spinnerDeparture, spinnerDestination;
    private TextView tvPassengerCount;
    private int passengerCount = 1;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private Button btnSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        db = new DatabaseHelper(requireContext());

        initViews(view);
        setupSpinners();
        setupRecyclerViews(view);

        view.findViewById(R.id.ivBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        EditText etSearch = view.findViewById(R.id.etSearchTicket);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOffers(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSearch.setOnClickListener(v -> performSearch());

        // Mặc định ngày 02/04/2026 cho tìm kiếm ngầm định
        calendar.set(2026, Calendar.APRIL, 2);

        return view;
    }

    private void initViews(View v) {
        spinnerDeparture = v.findViewById(R.id.spinnerDeparture);
        spinnerDestination = v.findViewById(R.id.spinnerDestination);
        tvPassengerCount = v.findViewById(R.id.tvPassengerCount);
        btnSearch = v.findViewById(R.id.btnSearchTransport);
        layoutBusResults = v.findViewById(R.id.layoutBusResults);
        tvNoBusResults = v.findViewById(R.id.tvNoBusResults);
        tvNoResults = v.findViewById(R.id.tvNoResultsTicket);
    }

    private void setupSpinners() {
        String[] locations = {"Hà Nội", "Hải Phòng", "Đà Nẵng", "TP. Hồ Chí Minh", "Cần Thơ", "Đà Lạt", "Lào Cai", "Phú Quốc"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDeparture.setAdapter(adapter);
        spinnerDestination.setAdapter(adapter);
        spinnerDeparture.setSelection(0); // Hà Nội
        spinnerDestination.setSelection(1); // Hải Phòng
    }

    private void setupRecyclerViews(View v) {
        // Offers
        RecyclerView rvOffers = v.findViewById(R.id.rvOffers);
        rvOffers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        allOffers = db.getAllTickets();
        offerAdapter = new TicketOfferAdapter(new ArrayList<>(allOffers));
        rvOffers.setAdapter(offerAdapter);

        // Bus Results
        RecyclerView rvBus = v.findViewById(R.id.rvBusTrips);
        rvBus.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBus.setNestedScrollingEnabled(false);
        busTripAdapter = new BusTripAdapter(tripResults, trip -> {
            // Chuyển sang BookingFragment
            String title = trip.getBusCompany() + " (" + trip.getDepartureTime() + ")";
            BookingFragment bookingFragment = BookingFragment.newInstance(
                    title,
                    trip.getPrice(),
                    trip.getDeparture() + " - " + trip.getDestination(),
                    "",
                    android.R.drawable.ic_menu_directions,
                    trip.getDate(),
                    trip.getDate()
            );
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, bookingFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rvBus.setAdapter(busTripAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        view.findViewById(R.id.btnMinusPassenger).setOnClickListener(v -> {
            if (passengerCount > 1) {
                passengerCount--;
                tvPassengerCount.setText(String.valueOf(passengerCount));
            }
        });

        view.findViewById(R.id.btnPlusPassenger).setOnClickListener(v -> {
            if (passengerCount < 10) {
                passengerCount++;
                tvPassengerCount.setText(String.valueOf(passengerCount));
            }
        });

        view.findViewById(R.id.ivSwapLocations).setOnClickListener(v -> {
            int posDep = spinnerDeparture.getSelectedItemPosition();
            int posDest = spinnerDestination.getSelectedItemPosition();
            spinnerDeparture.setSelection(posDest);
            spinnerDestination.setSelection(posDep);
        });
    }

    private void performSearch() {
        String dep = spinnerDeparture.getSelectedItem().toString();
        String dest = spinnerDestination.getSelectedItem().toString();
        String dateStr = dateFormatter.format(calendar.getTime());

        if (dep.equals(dest)) {
            Toast.makeText(getContext(), "Điểm đi và điểm đến không được trùng nhau", Toast.LENGTH_SHORT).show();
            return;
        }

        tripResults = db.searchBusTrips(dep, dest, dateStr, passengerCount);
        busTripAdapter.updateData(tripResults);
        
        layoutBusResults.setVisibility(View.VISIBLE);
        tvNoBusResults.setVisibility(tripResults.isEmpty() ? View.VISIBLE : View.GONE);
        
        // Cuộn xuống phần kết quả
        layoutBusResults.getParent().requestChildFocus(layoutBusResults, layoutBusResults);
    }

    private void filterOffers(String query) {
        String normalized = SearchUtils.normalize(query);
        if (normalized.isEmpty()) {
            offerAdapter.updateOffers(new ArrayList<>(allOffers));
            tvNoResults.setVisibility(View.GONE);
            return;
        }

        List<TicketOffer> filtered = new ArrayList<>();
        for (TicketOffer offer : allOffers) {
            String route = SearchUtils.normalize(offer.getRoute());
            if (route.contains(normalized)) {
                filtered.add(offer);
            }
        }

        offerAdapter.updateOffers(filtered);
        tvNoResults.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
