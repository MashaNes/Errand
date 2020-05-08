package runners.errand.adapter;

import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Address;
import runners.errand.model.Task;
import runners.errand.utils.dialogs.AddressSelectDialog;
import runners.errand.utils.dialogs.MapDialog;
import runners.errand.utils.dialogs.SimpleDialog;

public class TaskAdapter extends BaseAdapter {
	private int prevSize = 0;
	private ArrayList<Task> tasks;
	private ArrayList<Boolean> needsUpdate = new ArrayList<>();
	private MainActivity activity;
	private TaskAdapter taskAdapter;

	public TaskAdapter(MainActivity activity, ArrayList<Task> tasks) {
		this.tasks = tasks;
		prevSize = tasks.size();
		for (int i = 0; i < tasks.size(); i++) needsUpdate.add(false);
		this.activity = activity;
		taskAdapter = this;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (prevSize != tasks.size()) {
			for (int i = 0; i < tasks.size(); i++) needsUpdate.add(true);
			prevSize = tasks.size();
		}
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return tasks.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(activity).inflate(R.layout.item_task_nr, parent, false);
		} else {
			view = convertView;
			needsUpdate.set(position, true);
		}

		final Task task = tasks.get(position);

		((TextView) view.findViewById(R.id.item_task_number)).setText(String.format(Locale.getDefault(), "%d", position + 1));

		EditText title =  view.findViewById(R.id.item_task_title);
		title.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tasks.get(position).setName(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		EditText body = view.findViewById(R.id.item_task_body);
		body.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tasks.get(position).setDescription(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		final EditText address = view.findViewById(R.id.item_task_address);
		address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((LinearLayout) v.getParent()).callOnClick();
			}
		});
		view.findViewById(R.id.item_task_address_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddressSelectDialog addressSelectDialog = new AddressSelectDialog(activity, activity.getUser().getAddresses()) {
					@Override
					public void itemSelected(Address a) {
						super.itemSelected(a);
						task.setAddress(a);
						address.setText(task.getAddress().getName());
					}

					@Override
					public void otherSelected() {
						super.otherSelected();
						MapDialog mapDialog = new MapDialog(activity, null) {
							@Override
							public void positive(AlertDialog dialog) {
								super.positive(dialog);
								if (getAddress() != null) {
									task.setAddress(getAddress());
									address.setText(task.getAddress().getName());
								}
							}

							@Override
							public void negative(AlertDialog dialog) {
								super.negative(dialog);
							}
						};
						mapDialog.show();
					}
				};
				addressSelectDialog.show();
			}
		});

		if (needsUpdate.get(position)) {
			title.setText(task.getName());
			body.setText(task.getDescription());
			needsUpdate.set(position, false);
			address.setText(task.getAddress() != null ? task.getAddress().getName() : "");
		}

		view.findViewById(R.id.item_task_button_up).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (position != 0) {
					Task tmp = tasks.get(position);
					tasks.set(position, tasks.get(position - 1));
					tasks.set(position - 1, tmp);
					needsUpdate.set(position, true);
					needsUpdate.set(position - 1, true);
					taskAdapter.notifyDataSetChanged();
				} else {
					SimpleDialog.buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_top_item), "up/TA-L", null);
				}
			}
		});

		view.findViewById(R.id.item_task_button_down).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (position != (tasks.size() - 1)) {
					Task tmp = tasks.get(position);
					tasks.set(position, tasks.get(position + 1));
					tasks.set(position + 1, tmp);
					needsUpdate.set(position, true);
					needsUpdate.set(position + 1, true);
					taskAdapter.notifyDataSetChanged();
				} else {
					SimpleDialog.buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_bottom_item), "dn/TA-L", null);
				}
			}
		});

		view.findViewById(R.id.item_task_button_remove).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tasks.size() > 1) {
					tasks.remove(position);
					taskAdapter.notifyDataSetChanged();
					needsUpdate.clear();
					for (int i = 0; i < tasks.size(); i++) needsUpdate.add(true);
				} else {
					SimpleDialog.buildMessageDialog(activity, activity.getString(R.string.error), activity.getString(R.string.error_last_item), "la/TA-L", null);
				}
			}
		});

		return view;
	}
}