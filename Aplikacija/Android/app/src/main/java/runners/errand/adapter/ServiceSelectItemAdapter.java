package runners.errand.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Service;

public class ServiceSelectItemAdapter extends BaseAdapter {
	private MainActivity activity;
	private ArrayList<Service> services;

	public ServiceSelectItemAdapter(MainActivity activity, ArrayList<Service> services) {
		this.activity = activity;
		this.services = services;
	}

	@Override
	public int getCount() {
		return services.size();
	}

	@Override
	public Object getItem(int position) {
		return services.get(position);
	}

	@Override
	public long getItemId(int position) {
		return services.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(activity).inflate(R.layout.item_list_select, parent, false);
		} else {
			view = convertView;
		}

		((TextView) view.findViewById(R.id.item_address_select_address)).setText(services.get(position).getType());
		((TextView) view.findViewById(R.id.item_address_select_latlng)).setText(services.get(position).getDescription());

		return view;
	}
}
