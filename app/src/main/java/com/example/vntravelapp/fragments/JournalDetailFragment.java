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

import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;

public class JournalDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_RATING = "rating";
    private static final String ARG_DATE = "date";

    public static JournalDetailFragment newInstance(String title, String content, String imageUrl, float rating, String date) {
        JournalDetailFragment fragment = new JournalDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        args.putString(ARG_IMAGE, imageUrl);
        args.putFloat(ARG_RATING, rating);
        args.putString(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_detail, container, false);

        ImageView ivBack = view.findViewById(R.id.ivToolbarBack);
        TextView tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        if (tvToolbarTitle != null) tvToolbarTitle.setText(R.string.profile_title_journal);
        if (ivBack != null) ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        TextView tvTitle = view.findViewById(R.id.tvJournalDetailTitle);
        TextView tvContent = view.findViewById(R.id.tvJournalDetailContent);
        TextView tvRating = view.findViewById(R.id.tvJournalDetailRating);
        TextView tvDate = view.findViewById(R.id.tvJournalDetailDate);
        ImageView ivImage = view.findViewById(R.id.ivJournalDetailImage);

        Bundle args = getArguments();
        String title = args != null ? args.getString(ARG_TITLE, "") : "";
        String content = args != null ? args.getString(ARG_CONTENT, "") : "";
        String imageUrl = args != null ? args.getString(ARG_IMAGE, "") : "";
        float rating = args != null ? args.getFloat(ARG_RATING, 0f) : 0f;
        String date = args != null ? args.getString(ARG_DATE, "") : "";

        tvTitle.setText(title);
        tvContent.setText(content);
        tvRating.setText(rating + " ★");
        tvDate.setText(date);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(ivImage);

        return view;
    }
}

