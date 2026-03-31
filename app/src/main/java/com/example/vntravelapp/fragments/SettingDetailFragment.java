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

public class SettingDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";

    public static SettingDetailFragment newInstance(String title, String content) {
        SettingDetailFragment fragment = new SettingDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_detail, container, false);

        ImageView ivBack = view.findViewById(R.id.ivToolbarBack);
        TextView tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        if (tvToolbarTitle != null) tvToolbarTitle.setText(R.string.profile_title_setting_detail);
        if (ivBack != null) ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        TextView tvTitle = view.findViewById(R.id.tvSettingDetailTitle);
        TextView tvContent = view.findViewById(R.id.tvSettingDetailContent);

        Bundle args = getArguments();
        String title = args != null ? args.getString(ARG_TITLE, "") : "";
        String content = args != null ? args.getString(ARG_CONTENT, "") : "";

        tvTitle.setText(title);
        tvContent.setText(content);

        return view;
    }
}

