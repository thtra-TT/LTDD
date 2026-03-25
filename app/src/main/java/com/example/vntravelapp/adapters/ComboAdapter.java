package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.fragments.DetailFragment;
import com.example.vntravelapp.models.Combo;
import java.util.ArrayList;
import java.util.List;

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {

    private List<Combo> combos;

    public ComboAdapter(List<Combo> combos) {
        this.combos = combos;
    }

    @NonNull
    @Override
    public ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_combo, parent, false);
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboViewHolder holder, int position) {
        Combo combo = combos.get(position);
        holder.tvTitle.setText(combo.getTitle());
        holder.tvLocation.setText(combo.getLocation());
        holder.tvDescription.setText(combo.getDescription());
        holder.tvOriginalPrice.setText(combo.getOriginalPrice());
        holder.tvDiscountedPrice.setText(combo.getDiscountedPrice());
        holder.tvRating.setText("★ " + combo.getRating());
        holder.tvBadge.setText(combo.getBadgeText());

        if (combo.getImageUrl() != null && !combo.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(combo.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivCombo);
        } else {
            holder.ivCombo.setImageResource(combo.getImageRes());
        }
        
        holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            DetailFragment fragment = DetailFragment.newInstance(
                    combo.getTitle(),
                    combo.getLocation(),
                    combo.getDiscountedPrice(),
                    combo.getDescription(),
                    "", 
                    "", 
                    "", 
                    combo.getImageRes(),
                    combo.getImageUrl(),
                    new ArrayList<>(),
                    null,
                    combo.getRating(),
                    0,
                    null,
                    null
            );
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return combos.size();
    }

    public void updateCombos(List<Combo> items) {
        this.combos = items;
        notifyDataSetChanged();
    }

    static class ComboViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCombo;
        TextView tvTitle, tvLocation, tvDescription, tvOriginalPrice, tvDiscountedPrice, tvRating, tvBadge;

        public ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCombo = itemView.findViewById(R.id.ivComboImage);
            tvTitle = itemView.findViewById(R.id.tvComboTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvDiscountedPrice = itemView.findViewById(R.id.tvDiscountedPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvBadge = itemView.findViewById(R.id.tvBadge);
        }
    }
}
