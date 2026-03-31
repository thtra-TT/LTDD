package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.models.SellerStatItem;

import java.util.ArrayList;
import java.util.List;

public class SellerStatsAdapter extends RecyclerView.Adapter<SellerStatsAdapter.StatViewHolder> {

    private final List<SellerStatItem> items = new ArrayList<>();

    public SellerStatsAdapter(List<SellerStatItem> data) {
        if (data != null) items.addAll(data);
    }

    @NonNull
    @Override
    public StatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_stat, parent, false);
        return new StatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatViewHolder holder, int position) {
        SellerStatItem item = items.get(position);
        holder.tvStatTourTitle.setText(item.getTitle());
        holder.tvStatTotalOrders.setText(String.valueOf(item.getTotalOrders()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<SellerStatItem> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class StatViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatTourTitle, tvStatTotalOrders;

        public StatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatTourTitle = itemView.findViewById(R.id.tvStatTourTitle);
            tvStatTotalOrders = itemView.findViewById(R.id.tvStatTotalOrders);
        }
    }
}