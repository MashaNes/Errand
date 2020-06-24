package runners.errand.ui.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Request;
import runners.errand.model.ServicePrefs;
import runners.errand.utils.dialogs.ProfileDialog;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class SendOfferFragment extends Fragment {
	private MainActivity activity;
	private RequestFragment parent;
	private Request request;
	private EditText paymentAmount;
	private int paymentType = 0;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_send_offer, container, false);

		parent = (RequestFragment) getParentFragment();
		if (parent == null) return root;
		activity = parent.getMainActivity();
		request = parent.getRequest();

		// Create by
		if (request.getCreatedBy().getPicture_bmp() != null) {
			((ImageView) root.findViewById(R.id.item_user_image)).setImageBitmap(request.getCreatedBy().getPicture_bmp());
		} else {
			((ImageView) root.findViewById(R.id.item_user_image)).setImageDrawable(getResources().getDrawable(R.drawable.ic_face));
		}
		String name = request.getCreatedBy().getFirstName() + " " + request.getCreatedBy().getLastName();
		((TextView) root.findViewById(R.id.item_user_name)).setText(name);
		String rating = getString(R.string.generic_unrated);
		if (!Float.isNaN(request.getCreatedBy().getRating())) rating = String.format(Locale.getDefault(), "%.1f", request.getCreatedBy().getRating());
		((TextView) root.findViewById(R.id.item_user_rating)).setText(rating);
		root.findViewById(R.id.item_user_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new ProfileDialog(activity, request.getCreatedBy(), "", getString(R.string.generic_close)).show();
			}
		});

		// Offer
		paymentAmount = root.findViewById(R.id.dialog_edit_service_pref_payment_amount);
		((EditText) root.findViewById(R.id.dialog_edit_service_pref_payment_type)).setText(ServicePrefs.getPaymentTypeString(activity, paymentType));
		root.findViewById(R.id.dialog_edit_service_pref_payment_type).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentType++;
				if (paymentType > 3) paymentType = 0;
				((EditText) v).setText(ServicePrefs.getPaymentTypeString(activity, paymentType));
			}
		});
		root.findViewById(R.id.request_send_offer_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_OFFER_CREATE, NetManager.POST) {
					@Override
					public void success() {
						super.success();
						SimpleDialog.buildMessageDialog(activity, getString(R.string.generic_success), getString(R.string.offer_sent), "", new Runnable() {
							@Override
							public void run() {
								activity.navigateTo(R.id.nav_page_requests);
							}
						});
					}

					@Override
					public void error() {
						super.error();
					}
				};
				// "created_by" : 5,
				//	"payment_type" : 1,
				//	"payment_ammount" : 600,
				//	"request" : 1,
				//	"edit" : {
				//		"time" : "2020-05-21 18:00",
				//		"tasks" : [
				//			{
				//				"task" : 2,
				//				"address" : {
				//					"name" : "Dragise Cvetkovica 32",
				//					"longitude" : 43.319185,
				//					"latitude" : 21.913395,
				//					"home" : true,
				//					"arrived" : false
				//				}
				//
				//			}
				//		]
				//	}
				netRequest.putParam("created_by", activity.getUser().getId());
				netRequest.putParam("payment_type", paymentType);
				netRequest.putParam("payment_ammount", Integer.parseInt(paymentAmount.getText().toString()));
				netRequest.putParam("request", request.getId());
				netRequest.putNull("edit");
				NetManager.add(netRequest);
			}
		});

		return root;
	}
}
