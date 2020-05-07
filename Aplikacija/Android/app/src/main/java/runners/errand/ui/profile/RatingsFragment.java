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
import runners.errand.adapter.RatingAdapter;
import runners.errand.model.Rating;


public class RatingsFragment extends Fragment {

	public RatingsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_ratings, container, false);

		MainActivity activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		ArrayList<Rating> ratings = activity.getUser().getRatings();
		RatingAdapter adapter = new RatingAdapter(
			activity,
			ratings
		);

		ListView list = root.findViewById(R.id.list_view);
		list.setAdapter(adapter);
		if (ratings.size() <= 0) list.setVisibility(View.GONE);

		return root;
	}
}
