package runners.errand.ui.request;

import android.os.Bundle;
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

		fragments.clear();
		fragments.add(new TasksFragment());
		fragments.add(new MapFragment());
		if (request.getCreatedBy().getId() == activity.getUser().getId()) {
			if (request.getStatus() == Request.STATUS_PENDING) {
				fragments.add(new ViewOffersFragment());
			} else {
				fragments.add(new StatusFragment());
			}
			loadOffers();
		} else {
			if (activity.getUser().getRunning().contains(request)) {
				fragments.add(new StatusFragment());
				loadOffers();
			} else {
				fragments.add(new SendOfferFragment());
			}
		}

		ViewPager pager = root.findViewById(R.id.view_pager);
		CustomPagerAdapter adapter = new CustomPagerAdapter(
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
							for (int i = 0; i < offers.length(); i++) {
								request.getOffers().add(new Offer(offers.optJSONObject(i)));
							}
							if (fragments.get(2) instanceof ViewOffersFragment) ((ViewOffersFragment) fragments.get(2)).dateSetChanged();
						}
					} else {
						JSONObject acceptedOffer = o.optJSONObject("accepted_offer");
						if (acceptedOffer != null) request.setAcceptedOffer(new Offer(acceptedOffer));
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
}
