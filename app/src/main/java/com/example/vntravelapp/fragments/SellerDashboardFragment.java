package com.example.vntravelapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vntravelapp.R;
import com.example.vntravelapp.adapters.SellerOrdersAdapter;
import com.example.vntravelapp.adapters.SellerStatsAdapter;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.SellerOrderItem;
import com.example.vntravelapp.models.SellerStatItem;

import java.util.ArrayList;
import java.util.List;

public class SellerDashboardFragment extends Fragment {

    private RecyclerView rvSellerStats, rvSellerOrders;
    private DatabaseHelper dbHelper;
    private SellerOrdersAdapter ordersAdapter;
    private SellerStatsAdapter statsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_dashboard, container, false);

        rvSellerStats = view.findViewById(R.id.rvSellerStats);
        rvSellerOrders = view.findViewById(R.id.rvSellerOrders);

        dbHelper = new DatabaseHelper(requireContext());

        rvSellerStats.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSellerOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        statsAdapter = new SellerStatsAdapter(new ArrayList<>());
        ordersAdapter = new SellerOrdersAdapter(new ArrayList<>(), new SellerOrdersAdapter.OrderActionListener() {
            @Override
            public void onConfirm(int orderId) {
                dbHelper.confirmOrder(orderId);
                loadOrders();
            }

            @Override
            public void onCancel(int orderId) {
                dbHelper.cancelOrder(orderId);
                loadOrders();
            }
        });

        rvSellerStats.setAdapter(statsAdapter);
        rvSellerOrders.setAdapter(ordersAdapter);

        loadStats();
        loadOrders();

        return view;
    }

    private void loadOrders() {
        List<SellerOrderItem> items = new ArrayList<>();
        Cursor c = dbHelper.getAllOrdersForSeller();

        if (c != null && c.moveToFirst()) {
            do {
                items.add(new SellerOrderItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getInt(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6),
                        c.getString(7)
                ));
            } while (c.moveToNext());
            c.close();
        }

        ordersAdapter.updateData(items);
    }

    private void loadStats() {
        List<SellerStatItem> items = new ArrayList<>();
        Cursor c = dbHelper.getSellerTourStats();

        if (c != null && c.moveToFirst()) {
            do {
                items.add(new SellerStatItem(
                        c.getString(0),
                        c.getInt(1)
                ));
            } while (c.moveToNext());
            c.close();
        }

        statsAdapter.updateData(items);
    }
}