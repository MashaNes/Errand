package runners.errand.ui.newrequest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.CustomPagerAdapter;
import runners.errand.model.Request;
import runners.errand.model.Task;
import runners.errand.ui.requests.RequestsFragment;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class NewRequestFragment extends Fragment {
    private MainActivity activity;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ViewPager pager;
    private Request request = new Request();
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newrequest, container, false);

        activity = ((MainActivity) getActivity());
        if (activity == null) return root;
        activity.setFragment(this);

        fragments.clear();
        fragments.add(new NR1Fragment());
        fragments.add(new NR2Fragment());
        fragments.add(new NR3Fragment());

        request.setMaxDistance(10);
        request.setMinRating(3);
        request.setDirectId(-1);

        pager = root.findViewById(R.id.view_pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(
                getChildFragmentManager(),
                fragments
        );
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pageChanged(position);
            }

            @Override
            public void onPageSelected(int position) {
                pageChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        activity.setupTabs(pager, new int[] {
                R.drawable.ic_settings_white,
                R.drawable.ic_tasks,
                R.drawable.ic_walk
        });

        fab = root.findViewById(R.id.newrequest_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == 2) {
                    // Check if info is filled in
                    if (request.getName() == null || request.getName().isEmpty()) {
                        SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_required_field).replace("{1}", getString(R.string.request_info_name)), "", null);
                        return;
                    }
                    for (int i = 0; i < request.getTasks().size(); i++) {
                        Task task = request.getTasks().get(i);
                        if (task.getService() == null) {
                            SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_required_field).replace("{1}", getString(R.string.newrequest_task_service)), "", null);
                            return;
                        }
                        if (task.getName() == null || task.getName().isEmpty()) {
//                            SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_required_field).replace("{1}", getString(R.string.newrequest_task_title)), "", null);
//                            return;
                            task.setName(task.getService().getType());
                        }
                    }
                    if (!request.isBroadcast() && request.getDirectId() == -1) {
                        SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_broadcast_or_direct_required), "", null);
                        return;
                    }

                    // Create request
                    NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_CREATE, NetManager.POST) {
                        @Override
                        public void success() {
                            super.success();
                            Bundle bundle = new Bundle();
                            try {
                                JSONObject o = new JSONObject(getResult().getMsg());
                                activity.getUser().getRequests().add(new Request(o));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            activity.navigateTo(R.id.nav_page_requests, bundle);
                        }

                        @Override
                        public void error() {
                            super.error();
                            SimpleDialog.buildMessageDialog(activity, "Error", getResult().getMsg(), "AAAA", null);
                        }
                    };

                    netRequest.putParam("created_by", activity.getUser().getId());
                    netRequest.putParam("name", request.getName());
                    netRequest.putParam("time", request.getTimeStringParam());
                    netRequest.putParam("picture_required", false);
                    netRequest.putParam("note", request.getNote());
                    if (request.getMaxDistance() == 0) {
                        netRequest.putNull("max_dist");
                    } else {
                        netRequest.putParam("max_dist", request.getMaxDistance());
                    }
                    netRequest.putParam("min_rating", request.getMinRating());
                    if (request.getDestination() != null) {
                        netRequest.putParam("destination", request.getDestination().toJSON());
                    } else {
                        netRequest.putNull("destination");
                    }
                    netRequest.putParam("broadcast", request.isBroadcast());
                    if (request.getDirectId() == -1) {
                        netRequest.putNull("direct_user");
                    } else {
                        netRequest.putParam("direct_user", request.getDirectId());
                    }
                    JSONArray tasklist = new JSONArray();
                    for (Task task : request.getTasks()) {
                        tasklist.put(task.toJSON());
                    }
                    netRequest.putParam("tasklist", tasklist);

                    NetManager.add(netRequest);
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            }
        });

        return root;
    }

    public Request getRequest() {
        return request;
    }

    private void pageChanged(int index) {
        if (index == 2) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_goto_white));
        }
    }

    void addressChanged() {
        ((NR3Fragment) fragments.get(2)).loadDirect(activity);
    }

    MainActivity getMainActivity() { return activity; }
}

// {"created_by":1,"services":[],"min_rating":2.5,"no_rating":true,"max_dist":600,"latitude":43.317626953125,"longitude":21.92500877380371}
// {"created_by":1,"services":[],"min_rating":2.5,"no_rating":true,"max_dist":600,"latitude":43.31763458251953,"longitude":21.92500877380371}