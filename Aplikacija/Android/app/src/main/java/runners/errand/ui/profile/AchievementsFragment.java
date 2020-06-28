package runners.errand.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.AchievementAdapter;
import runners.errand.model.Achievement;

public class AchievementsFragment extends Fragment {
	private ArrayList<Achievement> achievements = new ArrayList<>();
	private AchievementAdapter adapter;
	private MainActivity activity;

	public AchievementsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_achievements, container, false);

		activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		achievements = activity.getUser().getAchievements();
		adapter = new AchievementAdapter(
				activity,
				achievements
		);

		ListView list = root.findViewById(R.id.list_view);
		list.setAdapter(adapter);
		if (achievements.size() <= 0) list.setVisibility(View.GONE);

		return root;
	}

	void load() {
		if (activity == null || adapter == null) return;
		achievements = activity.getUser().getAchievements();
		adapter.notifyDataSetChanged();
	}
}
