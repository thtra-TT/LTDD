package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.NotificationsAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.NotificationItem;

import java.util.List;

public class ProfileNotificationsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvNotifications;
    private TextView tvEmpty;
    private NotificationsAdapter adapter;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);
        db = new DatabaseHelper(getContext());

        TextView tvTitle = view.findViewById(R.id.tvToolbarTitle);
        ImageView ivBack = view.findViewById(R.id.ivToolbarBack);
        if (tvTitle != null) tvTitle.setText(R.string.profile_title_notifications);
        if (ivBack != null) ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        swipeRefreshLayout = view.findViewById(R.id.swipeProfileList);
        rvNotifications = view.findViewById(R.id.rvProfileList);
        tvEmpty = view.findViewById(R.id.tvProfileEmpty);
        tvEmpty.setText(R.string.profile_empty_notifications);

        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationsAdapter(this::openNotificationDetail);
        rvNotifications.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadNotifications);
        loadNotifications();

        return view;
    }

    private void loadNotifications() {
        List<NotificationItem> list = db.getNotifications();
        adapter.submitList(list);
        tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void openNotificationDetail(NotificationItem item) {
        NotificationDetailFragment fragment = NotificationDetailFragment.newInstance(item.getTitle(), item.getContent(), item.getStatus(), item.getCreatedAt());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

