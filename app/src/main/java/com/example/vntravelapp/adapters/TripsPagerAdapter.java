package com.example.vntravelapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vntravelapp.fragments.TripsFavoritesFragment;
import com.example.vntravelapp.fragments.TripsTabFragment;

public class TripsPagerAdapter extends FragmentStateAdapter {

    public TripsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 3) {
            return new TripsFavoritesFragment();
        }
        TripsTabFragment fragment = new TripsTabFragment();
        int type = TripsTabFragment.TYPE_UPCOMING;
        if (position == 1) type = TripsTabFragment.TYPE_COMPLETED;
        if (position == 2) type = TripsTabFragment.TYPE_CANCELLED;
        android.os.Bundle args = new android.os.Bundle();
        args.putInt(TripsTabFragment.ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
