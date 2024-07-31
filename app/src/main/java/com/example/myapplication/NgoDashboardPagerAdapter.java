package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class NgoDashboardPagerAdapter extends FragmentStateAdapter {

    public NgoDashboardPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AvailableDonationsFragment();
            case 1:
                return new CurrentRequestsFragment();
            default:
                // Handle unexpected tab position (optional)
                return new Fragment(); // Or a custom error fragment
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}