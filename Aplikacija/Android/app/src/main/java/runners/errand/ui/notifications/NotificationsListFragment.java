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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.NotificationAdapter;
import runners.errand.model.Notification;
import runners.errand.model.Request;
import runners.errand.model.Task;
import runners.errand.ui.profile.ProfileFragment;
import runners.errand.ui.request.RequestFragment;
import runners.errand.utils.Static;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

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

        root.findViewById(R.id.header_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateOpened(-1);
			}
		});

		list = root.findViewById(R.id.list_view);
		adapter = new NotificationAdapter(activity, notifications);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Notification n = ((Notification) adapter.getItem(position));
				if (n == null) return;

				updateOpened(n.getId());

				Bundle args = new Bundle();

				switch (n.getCategory()) {
					case Notification.CATEGORY_REQUEST_DIRECT:
					case Notification.CATEGORY_REQUEST_FAILED:
					case Notification.CATEGORY_OFFER_CREATED:
					case Notification.CATEGORY_OFFER_ACCEPTED:
					case Notification.CATEGORY_OFFER_CANCELED:
					case Notification.CATEGORY_EDIT_CREATED:
					case Notification.CATEGORY_EDIT_ACCEPTED:
					case Notification.CATEGORY_EDIT_CANCELED:
					case Notification.CATEGORY_REQUEST_SUCCESS:
						Request request = null;
						for (Request r : activity.getUser().getRequests()) {
							if (r.getId() == notifications.get(position).getType_id())
								request = r;
						}
//						if (request == null) {
//							for (Request r : activity.getUser().getRunning()) {
//								if (r.getId() == notifications.get(position).getType_id())
//									request = r;
//							}
//						}
						if (request == null) {
							NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUESTS + notifications.get(position).getType_id() + "/", NetManager.GET) {
								@Override
								public void success() {
									super.success();
									Request req;
									try {
										JSONObject o = new JSONObject(getResult().getMsg());
										o = o.optJSONObject("request");
										if (o != null)
											req = new Request(o);
										else return;
										Bundle bundle = new Bundle();
										bundle.putInt(RequestFragment.ARG_KEY_REQUEST_ID, req.getId());
										Static.request = req;
										activity.navigateTo(R.id.nav_page_request, bundle);
										activity.setTitle(R.string.menu_requests);
									} catch (JSONException e) {
										e.printStackTrace();
										SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_request_not_available), "", null);
									}
								}

								@Override
								public void error() {
									super.error();
									SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_request_not_available), "", null);
								}
							};
							NetManager.add(netRequest);
							break;
						} else {
							Bundle bundle = new Bundle();
							bundle.putInt(RequestFragment.ARG_KEY_REQUEST_ID, request.getId());
							Static.request = request;
							activity.navigateTo(R.id.nav_page_request, bundle);
							activity.setTitle(R.string.menu_requests);
						}
						break;
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

	void loadData() {
    	if (activity == null) return;
		notifications.clear();
		for (Notification n : activity.getUser().getNotifications()) {
			if (index == 0 && (
					n.getCategory() == Notification.CATEGORY_OFFER_CREATED ||
					n.getCategory() == Notification.CATEGORY_EDIT_CREATED
			))
				notifications.add(n);
			else if (index == 1 && (
					n.getCategory() == Notification.CATEGORY_REQUEST_DIRECT ||
					n.getCategory() == Notification.CATEGORY_OFFER_ACCEPTED ||
					n.getCategory() == Notification.CATEGORY_OFFER_CANCELED ||
					n.getCategory() == Notification.CATEGORY_EDIT_ACCEPTED ||
					n.getCategory() == Notification.CATEGORY_EDIT_CANCELED
			))
				notifications.add(n);
			else if (index == 2 && (
					n.getCategory() == Notification.CATEGORY_REQUEST_FAILED ||
					n.getCategory() == Notification.CATEGORY_RATING ||
					n.getCategory() == Notification.CATEGORY_ACHIEVEMENT ||
					n.getCategory() == Notification.CATEGORY_REQUEST_SUCCESS
			))
				notifications.add(n);
		}
		adapter.notifyDataSetChanged();
		if (notifications.size() <= 0) list.setVisibility(View.GONE);
	}

	private void updateOpened(int id) {
		final JSONArray ids = new JSONArray();
		if (id == -1) {
			for (Notification notification : notifications) {
				if (!notification.isOpened()) ids.put(notification.getId());
			}
		} else {
			ids.put(id);
		}
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_NOTIFICATIONS_FLAG_UPDATE, NetManager.PUT) {
			@Override
			public void success() {
				for (Notification notification : notifications) {
					for (int i = 0; i < ids.length(); i++) {
						if (notification.getId() == ids.optInt(i)) notification.setOpened(true);
					}
				}
				loadData();
				super.success();
			}

			@Override
			public void error() {
				super.error();
			}
		};
		netRequest.putParam("ids", ids);
		netRequest.putParam("seen", true);
		netRequest.putParam("opened", true);
		NetManager.add(netRequest);
	}
}
