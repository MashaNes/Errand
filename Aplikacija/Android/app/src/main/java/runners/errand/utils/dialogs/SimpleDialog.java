package runners.errand.utils.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import runners.errand.LoginActivity;
import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Benefit;
import runners.errand.model.Service;
import runners.errand.model.ServicePrefs;
import runners.errand.model.User;
import runners.errand.model.WorkingHours;
import runners.errand.ui.settings.SettingsListFragment;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class SimpleDialog {
	public static void buildMessageDialog(Activity activity, String title, String message, String errorCode, final Runnable runnableOK) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_error, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		TextView viewTitle = dialogView.findViewById(R.id.dialog_title);
		viewTitle.setText(title);
		TextView viewMessage = dialogView.findViewById(R.id.dialog_message);
		viewMessage.setText(message);
		TextView viewErrorCode = dialogView.findViewById(R.id.dialog_error_code);
		viewErrorCode.setText(errorCode);
		Button viewButton = dialogView.findViewById(R.id.dialog_button);
		viewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (runnableOK != null)
					runnableOK.run();
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity instanceof MainActivity && ((MainActivity) activity).active || activity instanceof LoginActivity && ((LoginActivity) activity).active) alertDialog.show();
	}

	public static void buildSelectDialog(Activity activity, String title, String message, String button1, String button2, final Runnable runnable1, final Runnable runnable2) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_select, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		TextView viewTitle = dialogView.findViewById(R.id.dialog_title);
		viewTitle.setText(title);
		TextView viewMessage = dialogView.findViewById(R.id.dialog_message);
		viewMessage.setText(message);
		Button viewButtonPositive = dialogView.findViewById(R.id.dialog_button_positive);
		viewButtonPositive.setText(button1);
		viewButtonPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (runnable1 != null)
					runnable1.run();
				alertDialog.dismiss();
			}
		});
		Button viewButtonNegative = dialogView.findViewById(R.id.dialog_button_negative);
		viewButtonNegative.setText(button2);
		viewButtonNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (runnable2 != null)
					runnable2.run();
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity instanceof MainActivity && ((MainActivity) activity).active || activity instanceof LoginActivity && ((LoginActivity) activity).active) alertDialog.show();
	}

	public static void buildReportDialog(final MainActivity activity, final int id, final String name) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_report, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		TextView viewTitle = dialogView.findViewById(R.id.dialog_title);
		viewTitle.setText(activity.getString(R.string.report) + " " + name);
		Button viewButtonPositive = dialogView.findViewById(R.id.dialog_button_positive);
		viewButtonPositive.setText(activity.getString(R.string.report));
		viewButtonPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.dismiss();
				NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REPORT, NetManager.POST) {
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
				netRequest.putParam("reported_user", id);
				netRequest.putParam("comment", ((EditText) dialogView.findViewById(R.id.dialog_report_reason)).getText().toString());
				netRequest.putNull("request");
				netRequest.putNull("pictures");
				NetManager.add(netRequest);
			}
		});
		Button viewButtonNegative = dialogView.findViewById(R.id.dialog_button_negative);
		viewButtonNegative.setText(activity.getString(R.string.generic_cancel));
		viewButtonNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity.active) alertDialog.show();
	}

	public static void buildImageDialog(final MainActivity activity, final Bitmap bitmap) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_image, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen).create();

		((ImageView) dialogView.findViewById(R.id.dialog_image)).setImageBitmap(bitmap);

		View viewButtonPositive = dialogView.findViewById(R.id.dialog_button_save);
		viewButtonPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (activity.permissionsGranted(MainActivity.PERMISSION_STORAGE)) {
					String path = Environment.getExternalStorageDirectory().toString();
					String[] list = Environment.getExternalStorageDirectory().list();
					if (list != null) {
						for (String s : list) {
							if (s.equalsIgnoreCase("download") || s.equalsIgnoreCase("downloads")) {
								path = path.concat("/").concat(s);
								break;
							}
						}
					}

					File dir = new File(path);
					if (!dir.exists()) {
						if (!dir.mkdirs()) {
							buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_file_write), "mkdir/FW-L", null);
							alertDialog.dismiss();
							return;
						}
					}

					File file = new File(dir, "errand_" + System.currentTimeMillis() + ".jpeg");
					if (file.exists()) {
						if (!file.delete()) {
							buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_file_write), "delete/FW-L", null);
							alertDialog.dismiss();
							return;
						}
					}

					try {
						FileOutputStream out = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
						out.flush();
						out.close();

						buildMessageDialog(activity, activity.getString(R.string.generic_success), activity.getString(R.string.request_info_picture_saved), "", null);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_file_write), "fnfex/FW-L", null);
					} catch (IOException e) {
						e.printStackTrace();
						buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_file_write), "ioex/FW-L", null);
					}

					alertDialog.dismiss();
				} else {
					activity.permissionsRequest(MainActivity.PERMISSION_STORAGE, 123);
				}
			}
		});
		View viewButtonNegative = dialogView.findViewById(R.id.dialog_button_close);
		viewButtonNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity.active) alertDialog.show();
	}

	private static int type = 0;
	public static void buildEditServicePrefDialog(final MainActivity activity, final ServicePrefs prefs, Service service, final Runnable runnable) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_edit_service_pref, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
		double d;
		String s;

		type = prefs.getPaymentType();

		((TextView) dialogView.findViewById(R.id.dialog_edit_service_pref_service_type)).setText(service.getType());
		((TextView) dialogView.findViewById(R.id.dialog_edit_service_pref_service_description)).setText(service.getDescription());
		((EditText) dialogView.findViewById(R.id.dialog_edit_service_pref_payment_type)).setText(ServicePrefs.getPaymentTypeString(activity, prefs.getPaymentType()));
		d = prefs.getPaymentAmount();
		if (d == (long) d) {
			s = String.format(Locale.getDefault(), "%d", (long) d);
		} else {
			s = String.format(Locale.getDefault(), "%s", d);
		}
		((EditText) dialogView.findViewById(R.id.dialog_edit_service_pref_payment_amount)).setText(s);
		d = prefs.getMaxDistance();
		if (d == (long) d) {
			s = String.format(Locale.getDefault(), "%d", (long) d);
		} else {
			s = String.format(Locale.getDefault(), "%s", d);
		}
		((EditText) dialogView.findViewById(R.id.dialog_edit_service_pref_max_distance)).setText(s);
		d = prefs.getMinRating();
		if (d == (long) d) {
			s = String.format(Locale.getDefault(), "%d", (long) d);
		} else {
			s = String.format(Locale.getDefault(), "%s", d);
		}
		((EditText) dialogView.findViewById(R.id.dialog_edit_service_pref_min_rating)).setText(s);

		dialogView.findViewById(R.id.dialog_edit_service_pref_payment_type).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				type++;
				if (type > 3) type = 0;
				((EditText) dialogView.findViewById(R.id.dialog_edit_service_pref_payment_type)).setText(ServicePrefs.getPaymentTypeString(activity, type));
			}
		});

		dialogView.findViewById(R.id.dialog_button_positive).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				prefs.setPaymentType(type);
				prefs.setPaymentAmount(Double.parseDouble(((EditText) dialogView.findViewById(R.id.dialog_edit_service_pref_payment_amount)).getText().toString()));
				prefs.setMaxDistance(Double.parseDouble(((EditText) dialogView.findViewById(R.id.dialog_edit_service_pref_max_distance)).getText().toString()));
				prefs.setMinRating(Double.parseDouble(((EditText) dialogView.findViewById(R.id.dialog_edit_service_pref_min_rating)).getText().toString()));

				runnable.run();

				alertDialog.dismiss();
			}
		});

		dialogView.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity.active) alertDialog.show();
	}

	public static void buildEditWorkingHoursDialog(final MainActivity activity, final WorkingHours hours, final Runnable runnable) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_edit_working_hours, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		final NumberPicker fromHours = dialogView.findViewById(R.id.dialog_time_from_hours);
		fromHours.setMinValue(0);
		fromHours.setMaxValue(23);
		final NumberPicker fromMinutes = dialogView.findViewById(R.id.dialog_time_from_minutes);
		fromMinutes.setMinValue(0);
		fromMinutes.setMaxValue(59);
		final NumberPicker untilHours = dialogView.findViewById(R.id.dialog_time_until_hours);
		untilHours.setMinValue(0);
		untilHours.setMaxValue(23);
		final NumberPicker untilMinutes = dialogView.findViewById(R.id.dialog_time_until_minutes);
		untilMinutes.setMinValue(0);
		untilMinutes.setMaxValue(59);

		if (hours.getFrom() != null && !hours.getFrom().isEmpty()) {
			String[] from = hours.getFrom().split(":");
			int fh = Integer.parseInt(from[0]);
			int fm = Integer.parseInt(from[1]);
			fromHours.setValue(fh);
			fromMinutes.setValue(fm);
		}

		if (hours.getUntil() != null && !hours.getUntil().isEmpty()) {
			String[] until = hours.getUntil().split(":");
			int uh = Integer.parseInt(until[0]);
			int um = Integer.parseInt(until[1]);
			untilHours.setValue(uh);
			untilMinutes.setValue(um);
		}

		dialogView.findViewById(R.id.dialog_button_positive).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (untilHours.getValue() < fromHours.getValue() || (untilHours.getValue() == fromHours.getValue() && untilMinutes.getValue() <= fromMinutes.getValue())) {
					alertDialog.dismiss();
					buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_hours), "", null);
				} else {
					hours.setUntil(untilHours.getValue(), untilMinutes.getValue());
					hours.setFrom(fromHours.getValue(), fromMinutes.getValue());
					runnable.run();
					alertDialog.dismiss();
				}
			}
		});

		dialogView.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity.active) alertDialog.show();
	}

	public static void buildEditBenefitDialog(final MainActivity activity, final Benefit benefit, final Runnable runnable) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_edit_benefit, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		String name = benefit.getUser().getFirstName() + " " + benefit.getUser().getLastName();
		((TextView) dialogView.findViewById(R.id.dialog_edit_benefits_name)).setText(name);

		String s;
		double d = benefit.getDiscount();
		if (d == (long) d) {
			s = String.format(Locale.getDefault(), "%d", (long) d);
		} else {
			s = String.format(Locale.getDefault(), "%s", d);
		}
		((EditText) dialogView.findViewById(R.id.dialog_edit_benefits_amount)).setText(s);

		dialogView.findViewById(R.id.dialog_button_positive).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				benefit.setDiscount(Double.parseDouble(((EditText) dialogView.findViewById(R.id.dialog_edit_benefits_amount)).getText().toString()));

				runnable.run();

				alertDialog.dismiss();
			}
		});

		dialogView.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity.active) alertDialog.show();
	}

	public static void buildEditAutoBenefitDialog(final MainActivity activity, final SettingsListFragment.AutoBenefit autoBenefit, final Runnable runnable) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_edit_auto_benefit, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		((EditText) dialogView.findViewById(R.id.dialog_edit_benefits_req_amount)).setText(String.format(Locale.getDefault(), "%d", autoBenefit.req));
		String s;
		float d = autoBenefit.disc;
		if (d == (long) d) {
			s = String.format(Locale.getDefault(), "%d", (long) d);
		} else {
			s = String.format(Locale.getDefault(), "%s", d);
		}
		((EditText) dialogView.findViewById(R.id.dialog_edit_benefits_amount)).setText(s);

		dialogView.findViewById(R.id.dialog_button_positive).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				autoBenefit.req = Integer.parseInt(((EditText) dialogView.findViewById(R.id.dialog_edit_benefits_req_amount)).getText().toString());
				autoBenefit.disc = Float.parseFloat(((EditText) dialogView.findViewById(R.id.dialog_edit_benefits_amount)).getText().toString());
				runnable.run();
				alertDialog.dismiss();
			}
		});

		dialogView.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity.active) alertDialog.show();
	}

	private static int rating = 3;
	public static void buildRateDialog(final MainActivity activity, final User user, final int requestId, final Runnable runnable) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_rate, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		String title = activity.getString(R.string.generic_rate) + " " + user.getFirstName() + " " + user.getLastName();
		((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);

		dialogView.findViewById(R.id.dialog_rate_1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((ImageView) dialogView.findViewById(R.id.dialog_rate_1)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_2)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_3)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_4)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_5)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				rating = 1;
			}
		});
		dialogView.findViewById(R.id.dialog_rate_2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((ImageView) dialogView.findViewById(R.id.dialog_rate_1)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_2)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_3)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_4)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_5)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				rating = 2;
			}
		});
		dialogView.findViewById(R.id.dialog_rate_3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((ImageView) dialogView.findViewById(R.id.dialog_rate_1)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_2)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_3)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_4)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_5)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				rating = 3;
			}
		});
		dialogView.findViewById(R.id.dialog_rate_4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((ImageView) dialogView.findViewById(R.id.dialog_rate_1)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_2)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_3)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_4)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_5)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_empty));
				rating = 4;
			}
		});
		dialogView.findViewById(R.id.dialog_rate_5).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((ImageView) dialogView.findViewById(R.id.dialog_rate_1)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_2)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_3)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_4)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				((ImageView) dialogView.findViewById(R.id.dialog_rate_5)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_full));
				rating = 5;
			}
		});

		dialogView.findViewById(R.id.dialog_button_positive).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_RATE, NetManager.POST) {
					@Override
					public void success() {
						runnable.run();
						super.success();
					}

					@Override
					public void error() {
						super.error();
					}
				};
				netRequest.putParam("created_by", activity.getUser().getId());
				netRequest.putParam("rated_user", user.getId());
				netRequest.putParam("request", requestId);
				netRequest.putParam("grade", rating);
				netRequest.putParam("comment", ((EditText) dialogView.findViewById(R.id.dialog_report_reason)).getText().toString());
				NetManager.add(netRequest);
				alertDialog.dismiss();
			}
		});

		dialogView.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		alertDialog.setView(dialogView);
		if (activity.active) alertDialog.show();
	}
}
