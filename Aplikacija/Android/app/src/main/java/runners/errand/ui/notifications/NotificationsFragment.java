package runners.errand.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.model.Notification;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class NotificationsFragment extends Fragment {
    private MainActivity activity;
    private ArrayList<Fragment> fragments = new ArrayList<>();

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        activity = ((MainActivity) getActivity());
        if (activity == null) return root;
        activity.setFragment(this);

        updateSeen();

        fragments.clear();
        for (int i = 0; i < 3; i++)
            fragments.add(new NotificationsListFragment(i));

        ViewPager pager = root.findViewById(R.id.view_pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(
                getChildFragmentManager(),
                fragments
        );
        pager.setAdapter(adapter);

        activity.setupTabs(pager, new int[] {
                R.drawable.ic_sit,
                R.drawable.ic_walk,
                R.drawable.ic_other
        });

        return root;
    }

    public void loadData() {
	    for (Fragment fragment : fragments) {
            ((NotificationsListFragment) fragment).loadData();
        }
    }

    private void updateSeen() {
        final JSONArray ids = new JSONArray();
	    for (Notification notification : activity.getUser().getNotifications())
	        if (!notification.isSeen()) ids.put(notification.getId());
        if (ids.length() == 0) return;
        NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_NOTIFICATIONS_FLAG_UPDATE, NetManager.PUT) {
            @Override
            public void success() {
                for (Notification notification : activity.getUser().getNotifications()) {
                    for (int i = 0; i < ids.length(); i++) {
                        if (notification.getId() == ids.optInt(i)) notification.setSeen(true);
                    }
                }
                activity.updateNotificationCount();
                loadData();
                super.success();
            }

            @Override
            public void error() {
                super.error();
            }
        };
        netRequest.putParam("ids", ids);
        netRequest.putParam("seen", true);
        netRequest.putParam("opened", false);
        NetManager.add(netRequest);
    }
}
