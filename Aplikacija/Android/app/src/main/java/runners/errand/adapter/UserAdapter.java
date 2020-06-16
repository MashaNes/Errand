package runners.errand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import runners.errand.R;
import runners.errand.model.User;

public class UserAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<User> ratings;

	public UserAdapter(Context context, ArrayList<User> ratings) {
		this.context = context;
		this.ratings = ratings;
	}

	@Override
	public int getCount() {
		return ratings.size();
	}

	@Override
	public Object getItem(int position) {
		return ratings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ratings.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
		} else {
			view = convertView;
		}

		User user = ratings.get(position);

		view.findViewById(R.id.item_user_selected).setVisibility(user.selected ? View.VISIBLE : View.GONE);

		((TextView) view.findViewById(R.id.item_user_name)).setText(user.getName());
		if (user.getPicture_bmp() != null) ((ImageView) view.findViewById(R.id.item_user_image)).setImageBitmap(user.getPicture_bmp());
		((TextView) view.findViewById(R.id.item_user_rating)).setText(String.format(Locale.getDefault(), "%.1f", user.getRating()));
		if (user.getStatus() == 0) {
			((TextView) view.findViewById(R.id.item_user_active)).setText(R.string.generic_inactive);
			((ImageView) view.findViewById(R.id.item_user_active_drawable)).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_inactive));
		} else {
			((TextView) view.findViewById(R.id.item_user_active)).setText(R.string.generic_active);
			((ImageView) view.findViewById(R.id.item_user_active_drawable)).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_active));
		}

		return view;
	}
}
