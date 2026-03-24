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
import com.example.vntravelapp.models.TicketOffer;
import java.util.List;

public class TicketOfferAdapter extends RecyclerView.Adapter<TicketOfferAdapter.OfferViewHolder> {

    private List<TicketOffer> offers;

    public TicketOfferAdapter(List<TicketOffer> offers) {
        this.offers = offers;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        TicketOffer offer = offers.get(position);
        holder.tvRoute.setText(offer.getRoute());
        holder.tvDateRange.setText(offer.getDateRange());
        holder.tvPrice.setText(offer.getPrice());
        holder.tvDiscount.setText(offer.getDiscount());
        holder.tvType.setText(offer.getType());

        if (offer.getImageUrl() != null && !offer.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(offer.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivOffer);
        } else {
            holder.ivOffer.setImageResource(offer.getImageRes());
        }
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public void updateOffers(List<TicketOffer> items) {
        this.offers = items;
        notifyDataSetChanged();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOffer;
        TextView tvRoute, tvDateRange, tvPrice, tvDiscount, tvType;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOffer = itemView.findViewById(R.id.ivOfferImage);
            tvRoute = itemView.findViewById(R.id.tvRoute);
            tvDateRange = itemView.findViewById(R.id.tvDateRange);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvType = itemView.findViewById(R.id.tvType);
        }
    }
}
