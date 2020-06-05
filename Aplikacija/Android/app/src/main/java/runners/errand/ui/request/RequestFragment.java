package runners.errand.ui.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.model.Request;
import runners.errand.utils.dialogs.SimpleDialog;

public class RequestFragment extends Fragment {
	public static String ARG_KEY_REQUEST_ID = "runners.errand.ui.request.id";

	private ArrayList<Fragment> fragments = new ArrayList<>();
	private Request request = null;
	private MainActivity activity;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_request, container, false);

		activity = (MainActivity) getActivity();
		if (activity == null) return root;

		int requestId = -1;
		Bundle args = getArguments();
		if (args != null) requestId = args.getInt(ARG_KEY_REQUEST_ID, -1);
		if (requestId == -1 || (request = activity.getUser().getRequest(requestId)) == null) {
			SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_request_load_failed), "MA-L", null);
			activity.navigateTo(R.id.nav_page_requests);
			return root;
		}

		activity.setTitle(request.getName());

		fragments.clear();
		fragments.add(new TasksFragment());
		fragments.add(new MapFragment());
		fragments.add(new MessagesFragment());

		ViewPager pager = root.findViewById(R.id.view_pager);
		CustomPagerAdapter adapter = new CustomPagerAdapter(
				getChildFragmentManager(),
				fragments
		);
		pager.setAdapter(adapter);

		activity.setupTabs(pager, new int[] {
				R.drawable.ic_tasks,
				R.drawable.ic_map_white,
				R.drawable.ic_messages
		});

		return root;
	}

	public MainActivity getMainActivity() {
		return activity;
	}

	Request getRequest() {
		return request;
	}
}
