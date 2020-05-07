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
			view = LayoutInflater.from(context).inflate(R.layout.item_rating, parent, false);
		} else {
			view = convertView;
		}

		User user = ratings.get(position);

		((TextView) view.findViewById(R.id.rating_name)).setText(user.getName());

		if (user.getRating() >= 1) {
			((ImageView) view.findViewById(R.id.rating_1)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		if (user.getRating() >= 2) {
			((ImageView) view.findViewById(R.id.rating_2)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		if (user.getRating() >= 3) {
			((ImageView) view.findViewById(R.id.rating_3)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		if (user.getRating() >= 4) {
			((ImageView) view.findViewById(R.id.rating_4)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		if (user.getRating() >= 5) {
			((ImageView) view.findViewById(R.id.rating_5)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		return view;
	}
}
