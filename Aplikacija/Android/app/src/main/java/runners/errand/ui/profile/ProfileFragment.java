package runners.errand.ui.profile;

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
import runners.errand.model.Achievement;

public class ProfileFragment extends Fragment {
	public static String
			ARG_KEY_TAB_SELECT = "runners.errand.ui.profile.tab_select",
			ARG_KEY_ITEM_ID = "runners.errand.ui.profile.item_id";
	// TODO: ARG_KEY_ITEM_ID -> id of the rating/achievement referenced in the notification
	// Scroll to the item with that id with list.smoothScrollToPosition(int position); in the appropriate fragment

    private ArrayList<Fragment> fragments = new ArrayList<>();
	private ViewPager pager;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        int tabSelect = 0;
        Bundle args = getArguments();
        if (args != null) tabSelect = args.getInt(ARG_KEY_TAB_SELECT, 0);

		MainActivity activity = ((MainActivity) getActivity());
        if (activity == null) return root;
		activity.setFragment(this);

		fragments.clear();
        fragments.add(new InfoFragment());
        fragments.add(new RatingsFragment());
        fragments.add(new AchievementsFragment());

        pager = root.findViewById(R.id.view_pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(
                getChildFragmentManager(),
                fragments
        );
        pager.setAdapter(adapter);

        activity.setupTabs(pager, new int[] {
                R.drawable.ic_profile,
                R.drawable.ic_star,
                R.drawable.ic_achievements
        });
        pager.setCurrentItem(tabSelect, true);

        return root;
    }

    public void loadAchievements() {
		((AchievementsFragment) fragments.get(2)).load();
	}

    void navigateTo(int index) {
    	pager.setCurrentItem(index, true);
	}
}
