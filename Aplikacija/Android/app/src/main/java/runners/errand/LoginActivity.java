package runners.errand;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import runners.errand.model.User;
import runners.errand.utils.Dialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;
import runners.errand.utils.net.NetResult;

public class LoginActivity extends AppCompatActivity {

	private boolean passShown = false;
	public boolean active = false;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		activity = this;

		final View icon = findViewById(R.id.login_app_icon);
		icon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
		icon.setVisibility(View.VISIBLE);

		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				String email = ((EditText) findViewById(R.id.login_email)).getText().toString();
				String password = ((EditText) findViewById(R.id.login_pass)).getText().toString();

				if (email.equals("setip")) {
					NetManager.setApiServer(password);
					return;
				}

				NetRequest request = new NetRequest(NetManager.API_AUTH, NetManager.POST) {
					@Override
					public void success() {
						NetRequest request = new NetRequest(NetManager.API_USERS + "4/", NetManager.GET) {
							@Override
							public void success() {
								icon.postOnAnimationDelayed(new Runnable() {
									@Override
									public void run() {
										Intent intent = new Intent(getApplicationContext(), MainActivity.class);
										intent.putExtra("user", getResult().getMsg());
										startActivity(intent);
									}
								}, 300);
								icon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out));
								icon.setVisibility(View.INVISIBLE);
							}

							@Override
							public void error() {
								Dialog.buildMessageDialog(activity, getString(R.string.error), getResult().getMsg(), "MA-" + (getResult().getType() == NetResult.TYPE_ERROR_LOCAL ? "L" : "R"), null);
							}
						};
						NetManager.add(request);
					}

					@Override
					public void error() {
						Dialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_login) + "\n" + getResult().getMsg(), "LA-" + (getResult().getType() == NetResult.TYPE_ERROR_LOCAL ? "L" : "R"), null);
						v.setEnabled(true);
					}
				};
				request.putParam("username", email);
				request.putParam("password", password);
				request.setLogin(true);
				NetManager.add(request);
				v.setEnabled(false);
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

	@Override
	protected void onPause() {
		active = false;
		super.onPause();
	}

	@Override
	protected void onResume() {
		active = true;
		super.onResume();
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
