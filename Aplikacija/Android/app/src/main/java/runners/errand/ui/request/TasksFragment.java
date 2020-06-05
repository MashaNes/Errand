package runners.errand.ui.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import runners.errand.R;
import runners.errand.adapter.TaskPicturesAdapter;
import runners.errand.model.Request;
import runners.errand.utils.BitmapUtils;

public class TasksFragment extends Fragment {

	private Request request;
	private TaskPicturesAdapter picturesAdapter;
	private ArrayList<Bitmap> bitmaps = new ArrayList<>();

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_tasks, container, false);

		RequestFragment parent = ((RequestFragment) getParentFragment());
		if (parent == null) return root;

		request = parent.getRequest();
		if (request == null) return root;

		DisplayMetrics metrics = new DisplayMetrics();
		parent.getMainActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// Destination
		((TextView) root.findViewById(R.id.task_destination)).setText(request.getDestination().getName());

		// Note
		((TextView) root.findViewById(R.id.task_note)).setText(request.getNote());

		// Tasks
		LinearLayout taskListLayout = root.findViewById(R.id.task_list_layout);
		for (int i = 0; i < request.getTasks().size(); i++) {
			View v = LayoutInflater.from(getContext()).inflate(R.layout.item_task, taskListLayout, false);
			((TextView) v.findViewById(R.id.item_task_number)).setText(String.format(Locale.getDefault(), "%d", i + 1));
			((TextView) v.findViewById(R.id.item_task_title)).setText(request.getTasks().get(i).getName());
			((TextView) v.findViewById(R.id.item_task_body)).setText(request.getTasks().get(i).getBody());
			((TextView) v.findViewById(R.id.item_task_service)).setText(request.getTasks().get(i).getService().getType());
			taskListLayout.addView(v);
		}

		// Pictures
		if (request.getPictures().size() == 0) {
			root.findViewById(R.id.task_pictures_layout).setVisibility(View.GONE);
			return root;

			//request.getPictures().add(BitmapUtils.encode(parent.getMainActivity(), R.drawable.img_test, BitmapUtils.COMPRESSION_OTHER));
		}

		RecyclerView pictures = root.findViewById(R.id.task_pictures);
		LinearLayoutManager picturesLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
		pictures.setLayoutManager(picturesLayoutManager);
		picturesAdapter = new TaskPicturesAdapter(parent.getMainActivity(), bitmaps, metrics.density);
		pictures.setAdapter(picturesAdapter);

		if (bitmaps.size() != request.getPictures().size()) {
			bitmaps.clear();
			for (int i = 0; i < request.getPictures().size(); i++)
				bitmaps.add(null);
			picturesAdapter.notifyDataSetChanged();

			new Thread(new Runnable() {
				@Override
				public void run() {
					loadPictures();
				}
			}).start();
		}

		return root;
	}

	private void loadPictures() {
		for (int i = 0; i < request.getPictures().size(); i++) {
			if (bitmaps.get(i) != null) continue;

			bitmaps.set(i, BitmapUtils.decode(request.getPictures().get(i)));
			try {
				picturesAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				// Context changes while thread is running and refuses UI update from old context
				e.printStackTrace();
			}
		}
	}
}
