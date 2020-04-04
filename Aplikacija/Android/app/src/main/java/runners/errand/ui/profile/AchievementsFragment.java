package runners.errand.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.AchievementAdapter;
import runners.errand.model.Achievement;

public class AchievementsFragment extends Fragment {

	public AchievementsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_achievements, container, false);

		MainActivity activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		ArrayList<Achievement> achievements = activity.getUser().getAchievements();
		AchievementAdapter adapter = new AchievementAdapter(
				activity,
				achievements
		);

		((ListView) root.findViewById(R.id.list_view)).setAdapter(adapter);

		return root;
	}
}
