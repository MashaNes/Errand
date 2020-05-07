package runners.errand.ui.newrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.TaskAdapter;
import runners.errand.model.Task;

public class NR2Fragment extends Fragment {
	private TaskAdapter taskAdapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_nr2, container, false);

		MainActivity activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		final NewRequestFragment parent = ((NewRequestFragment) getParentFragment());
		if (parent == null) return root;

		parent.getRequest().getTasks().add(new Task());

		ListView taskList = root.findViewById(R.id.nr2_tasks);
		taskAdapter = new TaskAdapter(activity, parent.getRequest().getTasks());
		taskList.setAdapter(taskAdapter);

		root.findViewById(R.id.header_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.getRequest().getTasks().add(new Task());
				taskAdapter.notifyDataSetChanged();
			}
		});

		return root;
	}
}
