package runners.errand.ui.request;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.model.Edit;
import runners.errand.model.Offer;
import runners.errand.model.Request;
import runners.errand.utils.Static;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class RequestFragment extends Fragment {
	public static String ARG_KEY_REQUEST_ID = "runners.errand.ui.request.id";

	private ArrayList<Fragment> fragments = new ArrayList<>();
	private Request request = null;
	private MainActivity activity;
	private CustomPagerAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_request, container, false);

		activity = (MainActivity) getActivity();
		if (activity == null) return root;
		activity.setFragment(this);

		int requestId = -1;
		Bundle args = getArguments();
		if (args != null) requestId = args.getInt(ARG_KEY_REQUEST_ID, -1);
		if (requestId == -1) {
			SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_request_load_failed), "MA-L", null);
			activity.navigateTo(R.id.nav_page_requests);
			return root;
		}

		request = Static.request;
		activity.setTitle(request.getName());
		activity.apiGetRequest(request.getId());

		fragments.clear();
		fragments.add(new TasksFragment());
		fragments.add(new MapFragment());
		if (activity.getUser().getId() == request.getCreatedBy().getId()) {
			if (request.getStatus() == Request.STATUS_PENDING) {
				fragments.add(new ViewOffersFragment());
			} else {
				fragments.add(new StatusFragment());
			}
			loadOffers();
		} else {
			if (request.getMyOffer() != null) {
				fragments.add(new StatusFragment());
				loadOffers();
			} else {
				fragments.add(new SendOfferFragment());
			}
		}
		loadEdits();

		ViewPager pager = root.findViewById(R.id.view_pager);
		adapter = new CustomPagerAdapter(
				getChildFragmentManager(),
				fragments
		);
		pager.setAdapter(adapter);

		activity.setupTabs(pager, new int[] {
				R.drawable.ic_tasks,
				R.drawable.ic_map_white,
				R.drawable.ic_other
		});

		return root;
	}

	MainActivity getMainActivity() {
		return activity;
	}

	Request getRequest() {
		return request;
	}

	private void loadOffers() {
		request.getOffers().clear();
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_INFO_FILTERED, NetManager.POST) {
			@Override
			public void success() {
				super.success();
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					if (request.getWorkingWith() == null) {
						JSONArray offers = o.optJSONArray("offers");
						if (offers != null) {
							request.getOffers().clear();
							for (int i = 0; i < offers.length(); i++) {
								request.getOffers().add(new Offer(offers.optJSONObject(i)));
							}
							if (fragments.get(2) instanceof ViewOffersFragment)	((ViewOffersFragment) fragments.get(2)).dataSetChanged();
							if (fragments.get(2) instanceof StatusFragment) ((StatusFragment) fragments.get(2)).setupUI();
						}
					} else {
						JSONObject acceptedOffer = o.optJSONObject("accepted_offer");
						if (acceptedOffer != null) request.setAcceptedOffer(new Offer(acceptedOffer));
//						if (fragments.get(2) instanceof ViewOffersFragment && request.getAcceptedOffer() != null) fragments.set(2, new StatusFragment());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void error() {
				super.error();
			}
		};
		netRequest.putParam("request", request.getId());
		netRequest.putParam("offers", request.getWorkingWith() == null);
		netRequest.putParam("edits", false);
		netRequest.putParam("accepted_offer", request.getWorkingWith() != null);
		netRequest.putParam("rating_created_by", false);
		netRequest.putParam("rating_working_with", false);
		netRequest.putParam("tasklist", false);
		netRequest.putParam("destination", false);
		netRequest.putParam("pictures", false);
		NetManager.add(netRequest);
	}

	private void loadEdits() {
		request.getEdits().clear();
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_INFO_FILTERED, NetManager.POST) {
			@Override
			public void success() {
				super.success();
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					JSONArray edits = o.optJSONArray("edits");
					if (edits != null) {
						for (int i = 0; i < edits.length(); i++) {
							request.getEdits().add(new Edit(edits.optJSONObject(i).optJSONObject("request_edit")));
							request.getEdits().get(request.getEdits().size() - 1).setId(edits.optJSONObject(i).optInt("id"));
						}
						if (fragments.get(2) instanceof StatusFragment) ((StatusFragment) fragments.get(2)).setupUI();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void error() {
				super.error();
			}
		};
		netRequest.putParam("request", request.getId());
		netRequest.putParam("offers", false);
		netRequest.putParam("edits", true);
		netRequest.putParam("accepted_offer", false);
		netRequest.putParam("rating_created_by", false);
		netRequest.putParam("rating_working_with", false);
		netRequest.putParam("tasklist", false);
		netRequest.putParam("destination", false);
		netRequest.putParam("pictures", false);
		NetManager.add(netRequest);
	}

	public void loadData(Request request) {
		this.request = request;
		loadData();
	}

	void loadData() {
		((TasksFragment) fragments.get(0)).loadData();
		((MapFragment) fragments.get(1)).loadData();
		if (fragments.get(2) instanceof StatusFragment) {
			loadOffers();
			loadEdits();
			((StatusFragment) fragments.get(2)).setRequest(request);
		}
		if (fragments.get(2) instanceof ViewOffersFragment) {
			if (request.getAcceptedOffer() != null && request.getWorkingWith() != null && request.getStatus() == Request.STATUS_ACTIVE) {
				fragments.set(2, new StatusFragment());
				adapter.notifyDataSetChanged();
			} else {
				loadOffers();
				((ViewOffersFragment) fragments.get(2)).dataSetChanged(request);
			}
		}
	}
}
