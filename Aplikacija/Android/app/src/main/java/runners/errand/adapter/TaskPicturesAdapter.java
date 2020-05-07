package runners.errand.adapter;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.utils.LoadingDrawable;
import runners.errand.utils.dialogs.SimpleDialog;

public class TaskPicturesAdapter extends RecyclerView.Adapter {
	private ArrayList<Bitmap> pictures;
	private MainActivity activity;

	public TaskPicturesAdapter(MainActivity activity, ArrayList<Bitmap> pictures, float density) {
		this.activity = activity;
		this.pictures = pictures;
		PictureViewHolder.density = density;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		ImageView v = new ImageView(parent.getContext());
		PictureViewHolder vh = new PictureViewHolder(v);
		vh.setData(activity, null);
		return vh;
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		((PictureViewHolder) holder).setData(null, pictures.get(position));
	}

	@Override
	public int getItemCount() {
		return pictures.size();
	}

	static class PictureViewHolder extends RecyclerView.ViewHolder {
		private ImageView v;
		static float density = 0;
		private int dimen;
		private Bitmap bitmap = null;

		PictureViewHolder(@NonNull View itemView) {
			super(itemView);
			v = ((ImageView) itemView);
			dimen = (int) Math.ceil(100 * density);
			v.setScaleType(ImageView.ScaleType.CENTER_CROP);
			int margin = (int) Math.ceil(4 * density);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dimen, dimen);
			lp.setMargins(margin / 2, margin, margin / 2, margin);
			v.setLayoutParams(lp);
			v.setBackgroundColor(v.getContext().getResources().getColor(R.color.colorLightGrey));
		}

		void setData(final MainActivity activity, Bitmap b) {
			if (b != null) {
				this.bitmap = b;
				int padding = 0;
				v.setPadding(padding, padding, padding, padding);
				Bitmap scaled;
				if (b.getWidth() > b.getHeight()) {
					scaled = Bitmap.createScaledBitmap(b, dimen * (b.getWidth() / b.getHeight()), dimen, false);
				} else {
					scaled = Bitmap.createScaledBitmap(b, dimen, dimen * (b.getHeight() / b.getWidth()), false);
				}
				v.setImageBitmap(scaled);
			} else {
				int padding = (int) Math.ceil(40 * density);
				v.setPadding(padding, padding, padding, padding);
				v.setImageDrawable(LoadingDrawable.get(activity, R.color.colorBlack));
			}

			if (activity != null) {
				v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (bitmap != null) {
							SimpleDialog.buildImageDialog(activity, bitmap);
						}
					}
				});
			}
		}
	}
}
