package runners.errand.ui.newrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.model.Request;
import runners.errand.model.User;
import runners.errand.ui.profile.AchievementsFragment;
import runners.errand.ui.profile.InfoFragment;
import runners.errand.ui.profile.RatingsFragment;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class NewRequestFragment extends Fragment {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ViewPager pager;
    private Request request = new Request();
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newrequest, container, false);

        MainActivity activity = ((MainActivity) getActivity());
        if (activity == null) return root;

        fragments.clear();
        fragments.add(new NR1Fragment());
        fragments.add(new NR2Fragment());
        fragments.add(new NR3Fragment());

        pager = root.findViewById(R.id.view_pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(
                getChildFragmentManager(),
                fragments
        );
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pageChanged(position);
            }

            @Override
            public void onPageSelected(int position) {
                pageChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        activity.setupTabs(pager, new int[] {
                R.drawable.ic_settings_white,
                R.drawable.ic_tasks,
                R.drawable.ic_walk
        });

        fab = root.findViewById(R.id.newrequest_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == 2) {
                    // TODO: Check if info is filled in & create request
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            }
        });

        return root;
    }

    public Request getRequest() {
        return request;
    }

    private void pageChanged(int index) {
        if (index == 2) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_goto_white));
        }
    }
}
