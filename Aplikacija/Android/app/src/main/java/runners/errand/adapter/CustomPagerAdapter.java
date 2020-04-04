package runners.errand.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class CustomPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public CustomPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm, androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
