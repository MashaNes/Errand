package runners.errand.ui.findrequests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.ServiceSelectItemAdapter;
import runners.errand.model.Service;
import runners.errand.model.User;
import runners.errand.utils.dialogs.ListSelectDialog;
import runners.errand.utils.dialogs.SimpleDialog;

public class FilterFragment extends Fragment {
	private MainActivity activity;
	private FindRequestsFragment parent;
	private ArrayList<Service> services = new ArrayList<>();
	private ListView list;
	private EditText maxDistance, minRating;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_filter, container, false);

		parent = (FindRequestsFragment) getParentFragment();
		if (parent == null) return root;
		activity = parent.getMainActivity();

		maxDistance = root.findViewById(R.id.find_requests_filter_max_distance);
		minRating = root.findViewById(R.id.find_requests_filter_min_rating);

		root.findViewById(R.id.find_requests_filter_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (activity.getUser().getStatus() == User.STATUS_RUNNING) {
					parent.loadRequests();
					parent.navigate();
				} else {
					SimpleDialog.buildSelectDialog(activity, getString(R.string.error), getString(R.string.error_start_request_location), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
						@Override
						public void run() {
							activity.becomeActive();
						}
					}, null);
				}
			}
		});
		root.findViewById(R.id.find_requests_filter_service_add).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ListSelectDialog serviceSelectDialog = new ListSelectDialog(activity, new ServiceSelectItemAdapter(activity, activity.getServices()), activity.getString(R.string.newrequest_task_service_default)) {
					@Override
					public void itemSelected(Object o, int index, int size) {
						super.itemSelected(o, index, size);
						if (!services.contains(o)) {
							services.add((Service) o);
							dataSetChanged();
						}
					}
				};
				serviceSelectDialog.show();
			}
		});
		list = root.findViewById(R.id.find_requests_filter_service_list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				services.remove(position);
				dataSetChanged();
			}
		});
		dataSetChanged();

		return root;
	}

	private void dataSetChanged() {
		String[] items = new String[services.size()];
		for (int i = 0; i < services.size(); i++)
			items[i] = services.get(i).getType();
		ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.item_service, items);
		list.setAdapter(adapter);
	}

	int getMaxDistance() {
		if (maxDistance != null) {
			return Integer.parseInt(maxDistance.getText().toString());
		} else {
			return 600;
		}
	}

	float getMinRating() {
		if (minRating != null) {
			return Float.parseFloat(minRating.getText().toString());
		} else {
			return 2.5f;
		}
	}

	ArrayList<Service> getServices() {
		return services;
	}
}
