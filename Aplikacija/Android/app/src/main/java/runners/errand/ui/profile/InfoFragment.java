package runners.errand.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Achievement;
import runners.errand.model.User;

public class InfoFragment extends Fragment {

	public InfoFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_info, container, false);

		MainActivity activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		User user = activity.getUser();

		String rating = String.format(Locale.getDefault(), "%.1f", user.getRating()) + " (" + String.format(Locale.getDefault(), "%d", user.getRatings().size()) + ")";

		float averageLevel = 0;
		int achievementCount = 0;
		for (Achievement a : user.getAchievements()) {
			if (a.getLevel() > 0)
				achievementCount++;
			averageLevel += a.getLevel();
		}
		averageLevel /= user.getAchievements().size();
		String achievements = String.format(Locale.getDefault(), "%.1f", averageLevel) + " (" + String.format(Locale.getDefault(), "%d", achievementCount) + "/" + String.format(Locale.getDefault(), "%d", user.getAchievements().size()) + ")";

		((TextView) root.findViewById(R.id.profile_name_value)).setText(user.getName());
		((TextView) root.findViewById(R.id.profile_email_value)).setText(user.getEmail());
		((TextView) root.findViewById(R.id.profile_phone_value)).setText(user.getPhone());
		((TextView) root.findViewById(R.id.profile_rating_value)).setText(rating);
		((TextView) root.findViewById(R.id.profile_achievement_value)).setText(achievements);

		// TODO: Setup edit stuff
		root.findViewById(R.id.profile_name_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		root.findViewById(R.id.profile_email_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		root.findViewById(R.id.profile_phone_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		root.findViewById(R.id.profile_rating_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				assert getParentFragment() != null;
				((ProfileFragment) getParentFragment()).navigateTo(1);
			}
		});
		root.findViewById(R.id.profile_achievement_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				assert getParentFragment() != null;
				((ProfileFragment) getParentFragment()).navigateTo(2);
			}
		});

		return root;
	}
}
