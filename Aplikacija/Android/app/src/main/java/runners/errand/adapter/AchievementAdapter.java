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
import runners.errand.model.Achievement;

public class AchievementAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Achievement> achievements;

	public AchievementAdapter(Context context, ArrayList<Achievement> achievements) {
		this.context = context;
		this.achievements = achievements;
	}

	@Override
	public int getCount() {
		return achievements.size();
	}

	@Override
	public Object getItem(int position) {
		return achievements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return achievements.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_achievement, parent, false);
		} else {
			view = convertView;
		}

		Achievement achievement = achievements.get(position);

		((TextView) view.findViewById(R.id.achievement_name)).setText(achievement.getName());
		((TextView) view.findViewById(R.id.achievement_description)).setText(achievement.getDescription());

		if (achievement.getLevel() >= 1) {
			((ImageView) view.findViewById(R.id.achievement_1)).setImageResource(R.drawable.ic_achievements_full);
		} else return view;

		if (achievement.getLevel() >= 2) {
			((ImageView) view.findViewById(R.id.achievement_2)).setImageResource(R.drawable.ic_achievements_full);
		} else return view;

		if (achievement.getLevel() >= 3) {
			((ImageView) view.findViewById(R.id.achievement_3)).setImageResource(R.drawable.ic_achievements_full);
		} else return view;

		if (achievement.getLevel() >= 4) {
			((ImageView) view.findViewById(R.id.achievement_4)).setImageResource(R.drawable.ic_achievements_full);
		} else return view;

		if (achievement.getLevel() >= 5) {
			((ImageView) view.findViewById(R.id.achievement_5)).setImageResource(R.drawable.ic_achievements_full);
		} else return view;

		return view;
	}
}
