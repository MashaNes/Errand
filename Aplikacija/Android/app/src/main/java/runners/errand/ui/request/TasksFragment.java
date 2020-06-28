package runners.errand.ui.request;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.TaskPicturesAdapter;
import runners.errand.model.Request;
import runners.errand.utils.ImageUtils;

public class TasksFragment extends Fragment {
	private MainActivity activity;
	private RequestFragment parent;
	private Request request;
	private TaskPicturesAdapter picturesAdapter;
	private ArrayList<TaskPicturesAdapter> taskPicturesAdapters = new ArrayList<>();
	private ArrayList<Bitmap> bitmaps = new ArrayList<>();
	private ArrayList<ArrayList<Bitmap>> taskBitmapList = new ArrayList<>();
	private TextView destination, time, note;
	private LinearLayout taskListLayout, pictureLayout, noteLayout;
	private RecyclerView pictures;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_tasks, container, false);

		parent = ((RequestFragment) getParentFragment());
		if (parent == null) return root;
		activity = parent.getMainActivity();

		request = parent.getRequest();
		if (request == null) return root;

		destination = root.findViewById(R.id.task_destination);
		time = root.findViewById(R.id.task_time);
		note = root.findViewById(R.id.task_note);
		noteLayout = root.findViewById(R.id.note_layout);
		taskListLayout = root.findViewById(R.id.task_list_layout);
		pictureLayout = root.findViewById(R.id.task_pictures_layout);
		pictures = root.findViewById(R.id.task_pictures);

		loadData();

		return root;
	}

	void loadData() {
		Log.e("TEST", request.getName());
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		if (request.getDestination() != null) destination.setText(request.getDestination().getName());
		time.setText(request.getTimeString());
		if (request.getNote() != null && !request.getNote().isEmpty() && !request.getNote().trim().isEmpty()) note.setText(request.getNote());
		else noteLayout.setVisibility(View.GONE);
		taskListLayout.removeAllViews();
		for (int i = 0; i < request.getTasks().size(); i++) {
			View v = LayoutInflater.from(getContext()).inflate(R.layout.item_task, taskListLayout, false);
			((TextView) v.findViewById(R.id.item_task_number)).setText(String.format(Locale.getDefault(), "%d", i + 1));
			((TextView) v.findViewById(R.id.item_task_title)).setText(request.getTasks().get(i).getName());
			((TextView) v.findViewById(R.id.item_task_body)).setText(request.getTasks().get(i).getBody(activity));
			if (request.getTasks().get(i).getAddress() != null) ((TextView) v.findViewById(R.id.item_task_address)).setText(request.getTasks().get(i).getAddress().getName());
			else v.findViewById(R.id.item_task_address).setVisibility(View.GONE);
			((TextView) v.findViewById(R.id.item_task_service)).setText(request.getTasks().get(i).getService().getType());
			if (request.getTasks().get(i).getPictures().size() == 0) {
				v.findViewById(R.id.item_task_pictures_layout).setVisibility(View.GONE);
			} else {
				final RecyclerView recyclerView = v.findViewById(R.id.item_task_pictures);
				recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
				if (taskBitmapList.size() < i + 1) taskBitmapList.add(new ArrayList<Bitmap>());
				if (taskPicturesAdapters.size() < i + 1) taskPicturesAdapters.add(new TaskPicturesAdapter(activity, taskBitmapList.get(i), metrics.density));
				recyclerView.setAdapter(taskPicturesAdapters.get(i));
				if (taskBitmapList.get(i).size() != request.getTasks().get(i).getPictures().size()) {
					taskBitmapList.get(i).clear();
					for (int j = 0; j < request.getTasks().get(i).getPictures().size(); j++)
						taskBitmapList.get(i).add(null);
					taskPicturesAdapters.get(i).notifyDataSetChanged();

					final int finalI = i;
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							loadPictures(request.getTasks().get(finalI).getPictures(), taskBitmapList.get(finalI), taskPicturesAdapters.get(finalI), recyclerView);
						}
					});
					t.start();
					try {
						t.join();
						taskPicturesAdapters.get(i).notifyDataSetChanged();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			taskListLayout.addView(v);
		}

		// Pictures
		if (request.getPictures().size() == 0) {
			pictureLayout.setVisibility(View.GONE);
			return;
		}

		LinearLayoutManager picturesLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
		pictures.setLayoutManager(picturesLayoutManager);
		picturesAdapter = new TaskPicturesAdapter(activity, bitmaps, metrics.density);
		pictures.setAdapter(picturesAdapter);

		if (bitmaps.size() != request.getPictures().size()) {
			bitmaps.clear();
			for (int i = 0; i < request.getPictures().size(); i++)
				bitmaps.add(null);
			picturesAdapter.notifyDataSetChanged();

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					loadPictures(request.getPictures(), bitmaps, picturesAdapter, pictures);
				}
			});
			t.start();
			try {
				t.join();
				picturesAdapter.notifyDataSetChanged();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadPictures(ArrayList<String> pictures, ArrayList<Bitmap> bitmaps, TaskPicturesAdapter adapter, RecyclerView recyclerView) {
		for (int i = 0; i < pictures.size(); i++) {
			if (bitmaps.get(i) != null) continue;

			bitmaps.set(i, ImageUtils.decode(pictures.get(i)));
		}
	}

	void setRequest(Request request) {
		this.request = request;
		loadData();
	}
}
