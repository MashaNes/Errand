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
import runners.errand.model.Rating;

public class RatingAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Rating> ratings;

	public RatingAdapter(Context context, ArrayList<Rating> ratings) {
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

		Rating rating = ratings.get(position);

		((TextView) view.findViewById(R.id.rating_name)).setText(rating.getName());

		if (rating.getComment().isEmpty()) {
			view.findViewById(R.id.rating_comment).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.rating_comment).setVisibility(View.VISIBLE);
			((TextView) view.findViewById(R.id.rating_comment)).setText(rating.getComment());
		}

		if (rating.getGrade() >= 1) {
			((ImageView) view.findViewById(R.id.rating_1)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		if (rating.getGrade() >= 2) {
			((ImageView) view.findViewById(R.id.rating_2)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		if (rating.getGrade() >= 3) {
			((ImageView) view.findViewById(R.id.rating_3)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		if (rating.getGrade() >= 4) {
			((ImageView) view.findViewById(R.id.rating_4)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		if (rating.getGrade() >= 5) {
			((ImageView) view.findViewById(R.id.rating_5)).setImageResource(R.drawable.ic_star_full);
		} else return view;

		return view;
	}
}
