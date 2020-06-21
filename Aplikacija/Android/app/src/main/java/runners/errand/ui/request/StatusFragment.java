package runners.errand.ui.request;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Offer;
import runners.errand.model.Request;
import runners.errand.model.ServicePrefs;
import runners.errand.model.User;
import runners.errand.utils.dialogs.ProfileDialog;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class StatusFragment extends Fragment {
	private MainActivity activity;
	private RequestFragment parent;
	private Request request;
	private User user;

	// TODO: Both users have the option to complete and cancel. One user canceling cancels the request, but the request is completed only when both users mark it as complete.
	// TODO: Rate user button after request is finished (canceled or completed).

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_status, container, false);

		parent = (RequestFragment) getParentFragment();
		if (parent == null) return root;
		activity = parent.getMainActivity();
		request = parent.getRequest();

		Button cancel = root.findViewById(R.id.find_requests_status_cancel);
		Button complete = root.findViewById(R.id.find_requests_status_complete);
		Button rate = root.findViewById(R.id.find_requests_status_rate);

		Offer offer = request.getAcceptedOffer();
		if (offer == null) offer = request.getMyOffer();

		if (request.getCreatedBy().getId() == activity.getUser().getId()) {
			if (request.isFinishedWorkingWith()) {
				String s = request.getWorkingWith().getFirstName() + " " + request.getWorkingWith().getLastName() + " " + getString(R.string.marked_as_finished);
				((TextView) root.findViewById(R.id.find_requests_status_other_user_completed)).setText(s);
			} else {
				root.findViewById(R.id.find_requests_status_other_user_completed).setVisibility(View.GONE);
			}
			user = request.getWorkingWith();
			((TextView) root.findViewById(R.id.request_status_text_user)).setText(R.string.request_send_offer_running);
			if (request.isFinishedWorkingWith() && request.isFinishedCreatedBy()) {
				cancel.setVisibility(View.GONE);
				complete.setVisibility(View.GONE);
				rate.setVisibility(View.VISIBLE);
			} else if (request.isFinishedCreatedBy()) {
				cancel.setVisibility(View.GONE);
				complete.setVisibility(View.GONE);
				rate.setVisibility(View.GONE);
			} else {
				cancel.setVisibility(View.VISIBLE);
				complete.setVisibility(View.VISIBLE);
				rate.setVisibility(View.GONE);
			}

			if (request.isRatedWorkingWith()) {
				rate.setVisibility(View.GONE);
			}
		} else {
			if (request.isFinishedCreatedBy()) {
				String s = request.getCreatedBy().getFirstName() + " " + request.getCreatedBy().getLastName() + " " + getString(R.string.marked_as_finished);
				((TextView) root.findViewById(R.id.find_requests_status_other_user_completed)).setText(s);
			} else {
				root.findViewById(R.id.find_requests_status_other_user_completed).setVisibility(View.GONE);
			}
			user = request.getCreatedBy();
			((TextView) root.findViewById(R.id.request_status_text_user)).setText(R.string.request_send_offer_created_by);
			if (request.isFinishedWorkingWith() && request.isFinishedCreatedBy()) {
				cancel.setVisibility(View.GONE);
				complete.setVisibility(View.GONE);
				rate.setVisibility(View.VISIBLE);
			} else if (request.isFinishedWorkingWith()) {
				cancel.setVisibility(View.GONE);
				complete.setVisibility(View.GONE);
				rate.setVisibility(View.GONE);
			} else if (request.getAcceptedOffer() != null) {
				cancel.setVisibility(View.VISIBLE);
				complete.setVisibility(View.VISIBLE);
				rate.setVisibility(View.GONE);
			} else {
				cancel.setVisibility(View.GONE);
				complete.setVisibility(View.GONE);
				rate.setVisibility(View.GONE);
			}

			if (request.isRatedCreatedBy()) {
				rate.setVisibility(View.GONE);
			}
		}

		String payment = "";
		payment += offer.getPaymentAmount();
		payment += " RSD ";
		payment += ServicePrefs.getPaymentTypeString(activity, offer.getPaymentType());
		((TextView) root.findViewById(R.id.item_offer_payment_amount)).setText(payment);
		((ImageView) root.findViewById(R.id.item_user_image)).setImageBitmap(user.getPicture_bmp());
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

		complete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialog.buildSelectDialog(activity, getString(R.string.request_complete), getString(R.string.request_complete_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
					@Override
					public void run() {
						apiRequestFinish(2);
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
						apiRequestFinish(3);
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
						activity.navigateTo(R.id.nav_page_requests);
					}
				});
			}
		});

		return root;
	}

	private void apiRequestFinish(int status) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_FINISH, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
			}

			@Override
			public void error() {
				super.error();
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("request", request.getId());
		netRequest.putParam("status", status);
		NetManager.add(netRequest);
	}
}
