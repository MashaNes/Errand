package runners.errand.ui.findrequests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.model.Address;
import runners.errand.model.Request;
import runners.errand.model.Service;
import runners.errand.model.User;
import runners.errand.utils.Static;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class FindRequestsFragment extends Fragment {
    private MainActivity activity;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ViewPager pager;
    private ArrayList<Request> requestsDirect = new ArrayList<>();
    private ArrayList<Request> requestsBroadcast = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_findrequests, container, false);

        activity = ((MainActivity) getActivity());
        if (activity == null) return root;
        activity.setFragment(this);

        fragments.clear();
        fragments.add(new FilterFragment());
        fragments.add(new FRListFragment(1));
        fragments.add(new FRListFragment(2));

        pager = root.findViewById(R.id.view_pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(
                getChildFragmentManager(),
                fragments
        );
        pager.setAdapter(adapter);

        activity.setupTabs(pager, new int[] {
                R.drawable.ic_settings_white,
                R.drawable.ic_broadcast,
                R.drawable.ic_direct
        });

        loadRequests();

        return root;
    }

    ArrayList<Request> getDirectRequests() {
        return requestsDirect;
    }
    ArrayList<Request> getBroadcastRequests() {
        return requestsBroadcast;
    }

    void loadRequests() {
        NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_SEARCH_REQUESTS, NetManager.POST) {
            @Override
            public void success() {
                try {
                    JSONObject o = new JSONObject(getResult().getMsg());
                    JSONArray direct = o.optJSONArray("direct");
                    if (direct != null) {
                        requestsDirect.clear();
                        for (int i = 0; i < direct.length(); i++) {
                            requestsDirect.add(new Request(direct.optJSONObject(i)));
                        }
                    }
                    JSONArray broadcast = o.optJSONArray("broadcast");
                    if (broadcast != null) {
                        requestsBroadcast.clear();
                        for (int i = 0; i < broadcast.length(); i++) {
                            requestsBroadcast.add(new Request(broadcast.optJSONObject(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ((FRListFragment) fragments.get(1)).dataSetChanged();
                ((FRListFragment) fragments.get(2)).dataSetChanged();
                super.success();
            }

            @Override
            public void error() {
                super.error();
            }
        };
        netRequest.putParam("created_by", activity.getUser().getId());
        JSONArray services = new JSONArray();
        for (Service service : ((FilterFragment) fragments.get(0)).getServices()) {
            services.put(service.getId());
        }
        netRequest.putParam("services", services);
        netRequest.putParam("min_rating", ((FilterFragment) fragments.get(0)).getMinRating());
        netRequest.putParam("no_rating", true);
        if (activity.getUser().getStatus() == User.STATUS_RUNNING) {
            netRequest.putParam("max_dist", ((FilterFragment) fragments.get(0)).getMaxDistance());
            netRequest.putParam("latitude", Static.lat);
            netRequest.putParam("longitude", Static.lng);
        } else {
            Address home = null;
            for (Address address : activity.getUser().getAddresses()) {
                if (address.isHome()) {
                    home = address;
                    break;
                }
            }
            if (home != null) {
                netRequest.putParam("max_dist", ((FilterFragment) fragments.get(0)).getMaxDistance());
                netRequest.putParam("latitude", home.getLat());
                netRequest.putParam("longitude", home.getLng());
            } else {
                netRequest.putNull("max_dist");
                netRequest.putNull("latitude");
                netRequest.putNull("longitude");
            }
        }
        NetManager.add(netRequest);
    }

    MainActivity getMainActivity() {
        return activity;
    }

    void navigate() {
        pager.setCurrentItem(1, true);
    }
}
