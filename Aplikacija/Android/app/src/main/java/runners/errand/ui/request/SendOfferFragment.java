package runners.errand.ui.request;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.print.PrinterId;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import runners.errand.model.Offer;
import runners.errand.model.Request;
import runners.errand.model.ServicePrefs;
import runners.errand.model.Task;
import runners.errand.ui.requests.RequestsFragment;
import runners.errand.utils.dialogs.MapDialog;
import runners.errand.utils.dialogs.ProfileDialog;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class SendOfferFragment extends Fragment {
	private MainActivity activity;
	private RequestFragment parent;
	private Request request;
	private EditText paymentAmount;
	private int paymentType = 0;
	private Date erDateTime = null;
	private ArrayList<Address> erAddresses = new ArrayList<>();
	private ArrayList<String> erAddressLabels = new ArrayList<>();
	private EditRequestAddressAdapter erAdapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_send_offer, container, false);

		parent = (RequestFragment) getParentFragment();
		if (parent == null) return root;
		activity = parent.getMainActivity();
		request = parent.getRequest();

		// Create by
		if (request.getCreatedBy().getPicture_bmp() != null) {
			((ImageView) root.findViewById(R.id.item_user_image)).setImageBitmap(request.getCreatedBy().getPicture_bmp());
		} else {
			((ImageView) root.findViewById(R.id.item_user_image)).setImageDrawable(getResources().getDrawable(R.drawable.ic_face));
		}
		String name = request.getCreatedBy().getFirstName() + " " + request.getCreatedBy().getLastName();
		((TextView) root.findViewById(R.id.item_user_name)).setText(name);
		String rating = getString(R.string.generic_unrated);
		if (!Float.isNaN(request.getCreatedBy().getRating())) rating = String.format(Locale.getDefault(), "%.1f", request.getCreatedBy().getRating());
		((TextView) root.findViewById(R.id.item_user_rating)).setText(rating);
		root.findViewById(R.id.item_user_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new ProfileDialog(activity, request.getCreatedBy(), "", getString(R.string.generic_close)).show();
			}
		});

		// Offer
		paymentAmount = root.findViewById(R.id.dialog_edit_service_pref_payment_amount);
		for (ServicePrefs servicePrefs : activity.getUser().getServicePrefs()) {
			if (servicePrefs.getService() == request.getTasks().get(0).getService().getId()) {
				paymentAmount.setText(String.format(Locale.getDefault(), "%.0f", servicePrefs.getPaymentAmount()));
				paymentType = servicePrefs.getPaymentType();
				break;
			}
		}
		((EditText) root.findViewById(R.id.dialog_edit_service_pref_payment_type)).setText(ServicePrefs.getPaymentTypeString(activity, paymentType));
		root.findViewById(R.id.dialog_edit_service_pref_payment_type).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paymentType++;
				if (paymentType > 3) paymentType = 0;
				((EditText) v).setText(ServicePrefs.getPaymentTypeString(activity, paymentType));
			}
		});

		// Edit request
		if (erDateTime == null) erDateTime = new Date(request.getTime().getTime());
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
//		if (request.getDestination() != null) {
//			erAddresses.add(request.getDestination());
//			erAddressLabels.add(getString(R.string.newrequest_info_destination));
//		}
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
				NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_OFFER_CREATE, NetManager.POST) {
					@Override
					public void success() {
						super.success();
						try {
							JSONObject o = new JSONObject(getResult().getMsg());
							Offer offer = new Offer(o);
							activity.getUser().getOffers().add(offer);
							activity.apiGetRequest(offer);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						SimpleDialog.buildMessageDialog(activity, getString(R.string.generic_success), getString(R.string.offer_sent), "", new Runnable() {
							@Override
							public void run() {
//								Bundle bundle = new Bundle();
//								bundle.putInt(RequestsFragment.EXTRA_REFRESH_BY_ID, request.getId());
								activity.navigateTo(R.id.nav_page_requests);
							}
						});
					}

					@Override
					public void error() {
						super.error();
					}
				};
				netRequest.putParam("created_by", activity.getUser().getId());
				netRequest.putParam("payment_type", paymentType);
				if (paymentAmount.getText().toString().trim().isEmpty()) {
					SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_required_field).replace("{1}", getString(R.string.settings_runner_service_defaults_payment_amount)), "", null);
					return;
				}
				netRequest.putParam("payment_ammount", Integer.parseInt(paymentAmount.getText().toString()));
				netRequest.putParam("request", request.getId());
				JSONObject edit = new JSONObject();
				boolean timeNull = false;
				boolean tasksNull = false;
				try {
					if (erDateTime.getTime() != request.getTime().getTime()) {
						edit.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(erDateTime));
					} else {
						timeNull = true;
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
					if (tasks.length() == 0) {
						tasksNull = true;
						edit.put("tasks", JSONObject.NULL);
					} else {
						edit.put("tasks", tasks);
					}
				} catch (JSONException e) {
					Log.e("TEST", "JSON 3");
					e.printStackTrace();
				}
				if (tasksNull && timeNull) {
					netRequest.putNull("edit");
				} else {
					netRequest.putParam("edit", edit);
				}
				NetManager.add(netRequest);
			}
		});

		return root;
	}
}
