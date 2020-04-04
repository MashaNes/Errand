package runners.errand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

	private boolean passShown = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final View icon = findViewById(R.id.login_app_icon);
		icon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
		icon.setVisibility(View.VISIBLE);

		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				icon.postOnAnimationDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO: Login
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(intent);
					}
				}, 300);
				icon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out));
				icon.setVisibility(View.INVISIBLE);
			}
		});

		findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkEmail()) {
					return;
				} else if (!checkPassword()) {
					return;
				} else if (!checkPhone()) {
					return;
				}

				icon.postOnAnimationDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO: Create account
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(intent);
					}
				}, 300);
				icon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out));
				icon.setVisibility(View.INVISIBLE);
			}
		});

		findViewById(R.id.login_up_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				findViewById(R.id.login_layout_bottom_in).setVisibility(View.GONE);
				findViewById(R.id.login_layout_center_in).setVisibility(View.GONE);
				findViewById(R.id.login_layout_bottom_up).setVisibility(View.VISIBLE);
				findViewById(R.id.login_layout_center_up).setVisibility(View.VISIBLE);
			}
		});

		findViewById(R.id.login_in_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				findViewById(R.id.login_layout_bottom_up).setVisibility(View.GONE);
				findViewById(R.id.login_layout_center_up).setVisibility(View.GONE);
				findViewById(R.id.login_layout_bottom_in).setVisibility(View.VISIBLE);
				findViewById(R.id.login_layout_center_in).setVisibility(View.VISIBLE);
			}
		});

		findViewById(R.id.login_hide_pass).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				passShown = !passShown;
				if (passShown) {
					((ImageView) v).setImageResource(R.drawable.ic_hide_pass);
					((EditText) findViewById(R.id.login_pass)).setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					((ImageView) v).setImageResource(R.drawable.ic_show_pass);
					((EditText) findViewById(R.id.login_pass)).setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});

		findViewById(R.id.sign_up_hide_pass).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				passShown = !passShown;
				if (passShown) {
					((ImageView) v).setImageResource(R.drawable.ic_hide_pass);
					((EditText) findViewById(R.id.sign_up_pass)).setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					((ImageView) v).setImageResource(R.drawable.ic_show_pass);
					((EditText) findViewById(R.id.sign_up_pass)).setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});

		findViewById(R.id.sign_up_address_map).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: Get current location
			}
		});

		findViewById(R.id.sign_up_address_my_location).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: Open map activity for location selection
			}
		});
	}

	private boolean checkEmail() {
		return true;
	}

	private boolean checkPassword() {
		return true;
	}

	private boolean checkPhone() {
		return true;
	}
}
