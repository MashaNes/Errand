package runners.errand.ui.requests;

import android.os.Bundle;
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

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.model.Request;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class RequestsFragment extends Fragment {
    private SwipeRefreshLayout refreshLayout;
    private MainActivity activity;

    private ArrayList<Fragment> fragments = new ArrayList<>();

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        activity = (MainActivity) getActivity();
        if (activity == null) return root;

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
                refresh();
            }
        });

        boolean refresh = false;
        if (savedInstanceState != null) refresh = savedInstanceState.getBoolean("refresh");
        if (refresh) {
            refresh();
        }

        return root;
    }

    private void refresh() {
        NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUESTS, NetManager.GET) {
            @Override
            public void success() {
                try {
                    JSONObject result = new JSONObject(getResult().getMsg());
                    JSONArray data = result.optJSONArray("results");
                    if (data != null) {
                        activity.getUser().getRequests().clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject request = data.optJSONObject(i).optJSONObject("request");
                            if (request != null) activity.getUser().getRequests().add(new Request(request));
                        }
                        for (Fragment fragment : getChildFragmentManager().getFragments()) {
                            ((RequestsListFragment) fragment).dataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refreshLayout.setRefreshing(false);
                super.success();
            }

            @Override
            public void error() {
                refreshLayout.setRefreshing(false);
                super.error();
            }
        };
        NetManager.add(netRequest);
    }
}
