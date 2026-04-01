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
import com.example.vntravelapp.adapters.JournalAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.JournalEntry;

import java.util.List;

public class ProfileJournalFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvJournal;
    private TextView tvEmpty;
    private JournalAdapter adapter;
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
        TextView tvAction = view.findViewById(R.id.tvToolbarAction);
        if (tvTitle != null) tvTitle.setText(R.string.profile_title_journal);
        if (ivBack != null) ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        if (tvAction != null) {
            tvAction.setText("Viết nhật ký");
            tvAction.setVisibility(View.VISIBLE);
            tvAction.setOnClickListener(v -> showCreateJournalDialog());
        }

        swipeRefreshLayout = view.findViewById(R.id.swipeProfileList);
        rvJournal = view.findViewById(R.id.rvProfileList);
        tvEmpty = view.findViewById(R.id.tvProfileEmpty);
        tvEmpty.setText(R.string.profile_empty_journal);

        rvJournal.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new JournalAdapter(this::openJournalDetail, this::openJournalActions);
        rvJournal.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadJournal);
        loadJournal();

        return view;
    }

    private void loadJournal() {
        List<JournalEntry> list = db.getTravelDiaryEntriesByUser(currentEmail);
        adapter.submitList(list);
        tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void openJournalDetail(JournalEntry entry) {
        JournalDetailFragment fragment = JournalDetailFragment.newInstance(entry.getTitle(), entry.getContent(), entry.getImageUrl(), entry.getRating(), entry.getCreatedAt());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openJournalActions(JournalEntry entry) {
        if (entry == null || entry.getId() <= 0) return;
        CharSequence[] actions = {"Chỉnh sửa", "Viết thêm", "Xóa"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Nhật ký du lịch")
                .setItems(actions, (dialog, which) -> {
                    if (which == 0) {
                        showEditJournalDialog(entry);
                    } else if (which == 1) {
                        showAppendJournalDialog(entry);
                    } else {
                        confirmDeleteJournal(entry);
                    }
                })
                .show();
    }

    private void showCreateJournalDialog() {
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        LinearLayout layout = buildJournalForm(null, padding);
        EditText etTitle = (EditText) layout.getChildAt(0);
        EditText etContent = (EditText) layout.getChildAt(1);
        EditText etImage = (EditText) layout.getChildAt(2);
        EditText etTour = (EditText) layout.getChildAt(3);
        RatingBar rb = (RatingBar) layout.getChildAt(4);

        new AlertDialog.Builder(requireContext())
                .setTitle("Viết nhật ký mới")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    int userId = db.getUserIdByEmail(currentEmail);
                    if (userId <= 0) {
                        Toast.makeText(getContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Integer tourId = null;
                    String tourTitle = etTour.getText().toString().trim();
                    if (!tourTitle.isEmpty()) {
                        int resolved = db.getTourIdByTitle(tourTitle);
                        if (resolved > 0) tourId = resolved;
                    }
                    long id = db.addTravelDiaryEntry(userId, tourId, etTitle.getText().toString(), etContent.getText().toString(), etImage.getText().toString(), rb.getRating());
                    if (id <= 0) {
                        Toast.makeText(getContext(), "Không thể tạo nhật ký", Toast.LENGTH_SHORT).show();
                    }
                    loadJournal();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditJournalDialog(JournalEntry entry) {
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        LinearLayout layout = buildJournalForm(entry, padding);
        EditText etTitle = (EditText) layout.getChildAt(0);
        EditText etContent = (EditText) layout.getChildAt(1);
        EditText etImage = (EditText) layout.getChildAt(2);
        EditText etTour = (EditText) layout.getChildAt(3);
        RatingBar rb = (RatingBar) layout.getChildAt(4);

        new AlertDialog.Builder(requireContext())
                .setTitle("Chỉnh sửa nhật ký")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    Integer tourId = null;
                    String tourTitle = etTour.getText().toString().trim();
                    if (!tourTitle.isEmpty()) {
                        int resolved = db.getTourIdByTitle(tourTitle);
                        if (resolved > 0) tourId = resolved;
                    }
                    boolean ok = db.updateTravelDiaryEntry(entry.getId(), etTitle.getText().toString(), etContent.getText().toString(), etImage.getText().toString(), rb.getRating(), tourId);
                    if (!ok) {
                        Toast.makeText(getContext(), "Không thể cập nhật nhật ký", Toast.LENGTH_SHORT).show();
                    }
                    loadJournal();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showAppendJournalDialog(JournalEntry entry) {
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        EditText input = new EditText(requireContext());
        input.setMinLines(3);
        input.setHint("Viết thêm cảm xúc, trải nghiệm mới...");
        input.setPadding(padding, padding, padding, padding);

        new AlertDialog.Builder(requireContext())
                .setTitle("Cập nhật nhật ký")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    boolean ok = db.appendTravelDiaryEntry(entry.getId(), input.getText().toString());
                    if (!ok) {
                        Toast.makeText(getContext(), "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                    }
                    loadJournal();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmDeleteJournal(JournalEntry entry) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa nhật ký?")
                .setMessage("Bạn chắc chắn muốn xóa nhật ký này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean ok = db.deleteTravelDiaryEntry(entry.getId());
                    if (!ok) {
                        Toast.makeText(getContext(), "Không thể xóa nhật ký", Toast.LENGTH_SHORT).show();
                    }
                    loadJournal();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private LinearLayout buildJournalForm(@Nullable JournalEntry entry, int padding) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(padding, padding, padding, padding);

        EditText etTitle = new EditText(requireContext());
        etTitle.setHint("Tiêu đề nhật ký");
        if (entry != null) etTitle.setText(entry.getTitle());

        EditText etContent = new EditText(requireContext());
        etContent.setHint("Nội dung trải nghiệm...");
        etContent.setMinLines(4);
        if (entry != null) etContent.setText(entry.getContent());

        EditText etImage = new EditText(requireContext());
        etImage.setHint("Link ảnh (tuỳ chọn)");
        if (entry != null) etImage.setText(entry.getImageUrl());

        EditText etTour = new EditText(requireContext());
        etTour.setHint("Tên tour liên quan (tuỳ chọn)");

        RatingBar ratingBar = new RatingBar(requireContext(), null, android.R.attr.ratingBarStyleSmall);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.5f);
        if (entry != null) ratingBar.setRating(entry.getRating());
        else ratingBar.setRating(5f);

        layout.addView(etTitle);
        layout.addView(etContent);
        layout.addView(etImage);
        layout.addView(etTour);
        layout.addView(ratingBar);

        return layout;
    }
}

