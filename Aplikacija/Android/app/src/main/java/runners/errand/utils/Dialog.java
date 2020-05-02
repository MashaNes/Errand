package runners.errand.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import runners.errand.LoginActivity;
import runners.errand.MainActivity;
import runners.errand.R;

public class Dialog {
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

	public static void buildSelectDialog(MainActivity activity, String title, String message, String button1, String button2, final Runnable runnable1, final Runnable runnable2) {
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
		if (activity.active) alertDialog.show();
	}
}
