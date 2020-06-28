package runners.errand.ui.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.OfferAdapter;
import runners.errand.model.Request;
import runners.errand.model.User;
import runners.errand.ui.requests.RequestsFragment;
import runners.errand.utils.dialogs.ProfileDialog;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class ViewOffersFragment extends Fragment {
	private MainActivity activity;
	private RequestFragment parent;
	private Request request;
	private OfferAdapter adapter;
	private TextView noOffers;
	private ListView list;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_view_offers, container, false);

		parent = (RequestFragment) getParentFragment();
		if (parent == null) return root;
		activity = parent.getMainActivity();
		request = parent.getRequest();

		noOffers = root.findViewById(R.id.no_offers);

		list = root.findViewById(R.id.list_offers);
		adapter = new OfferAdapter(activity, request.getOffers(), request);
		list.setAdapter(adapter);
		dataSetChanged();

		if (request.getDirect() != null) {
			root.findViewById(R.id.request_view_offers_direct).setVisibility(View.VISIBLE);
			root.findViewById(R.id.item_user_layout).setVisibility(View.VISIBLE);
			final User user = request.getDirect();
			if (user.getPicture_bmp() != null) {
				((ImageView) root.findViewById(R.id.item_user_image)).setImageBitmap(user.getPicture_bmp());
			} else {
				((ImageView) root.findViewById(R.id.item_user_image)).setImageDrawable(getResources().getDrawable(R.drawable.ic_face));
			}
			String name = user.getFirstName() + " " + user.getLastName();
			((TextView) root.findViewById(R.id.item_user_name)).setText(name);
			String rating = Float.isNaN(user.getRating()) ? getString(R.string.generic_unrated) : String.format(Locale.getDefault(), "%.1f", user.getRating());
			((TextView) root.findViewById(R.id.item_user_rating)).setText(rating);
			root.findViewById(R.id.item_user_layout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new ProfileDialog(activity, user, "", getString(R.string.generic_close)).show();
				}
			});
		}

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				new ProfileDialog(activity, request.getOffers().get(position).getCreatedBy(), getString(R.string.request_offer_accept), getString(R.string.generic_close)) {
					@Override
					public void buttonPressed(boolean positive) {
						if (positive) {
							NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_OFFER_ACCEPT, NetManager.PUT) {
								@Override
								public void success() {
									super.success();
									request.setAcceptedOffer(request.getOffers().get(position));
									dataSetChanged();
									Bundle bundle = new Bundle();
									bundle.putInt(RequestsFragment.EXTRA_REFRESH_BY_ID, request.getId());
									activity.navigateTo(R.id.nav_page_requests, bundle);
								}

								@Override
								public void error() {
									super.error();
								}
							};
							netRequest.putParam("created_by", activity.getUser().getId());
							netRequest.putParam("request", request.getId());
							netRequest.putParam("offer", request.getOffers().get(position).getId());
							NetManager.add(netRequest);
						}
						super.buttonPressed(positive);
					}
				}.show();
			}
		});
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				SimpleDialog.buildSelectDialog(activity, getString(R.string.request_offer_reject), getString(R.string.request_offer_reject_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
					@Override
					public void run() {
						NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_OFFER_CANCEL, NetManager.DELETE) {
							@Override
							public void success() {
								request.getOffers().remove(position);
								dataSetChanged();
								super.success();
							}

							@Override
							public void error() {
								super.error();
							}
						};
						netRequest.putParam("created_by", activity.getUser().getId());
						netRequest.putParam("offer", request.getOffers().get(position).getId());
						NetManager.add(netRequest);
					}
				}, null);
				return true;
			}
		});

		root.findViewById(R.id.find_requests_status_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_CANCEL, NetManager.DELETE) {
					@Override
					public void success() {
						super.success();
						activity.getUser().getRequests().remove(request);
						activity.navigateTo(R.id.nav_page_requests);
					}

					@Override
					public void error() {
						super.error();
					}
				};
				netRequest.putParam("created_by", activity.getUser().getId());
				netRequest.putParam("request", request.getId());
				NetManager.add(netRequest);
			}
		});

		return root;
	}

	void dataSetChanged(Request request) {
		this.request = request;
		if (activity != null) {
			adapter = new OfferAdapter(activity, request.getOffers(), request);
			list.setAdapter(adapter);
			dataSetChanged();
		}
	}

	void dataSetChanged() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
			if (request.getOffers().isEmpty()) {
				noOffers.setVisibility(View.VISIBLE);
			} else {
				noOffers.setVisibility(View.GONE);
			}
		}
	}
}
