package runners.errand.utils.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import runners.errand.LoginActivity;
import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.UserAdapter;
import runners.errand.model.Edit;
import runners.errand.model.Service;
import runners.errand.model.ServicePrefs;
import runners.errand.model.User;
import runners.errand.model.WorkingHours;

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
				if (type > 2) type = 0;
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

	public static void buildEditWorkingHoursDialog(MainActivity activity) {
		final View dialogView = View.inflate(activity,  R.layout.dialog_edit_working_hours, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();



		alertDialog.setView(dialogView);
		if (activity.active) alertDialog.show();
	}
}
