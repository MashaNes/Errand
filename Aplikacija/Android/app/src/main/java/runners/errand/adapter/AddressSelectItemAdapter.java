package runners.errand.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Address;

public class AddressSelectItemAdapter extends BaseAdapter {
	private MainActivity activity;
	private ArrayList<Address> addresses;

	public AddressSelectItemAdapter(MainActivity activity, ArrayList<Address> addresses) {
		this.activity = activity;
		this.addresses = addresses;
	}

	@Override
	public int getCount() {
		return addresses.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position >= addresses.size()) {
			return null;
		} else {
			return addresses.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		if (position >= addresses.size()) {
			return 0;
		} else {
			return addresses.get(position).getId();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(activity).inflate(R.layout.item_address_select, parent, false);
		} else {
			view = convertView;
		}

		if (position == addresses.size()) {
			((TextView) view.findViewById(R.id.item_address_select_address)).setText(activity.getString(R.string.newrequest_task_address_other));
			((TextView) view.findViewById(R.id.item_address_select_latlng)).setText(activity.getString(R.string.newrequest_task_address_other_desc));
		} else {
			((TextView) view.findViewById(R.id.item_address_select_address)).setText(addresses.get(position).getName());
			String latlng = addresses.get(position).getLat() + ", " + addresses.get(position).getLng();
			((TextView) view.findViewById(R.id.item_address_select_latlng)).setText(latlng);
		}

		return view;
	}
}
