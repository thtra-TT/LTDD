package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.models.ReviewEntry;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    public interface ReviewActionListener {
        void onOpen(ReviewEntry item);
    }

    private final List<ReviewEntry> items = new ArrayList<>();
    private final ReviewActionListener listener;

    public ReviewsAdapter(ReviewActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_card, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewEntry item = items.get(position);
        holder.tvTitle.setText(item.getItemTitle());
        holder.tvType.setText(item.getItemType());
        holder.tvRating.setText(String.valueOf(item.getRating()));
        holder.tvContent.setText(item.getContent());
        holder.tvDate.setText(item.getCreatedAt());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOpen(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<ReviewEntry> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvType;
        TextView tvRating;
        TextView tvContent;
        TextView tvDate;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvReviewTitle);
            tvType = itemView.findViewById(R.id.tvReviewType);
            tvRating = itemView.findViewById(R.id.tvReviewRating);
            tvContent = itemView.findViewById(R.id.tvReviewContent);
            tvDate = itemView.findViewById(R.id.tvReviewDate);
        }
    }
}

