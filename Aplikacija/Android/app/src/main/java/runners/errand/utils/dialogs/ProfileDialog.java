package runners.errand.utils.dialogs;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.User;

public class ProfileDialog extends AlertDialog {
	protected ProfileDialog(MainActivity activity, User user, boolean selected) {
		super(activity);
		View root = View.inflate(activity, R.layout.dialog_profile, null);

		if (selected) ((Button) root.findViewById(R.id.dialog_button_positive)).setText(R.string.generic_deselect);
		root.findViewById(R.id.dialog_button_positive).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonPressed(true);
			}
		});
		root.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonPressed(false);
			}
		});

		if (user.getPicture_bmp() != null) ((ImageView) root.findViewById(R.id.dialog_profile_picture)).setImageBitmap(user.getPicture_bmp());
		((EditText) root.findViewById(R.id.dialog_profile_name_et)).setText(user.getFirstName());
		((EditText) root.findViewById(R.id.dialog_profile_lastname_et)).setText(user.getLastName());
		((EditText) root.findViewById(R.id.dialog_profile_email_et)).setText(user.getEmail());
		((EditText) root.findViewById(R.id.dialog_profile_phone_et)).setText(user.getPhone());
		((TextView) root.findViewById(R.id.dialog_profile_value)).setText(String.format(Locale.getDefault(), "%.1f", user.getRating()));

		this.setView(root);
	}

	public void buttonPressed(boolean positive) {
		this.dismiss();
	}
}
