package runners.errand.ui.requests;

import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.RequestAdapter;
import runners.errand.model.Request;
import runners.errand.ui.request.RequestFragment;
import runners.errand.utils.Static;

public class RequestsListFragment extends Fragment {
	private static final String SIS_KEY_INDEX = "runners.errand.ui.requests.index";

	private MainActivity activity;

	private int index;
    private String title;
    private View.OnClickListener listener;
    private boolean hidePlusButton = false;
	private int icon_id;
	private ListView list;
	private RequestAdapter adapter;
	private TextView noItems;

	private ArrayList<Request> requests;

	public RequestsListFragment() {
        // Required empty public constructor
    }

	RequestsListFragment(int index) {
		this.index = index;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests_list, container, false);

        if (savedInstanceState != null)
			index = savedInstanceState.getInt(SIS_KEY_INDEX);

		activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		switch (index) {
			case 0:
				title = getString(R.string.requests_tab_requested);
				hidePlusButton = false;
				listener = new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.navigateTo(R.id.nav_page_newrequest);
					}
				};
				icon_id = R.drawable.ic_add;
				break;
			case 1:
				title = getString(R.string.requests_tab_running);
				hidePlusButton = false;
				listener = new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.navigateTo(R.id.nav_page_findrequests);
					}
				};
				icon_id = R.drawable.ic_find;
				break;
			case 2:
				title = getString(R.string.requests_tab_history);
				hidePlusButton = true;
				break;
		}

        ((TextView) root.findViewById(R.id.page_title)).setText(title);
        if (hidePlusButton)
            root.findViewById(R.id.header_button).setVisibility(View.GONE);
        else {
			root.findViewById(R.id.header_button).setVisibility(View.VISIBLE);
            ((ImageView) root.findViewById(R.id.header_button)).setImageResource(icon_id);
            root.findViewById(R.id.header_button).setOnClickListener(listener);
        }

        if (requests == null) {
        	requests = new ArrayList<>();
			loadRequests();
		}

		noItems = root.findViewById(R.id.requests_list_no_items);
		list = root.findViewById(R.id.list_view);
		adapter = new RequestAdapter(activity, requests);
		list.setAdapter(adapter);
		if (requests.size() <= 0) noItems.setVisibility(View.VISIBLE);
		else noItems.setVisibility(View.GONE);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt(RequestFragment.ARG_KEY_REQUEST_ID, requests.get(position).getId());
				Static.request = requests.get(position);
				activity.navigateTo(R.id.nav_page_request, bundle);
				activity.setTitle(R.string.menu_requests);
			}
		});

        return root;
    }

    private void loadRequests() {
		requests.clear();
		for (Request r : activity.getUser().getRequests()) {
			if (index == 0 && r.getStatus() <= Request.STATUS_ACTIVE)
				add(r);
			if (index == 2 && r.getStatus() > Request.STATUS_ACTIVE)
				add(r);
		}
		for (Request r : activity.getUser().getRunning()) {
			if (index == 1 && r.getStatus() <= Request.STATUS_ACTIVE)
				add(r);
			if (index == 2 && r.getStatus() > Request.STATUS_ACTIVE)
				add(r);
		}
	}

	private void add(Request r) {
		for (int i = 0; i < requests.size(); i++) {
			if (requests.get(i).getTime().getTime() < r.getTime().getTime()) {
				requests.add(i, r);
				return;
			}
		}
		requests.add(r);
	}

    void dataSetChanged() {
		loadRequests();
		adapter.notifyDataSetChanged();
		if (requests.size() <= 0) noItems.setVisibility(View.VISIBLE);
		else noItems.setVisibility(View.GONE);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putInt(SIS_KEY_INDEX, index);
		super.onSaveInstanceState(outState);
	}
}
