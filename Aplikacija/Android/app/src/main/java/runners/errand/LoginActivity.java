package runners.errand;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import runners.errand.model.User;
import runners.errand.utils.Static;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.PreferenceManager;
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

				signIn(v, email, password);
			}
		});

		findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				signUp(v);
			}
		});

		findViewById(R.id.login_up_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSignUp();
			}
		});

		findViewById(R.id.login_in_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showLogin();
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

		// TODO: Address on sign up?
		findViewById(R.id.sign_up_address_map).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get current location
			}
		});

		findViewById(R.id.sign_up_address_my_location).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Open map activity
			}
		});

		((ProgressBar) findViewById(R.id.login_progress)).getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite), PorterDuff.Mode.MULTIPLY));
		((ProgressBar) findViewById(R.id.sign_up_progress)).getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite), PorterDuff.Mode.MULTIPLY));

		String email = PreferenceManager.getString(getApplicationContext(), PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_EMAIL);
		if (email != null) {
			((EditText) findViewById(R.id.login_email)).setText(email);
			((EditText) findViewById(R.id.login_email)).setSelection(email.length());
		}

		String token = PreferenceManager.getString(getApplicationContext(), PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_TOKEN);
		int userId = PreferenceManager.getInt(getApplicationContext(), PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_USER_ID);
		if (token != null && userId != -1) {
			NetManager.setToken(token);
			apiGetUser(findViewById(R.id.login), userId);
		} else {
			showLogin();
		}
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

	private void showLogin() {
		findViewById(R.id.login_layout_bottom_up).setVisibility(View.GONE);
		findViewById(R.id.login_layout_center_up).setVisibility(View.GONE);
		findViewById(R.id.login_layout_bottom_in).setVisibility(View.VISIBLE);
		findViewById(R.id.login_layout_center_in).setVisibility(View.VISIBLE);
	}

	private void showSignUp() {
		findViewById(R.id.login_layout_bottom_in).setVisibility(View.GONE);
		findViewById(R.id.login_layout_center_in).setVisibility(View.GONE);
		findViewById(R.id.login_layout_bottom_up).setVisibility(View.VISIBLE);
		findViewById(R.id.login_layout_center_up).setVisibility(View.VISIBLE);
	}

	private void startLoading() {
		findViewById(R.id.login).setVisibility(View.INVISIBLE);
		findViewById(R.id.sign_up).setVisibility(View.INVISIBLE);
		findViewById(R.id.login_progress).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_up_progress).setVisibility(View.VISIBLE);
	}

	private void stopLoading() {
		if (findViewById(R.id.login_layout_bottom_in).getVisibility() != View.VISIBLE && findViewById(R.id.login_layout_bottom_up).getVisibility() != View.VISIBLE) {
			showLogin();
		}
		findViewById(R.id.login_progress).setVisibility(View.INVISIBLE);
		findViewById(R.id.sign_up_progress).setVisibility(View.INVISIBLE);
		findViewById(R.id.login).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_up).setVisibility(View.VISIBLE);
	}

	private void apiGetUser(final View v, int id) {
		NetRequest request = new NetRequest(NetManager.getApiServer() + NetManager.API_USERS + id + "/", NetManager.GET) {
			@Override
			public void success() {
				View icon = findViewById(R.id.login_app_icon);
				icon.postOnAnimationDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						try {
							Static.user = new User(new JSONObject(getResult().getMsg()));
							startActivity(intent);
						} catch (JSONException e) {
							e.printStackTrace();
							SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_login) + "\n" + getResult().getMsg(), "json/LA-" + (getResult().getType() == NetResult.TYPE_ERROR_LOCAL ? "L" : "R"), null);
						}
					}
				}, 300);
				icon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out));
				icon.setVisibility(View.INVISIBLE);
			}

			@Override
			public void error() {
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_login) + "\n" + getResult().getMsg(), "login/LA-" + (getResult().getType() == NetResult.TYPE_ERROR_LOCAL ? "L" : "R"), null);
				v.setEnabled(true);
				stopLoading();
			}
		};
		NetManager.add(request);
	}

	private void signIn(final View v, final String email, final String password) {
		startLoading();

		if (email.equals("set_ip") && BuildConfig.DEBUG) {
			NetManager.setApiServer(password);
			PreferenceManager.putString(getApplicationContext(), PreferenceManager.GROUP_API, PreferenceManager.KEY_SERVER_IP, password);
			SimpleDialog.buildMessageDialog(activity, getString(R.string.debug_server_ip), String.format(getString(R.string.debug_server_ip_set), password), "", null);
			stopLoading();
			return;
		}

		NetRequest request = new NetRequest(NetManager.getApiServer() + NetManager.API_AUTH, NetManager.POST) {
			@Override
			public void success() {
				try {
					JSONObject jsonObject = new JSONObject(getResult().getMsg());
					int id = jsonObject.optInt("id");
					PreferenceManager.putString(getApplicationContext(), PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_EMAIL, email);
					PreferenceManager.putInt(getApplicationContext(), PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_USER_ID, id);
					apiGetUser(v, jsonObject.optInt("id"));
				} catch (JSONException e) {
					e.printStackTrace();
					SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_login), "json/LA-L", null);
					v.setEnabled(true);
					stopLoading();
				}
			}

			@Override
			public void error() {
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_login) + "\n" + getResult().getMsg(), "login/LA-" + (getResult().getType() == NetResult.TYPE_ERROR_LOCAL ? "L" : "R"), null);
				v.setEnabled(true);
				stopLoading();
			}
		};

		request.putParam("username", email);
		request.putParam("password", password);
		request.setLogin(true, getApplicationContext());
		NetManager.add(request);
		v.setEnabled(false);
	}

	private void signUp(final View v) {
		startLoading();

		if (!checkEmail()) {
			return;
		} else if (!checkPassword()) {
			return;
		} else if (!checkPhone()) {
			return;
		}

		final String email = ((EditText) findViewById(R.id.sign_up_email)).getText().toString();
		final String password = ((EditText) findViewById(R.id.sign_up_pass)).getText().toString();
		String firstName = ((EditText) findViewById(R.id.sign_up_name)).getText().toString();
		String lastName = ((EditText) findViewById(R.id.sign_up_lastname)).getText().toString();
		String phone = ((EditText) findViewById(R.id.sign_up_phone)).getText().toString();

		NetRequest request = new NetRequest(NetManager.getApiServer() + NetManager.API_USER_CREATE, NetManager.POST) {
			@Override
			public void success() {
				signIn(v, email, password);
				stopLoading();
			}

			@Override
			public void error() {
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getResult().getMsg(), "user_create/LA-" + (getResult().getType() == NetResult.TYPE_ERROR_LOCAL ? "L" : "R"), null);
				v.setEnabled(true);
				stopLoading();
			}
		};
		request.putParam("is_admin", false);
		request.putParam("email", email);
		request.putParam("password", password);
		request.putParam("first_name", firstName);
		request.putParam("last_name", lastName);
		request.putParam("phone", phone);
		request.putNull("picture");
		request.putNull("benefit_discount");
		request.putNull("benefit_requirement");
		NetManager.add(request);
		v.setEnabled(false);
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
