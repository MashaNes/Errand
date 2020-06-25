package runners.errand.utils.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.EditRequestAddressAdapter;
import runners.errand.model.Address;
import runners.errand.model.Edit;
import runners.errand.model.Request;
import runners.errand.model.Task;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class EditRequestDialog extends AlertDialog {
	private Date erDateTime;
	private ArrayList<Address> erAddresses = new ArrayList<>();
	private ArrayList<String> erAddressLabels = new ArrayList<>();
	private EditRequestAddressAdapter erAdapter;

	public EditRequestDialog(final MainActivity activity, final Request request, final int id) {
		super(activity);
		View root = View.inflate(activity, R.layout.dialog_edit_create, null);

		// Edit request
		erDateTime = new Date(request.getTime().getTime());
		final TextView erDate = root.findViewById(R.id.newrequest_info_date_et);
		erDate.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(erDateTime));
		erDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				if (erDateTime != null) {
					calendar.setTime(erDateTime);
				} else {
					calendar.setTimeInMillis(System.currentTimeMillis());
				}

				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						Calendar calendar = Calendar.getInstance();
						if (erDateTime != null) {
							calendar.setTime(erDateTime);
						} else {
							calendar.setTimeInMillis(System.currentTimeMillis());
						}
						calendar.set(Calendar.YEAR, year);
						calendar.set(Calendar.MONTH, month);
						calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						erDateTime = calendar.getTime();
						erDate.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(erDateTime));
					}
				}, year, month, day);
				dialog.show();
			}
		});
		final TextView erTime = root.findViewById(R.id.newrequest_info_time_et);
		erTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(erDateTime));
		erTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				if (erDateTime != null) {
					calendar.setTime(erDateTime);
				} else {
					calendar.setTimeInMillis(System.currentTimeMillis());
				}

				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);

				TimePickerDialog dialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						Calendar calendar = Calendar.getInstance();
						if (erDateTime != null) {
							calendar.setTime(erDateTime);
						} else {
							calendar.setTimeInMillis(System.currentTimeMillis());
						}
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						erDateTime = calendar.getTime();
						erTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(erDateTime));
					}
				}, hour, minute, true);
				dialog.show();
			}
		});
		ListView erList = root.findViewById(R.id.request_send_offer_address_layout);
		if (erAdapter == null) {
			for (Task task : request.getTasks()) {
				if (task.getAddress() != null) {
					erAddresses.add(task.getAddress());
					erAddressLabels.add(task.getName());
				}
			}
			erAdapter = new EditRequestAddressAdapter(activity, erAddresses, erAddressLabels);
		}
		erList.setAdapter(erAdapter);
		erList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				MapDialog mapDialog = new MapDialog(activity, erAddresses.get(position)) {
					@Override
					public void positive(AlertDialog dialog) {
						super.positive(dialog);
						if (getAddress() != null) {
							erAddresses.set(position, getAddress());
							erAdapter.notifyDataSetChanged();
						}
					}

					@Override
					public void negative(AlertDialog dialog) {
						super.negative(dialog);
					}
				};
				mapDialog.show();
			}
		});
		root.findViewById(R.id.request_send_offer_reset_edit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				erDateTime = new Date(request.getTime().getTime());
				erDate.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(request.getTime()));
				erTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(request.getTime()));
				int i = 0;
				for (Task task : request.getTasks()) {
					if (task.getAddress() != null) {
						erAddresses.set(i, task.getAddress());
						i++;
					}
				}
				erAdapter.notifyDataSetChanged();
			}
		});

		root.findViewById(R.id.request_send_offer_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_EDIT_CREATE, NetManager.POST) {
					@Override
					public void success() {
						super.success();
						JSONObject o = null;
						try {
							o = new JSONObject(getResult().getMsg());
							Edit edit = new Edit(o.optJSONObject("request_edit"));
							edit.setId(o.optInt("id"));
							editRequested(edit);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						editRequested(null);
						dismiss();
					}

					@Override
					public void error() {
						super.error();
						dismiss();
					}
				};
				netRequest.putParam("created_by", activity.getUser().getId());
				netRequest.putParam("request", request.getId());
				JSONObject edit = new JSONObject();
				try {
					if (erDateTime.getTime() != request.getTime().getTime()) {
						edit.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(erDateTime));
					} else {
						edit.put("time", JSONObject.NULL);
					}
				} catch (JSONException e) {
					Log.e("TEST", "JSON 1");
					e.printStackTrace();
				}
				JSONArray tasks = new JSONArray();
				int i = 0;
				for (Task task : request.getTasks()) {
					if (task.getAddress() != null) {
						if (!erAddresses.get(i).equals(task.getAddress())) {
							JSONObject t = new JSONObject();
							try {
								t.put("task", task.getId());
								JSONObject addr = new JSONObject();
								addr.put("name", erAddresses.get(i).getName());
								addr.put("latitude", erAddresses.get(i).getLat());
								addr.put("longitude", erAddresses.get(i).getLng());
								addr.put("home", erAddresses.get(i).isHome());
								addr.put("arrived", erAddresses.get(i).isArrived());
								t.put("address", addr);
								tasks.put(t);
							} catch (JSONException e) {
								Log.e("TEST", "JSON 2");
								e.printStackTrace();
							}
						}
						i++;
					}
				}
				try {
					edit.put("tasks", tasks);
				} catch (JSONException e) {
					Log.e("TEST", "JSON 3");
					e.printStackTrace();
				}
				netRequest.putParam("edit", edit);
				NetManager.add(netRequest);
			}
		});

		root.findViewById(R.id.request_send_offer_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		this.setView(root);
	}

	public void editRequested(Edit edit) {}
}
