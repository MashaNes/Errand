package runners.errand.ui.request;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.geofencing.GeofencingBroadcastReceiver;
import runners.errand.model.Offer;
import runners.errand.model.Request;
import runners.errand.model.ServicePrefs;
import runners.errand.model.Task;
import runners.errand.model.User;
import runners.errand.utils.Static;
import runners.errand.utils.dialogs.ProfileDialog;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class StatusFragment extends Fragment {
	private MainActivity activity;
	private Request request;
	private User user;
	private Offer offer;
	private Button start, cancel, complete, rate;
	private TextView total, total_label, edit, edit_label, completed, top_text;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_status, container, false);

		RequestFragment parent = (RequestFragment) getParentFragment();
		if (parent == null) return root;
		activity = parent.getMainActivity();
		request = parent.getRequest();

		start = root.findViewById(R.id.find_requests_status_start);
		cancel = root.findViewById(R.id.find_requests_status_cancel);
		complete = root.findViewById(R.id.find_requests_status_complete);
		rate = root.findViewById(R.id.find_requests_status_rate);

		top_text = root.findViewById(R.id.request_status_text_user);
		completed = root.findViewById(R.id.find_requests_status_other_user_completed);
		total_label = root.findViewById(R.id.item_offer_total_label);
		total = root.findViewById(R.id.item_offer_total);
		edit_label = root.findViewById(R.id.item_offer_edit_label);
		edit = root.findViewById(R.id.item_offer_edit);

		offer = request.getAcceptedOffer();
		if (offer == null) offer = request.getMyOffer();

		setupUI();

		String payment = "";
		payment += offer.getPaymentAmount();
		payment += " RSD ";
		payment += ServicePrefs.getPaymentTypeString(activity, offer.getPaymentType());
		((TextView) root.findViewById(R.id.item_offer_payment_amount)).setText(payment);
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

		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (activity.getUser().getStatus() == User.STATUS_RUNNING) {
					apiStartRequest(Static.lat, Static.lng);
				} else {
					SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_start_request_location), "", null);
				}
			}
		});

		complete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialog.buildSelectDialog(activity, getString(R.string.request_complete), getString(R.string.request_complete_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
					@Override
					public void run() {
						if (request.getCreatedBy().getId() != activity.getUser().getId() && request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_KM) {
							if (activity.getUser().getStatus() == User.STATUS_RUNNING) {
								GeofencingBroadcastReceiver.removeGeofence(activity, request.getId());
								apiRequestFinish(2, Static.lat, Static.lng);
							} else {
								SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_finish_request_location), "", null);
							}
						} else {
							GeofencingBroadcastReceiver.removeGeofence(activity, request.getId());
							apiRequestFinish(2, 0, 0);
						}
					}
				}, null);
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialog.buildSelectDialog(activity, getString(R.string.request_cancel), getString(R.string.request_cancel_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
					@Override
					public void run() {
						GeofencingBroadcastReceiver.removeGeofence(activity, request.getId());
						apiRequestFinish(3, 0, 0);
					}
				}, null);
			}
		});

		rate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialog.buildRateDialog(activity, user, request.getId(), new Runnable() {
					@Override
					public void run() {
						if (request.getCreatedBy().getId() == activity.getUser().getId()) {
							request.setRatedWorkingWith(true);
						} else {
							request.setRatedCreatedBy(true);
						}
						setupUI();
					}
				});
			}
		});

		return root;
	}

	private void setupUI() {
		start.setVisibility(View.GONE);
		cancel.setVisibility(View.GONE);
		complete.setVisibility(View.GONE);
		rate.setVisibility(View.GONE);
		total_label.setVisibility(View.GONE);
		total.setVisibility(View.GONE);
		edit_label.setVisibility(View.GONE);
		edit.setVisibility(View.GONE);
		if (request.getCreatedBy().getId() == activity.getUser().getId()) {
			if (request.isFinishedWorkingWith()) {
				String s = request.getWorkingWith().getFirstName() + " " + request.getWorkingWith().getLastName() + " " + getString(R.string.marked_as_finished);
				completed.setText(s);
			} else {
				completed.setVisibility(View.GONE);
			}
			user = request.getWorkingWith();
			top_text.setText(R.string.request_send_offer_running);
			if (request.getStatus() == Request.STATUS_CANCELED) {
				Log.e("REQUEST", "1-0");
				if (!request.isRatedWorkingWith())
					rate.setVisibility(View.VISIBLE);
			} else if (request.isFinishedWorkingWith() && request.isFinishedCreatedBy()) {
				// Mark as completed by both
				Log.e("REQUEST", "1-1");
				if (!request.isRatedWorkingWith())
					rate.setVisibility(View.VISIBLE);
				total_label.setVisibility(View.VISIBLE);
				total.setVisibility(View.VISIBLE);
				total.setText(String.format(Locale.getDefault(), "%.0f RSD", request.getPrice()));
			} else if (request.isFinishedCreatedBy()) {
				// Mark as completed by you
				Log.e("REQUEST", "1-2");
			} else {
				// Offer accepted
				Log.e("REQUEST", "1-3");
				cancel.setVisibility(View.VISIBLE);
				complete.setVisibility(View.VISIBLE);
			}
		} else {
			if (request.isFinishedCreatedBy()) {
				String s = request.getCreatedBy().getFirstName() + " " + request.getCreatedBy().getLastName() + " " + getString(R.string.marked_as_finished);
				completed.setText(s);
			} else {
				completed.setVisibility(View.GONE);
			}
			user = request.getCreatedBy();
			top_text.setText(R.string.request_send_offer_created_by);
			if (request.getStatus() == Request.STATUS_CANCELED) {
				Log.e("REQUEST", "2-0");
				if (!request.isRatedCreatedBy())
					rate.setVisibility(View.VISIBLE);
			} else if (request.isFinishedWorkingWith() && request.isFinishedCreatedBy()) {
				Log.e("REQUEST", "2-1");
				// Mark as completed by both
				if (!request.isRatedCreatedBy())
					rate.setVisibility(View.VISIBLE);
				total_label.setVisibility(View.VISIBLE);
				total.setVisibility(View.VISIBLE);
				total.setText(String.format(Locale.getDefault(), "%.0f RSD", request.getPrice()));
			} else if (request.isFinishedWorkingWith()) {
				// Marked as completed by you
				Log.e("REQUEST", "2-2");
			} else if (request.getAcceptedOffer() != null) {
				// Offer accepted
				int payment_type = request.getAcceptedOffer().getPaymentType();
				if (payment_type == ServicePrefs.PAYMENT_TYPE_FIXED || payment_type == ServicePrefs.PAYMENT_TYPE_FIXED_PLUS) {
					Log.e("REQUEST", "2-3-1");
					cancel.setVisibility(View.VISIBLE);
					complete.setVisibility(View.VISIBLE);
				} else if (payment_type == ServicePrefs.PAYMENT_TYPE_PER_HOUR) {
					if (request.getPriceTime() == null) {
						Log.e("REQUEST", "2-3-2-1");
						start.setVisibility(View.VISIBLE);
					} else {
						Log.e("REQUEST", "2-3-2-2");
						cancel.setVisibility(View.VISIBLE);
						complete.setVisibility(View.VISIBLE);
					}
				} else if (payment_type == ServicePrefs.PAYMENT_TYPE_PER_KM) {
					if (Double.isNaN(request.getPriceLat()) || Double.isNaN(request.getPriceLng()) ) {
						Log.e("REQUEST", "2-3-3-1");
						start.setVisibility(View.VISIBLE);
					} else {
						Log.e("REQUEST", "2-3-3-2");
						cancel.setVisibility(View.VISIBLE);
						complete.setVisibility(View.VISIBLE);
					}
				}
			} else {
				// Offer sent, not accepted
				Log.e("REQUEST", "2-4");
			}
		}
	}

	private void apiRequestFinish(int status, double lat, double lng) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_FINISH, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
				// price:null
				if (request.getCreatedBy().getId() == activity.getUser().getId()) {
					request.setFinishedCreatedBy(true);
				} else {
					request.setFinishedWorkingWith(true);
				}
				Log.e("REQUEST", "Finish: Success");
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					request.setPrice(o.optDouble("price"));
					Log.e("REQUEST", "Finish JSON: Success");
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("REQUEST", "Finish JSON: Error");
				}
				setupUI();
			}

			@Override
			public void error() {
				super.error();
				Log.e("REQUEST", "Finish: Error");
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("request", request.getId());
		netRequest.putParam("status", status);
		if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_KM) {
			JSONObject location = new JSONObject();
			try {
				location.put("latitude", lat);
				location.put("longitude", lng);
				netRequest.putParam("location", location);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			netRequest.putNull("location");
		}
		NetManager.add(netRequest);
	}

	private void apiStartRequest(final double lat, final double lng) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_START, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
				// detail:"success"
				Log.e("REQUEST", "Start: Success");
				if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_HOUR) {
					request.setPriceTime(calendar.getTime());
				} else if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_KM) {
					request.setPriceLat(lat);
					request.setPriceLng(lng);
				}
				// Setup geo-fencing
				if (request.getDestination() != null) {
					GeofencingBroadcastReceiver.addGeofence(
							activity,
							request.getId(),
							request.getDestination().getId(),
							request.getDestination().getLat(),
							request.getDestination().getLng()
					);
				}
				for (Task task : request.getTasks()) {
					if (task.getAddress() != null) {
						GeofencingBroadcastReceiver.addGeofence(
								activity,
								request.getId(),
								task.getAddress().getId(),
								task.getAddress().getLat(),
								task.getAddress().getLng()
						);
					}
				}
				setupUI();
			}

			@Override
			public void error() {
				super.error();
				Log.e("REQUEST", "Start: Error");
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("request", request.getId());
		if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_HOUR) {
			netRequest.putParam("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.getTime()));
		} else {
			netRequest.putNull("timestamp");
		}
		JSONObject location = new JSONObject();
		try {
			if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_KM) {
				location.put("latitude", lat);
				location.put("longitude", lng);
			} else {
				location.put("latitude", 0);
				location.put("longitude", 0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		netRequest.putParam("location", location);
		NetManager.add(netRequest);
	}
}
