package runners.errand.ui.requests;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.model.Request;
import runners.errand.ui.request.StatusFragment;
import runners.errand.utils.Static;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class RequestsFragment extends Fragment {
    public static final String EXTRA_REFRESH_ALL = "runners.errand.ui.requests.EXTRA_REFRESH_ALL";
    public static final String EXTRA_REFRESH_BY_ID = "runners.errand.ui.requests.EXTRA_REFRESH_BY_ID";
    public static final String EXTRA_REFRESH_TAB = "runners.errand.ui.requests.EXTRA_REFRESH_TAB";

    private SwipeRefreshLayout refreshLayout;
    private MainActivity activity;

    private ArrayList<Fragment> fragments = new ArrayList<>();

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        activity = (MainActivity) getActivity();
        if (activity == null) return root;
        activity.setFragment(this);

        refreshLayout = ((SwipeRefreshLayout) root);

        fragments.clear();
        for (int i = 0; i < 3; i++)
			fragments.add(new RequestsListFragment(i));

        ViewPager pager = root.findViewById(R.id.view_pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(
                getChildFragmentManager(),
                fragments
        );
        pager.setAdapter(adapter);

        activity.setupTabs(pager, new int[] {
                R.drawable.ic_sit,
                R.drawable.ic_walk,
                R.drawable.ic_history
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activity.apiGetRequests();
            }
        });

        boolean refresh = false;
        if (savedInstanceState != null) refresh = savedInstanceState.getBoolean("refresh", false);
        if (refresh) {
            refreshLayout.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                    activity.apiGetRequests();
                }
            }, 500);
        }

        int id = -1;
        if (savedInstanceState != null) id = savedInstanceState.getInt(EXTRA_REFRESH_BY_ID, -1);
        if (id != -1) {
            refreshLayout.setRefreshing(true);
            activity.apiGetRequest(id);
        } else if (Static.requests_id != Integer.MIN_VALUE) {
            refreshLayout.setRefreshing(true);
            activity.apiGetRequest(Static.requests_id);
            Static.requests_id = Integer.MIN_VALUE;
        }

        int tab = -1;
        if (savedInstanceState != null) tab = savedInstanceState.getInt(EXTRA_REFRESH_TAB, -1);
        if (tab > -1 && tab < 3) {
            pager.setCurrentItem(tab, true);
        } else if (Static.requests_tab > -1 && Static.requests_tab < 3) {
            pager.setCurrentItem(Static.requests_tab, true);
            Static.requests_tab = Integer.MIN_VALUE;
        }

        if (activity.getUser().getRequests() == null || activity.getUser().getRequests().isEmpty()) {
            refreshLayout.setRefreshing(true);
            activity.apiGetRequests();
        }

        return root;
    }

    public void clearRequests() {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            ((RequestsListFragment) fragment).clearRequests();
        }
    }

    public void doneLoading() {
        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
    }

    public void loadData() {
	    if (!isAdded()) return;
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            ((RequestsListFragment) fragment).dataSetChanged();
        }
    }
}
