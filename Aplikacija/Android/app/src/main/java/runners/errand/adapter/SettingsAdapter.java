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
import runners.errand.model.Setting;

public class SettingsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Setting> settings;

	public SettingsAdapter(Context context, ArrayList<Setting> settings) {
		this.context = context;
		this.settings = settings;
	}

	@Override
	public int getCount() {
		return settings.size();
	}

	@Override
	public Setting getItem(int position) {
		return settings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return settings.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_setting, parent, false);
		} else {
			view = convertView;
		}

		final Setting setting = settings.get(position);

		((TextView) view.findViewById(R.id.item_setting_title)).setText(setting.getName());
		((TextView) view.findViewById(R.id.item_setting_body)).setText(setting.getDescription());
		((ImageView) view.findViewById(R.id.item_setting_image)).setImageDrawable(context.getResources().getDrawable(setting.getDrawableId()));

		return view;
	}
}
