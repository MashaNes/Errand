package runners.errand.ui.requests;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.RequestAdapter;
import runners.errand.model.Request;

public class RequestsListFragment extends Fragment {
	private static final String
			SIS_KEY_INDEX = "runners.errand.ui.requests.index";

	private MainActivity activity;

	private int index;
    private String title;
    private View.OnClickListener listener;
    private boolean hidePlusButton = false;
	private int icon_id;

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
				title = getString(R.string.request_tab_requested);
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
				title = getString(R.string.request_tab_running);
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
				title = getString(R.string.request_tab_history);
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
			for (Request r : activity.getUser().getRequests()) {
				if (index == 0 && r.getStatus() <= Request.STATUS_ACTIVE && r.getCreatedBy().getId() == activity.getUser().getId())
					requests.add(r);
				if (index == 1 && r.getStatus() <= Request.STATUS_ACTIVE && r.getCreatedBy().getId() != activity.getUser().getId())
					requests.add(r);
				if (index == 2 && r.getStatus() > Request.STATUS_ACTIVE)
					requests.add(r);
			}
		}

		ListView list = root.findViewById(R.id.list_view);
		RequestAdapter adapter = new RequestAdapter(activity, requests);
		list.setAdapter(adapter);

        return root;
    }

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putInt(SIS_KEY_INDEX, index);
		super.onSaveInstanceState(outState);
	}
}
