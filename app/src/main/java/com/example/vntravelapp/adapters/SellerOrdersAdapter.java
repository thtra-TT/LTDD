package com.example.vntravelapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.models.SellerOrderItem;

import java.util.ArrayList;
import java.util.List;

public class SellerOrdersAdapter extends RecyclerView.Adapter<SellerOrdersAdapter.OrderViewHolder> {

    public interface OrderActionListener {
        void onConfirm(int orderId);
        void onCancel(int orderId);
    }

    private final List<SellerOrderItem> items = new ArrayList<>();
    private final OrderActionListener listener;

    public SellerOrdersAdapter(List<SellerOrderItem> data, OrderActionListener listener) {
        if (data != null) items.addAll(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        SellerOrderItem item = items.get(position);

        holder.tvOrderTitle.setText(item.getTitle());
        holder.tvOrderCustomer.setText("Người đặt: " + item.getName());
        holder.tvOrderPhone.setText("SĐT: " + item.getPhone());
        holder.tvOrderEmail.setText("Email: " + item.getUserEmail());
        holder.tvOrderDate.setText("Ngày đi: " + item.getDate());
        holder.tvOrderStatus.setText(item.getOrderStatus());

        if ("CONFIRMED".equals(item.getOrderStatus())) {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else if ("CANCELLED".equals(item.getOrderStatus())) {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#D32F2F"));
        } else {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#F57C00"));
        }

        holder.btnConfirmOrder.setOnClickListener(v -> {
            if (listener != null) listener.onConfirm(item.getId());
        });

        holder.btnCancelOrder.setOnClickListener(v -> {
            if (listener != null) listener.onCancel(item.getId());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<SellerOrderItem> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderTitle, tvOrderCustomer, tvOrderPhone, tvOrderEmail, tvOrderDate, tvOrderStatus;
        Button btnConfirmOrder, btnCancelOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderTitle = itemView.findViewById(R.id.tvOrderTitle);
            tvOrderCustomer = itemView.findViewById(R.id.tvOrderCustomer);
            tvOrderPhone = itemView.findViewById(R.id.tvOrderPhone);
            tvOrderEmail = itemView.findViewById(R.id.tvOrderEmail);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            btnConfirmOrder = itemView.findViewById(R.id.btnConfirmOrder);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }
    }
}