package com.example.vntravelapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vntravelapp.R;
import com.example.vntravelapp.models.BusTrip;
import java.util.List;

public class BusTripAdapter extends RecyclerView.Adapter<BusTripAdapter.BusViewHolder> {

    private List<BusTrip> trips;
    private OnBusSelectedListener listener;

    public interface OnBusSelectedListener {
        void onBusSelected(BusTrip trip);
    }

    public BusTripAdapter(List<BusTrip> trips, OnBusSelectedListener listener) {
        this.trips = trips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_trip, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        BusTrip trip = trips.get(position);
        holder.tvTime.setText(trip.getDepartureTime());
        holder.tvCompany.setText(trip.getBusCompany());
        holder.tvPrice.setText(trip.getPrice());
        holder.tvSeats.setText("Còn " + trip.getAvailableSeats() + " ghế");
        holder.tvRoute.setText(trip.getDeparture() + " → " + trip.getDestination());

        holder.btnSelect.setOnClickListener(v -> {
            if (listener != null) listener.onBusSelected(trip);
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void updateData(List<BusTrip> newTrips) {
        this.trips = newTrips;
        notifyDataSetChanged();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvCompany, tvPrice, tvSeats, tvRoute;
        Button btnSelect;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvBusTime);
            tvCompany = itemView.findViewById(R.id.tvBusCompany);
            tvPrice = itemView.findViewById(R.id.tvBusPrice);
            tvSeats = itemView.findViewById(R.id.tvBusSeats);
            tvRoute = itemView.findViewById(R.id.tvBusRoute);
            btnSelect = itemView.findViewById(R.id.btnSelectBus);
        }
    }
}
