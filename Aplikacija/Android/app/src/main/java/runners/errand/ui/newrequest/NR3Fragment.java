package runners.errand.ui.newrequest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.UserAdapter;
import runners.errand.model.Task;
import runners.errand.model.User;
import runners.errand.utils.dialogs.ProfileDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class NR3Fragment extends Fragment {
	private MainActivity activity;
	private NewRequestFragment parent;
	private UserAdapter adapter;
	private ArrayList<User> directUsers = new ArrayList<>();
	private ArrayList<User> directActive = new ArrayList<>();
	private ArrayList<User> directAll = new ArrayList<>();
	private ListView directList;
	private TextView directEmpty;
	private int directSelected = -1;
	private boolean activeOnly = false;
	private boolean sortByRating = true;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_nr3, container, false);

		parent = ((NewRequestFragment) getParentFragment());
		if (parent == null) return root;
		activity = parent.getMainActivity();

		final TextView broadcast = root.findViewById(R.id.newrequest_offers_broadcast);
		broadcast.setText(parent.getRequest().isBroadcast() ? getString(R.string.generic_enabled) : getString(R.string.generic_disabled));
		broadcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((View) v.getParent()).callOnClick();
			}
		});
		((View) broadcast.getParent()).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.getRequest().setBroadcast(!parent.getRequest().isBroadcast());
				broadcast.setText(parent.getRequest().isBroadcast() ? getString(R.string.generic_enabled) : getString(R.string.generic_disabled));
			}
		});

		root.findViewById(R.id.newrequest_offers_direct_active_only).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activeOnly = !activeOnly;
				((TextView) v).setText(activeOnly ? R.string.generic_enabled : R.string.generic_disabled);
				setupArrays();
			}
		});

		root.findViewById(R.id.newrequest_offers_direct_sort_by).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sortByRating = !sortByRating;
				((TextView) v).setText(sortByRating ? R.string.newrequest_offers_direct_sort_by_rating : R.string.newrequest_offers_direct_sort_by_disctance);
				setupArrays();
			}
		});

		directEmpty = root.findViewById(R.id.newrequest_offers_no_direct_users);
		directList = root.findViewById(R.id.newrequest_offers_direct_list);
		directList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> p, View view, final int position, long id) {
				ProfileDialog profileDialog = new ProfileDialog(activity, directUsers.get(position), position == directSelected ? getString(R.string.generic_deselect) : getString(R.string.generic_select), getString(R.string.generic_cancel)) {
					@Override
					public void buttonPressed(boolean positive) {
						super.buttonPressed(positive);
						if (positive) {
							Log.e("DIRECT", position + ", " + directSelected);
							if (directSelected == position) {
								directUsers.get(position).selected = false;
								directSelected = -1;
							} else {
								if (directSelected != -1)
									directUsers.get(directSelected).selected = false;
								directUsers.get(position).selected = true;
								directSelected = position;
							}
							parent.getRequest().setDirectId(directSelected == -1 ? -1 : directUsers.get(directSelected).getId());
							adapter.notifyDataSetChanged();
						}
					}
				};
				profileDialog.show();
			}
		});
		adapter = new UserAdapter(activity, directUsers);
		directList.setAdapter(adapter);
		if (directUsers.isEmpty()) loadDirect(activity);

		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (activity != null) loadDirect(activity);
	}

	private void setupArrays() {
		directUsers.clear();
		if (activeOnly) {
			directUsers.addAll(directActive);
		} else {
			directUsers.addAll(directAll);
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
			if (directUsers.isEmpty()) {
				directEmpty.setVisibility(View.VISIBLE);
			} else {
				directEmpty.setVisibility(View.GONE);
			}
		}
	}

	void loadDirect(MainActivity activity) {
		this.activity = activity;
		NetRequest request = new NetRequest(NetManager.getApiServer() + NetManager.API_FILTER_USERS, NetManager.POST) {
			@Override
			public void success() {
				directAll.clear();
				directActive.clear();
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					JSONArray results = o.optJSONArray("results");
					if (results != null && results.length() > 0) {
						for (int i = results.length() - 1; i >= 0; i--) {
							JSONObject tmp = results.optJSONObject(i);
							if (tmp != null) {
								User user = new User(tmp);
								directAll.add(user);
								if (user.getStatus() == User.STATUS_RUNNING) directActive.add(user);

							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				for (User user : directAll) {
//					if (user.getStatus() == User.STATUS_RUNNING) directActive.add(user);
//				}
				setupArrays();
				super.success();
			}

			@Override
			public void error() {
				super.error();
			}
		};
		request.putParam("created_by", activity.getUser().getId());
		request.putParam("sort_rating", true);
		request.putNull("locations");
		request.putParam("sort_rating_asc", false);
		request.putParam("rating_limit_up", 6);
		request.putParam("rating_limit_down", ((NewRequestFragment) activity.getFragment()).getRequest().getMinRating());
		JSONArray services = new JSONArray();
		for (Task task : ((NewRequestFragment) activity.getFragment()).getRequest().getTasks()) {
			if (task.getService() != null) {
				int id = task.getService().getId();
				JSONObject o = new JSONObject();
				try {
					o.put("id", id);
					if (parent != null) {
						o.put("min_rating", parent.getRequest().getMinRating());
						o.put("max_dist", parent.getRequest().getMaxDistance());
					} else {
						o.put("min_rating", 0);
						o.put("max_dist", 100000);
					}
					services.put(o);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		request.putParam("services", services);
		request.putParam("no_rating", true);
		request.putParam("name", "");
		request.putParam("not_in_benefit", false);
		request.putParam("active", false);
		JSONArray locations = new JSONArray();
		JSONObject location = new JSONObject();
		if (parent != null) {
				try {
					if (parent.getRequest().getDestination() != null) {
						location.putOpt("latitude", parent.getRequest().getDestination().getLat());
						location.putOpt("longitude", parent.getRequest().getDestination().getLng());
						locations.put(location);
					} else {
						for (Task task : parent.getRequest().getTasks()) {
							if (task.getAddress() != null) {
								location.putOpt("latitude", task.getAddress().getLat());
								location.putOpt("longitude", task.getAddress().getLng());
								locations.put(location);
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
		request.putParam("locations", locations);
		NetManager.add(request);
	}
}
