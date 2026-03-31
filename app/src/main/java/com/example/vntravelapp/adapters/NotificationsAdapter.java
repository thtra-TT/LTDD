package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.models.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    public interface NotificationActionListener {
        void onOpen(NotificationItem item);
    }

    private final List<NotificationItem> items = new ArrayList<>();
    private final NotificationActionListener listener;

    public NotificationsAdapter(NotificationActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_card, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvContent.setText(item.getContent());
        holder.tvDate.setText(item.getCreatedAt());
        holder.tvStatus.setText("new".equalsIgnoreCase(item.getStatus()) ? holder.itemView.getContext().getString(R.string.label_new) : holder.itemView.getContext().getString(R.string.label_info));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOpen(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<NotificationItem> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDate;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvNotificationStatus);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvContent = itemView.findViewById(R.id.tvNotificationContent);
            tvDate = itemView.findViewById(R.id.tvNotificationDate);
        }
    }
}

