package runners.errand;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import runners.errand.model.Service;
import runners.errand.model.User;
import runners.errand.utils.Static;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.PreferenceManager;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;
import runners.errand.utils.net.NetResult;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	public static final int PERMISSION_LOCATION = 0, PERMISSION_STORAGE = 1;
    private static final int TAB_ICON_TRANSPARENCY = 125, PERMISSION_REQUEST_CALLBACK_ID = 1;

    private NavController navController;
	private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private TabLayout tabs;
	private Toolbar toolbar;

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

        // TODO: For notification badges
        // To change a menu icon at runtime
        // navigationView.getMenu().findItem(R.id.nav_page_notifications).setIcon(R.drawable.ic_achievements);
        // To change the nav menu icon in the toolbar at runtime
        // toolbar.setNavigationIcon(R.drawable.ic_achievements);

		user = Static.user;
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
			NetManager.clearToken(activity);
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}

        return false;
    }

	public boolean permissionsGranted(int permission) {
    	switch (permission) {
			case PERMISSION_LOCATION:
				return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
			case PERMISSION_STORAGE:
				return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
			default:
				return false;
		}
	}

	public boolean permissionsRequireExplanation(int permission) {
    	switch (permission) {
			case PERMISSION_LOCATION:
				return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
			case PERMISSION_STORAGE:
				return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
			default:
				return false;
		}
	}

	public void permissionsRequest(int permission, int callbackId) {
    	switch (permission) {
			case PERMISSION_LOCATION:
				ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, callbackId);
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
						LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
						if (locationManager != null) {
							String provider = locationManager.getBestProvider(new Criteria(), true);
							if (provider != null && (provider.equals(LocationManager.GPS_PROVIDER) || provider.equals(LocationManager.NETWORK_PROVIDER))) {
								Log.e("LM", provider);
								user.setStatus(user.getStatus() == User.STATUS_RUNNING ? User.STATUS_NOT_RUNNING : User.STATUS_RUNNING);
								updateMenuItemRun(item);
								return;
							}
						}
						SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_location_required), "lm/MA-L", null);
					}
				});
				return false;
            }
        });

        updateMenuItemRun(menu.getItem(0));
        return true;
    }

    private void updateMenuItemRun(MenuItem item) {
        if (user.getStatus() == User.STATUS_RUNNING) {
            item.getIcon().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen), PorterDuff.Mode.MULTIPLY));
            item.setTitle(R.string.action_run_stop);
        } else {
            item.getIcon().clearColorFilter();
            item.setTitle(R.string.action_run_start);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void noTabs() {
        tabs.setVisibility(View.GONE);
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

    public ArrayList<Service> getServices() { return services; }

    public void navigateTo(int id) {
        navController.navigate(id);
    }

	public void navigateTo(int id, Bundle args) {
		navController.navigate(id, args);
	}
}
