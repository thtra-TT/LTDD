package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.models.JournalEntry;

import java.util.ArrayList;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    public interface JournalActionListener {
        void onOpen(JournalEntry item);
    }

    public interface JournalLongClickListener {
        void onLongPress(JournalEntry item);
    }

    private final List<JournalEntry> items = new ArrayList<>();
    private final JournalActionListener listener;
    private final JournalLongClickListener longClickListener;

    public JournalAdapter(JournalActionListener listener) {
        this(listener, null);
    }

    public JournalAdapter(JournalActionListener listener, JournalLongClickListener longClickListener) {
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal_card, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalEntry item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvContent.setText(item.getContent());
        holder.tvRating.setText(String.valueOf(item.getRating()));
        holder.tvDate.setText(item.getCreatedAt());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOpen(item);
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onLongPress(item);
                return true;
            }
            return false;
        });

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<JournalEntry> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class JournalViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvContent;
        TextView tvRating;
        TextView tvDate;

        JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivJournalImage);
            tvTitle = itemView.findViewById(R.id.tvJournalTitle);
            tvContent = itemView.findViewById(R.id.tvJournalContent);
            tvRating = itemView.findViewById(R.id.tvJournalRating);
            tvDate = itemView.findViewById(R.id.tvJournalDate);
        }
    }
}
