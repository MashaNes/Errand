package runners.errand.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.ui.requests.RequestsListFragment;

public class SettingsFragment extends Fragment {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        MainActivity activity = ((MainActivity) getActivity());
        if (activity == null) return root;

        fragments.clear();
        for (int i = 0; i < 3; i++)
            fragments.add(new SettingsListFragment(i));

        ViewPager pager = root.findViewById(R.id.view_pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(
                getChildFragmentManager(),
                fragments
        );
        pager.setAdapter(adapter);

        activity.setupTabs(pager, new int[] {
                R.drawable.ic_notifications_white,
                R.drawable.ic_walk,
                R.drawable.ic_block
        });

        return root;
    }
}
