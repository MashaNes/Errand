package runners.errand.utils.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Achievement;
import runners.errand.model.Benefit;
import runners.errand.model.User;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class ProfileDialog extends AlertDialog {
	public ProfileDialog(final MainActivity activity, final User user, String positive, String negative) {
		super(activity);
		View root = View.inflate(activity, R.layout.dialog_profile, null);

		if (positive.isEmpty()) {
			root.findViewById(R.id.dialog_button_positive).setVisibility(View.INVISIBLE);
		} else {
			((Button) root.findViewById(R.id.dialog_button_positive)).setText(positive);
			root.findViewById(R.id.dialog_button_positive).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					buttonPressed(true);
					}
			});
		}
		if (negative.isEmpty()) {
			root.findViewById(R.id.dialog_button_negative).setVisibility(View.INVISIBLE);
		} else {
			((Button) root.findViewById(R.id.dialog_button_negative)).setText(negative);
			root.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					buttonPressed(false);
					}
			});
		}

		if (user.getPicture_bmp() != null) ((ImageView) root.findViewById(R.id.dialog_profile_picture)).setImageBitmap(user.getPicture_bmp());
		((EditText) root.findViewById(R.id.dialog_profile_name_et)).setText(user.getFirstName());
		((EditText) root.findViewById(R.id.dialog_profile_lastname_et)).setText(user.getLastName());
		((TextView) root.findViewById(R.id.dialog_profile_email_et)).setText(user.getEmail());
		root.findViewById(R.id.dialog_profile_email_et).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				email(activity, user.getEmail().trim());
			}
		});
		((View) root.findViewById(R.id.dialog_profile_email_et).getParent()).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				email(activity, user.getEmail().trim());
			}
		});
		((TextView) root.findViewById(R.id.dialog_profile_phone_et)).setText(user.getPhone());
		root.findViewById(R.id.dialog_profile_phone_et).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				call(activity, user.getPhone().trim());
			}
		});
		((View) root.findViewById(R.id.dialog_profile_phone_et).getParent()).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				call(activity, user.getPhone().trim());
			}
		});

		if (Float.isNaN(user.getRating())) {
			((TextView) root.findViewById(R.id.dialog_profile_value)).setText(activity.getString(R.string.generic_unrated));
		} else {
			((TextView) root.findViewById(R.id.dialog_profile_value)).setText(String.format(Locale.getDefault(), "%.1f", user.getRating()));
		}

		root.findViewById(R.id.dialog_profile_report).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				SimpleDialog.buildReportDialog(activity, user.getId(), user.getFirstName() + " " + user.getLastName());
			}
		});

		for (Benefit benefit : activity.getUser().getBenefits()) {
			if (benefit.getUser().getId() == user.getId()) {
				root.findViewById(R.id.dialog_profile_benefit).setVisibility(View.GONE);
			}
		}
		root.findViewById(R.id.dialog_profile_benefit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				final Benefit benefit = new Benefit(-1, user, 0);
				SimpleDialog.buildEditBenefitDialog(activity, benefit, new Runnable() {
					@Override
					public void run() {
						NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_BENEFIT_ADD, NetManager.POST) {
							@Override
							public void success() {
								super.success();
								try {
									JSONObject o = new JSONObject(getResult().getMsg());
									benefit.setId(o.optInt("id"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
								activity.getUser().getBenefits().add(benefit);
							}

							@Override
							public void error() {
								super.error();
								SimpleDialog.buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_generic_api), "", null);
							}
						};

						netRequest.putParam("created_by", activity.getUser().getId());
						netRequest.putParam("benefit_user", benefit.getUser().getId());
						netRequest.putParam("discount", benefit.getDiscount());
						NetManager.add(netRequest);
					}
				});
			}
		});

		this.setView(root);
	}

	public void buttonPressed(boolean positive) {
		this.dismiss();
	}

	private void call(Activity activity, String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
		activity.startActivity(intent);
	}

	private void email(Activity activity, String email) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:"));
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
		intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
		activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.chooser_email)));
	}
}
