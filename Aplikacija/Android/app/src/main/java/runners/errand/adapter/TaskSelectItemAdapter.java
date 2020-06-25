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
import runners.errand.model.Task;

public class TaskSelectItemAdapter extends BaseAdapter {
	private MainActivity activity;
	private ArrayList<Task> tasks;

	public TaskSelectItemAdapter(MainActivity activity, ArrayList<Task> tasks) {
		this.activity = activity;
		this.tasks = tasks;
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return tasks.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(activity).inflate(R.layout.item_list_select, parent, false);
		} else {
			view = convertView;
		}

		((TextView) view.findViewById(R.id.item_address_select_address)).setText(tasks.get(position).getName());
		((TextView) view.findViewById(R.id.item_address_select_latlng)).setText(tasks.get(position).getDescription());

		return view;
	}
}
