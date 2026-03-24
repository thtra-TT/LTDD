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
import com.example.vntravelapp.adapters.TicketOfferAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.TicketOffer;
import com.example.vntravelapp.utils.SearchUtils;
import java.util.ArrayList;
import java.util.List;

public class TicketFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TicketOfferAdapter adapter;
    private List<TicketOffer> allOffers = new ArrayList<>();
    private TextView tvNoResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        dbHelper = new DatabaseHelper(getContext());

        view.findViewById(R.id.ivBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        RecyclerView rvOffers = view.findViewById(R.id.rvOffers);
        rvOffers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        allOffers = dbHelper.getAllTickets();
        adapter = new TicketOfferAdapter(new ArrayList<>(allOffers));
        rvOffers.setAdapter(adapter);

        tvNoResults = view.findViewById(R.id.tvNoResultsTicket);
        EditText etSearch = view.findViewById(R.id.etSearchTicket);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOffers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void filterOffers(String query) {
        String normalized = SearchUtils.normalize(query);
        if (normalized.isEmpty()) {
            adapter.updateOffers(new ArrayList<>(allOffers));
            tvNoResults.setVisibility(View.GONE);
            return;
        }

        List<TicketOffer> filtered = new ArrayList<>();
        for (TicketOffer offer : allOffers) {
            String route = SearchUtils.normalize(offer.getRoute());
            String type = SearchUtils.normalize(offer.getType());
            double score = Math.max(
                SearchUtils.matchScore(normalized, route),
                SearchUtils.matchScore(normalized, type) * 0.9
            );
            if (score >= 65.0) {
                filtered.add(offer);
            }
        }

        adapter.updateOffers(filtered);
        tvNoResults.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
