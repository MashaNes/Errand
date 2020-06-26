package runners.errand.ui.findrequests;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.RequestAdapter;
import runners.errand.model.Request;
import runners.errand.ui.request.RequestFragment;
import runners.errand.utils.Static;

public class FRListFragment extends Fragment {
	private static final String SIS_KEY_INDEX = "runners.errand.ui.requests.index";

	private MainActivity activity;
	private FindRequestsFragment parent;

	private int index;
	private RequestAdapter adapter;
	private TextView noItems;

	private ArrayList<Request> requests = new ArrayList<>();

	public FRListFragment() {
        // Required empty public constructor
    }

	FRListFragment(int index) {
		this.index = index;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests_list, container, false);

        if (savedInstanceState != null)
			index = savedInstanceState.getInt(SIS_KEY_INDEX);

		activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		parent = ((FindRequestsFragment) getParentFragment());
		if (parent == null) return root;

		String title;
		if (index == 1) {
			title = getString(R.string.find_requests_tab_broadcast);
		} else {
			title = getString(R.string.find_requests_tab_direct);
		}

		root.findViewById(R.id.header_button).setVisibility(View.GONE);

		((TextView) root.findViewById(R.id.page_title)).setText(title);

		noItems = root.findViewById(R.id.requests_list_no_items);
		ListView list = root.findViewById(R.id.list_view);
		adapter = new RequestAdapter(activity, requests);
		list.setAdapter(adapter);
		if (requests.size() <= 0) noItems.setVisibility(View.VISIBLE);
		else noItems.setVisibility(View.GONE);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> p, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt(RequestFragment.ARG_KEY_REQUEST_ID, requests.get(position).getId());
				Static.request = requests.get(position);
				activity.navigateTo(R.id.nav_page_request, bundle);
				activity.setTitle(R.string.menu_requests);
			}
		});

		dataSetChanged();

        return root;
    }

    private void loadRequests() {
		if (parent == null) return;
		requests.clear();
		if (index == 1) {
			add(parent.getBroadcastRequests());
		} else {
			add(parent.getDirectRequests());
		}
	}

	private void add(ArrayList<Request> requests) {
		for (Request request : requests) {
			if (request.getCreatedBy().getId() != activity.getUser().getId()) this.requests.add(request);
		}
	}

    void dataSetChanged() {
		if (adapter != null) {
			loadRequests();
			adapter.notifyDataSetChanged();
			if (requests.size() <= 0) noItems.setVisibility(View.VISIBLE);
			else noItems.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putInt(SIS_KEY_INDEX, index);
		super.onSaveInstanceState(outState);
	}
}
