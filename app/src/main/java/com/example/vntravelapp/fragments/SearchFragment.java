package com.example.vntravelapp.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.TourAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.Tour;
import com.example.vntravelapp.utils.SearchUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment {

    private EditText etSearch;
    private ImageView ivBack, ivClear;
    private ListView lvSuggestions;
    private LinearLayout layoutChips, llChips;
    private RecyclerView rvResults;
    private LinearLayout layoutEmpty;
    private TextView tvEmptyMsg;

    private DatabaseHelper dbHelper;
    private List<Tour> allTours = new ArrayList<>();
    private List<Hotel> allHotels = new ArrayList<>();

    // Adapter cho dropdown gợi ý
    private SuggestionAdapter suggestionAdapter;
    private List<SuggestionItem> suggestionList = new ArrayList<>();

    // Adapter cho kết quả cuối
    private TourAdapter tourAdapter;

    // ─── Popular chips ────────────────────────────────────────────────────────
    private static final String[] POPULAR = {
            "Đà Nẵng", "Phú Quốc", "Hội An", "Hạ Long", "Đà Lạt", "Nha Trang", "Sa Pa", "Hà Nội"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Ánh xạ ───────────────────────────────────────────────────────────
        etSearch      = view.findViewById(R.id.etSearch);
        ivBack        = view.findViewById(R.id.ivBackSearch);
        ivClear       = view.findViewById(R.id.ivClearSearch);
        lvSuggestions = view.findViewById(R.id.lvSuggestions);
        layoutChips   = view.findViewById(R.id.layoutChips);
        llChips       = view.findViewById(R.id.llChips);
        rvResults     = view.findViewById(R.id.rvSearchResults);
        layoutEmpty   = view.findViewById(R.id.layoutEmpty);
        tvEmptyMsg    = view.findViewById(R.id.tvEmptyMsg);

        // ── Data ─────────────────────────────────────────────────────────────
        dbHelper  = new DatabaseHelper(requireContext());
        allTours  = dbHelper.getAllTours();
        allHotels = dbHelper.getAllHotels();

        // ── RecyclerView kết quả ─────────────────────────────────────────────
        tourAdapter = new TourAdapter(new ArrayList<>(), false, "");        rvResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvResults.setAdapter(tourAdapter);

        // ── Dropdown suggestions ─────────────────────────────────────────────
        suggestionAdapter = new SuggestionAdapter(requireContext(), suggestionList);
        lvSuggestions.setAdapter(suggestionAdapter);

        lvSuggestions.setOnItemClickListener((parent, v2, pos, id) -> {
            SuggestionItem item = suggestionList.get(pos);
            // Điền text vào ô search và tìm luôn
            etSearch.setText(item.label);
            etSearch.setSelection(item.label.length());
            hideSuggestions();
            hideKeyboard();
            performSearch(item.label);
        });

        // ── Popular chips ────────────────────────────────────────────────────
        buildChips();

        // ── Back button ──────────────────────────────────────────────────────
        ivBack.setOnClickListener(v2 -> {
            hideKeyboard();
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        });

        // ── Clear button ─────────────────────────────────────────────────────
        ivClear.setOnClickListener(v2 -> {
            etSearch.setText("");
            etSearch.requestFocus();
        });

        // ── TextWatcher — gợi ý real-time ────────────────────────────────────
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                ivClear.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);

                if (query.isEmpty()) {
                    hideSuggestions();
                    showChips();
                    hideResults();
                } else {
                    showSuggestions(query);
                    hideChips();
                }
            }
        });

        // ── Enter / search action ─────────────────────────────────────────────
        etSearch.setOnEditorActionListener((v2, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSuggestions();
                hideKeyboard();
                performSearch(etSearch.getText().toString().trim());
                return true;
            }
            return false;
        });

        // ── Auto focus ───────────────────────────────────────────────────────
        etSearch.post(() -> {
            etSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager)
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    // ─── Tạo gợi ý dropdown ──────────────────────────────────────────────────
    private void showSuggestions(String query) {
        String q = query.toLowerCase().trim();
        suggestionList.clear();

        // Thu thập địa điểm không trùng
        Set<String> addedLocations = new LinkedHashSet<>();
        for (Tour t : allTours) {
            if (t.getLocation() != null &&
                    t.getLocation().toLowerCase().contains(q)) {
                addedLocations.add(t.getLocation());
            }
        }
        for (Hotel h : allHotels) {
            if (h.getLocation() != null &&
                    h.getLocation().toLowerCase().contains(q)) {
                addedLocations.add(h.getLocation());
            }
        }
        for (String loc : addedLocations) {
            suggestionList.add(new SuggestionItem("📍 " + loc, loc, SuggestionType.LOCATION));
        }

        // Tour match
        for (Tour t : allTours) {
            if (t.getTitle() != null &&
                    t.getTitle().toLowerCase().contains(q)) {
                suggestionList.add(new SuggestionItem("🗺 " + t.getTitle(), t.getTitle(), SuggestionType.TOUR));
            }
        }

        // Hotel match
        for (Hotel h : allHotels) {
            if (h.getName() != null &&
                    h.getName().toLowerCase().contains(q)) {
                suggestionList.add(new SuggestionItem("🏨 " + h.getName(), h.getName(), SuggestionType.HOTEL));
            }
        }

        if (suggestionList.isEmpty()) {
            lvSuggestions.setVisibility(View.GONE);
        } else {
            // Giới hạn tối đa 7 gợi ý
            if (suggestionList.size() > 7) {
                suggestionList = suggestionList.subList(0, 7);
            }
            suggestionAdapter.notifyDataSetChanged();
            lvSuggestions.setVisibility(View.VISIBLE);
        }
    }

    // ─── Tìm kiếm và hiển thị kết quả ───────────────────────────────────────
    private void performSearch(String query) {
        if (query.isEmpty()) return;
        String q = query.toLowerCase();

        List<Tour> results = new ArrayList<>();
        for (Tour t : allTours) {
            boolean matchTitle    = t.getTitle()    != null && t.getTitle().toLowerCase().contains(q);
            boolean matchLocation = t.getLocation() != null && t.getLocation().toLowerCase().contains(q);
            if (matchTitle || matchLocation) results.add(t);
        }

        tourAdapter = new TourAdapter(results, false, "");        rvResults.setAdapter(tourAdapter);

        if (results.isEmpty()) {
            rvResults.setVisibility(View.GONE);
            tvEmptyMsg.setText("Không tìm thấy \"" + query + "\"");
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvResults.setVisibility(View.VISIBLE);
        }
        hideChips();
    }

    // ─── Chips địa điểm phổ biến ─────────────────────────────────────────────
    private void buildChips() {
        llChips.removeAllViews();
        int dp = (int) (requireContext().getResources().getDisplayMetrics().density);

        for (String place : POPULAR) {
            TextView chip = new TextView(requireContext());
            chip.setText(place);
            chip.setTextSize(13);
            chip.setTextColor(0xFF1148B8);
            chip.setBackgroundColor(0xFFDDE8FF);
            chip.setPadding(14 * dp, 7 * dp, 14 * dp, 7 * dp);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 10 * dp, 0);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                etSearch.setText(place);
                etSearch.setSelection(place.length());
                hideSuggestions();
                hideKeyboard();
                performSearch(place);
            });

            llChips.addView(chip);
        }
    }

    // ─── Helpers hiện/ẩn ─────────────────────────────────────────────────────
    private void hideSuggestions() { lvSuggestions.setVisibility(View.GONE); }
    private void showChips()       { layoutChips.setVisibility(View.VISIBLE); }
    private void hideChips()       { layoutChips.setVisibility(View.GONE); }
    private void hideResults() {
        rvResults.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getView() != null)
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    // ─── SuggestionItem model ─────────────────────────────────────────────────
    enum SuggestionType { LOCATION, TOUR, HOTEL }

    static class SuggestionItem {
        String display;  // text hiển thị trong dropdown (có emoji)
        String label;    // text điền vào ô search khi chọn
        SuggestionType type;

        SuggestionItem(String display, String label, SuggestionType type) {
            this.display = display;
            this.label   = label;
            this.type    = type;
        }
    }

    // ─── SuggestionAdapter ───────────────────────────────────────────────────
    static class SuggestionAdapter extends ArrayAdapter<SuggestionItem> {
        private final List<SuggestionItem> items;

        SuggestionAdapter(Context ctx, List<SuggestionItem> items) {
            super(ctx, android.R.layout.simple_list_item_1, items);
            this.items = items;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView tv = convertView.findViewById(android.R.id.text1);
            SuggestionItem item = items.get(position);
            tv.setText(item.display);
            tv.setTextColor(0xFF1A2A4A);
            tv.setTextSize(14);
            tv.setPadding(48, 28, 32, 28);

            // Màu phân biệt theo loại
            switch (item.type) {
                case LOCATION: tv.setTextColor(0xFF1565C0); break;
                case TOUR:     tv.setTextColor(0xFF1A2A4A); break;
                case HOTEL:    tv.setTextColor(0xFF2E7D32); break;
            }

            return convertView;
        }
    }
}