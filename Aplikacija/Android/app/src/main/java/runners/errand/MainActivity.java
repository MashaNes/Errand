package runners.errand;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import java.sql.Date;
import java.util.ArrayList;

import runners.errand.model.Achievement;
import runners.errand.model.Notification;
import runners.errand.model.Rating;
import runners.errand.model.Request;
import runners.errand.model.Service;
import runners.errand.model.Task;
import runners.errand.model.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int TAB_ICON_TRANSPARENCY = 125;

    private NavController navController;
	private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private TabLayout tabs;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        user = new User(
                0,
                User.STATUS_NOT_RUNNING,
                5,
                "Test",
                "Name",
                "testemail@testing.com",
                "+38154321234",
                null,
                3.4f,
                3.5f,
                4f,
                10f
        );

        mockRatingData();
        mockAchievementData();
        mockRequestData();
        mockNotificationData();
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
			// TODO: Logout
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                user.setStatus(user.getStatus() == User.STATUS_RUNNING ? User.STATUS_NOT_RUNNING : User.STATUS_RUNNING);
                updateMenuItemRun(item);
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

    public void navigateTo(int id) {
        navController.navigate(id);
    }

	public void navigateTo(int id, Bundle args) {
		navController.navigate(id, args);
	}

    private void mockRatingData() {
        ArrayList<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating(
                0,
                4,
                1,
                0,
                0,
                "Good job",
                "First",
                "Last",
				null
        ));
        ratings.add(new Rating(
                1,
                3,
                2,
                0,
                1,
                "Very slow! otherwise good",
                "Chase",
                "Royce",
				null
        ));
        ratings.add(new Rating(
                2,
                5,
                3,
                0,
                2,
                "",
                "Melissa",
                "Rhodes",
				null
        ));
        ratings.add(new Rating(
                3,
                4,
                4,
                0,
                3,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis vel ex mi. Nunc ornare nisl nibh, ac mattis ipsum suscipit ac. Donec dictum feugiat nunc in consectetur. Aliquam a ante molestie, viverra elit nec, dictum magna. Nullam pellentesque vestibulum dui, vel sollicitudin tortor dapibus quis.",
                "Linsey",
                "Ingram",
				null
        ));
        user.setRatings(ratings);
    }

    private void mockAchievementData() {
        ArrayList<Achievement> achievements = new ArrayList<>();

        achievements.add(new Achievement(
                0,
                1,
                "Achievement A",
                "Description of Achievement A."
        ));

        achievements.add(new Achievement(
                1,
                3,
                "Achievement B",
                "Etiam vitae ante sit amet mi gravida pretium. Duis ut risus hendrerit lectus vulputate tincidunt vitae sit amet sem."
        ));

		achievements.add(new Achievement(
				2,
				0,
				"Achievement C",
				"ASd dih sadashd asdkajs dasdasdoias r r rsk sfahs fash sa."
		));

        user.setAchievements(achievements);
    }

	private void mockRequestData() {
		ArrayList<Task> tasksGroceries = new ArrayList<>();
		tasksGroceries.add(new Task(0, "Go to grocery store.", null));
		Service serviceGroceries = new Service(0, "Groceries", "", false);
		serviceGroceries.setTasks(tasksGroceries);

		ArrayList<Task> tasksDelivery = new ArrayList<>();
		tasksDelivery.add(new Task(0, "Pick up these items.", null));
		tasksDelivery.add(new Task(1, "Deliver them to given address.", null));
		Service serviceDelivery = new Service(1, "Delivery", "", false);
		serviceDelivery.setTasks(tasksDelivery);

		ArrayList<Request> requests = new ArrayList<>();

		requests.add(new Request(
				1,
				Request.STATUS_ACTIVE,
				0,
				new Date(new java.util.Date().getTime()),
				"Deliver a package",
				"",
				false,
				serviceDelivery
		));

		requests.add(new Request(
				1,
				Request.STATUS_PENDING,
				0,
				new Date(new java.util.Date().getTime() + 5000000),
				"Grocery shopping",
				"",
				false,
				serviceGroceries
		));

		requests.add(new Request(
				1,
				Request.STATUS_COMPLETED,
				0,
				new Date(new java.util.Date().getTime() - 500000000),
				"Delivery",
				"",
				false,
				serviceDelivery
		));

		requests.add(new Request(
				1,
				Request.STATUS_CANCELED,
				0,
				new Date(new java.util.Date().getTime() - 750000000),
				"Groceries",
				"",
				false,
				serviceGroceries
		));

		user.setRequests(requests);
	}

	private void mockNotificationData() {
		ArrayList<Notification> notifications = new ArrayList<>();

		notifications.add(new Notification(
				Notification.CATEGORY_REQUEST,
				"New offer",
				"Your request \"Grocery shopping\" got a new offer from Bob.",
				new Date(new java.util.Date().getTime())
		));

		notifications.add(new Notification(
				Notification.CATEGORY_REQUEST,
				"Accepted",
				"Your direct request \"Delivery\" got accepted by Firstname.",
				new Date(new java.util.Date().getTime())
		));

		notifications.add(new Notification(
				Notification.CATEGORY_REQUEST,
				"Edit requested",
				"Testname sent you an edit request regarding \"Cleaning\".",
				new Date(new java.util.Date().getTime())
		));

		notifications.add(new Notification(
				Notification.CATEGORY_RUNNING,
				"Offer accepted",
				"Your offer for \"Groceries\" has been accepted.",
				new Date(new java.util.Date().getTime())
		));

		notifications.add(new Notification(
				Notification.CATEGORY_RUNNING,
				"Edit request denied",
				"Your edit request for \"Laptop repairs\" has been denied.",
				new Date(new java.util.Date().getTime())
		));

		notifications.add(new Notification(
				Notification.CATEGORY_RATING,
				"New rating",
				"Bob rated you 5 stars and said: \"Great job!\".",
				new Date(new java.util.Date().getTime())
		));

		notifications.add(new Notification(
				Notification.CATEGORY_ACHIEVEMENT,
				"Achievement unlocked",
				"You have unlocked the \"Experienced runner\" achievement. Congratulations!",
				new Date(new java.util.Date().getTime())
		));

		user.setNotifications(notifications);
	}
}
