package runners.errand;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import runners.errand.firebase.MessagingService;
import runners.errand.model.Achievement;
import runners.errand.model.Notification;
import runners.errand.model.Offer;
import runners.errand.model.Rating;
import runners.errand.model.Request;
import runners.errand.model.Service;
import runners.errand.model.User;
import runners.errand.ui.notifications.NotificationsFragment;
import runners.errand.ui.profile.ProfileFragment;
import runners.errand.ui.request.RequestFragment;
import runners.errand.ui.requests.RequestsFragment;
import runners.errand.location.LocationUtils;
import runners.errand.utils.Static;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;
import runners.errand.utils.net.NetResult;
import runners.errand.location.LocationService;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	public static final int PERMISSION_LOCATION = 0, PERMISSION_STORAGE = 1;
    private static final int TAB_ICON_TRANSPARENCY = 125, PERMISSION_REQUEST_CALLBACK_ID = 1;

    private NavController navController;
	private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private TabLayout tabs;
	private Toolbar toolbar;
	private Fragment fragment;
	private String fcmToken;
	private int pendingOfferRequestCounter = 0;
	private boolean doneLoadingOffers = true;

	private MenuItem statusMenuItem;

	private Activity activity;
	public boolean active = false;

	private User user;
	private ArrayList<Service> services = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

		apiGetServices();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getIcon() != null) tab.getIcon().setAlpha(255);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getIcon() != null) tab.getIcon().setAlpha(TAB_ICON_TRANSPARENCY);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_page_requests,
                R.id.nav_page_newrequest,
                R.id.nav_page_findrequests,
                R.id.nav_page_notifications,
                R.id.nav_page_profile,
                R.id.nav_page_settings)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

		user = Static.user;
//		loadOfferRequests();

		FirebaseInstanceId.getInstance().getInstanceId()
				.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
					@Override
					public void onComplete(@NonNull Task<InstanceIdResult> task) {
						if (!task.isSuccessful() || task.getResult() == null) {
							Log.w("FCM", "getInstanceId failed", task.getException());
							return;
						}

						// Get new Instance ID token
						fcmToken = task.getResult().getToken();

						NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_FCM_REGISTER, NetManager.POST);
						netRequest.putParam("created_by", Static.user.getId());
						netRequest.putParam("dev_id", 123);
						netRequest.putParam("reg_id", fcmToken);
						NetManager.add(netRequest);
					}
				});

		// Location service broadcast receiver
		LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				int newStatus = intent.getIntExtra(LocationService.ACTION_BROADCAST_EXTRA_STATUS, User.STATUS_NOT_RUNNING);
				if (newStatus != user.getStatus()) {
					user.setStatus(newStatus);
					updateMenuItemRun(false, false);
				}
			}
		}, new IntentFilter(LocationService.ACTION_BROADCAST_STATUS_CHANGED));

		// Notification broadcast receiver
		LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				user.getNotifications().add(0, new Notification(
						intent.getIntExtra(MessagingService.NOTIFICATION_EXTRA_ID, -1),
						intent.getIntExtra(MessagingService.NOTIFICATION_EXTRA_TYPE_ID, -1),
						intent.getIntExtra(MessagingService.NOTIFICATION_EXTRA_CATEGORY, -1),
						intent.getStringExtra(MessagingService.NOTIFICATION_EXTRA_TITLE_EN),
						intent.getStringExtra(MessagingService.NOTIFICATION_EXTRA_BODY_EN),
						intent.getStringExtra(MessagingService.NOTIFICATION_EXTRA_TITLE_SR),
						intent.getStringExtra(MessagingService.NOTIFICATION_EXTRA_BODY_SR),
						intent.getStringExtra(MessagingService.NOTIFICATION_EXTRA_TIME)
				));

				if (fragment instanceof NotificationsFragment) {
					((NotificationsFragment) fragment).loadData();
				} else {
					updateNotificationCount();
				}

				int category = intent.getIntExtra(MessagingService.NOTIFICATION_EXTRA_CATEGORY, -1);

				if (category == Notification.CATEGORY_ACHIEVEMENT) {
					NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USERS_INFO_FILTERED, NetManager.POST) {
						@Override
						public void success() {
							super.success();
							try {
								JSONObject o = new JSONObject(getResult().getMsg());
								JSONArray results = o.optJSONArray("results");
								if (results != null) {
									user.getAchievements().clear();
									for (int i = 0; i < results.length(); i++) {
										JSONObject achievement = results.optJSONObject(i).optJSONObject("achievement");
										if (achievement != null) user.getAchievements().add(new Achievement(achievement));
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (fragment instanceof ProfileFragment) {
								((ProfileFragment) fragment).loadAchievements();
							}
						}

						@Override
						public void error() {
							super.error();
						}
					};
					netRequest.putParam("created_by", user.getId());
					netRequest.putParam("blocked", false);
					netRequest.putParam("working_hours", false);
					netRequest.putParam("addresses", false);
					netRequest.putParam("services", false);
					netRequest.putParam("offers", false);
					netRequest.putParam("notifications", false);
					netRequest.putParam("ratings", false);
					netRequest.putParam("benefitlist", false);
					netRequest.putParam("achievements", true);
					netRequest.putParam("requests", false);
					NetManager.add(netRequest);
				} else if (category == Notification.CATEGORY_RATING) {
					NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USERS_INFO_FILTERED, NetManager.POST) {
						@Override
						public void success() {
							super.success();
							try {
								JSONObject o = new JSONObject(getResult().getMsg());
								JSONArray results = o.optJSONArray("results");
								if (results != null) {
									user.getRatings().clear();
									for (int i = 0; i < results.length(); i++) {
										JSONObject rating = results.optJSONObject(i);
										if (rating != null) user.getRatings().add(new Rating(rating));
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (fragment instanceof ProfileFragment) {
								((ProfileFragment) fragment).loadAchievements();
							}
						}

						@Override
						public void error() {
							super.error();
						}
					};
					netRequest.putParam("created_by", user.getId());
					netRequest.putParam("blocked", false);
					netRequest.putParam("working_hours", false);
					netRequest.putParam("addresses", false);
					netRequest.putParam("services", false);
					netRequest.putParam("offers", false);
					netRequest.putParam("notifications", false);
					netRequest.putParam("ratings", true);
					netRequest.putParam("benefitlist", false);
					netRequest.putParam("achievements", false);
					netRequest.putParam("requests", false);
					NetManager.add(netRequest);
				} else if (
						category == Notification.CATEGORY_REQUEST_SUCCESS ||
						category == Notification.CATEGORY_REQUEST_FAILED ||
						category == Notification.CATEGORY_EDIT_CANCELED ||
						category == Notification.CATEGORY_OFFER_ACCEPTED ||
						category == Notification.CATEGORY_OFFER_CREATED ||
						category == Notification.CATEGORY_EDIT_ACCEPTED ||
						category == Notification.CATEGORY_EDIT_CREATED
				) {
					int id = intent.getIntExtra(MessagingService.NOTIFICATION_EXTRA_TYPE_ID, -1);
					if (id != -1) apiGetRequest(id);
				} else if (category == 11) { // Banned
					apiSetStatus(User.STATUS_NOT_RUNNING);
					apiFCMUnregister();
				}
			}
		}, new IntentFilter(MessagingService.ACTION_BROADCAST_NOTIFICATION));

		updateNotificationCount();
//		loadOfferRequests();
//		apiGetRequests();
    }

	@Override
	public void setTitle(CharSequence title) {
		toolbar.setTitle(title);
	}

	@Override
	public void setTitle(int titleId) {
		toolbar.setTitle(titleId);
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

	@Override
	protected void onStart() {
		super.onStart();
//		GeofencingBroadcastReceiver.addGeofence(this, 0, 0, 43.3176378, 21.9250031);
	}

	@Override
	protected void onStop() {
		super.onStop();
//		GeofencingBroadcastReceiver.removeGeofence(this, 0, 0);
	}

	@Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
				drawer.closeDrawer(GravityCompat.START);
            }
        }, 50);

		if (item.getItemId() != R.id.nav_page_logout) {
			navigationView.setCheckedItem(item.getItemId());
			navController.navigate(item.getItemId());
		} else {
			apiSetStatus(User.STATUS_NOT_RUNNING);
			apiFCMUnregister();
		}
        return false;
    }

    public void updateNotificationCount() {
		// To change a menu icon at runtime
//        navigationView.getMenu().findItem(R.id.nav_page_notifications).setIcon(R.drawable.ic_notification_unread);
		// To change menu item title
//		navigationView.getMenu().findItem(R.id.nav_page_notifications).setTitle("test");
		// To change the nav menu icon in the toolbar at runtime
//		toolbar.setNavigationIcon(R.drawable.ic_menu_unread);

    	int notificationCount = 0;
    	for (Notification notification : user.getNotifications()) {
    		if (!notification.isSeen()) notificationCount++;
		}
    	if (notificationCount > 0) {
    		String s = getString(R.string.menu_notifications) + " +" + notificationCount;
    		navigationView.getMenu().findItem(R.id.nav_page_notifications).setTitle(s);
		} else {
			navigationView.getMenu().findItem(R.id.nav_page_notifications).setTitle(R.string.menu_notifications);
		}
	}

	public boolean permissionsGranted(int permission) {
    	switch (permission) {
			case PERMISSION_LOCATION:
				boolean res = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					res = res && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
				}
				return res;
			case PERMISSION_STORAGE:
				return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
			default:
				return false;
		}
	}

	public boolean permissionsRequireExplanation(int permission) {
    	switch (permission) {
			case PERMISSION_LOCATION:
				boolean res = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					res = res || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
				}
				return res;
			case PERMISSION_STORAGE:
				return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
			default:
				return false;
		}
	}

	public void permissionsRequest(int permission, int callbackId) {
    	switch (permission) {
			case PERMISSION_LOCATION:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION }, callbackId);
				} else {
					ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, callbackId);
				}
				break;
			case PERMISSION_STORAGE:
				ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, callbackId);
				break;
		}
	}

	public void permissionsRun(Runnable r) {
		if (!permissionsGranted(PERMISSION_LOCATION)) {
			if (permissionsRequireExplanation(PERMISSION_LOCATION)) {
				SimpleDialog.buildSelectDialog(activity, getString(R.string.permissions), getString(R.string.permissions_location_runner), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
					@Override
					public void run() {
						permissionsRequest(PERMISSION_LOCATION, PERMISSION_REQUEST_CALLBACK_ID);
					}
				}, null);
			} else {
				permissionsRequest(PERMISSION_LOCATION, PERMISSION_REQUEST_CALLBACK_ID);
			}
		} else {
			r.run();
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
				permissionsRun(new Runnable() {
					@Override
					public void run() {
						becomeActive();
					}
				});
				return false;
            }
        });
		statusMenuItem = menu.getItem(0);
        updateMenuItemRun(false, true);
        return true;
    }

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		if (manager != null) {
			for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
				if (LocationRequest.class.getName().equals(service.service.getClassName())) {
					return true;
				}
			}
		}
		return false;
	}

	public void becomeActive() {
		if (user.getStatus() == User.STATUS_RUNNING) {
			user.setStatus(User.STATUS_NOT_RUNNING);
			updateMenuItemRun(true, true);
		} else {
			LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager != null) {
				String provider = locationManager.getBestProvider(new Criteria(), true);
				if (provider != null && (provider.equals(LocationManager.GPS_PROVIDER) || provider.equals(LocationManager.NETWORK_PROVIDER))) {
					Log.e("LM", provider);
					user.setStatus(User.STATUS_RUNNING);
					updateMenuItemRun(true, true);
					return;
				}
			}
			SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_location_required), "lm/MA-L", null);
		}
	}

    private void updateMenuItemRun(boolean update, boolean service) {
    	if (statusMenuItem == null) return;
    	if (update) {
			apiSetStatus(user.getStatus());
		} else {
			if (user.getStatus() == User.STATUS_RUNNING) {
				statusMenuItem.getIcon().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen), PorterDuff.Mode.MULTIPLY));
				statusMenuItem.setTitle(R.string.action_run_stop);
				if (!isMyServiceRunning() && service) {
					LocationUtils.becomeActive(this, user.getId());
				}
			} else {
				statusMenuItem.getIcon().clearColorFilter();
				statusMenuItem.setTitle(R.string.action_run_start);
				LocationUtils.becomeInactive(this);
			}
		}
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void setupTabs(ViewPager pager, int[] icons) {
        tabs.setVisibility(View.VISIBLE);
        tabs.setupWithViewPager(pager, true);
        for (int i = 0; i < tabs.getTabCount() && i < icons.length; i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            if (tab != null) {
                tab.setIcon(ContextCompat.getDrawable(getApplicationContext(), icons[i]));
                if (tab.getIcon() != null && i > 0) tab.getIcon().setAlpha(TAB_ICON_TRANSPARENCY);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) { this.user = user; }

    private void apiGetServices() {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_SERVICES, NetManager.GET) {
			@Override
			public void success() {
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					JSONArray a = o.optJSONArray("results");
					services.clear();
					for (int i = 0; i < (a != null ? a.length() : 0); i++) {
						services.add(0, new Service(a.getJSONObject(i)));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "service/MA-" + (getResult().getType() == NetResult.TYPE_ERROR_LOCAL ? "L" : "R"), null);
				}
			}

			@Override
			public void error() {
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "service/MA-" + (getResult().getType() == NetResult.TYPE_ERROR_LOCAL ? "L" : "R"), null);
			}
		};
		NetManager.add(netRequest);
	}

	private void apiSetStatus(int status) {
    	NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USER_STATUS_UPDATE, NetManager.PUT) {
			@Override
			public void success() {
				try {
					if (new JSONObject(getResult().getMsg()).optString("detail").equals("success")) {
						updateMenuItemRun(false, true);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.success();
			}

			@Override
			public void error() {
				super.error();
			}
		};
    	netRequest.putParam("created_by", user.getId());
    	netRequest.putParam("status", status);
    	NetManager.add(netRequest);
	}

	public void loadOfferRequests() {
    	doneLoadingOffers = false;
    	NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USERS_INFO_FILTERED, NetManager.POST) {
			@Override
			public void success() {
				super.success();
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					JSONArray results = o.optJSONArray("results");
					if (results != null) {
						user.getOffers().clear();
						for (int i = 0; i < results.length(); i++) {
							user.getOffers().add(new Offer(results.optJSONObject(i)));
							apiGetRequest(user.getOffers().get(i));
						}
						doneLoadingOffers = true;
						if (pendingOfferRequestCounter == 0 && fragment instanceof RequestsFragment) {
							((RequestsFragment) fragment).loadData();
							((RequestsFragment) fragment).doneLoading();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void error() {
				super.error();
			}
		};
    	netRequest.putParam("created_by", user.getId());
		netRequest.putParam("blocked", false);
		netRequest.putParam("working_hours", false);
		netRequest.putParam("addresses", false);
		netRequest.putParam("services", false);
		netRequest.putParam("offers", true);
		netRequest.putParam("notifications", false);
		netRequest.putParam("ratings", false);
		netRequest.putParam("benefitlist", false);
		netRequest.putParam("achievements", false);
		netRequest.putParam("requests", false);
    	NetManager.add(netRequest);
	}

	public void apiGetRequests() {
		if (fragment instanceof RequestsFragment) {
			((RequestsFragment) fragment).clearRequests();
		}
		user.getRequests().clear();
    	NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_FILTERED_REQUESTS, NetManager.POST) {
			@Override
			public void success() {
				super.success();
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					JSONArray results = o.optJSONArray("results");
					if (results != null && results.length() > 0) {
						for (int i = 0; i < results.length(); i++) {
							JSONObject request = results.optJSONObject(i);
							if (request != null) user.getRequests().add(new Request(request));
						}
					}
//					if (fragment instanceof RequestsFragment) ((RequestsFragment) fragment).loadData();
					loadOfferRequests();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void error() {
				super.error();
			}
		};
// {
//	"created_by" : null,
//	"done_by" : 2,
//	"created_or_done_by": null,
//	"statuses" : null,
//	"unrated_created_by" : false,
//	"unrated_done_by" : false
//}
		netRequest.putParam("unrated_created_by", false);
		netRequest.putParam("unrated_done_by", false);
		netRequest.putParam("created_or_done_by", user.getId());
		netRequest.putNull("created_by");
		netRequest.putNull("done_by");
		netRequest.putNull("statuses");
    	NetManager.add(netRequest);
	}

	public void apiGetRequest(final Offer offer) {
    	for (Request r : user.getRequests()) {
    		if (r.getId() == offer.getRequest()) {
    			r.setMyOffer(offer);
//				if (fragment instanceof RequestsFragment) {
//					((RequestsFragment) fragment).loadData();
//				}
				return;
			}
		}
    	pendingOfferRequestCounter++;
		Log.e("OFFERS", "Request: " + offer.getRequest());
    	NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUESTS + offer.getRequest() + "/", NetManager.GET) {
			@Override
			public void success() {
				super.success();
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					JSONObject request = o.optJSONObject("request");
					if (request != null) {
						Request r = new Request(request);
						r.setMyOffer(offer);
						user.getRequests().add(r);
//						user.getRunning().add(r);
//						if (fragment instanceof RequestsFragment) {
//							((RequestsFragment) fragment).loadData(r);
//						}
					}
					pendingOfferRequestCounter--;
					if (pendingOfferRequestCounter == 0 && doneLoadingOffers && fragment instanceof RequestsFragment) {
						((RequestsFragment) fragment).loadData();
						((RequestsFragment) fragment).doneLoading();
					}
				} catch (JSONException e) {
					pendingOfferRequestCounter--;
					e.printStackTrace();
				}
			}

			@Override
			public void error() {
				pendingOfferRequestCounter--;
				super.error();
			}
		};
    	NetManager.add(netRequest);
	}

	public void apiGetRequest(int id) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUESTS + id + "/", NetManager.GET) {
			@Override
			public void success() {
				super.success();
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					JSONObject request = o.optJSONObject("request");
					if (request != null) {
						Request r = new Request(request);
						boolean added = false;
						for (int i = 0; i < user.getRequests().size(); i++) {
							if (r.getId() == user.getRequests().get(i).getId()) {
								Offer myOffer = user.getRequests().get(i).getMyOffer();
								user.getRequests().set(i, r);
								user.getRequests().get(i).setMyOffer(myOffer);
								added = true;
								break;
							}
						}
						if (!added) user.getRequests().add(r);
						if (fragment instanceof RequestsFragment) {
							((RequestsFragment) fragment).loadData();
							((RequestsFragment) fragment).doneLoading();
						}
						if (fragment instanceof RequestFragment) {
							((RequestFragment) fragment).loadData(r);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void error() {
				super.error();
			}
		};
		NetManager.add(netRequest);
	}

	private void apiFCMUnregister() {
		LocationUtils.becomeInactive(activity);
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_FCM_UNREGISTER, NetManager.DELETE) {
			@Override
			public void success() {
				super.success();
				NetManager.clearToken(activity);
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
			}

			@Override
			public void error() {
				super.error();
			}
		};
		netRequest.putParam("reg_id", fcmToken);
		NetManager.add(netRequest);
	}

    public ArrayList<Service> getServices() { return services; }

    public void navigateTo(int id) {
        navController.navigate(id);
    }

	public void navigateTo(int id, Bundle args) {
		navController.navigate(id, args);
	}

	public void setFragment(Fragment fragment) { this.fragment = fragment; }

	public Fragment getFragment() { return this.fragment; }
}
