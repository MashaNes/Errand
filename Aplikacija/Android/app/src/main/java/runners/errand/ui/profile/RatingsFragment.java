package runners.errand.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.RatingAdapter;
import runners.errand.model.Rating;
import runners.errand.utils.dialogs.ProfileDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;


public class RatingsFragment extends Fragment {

	public RatingsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_ratings, container, false);

		final MainActivity activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		final ArrayList<Rating> ratings = activity.getUser().getRatings();
		RatingAdapter adapter = new RatingAdapter(
			activity,
			ratings
		);

		ListView list = root.findViewById(R.id.list_view);
		list.setAdapter(adapter);
		if (ratings.size() <= 0) list.setVisibility(View.GONE);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				new ProfileDialog(activity, ratings.get(position).getUser(), "", getString(R.string.generic_close)).show();
			}
		});

		return root;
	}
}
