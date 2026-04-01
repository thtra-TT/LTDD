package com.example.vntravelapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.ReviewsAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.ReviewEntry;
import com.example.vntravelapp.models.Tour;

import java.util.List;

public class ProfileReviewsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvReviews;
    private TextView tvEmpty;
    private ReviewsAdapter adapter;
    private DatabaseHelper db;
    private String currentEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);
        db = new DatabaseHelper(getContext());

        SharedPreferences pref = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        currentEmail = pref.getString("saved_email", "");

        TextView tvTitle = view.findViewById(R.id.tvToolbarTitle);
        ImageView ivBack = view.findViewById(R.id.ivToolbarBack);
        if (tvTitle != null) tvTitle.setText(R.string.profile_title_reviews);
        if (ivBack != null) ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        swipeRefreshLayout = view.findViewById(R.id.swipeProfileList);
        rvReviews = view.findViewById(R.id.rvProfileList);
        tvEmpty = view.findViewById(R.id.tvProfileEmpty);
        tvEmpty.setText(R.string.profile_empty_reviews);

        rvReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReviewsAdapter(this::openReviewDetail, this::openReviewActions);
        rvReviews.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadReviews);
        loadReviews();

        return view;
    }

    private void loadReviews() {
        List<ReviewEntry> list = db.getReviewEntriesByUser(currentEmail);
        adapter.submitList(list);
        tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void openReviewDetail(ReviewEntry item) {
        if ("Hotel".equalsIgnoreCase(item.getItemType())) {
            Hotel hotel = db.getHotelByTitle(item.getItemTitle());
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

        Tour tour = db.getTourByTitle(item.getItemTitle());
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

    private void openReviewActions(ReviewEntry item) {
        if (item == null || item.getId() <= 0) return;
        CharSequence[] actions = {"Chỉnh sửa", "Xóa"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Đánh giá của tôi")
                .setItems(actions, (dialog, which) -> {
                    if (which == 0) {
                        showEditReviewDialog(item);
                    } else {
                        confirmDeleteReview(item);
                    }
                })
                .show();
    }

    private void showEditReviewDialog(ReviewEntry item) {
        if (item == null) return;
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(padding, padding, padding, padding);

        RatingBar ratingBar = new RatingBar(requireContext(), null, android.R.attr.ratingBarStyleSmall);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.5f);
        ratingBar.setRating(item.getRating());

        EditText input = new EditText(requireContext());
        input.setText(item.getContent());
        input.setMinLines(3);
        input.setHint("Chia sẻ trải nghiệm...");

        layout.addView(ratingBar);
        layout.addView(input);

        new AlertDialog.Builder(requireContext())
                .setTitle("Chỉnh sửa đánh giá")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    boolean ok = db.updateComment(item.getId(), input.getText().toString(), ratingBar.getRating());
                    if (!ok) {
                        Toast.makeText(getContext(), "Không thể cập nhật đánh giá", Toast.LENGTH_SHORT).show();
                    }
                    loadReviews();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmDeleteReview(ReviewEntry item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa đánh giá?")
                .setMessage("Bạn chắc chắn muốn xóa đánh giá này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean ok = db.deleteComment(item.getId());
                    if (!ok) {
                        Toast.makeText(getContext(), "Không thể xóa đánh giá", Toast.LENGTH_SHORT).show();
                    }
                    loadReviews();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
