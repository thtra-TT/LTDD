package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.models.SettingItem;

import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingViewHolder> {

    public interface SettingActionListener {
        void onOpen(SettingItem item);
    }

    private final List<SettingItem> items = new ArrayList<>();
    private final SettingActionListener listener;

    public SettingsAdapter(SettingActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_option, parent, false);
        return new SettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        SettingItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvSub.setText(item.getSubtitle());
        holder.ivIcon.setImageResource(item.getIconRes());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOpen(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<SettingItem> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class SettingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvSub;

        SettingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivSettingIcon);
            tvTitle = itemView.findViewById(R.id.tvSettingTitle);
            tvSub = itemView.findViewById(R.id.tvSettingSub);
        }
    }
}

