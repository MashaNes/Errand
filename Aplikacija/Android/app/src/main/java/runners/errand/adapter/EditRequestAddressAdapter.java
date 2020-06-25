package runners.errand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import runners.errand.R;
import runners.errand.model.Address;
import runners.errand.model.Rating;

public class EditRequestAddressAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Address> addresses;
	private ArrayList<String> labels;

	public EditRequestAddressAdapter(Context context, ArrayList<Address> addresses, ArrayList<String> labels) {
		this.context = context;
		this.addresses = addresses;
		this.labels = labels;
	}

	@Override
	public int getCount() {
		return addresses.size();
	}

	@Override
	public Object getItem(int position) {
		return addresses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return addresses.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
		} else {
			view = convertView;
		}

		Address address = addresses.get(position);

		((TextView) view.findViewById(R.id.item_address_et)).setText(address.getName());
		((TextView) view.findViewById(R.id.item_address_label)).setText(labels.get(position));

		return view;
	}
}
