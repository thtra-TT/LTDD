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

import com.example.vntravelapp.R;

public class NotificationDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_STATUS = "status";
    private static final String ARG_DATE = "date";

    public static NotificationDetailFragment newInstance(String title, String content, String status, String date) {
        NotificationDetailFragment fragment = new NotificationDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        args.putString(ARG_STATUS, status);
        args.putString(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_detail, container, false);

        ImageView ivBack = view.findViewById(R.id.ivToolbarBack);
        TextView tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        if (tvToolbarTitle != null) tvToolbarTitle.setText(R.string.profile_title_notifications);
        if (ivBack != null) ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        TextView tvStatus = view.findViewById(R.id.tvNotificationDetailStatus);
        TextView tvTitle = view.findViewById(R.id.tvNotificationDetailTitle);
        TextView tvContent = view.findViewById(R.id.tvNotificationDetailContent);
        TextView tvDate = view.findViewById(R.id.tvNotificationDetailDate);

        Bundle args = getArguments();
        String title = args != null ? args.getString(ARG_TITLE, "") : "";
        String content = args != null ? args.getString(ARG_CONTENT, "") : "";
        String status = args != null ? args.getString(ARG_STATUS, "") : "";
        String date = args != null ? args.getString(ARG_DATE, "") : "";

        tvTitle.setText(title);
        tvContent.setText(content);
        tvDate.setText(date);
        tvStatus.setText("new".equalsIgnoreCase(status) ? getString(R.string.label_new) : getString(R.string.label_info));

        return view;
    }
}

