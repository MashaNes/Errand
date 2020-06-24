package runners.errand.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

		if (achievement.getIcon_bmp() != null) {
			((ImageView) view.findViewById(R.id.achievement_image)).setImageBitmap(achievement.getIcon_bmp());
		} else {
			((ImageView) view.findViewById(R.id.achievement_image)).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_achievements_full));
		}

		LinearLayout levelLayout = view.findViewById(R.id.achievement_stars);
		levelLayout.removeAllViews();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int size = (int) (16 * metrics.density);
		for (int i = 0; i < achievement.getMax_level(); i++) {
			ImageView imageView = new ImageView(context);
			levelLayout.addView(imageView);
			if (i < achievement.getLevel()) {
				imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_achievements_full));
			} else {
				imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_achievements_empty));
			}
			ViewGroup.LayoutParams lp = imageView.getLayoutParams();
			lp.height = size;
			lp.width = size;
			imageView.setLayoutParams(lp);
		}

		return view;
	}
}
