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
import com.example.vntravelapp.adapters.ComboAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Combo;
import com.example.vntravelapp.utils.SearchUtils;
import java.util.ArrayList;
import java.util.List;

public class ComboFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ComboAdapter adapter;
    private List<Combo> allCombos = new ArrayList<>();
    private TextView tvNoResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combo, container, false);
        dbHelper = new DatabaseHelper(getContext());

        view.findViewById(R.id.ivBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        RecyclerView rvCombos = view.findViewById(R.id.rvCombos);
        rvCombos.setLayoutManager(new LinearLayoutManager(getContext()));

        allCombos = dbHelper.getAllCombos();
        adapter = new ComboAdapter(new ArrayList<>(allCombos));
        rvCombos.setAdapter(adapter);

        tvNoResults = view.findViewById(R.id.tvNoResultsCombo);
        EditText etSearch = view.findViewById(R.id.etSearchCombo);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCombos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void filterCombos(String query) {
        String normalized = SearchUtils.normalize(query);
        if (normalized.isEmpty()) {
            adapter.updateCombos(new ArrayList<>(allCombos));
            tvNoResults.setVisibility(View.GONE);
            return;
        }

        List<Combo> filtered = new ArrayList<>();
        for (Combo combo : allCombos) {
            String title = SearchUtils.normalize(combo.getTitle());
            String location = SearchUtils.normalize(combo.getLocation());
            String description = SearchUtils.normalize(combo.getDescription());
            double score = Math.max(
                SearchUtils.matchScore(normalized, title),
                SearchUtils.matchScore(normalized, location) * 1.1
            );
            score = Math.max(score, SearchUtils.matchScore(normalized, description) * 0.9);

            if (score >= 65.0) {
                filtered.add(combo);
            }
        }

        adapter.updateCombos(filtered);
        tvNoResults.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
