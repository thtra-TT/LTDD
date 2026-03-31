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

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.SettingsAdapter;
import com.example.vntravelapp.models.SettingItem;

import java.util.ArrayList;
import java.util.List;

public class ProfileSettingsFragment extends Fragment {

    private RecyclerView rvSettings;
    private SettingsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);

        TextView tvTitle = view.findViewById(R.id.tvToolbarTitle);
        ImageView ivBack = view.findViewById(R.id.ivToolbarBack);
        if (tvTitle != null) tvTitle.setText(R.string.profile_title_settings);
        if (ivBack != null) ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        view.findViewById(R.id.swipeProfileList).setEnabled(false);

        rvSettings = view.findViewById(R.id.rvProfileList);
        rvSettings.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SettingsAdapter(this::openSettingDetail);
        rvSettings.setAdapter(adapter);

        adapter.submitList(buildSettings());

        return view;
    }

    private List<SettingItem> buildSettings() {
        List<SettingItem> items = new ArrayList<>();
        items.add(new SettingItem(getString(R.string.settings_dark_mode), getString(R.string.settings_dark_mode_sub), R.drawable.ic_profile_settings));
        items.add(new SettingItem(getString(R.string.settings_language), getString(R.string.settings_language_sub), R.drawable.ic_profile_settings));
        items.add(new SettingItem(getString(R.string.settings_privacy), getString(R.string.settings_privacy_sub), R.drawable.ic_profile_settings));
        items.add(new SettingItem(getString(R.string.settings_support), getString(R.string.settings_support_sub), R.drawable.ic_profile_settings));
        return items;
    }

    private void openSettingDetail(SettingItem item) {
        SettingDetailFragment fragment = SettingDetailFragment.newInstance(item.getTitle(), item.getSubtitle());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

