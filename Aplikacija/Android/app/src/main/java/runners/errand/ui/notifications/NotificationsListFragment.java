package runners.errand.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.NotificationAdapter;
import runners.errand.model.Notification;
import runners.errand.ui.profile.ProfileFragment;

public class NotificationsListFragment extends Fragment {
	private static final String SIS_KEY_INDEX = "runners.errand.ui.notifications.index";

	private MainActivity activity;
	private NotificationAdapter adapter;
	private ListView list;
	private int index;
    private String title;

	private ArrayList<Notification> notifications = new ArrayList<>();

    public NotificationsListFragment() {
        // Required empty public constructor
    }

	NotificationsListFragment(int index) {
		this.index = index;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications_list, container, false);

        if (savedInstanceState != null)
        	index = savedInstanceState.getInt(SIS_KEY_INDEX);

        activity = ((MainActivity) getActivity());
        if (activity == null) return root;

        switch (index) {
			case 0:
				title = getString(R.string.notifications_tab_requested);
				break;
			case 1:
				title = getString(R.string.notifications_tab_running);
				break;
			case 2:
				title = getString(R.string.notifications_tab_other);
				break;
		}

        ((TextView) root.findViewById(R.id.page_title)).setText(title);

		list = root.findViewById(R.id.list_view);
		adapter = new NotificationAdapter(activity, notifications);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Notification n = ((Notification) adapter.getItem(position));
				if (n == null) return;

				Bundle args = new Bundle();

				switch (n.getCategory()) {
					case Notification.CATEGORY_RATING:
						args.putInt(ProfileFragment.ARG_KEY_TAB_SELECT, 1);
						activity.navigateTo(R.id.nav_page_profile, args);
						break;
					case Notification.CATEGORY_ACHIEVEMENT:
						args.putInt(ProfileFragment.ARG_KEY_TAB_SELECT, 2);
						activity.navigateTo(R.id.nav_page_profile, args);
						break;
				}
			}
		});

		loadData();

        return root;
    }

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putInt(SIS_KEY_INDEX, index);
		super.onSaveInstanceState(outState);
	}

	public void loadData() {
		Log.e("BR", index + "");
		notifications.clear();
		for (Notification n : activity.getUser().getNotifications()) {
			if (index == 0 && (n.getCategory() == 2 || n.getCategory() == 5))
				notifications.add(n);
			else if (index == 1 && (n.getCategory() == 0 || n.getCategory() == 3 || n.getCategory() == 4 || n.getCategory() == 6 || n.getCategory() == 7))
				notifications.add(n);
			else if (index == 2 && (n.getCategory() == 1 || n.getCategory() == 8 || n.getCategory() == 9 || n.getCategory() == 10))
				notifications.add(n);
		}
		adapter.notifyDataSetChanged();
		if (notifications.size() <= 0) list.setVisibility(View.GONE);
	}
}
