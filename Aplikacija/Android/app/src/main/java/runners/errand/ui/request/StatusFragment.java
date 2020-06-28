package runners.errand.ui.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.TaskSelectItemAdapter;
import runners.errand.location.GeofencingBroadcastReceiver;
import runners.errand.model.Edit;
import runners.errand.model.Offer;
import runners.errand.model.Request;
import runners.errand.model.ServicePrefs;
import runners.errand.model.Task;
import runners.errand.model.User;
import runners.errand.utils.ImageUtils;
import runners.errand.utils.Static;
import runners.errand.utils.dialogs.EditRequestDialog;
import runners.errand.utils.dialogs.ListSelectDialog;
import runners.errand.utils.dialogs.ProfileDialog;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class StatusFragment extends Fragment {
	private static final int CALLBACK_PICTURE = 1298;

	private MainActivity activity;
	private RequestFragment parent;
	private Request request;
	private User user;
	private Offer offer;
	private Button start, cancel, complete, rate, request_edit, upload_picture;
	private TextView total, total_label, edit, edit_label, completed, top_text;
	private ListView edit_list;
	private ArrayAdapter<String> edit_adapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_status, container, false);

		parent = (RequestFragment) getParentFragment();
		if (parent == null) return root;
		activity = parent.getMainActivity();
		request = parent.getRequest();

		start = root.findViewById(R.id.find_requests_status_start);
		cancel = root.findViewById(R.id.find_requests_status_cancel);
		complete = root.findViewById(R.id.find_requests_status_complete);
		rate = root.findViewById(R.id.find_requests_status_rate);
		request_edit = root.findViewById(R.id.find_requests_status_edit);
		upload_picture = root.findViewById(R.id.find_requests_status_picture);

		top_text = root.findViewById(R.id.request_status_text_user);
		completed = root.findViewById(R.id.find_requests_status_other_user_completed);
		total_label = root.findViewById(R.id.item_offer_total_label);
		total = root.findViewById(R.id.item_offer_total);
		edit_label = root.findViewById(R.id.item_offer_edit_label);
		edit = root.findViewById(R.id.item_offer_edit);
		edit_list = root.findViewById(R.id.item_offer_edit_list);

		offer = request.getAcceptedOffer();
		if (offer == null) offer = request.getMyOffer();

		setupUI();

		String payment = "";
		payment += offer.getPaymentAmount();
		payment += " RSD ";
		payment += ServicePrefs.getPaymentTypeString(activity, offer.getPaymentType());
		((TextView) root.findViewById(R.id.item_offer_payment_amount)).setText(payment);
		if (user.getPicture_bmp() != null) {
			((ImageView) root.findViewById(R.id.item_user_image)).setImageBitmap(user.getPicture_bmp());
		} else {
			((ImageView) root.findViewById(R.id.item_user_image)).setImageDrawable(getResources().getDrawable(R.drawable.ic_face));
		}
		String name = user.getFirstName() + " " + user.getLastName();
		((TextView) root.findViewById(R.id.item_user_name)).setText(name);
		String rating = Float.isNaN(user.getRating()) ? getString(R.string.generic_unrated) : String.format(Locale.getDefault(), "%.1f", user.getRating());
		((TextView) root.findViewById(R.id.item_user_rating)).setText(rating);
		root.findViewById(R.id.item_user_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new ProfileDialog(activity, user, "", getString(R.string.generic_close)).show();
			}
		});

		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (activity.getUser().getStatus() == User.STATUS_RUNNING) {
					apiStartRequest(Static.lat, Static.lng);
				} else {
					SimpleDialog.buildSelectDialog(activity, getString(R.string.error), getString(R.string.error_start_request_location), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
						@Override
						public void run() {
							activity.becomeActive();
						}
					}, null);
				}
			}
		});

		complete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialog.buildSelectDialog(activity, getString(R.string.request_complete), getString(R.string.request_complete_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
					@Override
					public void run() {
						if (request.getCreatedBy().getId() != activity.getUser().getId() && request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_KM) {
							if (activity.getUser().getStatus() == User.STATUS_RUNNING) {
								GeofencingBroadcastReceiver.removeGeofence(activity, request.getId());
								apiRequestFinish(2, Static.lat, Static.lng);
							} else {
								SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_finish_request_location), "", null);
							}
						} else {
							GeofencingBroadcastReceiver.removeGeofence(activity, request.getId());
							apiRequestFinish(2, 0, 0);
						}
					}
				}, null);
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialog.buildSelectDialog(activity, getString(R.string.request_cancel), getString(R.string.request_cancel_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
					@Override
					public void run() {
						GeofencingBroadcastReceiver.removeGeofence(activity, request.getId());
						apiRequestFinish(3, 0, 0);
					}
				}, null);
			}
		});

		rate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleDialog.buildRateDialog(activity, user, request.getId(), new Runnable() {
					@Override
					public void run() {
						if (request.getCreatedBy().getId() == activity.getUser().getId()) {
							request.setRatedWorkingWith(true);
						} else {
							request.setRatedCreatedBy(true);
						}
						setupUI();
					}
				});
			}
		});

		request_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditRequestDialog editRequestDialog = new EditRequestDialog(activity, request, activity.getUser().getId()) {
					@Override
					public void editRequested(Edit edit) {
						if (edit != null) request.getEdits().add(edit);
						setupUI();
					}
				};
				editRequestDialog.show();
			}
		});

		upload_picture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_PICK);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), CALLBACK_PICTURE);
			}
		});

		return root;
	}

	@Override
	public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == CALLBACK_PICTURE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
			try {
				InputStream is = activity.getContentResolver().openInputStream(data.getData());
				if (is != null) {
					final String picture_b64 = ImageUtils.encodeBig(is);
					new ListSelectDialog(activity, new TaskSelectItemAdapter(activity, request.getTasks()), getString(R.string.select_task)) {
						@Override
						public void itemSelected(Object o, int index, int size) {
							super.itemSelected(o, index, size);
							final int task_id = ((Task) o).getId();
							NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_UPLOAD_PICTURE, NetManager.POST) {
								@Override
								public void success() {
									super.success();
									for (Task task : request.getTasks()) {
										if (task.getId() == task_id)
											task.getPictures().add(picture_b64);
									}
									parent.loadData();
								}

								@Override
								public void error() {
									super.error();
								}
							};
							netRequest.putNull("request_id");
							netRequest.putParam("task_id", task_id);
							netRequest.putParam("picture", picture_b64);
							NetManager.add(netRequest);
						}
					}.show();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_image_load), "fnf/IF-L", null);
			}
		}
	}

	private void showEdits(final boolean runner) {
		if (request.getEdits().size() != 0) {
			edit_list.setVisibility(View.VISIBLE);
			edit_label.setVisibility(View.VISIBLE);
			List<String> edit_items = new ArrayList<>();
			for (Edit e : request.getEdits()) {
				StringBuilder s = new StringBuilder();
				if (e.getTime() != null) {
					s.append(getString(R.string.newrequest_info_time)).append(": ").append(new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(e.getTime())).append("\n");
				}
				for (Task task : e.getTasks()) {
					for (Task task1 : request.getTasks()) {
						if (task.getId() == task1.getId()) {
							s.append(task1.getName()).append(": ").append(task.getAddress().getName()).append("\n");
							break;
						}
					}
				}
				if (s.length() > 0) s.deleteCharAt(s.length() - 1);
				edit_items.add(s.toString());
			}
			edit_adapter = new ArrayAdapter<>(activity, R.layout.item_edit_request, edit_items);
			edit_list.setAdapter(edit_adapter);
			if (!runner) {
				edit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> p, View view, final int position, long id) {
						SimpleDialog.buildSelectDialog(activity, getString(R.string.edit_accept), getString(R.string.edit_accept_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
							@Override
							public void run() {
								NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_EDIT_ACCEPT, NetManager.PUT) {
									@Override
									public void success() {
										Log.e("EDIT", "Accepted.");
										try {
											Request tmp = new Request(new JSONObject(getResult().getMsg()).optJSONObject("request"));
											request.getTasks().clear();
											request.getTasks().addAll(tmp.getTasks());
											request.setTime(tmp.getTime());
										} catch (JSONException e) {
											e.printStackTrace();
										}
										request.getEdits().remove(position);
										setupUI();
										parent.loadData();
										super.success();
									}

									@Override
									public void error() {
										Log.e("EDIT", "Accept failed.");
										super.error();
									}
								};
								netRequest.putParam("created_by", activity.getUser().getId());
								netRequest.putParam("request", request.getId());
								netRequest.putParam("edit", request.getEdits().get(position).getId());
								NetManager.add(netRequest);
							}
						}, null);
					}
				});
			}
			edit_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
					SimpleDialog.buildSelectDialog(activity, getString(runner ? R.string.edit_cancel : R.string.edit_reject), getString(runner ? R.string.edit_cancel_desc : R.string.edit_reject_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
						@Override
						public void run() {
							NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_EDIT_CANCEL, NetManager.DELETE) {
								@Override
								public void success() {
									request.getEdits().remove(position);
									setupUI();
									super.success();

								}

								@Override
								public void error() {
									super.error();
								}
							};
							netRequest.putParam("created_by", activity.getUser().getId());
							netRequest.putParam("edit", request.getEdits().get(position).getId());
							NetManager.add(netRequest);
						}
					}, null);
					return true;
				}
			});
		}
	}

	void setupUI() {
		if (activity == null) return;
		start.setVisibility(View.GONE);
		cancel.setVisibility(View.GONE);
		complete.setVisibility(View.GONE);
		rate.setVisibility(View.GONE);
		request_edit.setVisibility(View.GONE);
		upload_picture.setVisibility(View.GONE);
		total_label.setVisibility(View.GONE);
		total.setVisibility(View.GONE);
		edit_label.setVisibility(View.GONE);
		edit.setVisibility(View.GONE);
		edit_list.setVisibility(View.GONE);
		if (request.getCreatedBy().getId() == activity.getUser().getId()) {
			if (request.isFinishedWorkingWith()) {
				String s = request.getWorkingWith().getFirstName() + " " + request.getWorkingWith().getLastName() + " " + getString(R.string.marked_as_finished);
				completed.setText(s);
			} else {
				completed.setVisibility(View.GONE);
			}
			user = request.getWorkingWith();
			top_text.setText(R.string.request_send_offer_running);
			if (request.getStatus() == Request.STATUS_CANCELED) {
				Log.e("REQUEST", "1-0");
				if (!request.isRatedWorkingWith())
					rate.setVisibility(View.VISIBLE);
			} else if (request.isFinishedWorkingWith() && request.isFinishedCreatedBy()) {
				// Mark as completed by both
				Log.e("REQUEST", "1-1");
				if (!request.isRatedWorkingWith())
					rate.setVisibility(View.VISIBLE);
				total_label.setVisibility(View.VISIBLE);
				total.setVisibility(View.VISIBLE);
				total.setText(String.format(Locale.getDefault(), "%.0f RSD", request.getPrice()));
			} else if (request.isFinishedCreatedBy()) {
				// Mark as completed by you
				Log.e("REQUEST", "1-2");
			} else {
				// Offer accepted
				Log.e("REQUEST", "1-3");
				cancel.setVisibility(View.VISIBLE);
				complete.setVisibility(View.VISIBLE);
				showEdits(false);
			}
		} else {
			if (request.isFinishedCreatedBy()) {
				String s = request.getCreatedBy().getFirstName() + " " + request.getCreatedBy().getLastName() + " " + getString(R.string.marked_as_finished);
				completed.setText(s);
			} else {
				completed.setVisibility(View.GONE);
			}
			user = request.getCreatedBy();
			top_text.setText(R.string.request_send_offer_created_by);
			if (request.getStatus() == Request.STATUS_CANCELED) {
				Log.e("REQUEST", "2-0");
				if (!request.isRatedCreatedBy())
					rate.setVisibility(View.VISIBLE);
			} else if (request.isFinishedWorkingWith() && request.isFinishedCreatedBy()) {
				Log.e("REQUEST", "2-1");
				// Mark as completed by both
				if (!request.isRatedCreatedBy())
					rate.setVisibility(View.VISIBLE);
				total_label.setVisibility(View.VISIBLE);
				total.setVisibility(View.VISIBLE);
				total.setText(String.format(Locale.getDefault(), "%.0f RSD", request.getPrice()));
			} else if (request.isFinishedWorkingWith()) {
				// Marked as completed by you
				Log.e("REQUEST", "2-2");
			} else if (request.getAcceptedOffer() != null) {
				// Offer accepted
				int payment_type = request.getAcceptedOffer().getPaymentType();
				if (payment_type == ServicePrefs.PAYMENT_TYPE_FIXED || payment_type == ServicePrefs.PAYMENT_TYPE_FIXED_PLUS) {
					Log.e("REQUEST", "2-3-1");
					cancel.setVisibility(View.VISIBLE);
					complete.setVisibility(View.VISIBLE);
					request_edit.setVisibility(View.VISIBLE);
					upload_picture.setVisibility(View.VISIBLE);
					showEdits(true);
				} else if (payment_type == ServicePrefs.PAYMENT_TYPE_PER_HOUR) {
					if (request.getPriceTime() == null) {
						Log.e("REQUEST", "2-3-2-1");
						start.setVisibility(View.VISIBLE);
					} else {
						Log.e("REQUEST", "2-3-2-2");
						cancel.setVisibility(View.VISIBLE);
						complete.setVisibility(View.VISIBLE);
						request_edit.setVisibility(View.VISIBLE);
						upload_picture.setVisibility(View.VISIBLE);
						showEdits(true);
					}
				} else if (payment_type == ServicePrefs.PAYMENT_TYPE_PER_KM) {
					if (Double.isNaN(request.getPriceLat()) || Double.isNaN(request.getPriceLng()) ) {
						Log.e("REQUEST", "2-3-3-1");
						start.setVisibility(View.VISIBLE);
					} else {
						Log.e("REQUEST", "2-3-3-2");
						cancel.setVisibility(View.VISIBLE);
						complete.setVisibility(View.VISIBLE);
						request_edit.setVisibility(View.VISIBLE);
						upload_picture.setVisibility(View.VISIBLE);
						showEdits(true);
					}
				}
			} else {
				// Offer sent, not accepted
				Log.e("REQUEST", "2-4");
				if (offer.getEdit() != null && (!offer.getEdit().getTasks().isEmpty() || offer.getEdit().getTime() != null)) {
					edit_label.setVisibility(View.VISIBLE);
					edit.setVisibility(View.VISIBLE);
					StringBuilder s = new StringBuilder();
					if (offer.getEdit().getTime() != null) {
						s.append(getString(R.string.newrequest_info_time)).append(": ").append(new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(offer.getEdit().getTime())).append("\n");
					}
					for (Task task : offer.getEdit().getTasks()) {
						for (Task task1 : request.getTasks()) {
							if (task.getId() == task1.getId()) {
								s.append(task1.getName()).append(": ").append(task.getAddress().getName()).append("\n");
								break;
							}
						}
					}
					if (s.length() > 0) s.delete(s.length() - 1, s.length());
					edit.setText(s);
				}
			}
		}
	}

	private void apiRequestFinish(int status, double lat, double lng) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_FINISH, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
				// price:null
				if (request.getCreatedBy().getId() == activity.getUser().getId()) {
					request.setFinishedCreatedBy(true);
				} else {
					request.setFinishedWorkingWith(true);
				}
				Log.e("REQUEST", "Finish: Success");
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					request.setPrice(o.optDouble("price"));
					Log.e("REQUEST", "Finish JSON: Success");
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("REQUEST", "Finish JSON: Error");
				}
				setupUI();
			}

			@Override
			public void error() {
				super.error();
				Log.e("REQUEST", "Finish: Error");
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("request", request.getId());
		netRequest.putParam("status", status);
		if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_KM) {
			JSONObject location = new JSONObject();
			try {
				location.put("latitude", lat);
				location.put("longitude", lng);
				netRequest.putParam("location", location);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			netRequest.putNull("location");
		}
		NetManager.add(netRequest);
	}

	private void apiStartRequest(final double lat, final double lng) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUEST_START, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
				// detail:"success"
				Log.e("REQUEST", "Start: Success");
				if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_HOUR) {
					request.setPriceTime(calendar.getTime());
				} else if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_KM) {
					request.setPriceLat(lat);
					request.setPriceLng(lng);
				}
				// Setup geo-fencing
				if (request.getDestination() != null) {
					GeofencingBroadcastReceiver.addGeofence(
							activity,
							request.getId(),
							request.getDestination().getId(),
							request.getDestination().getLat(),
							request.getDestination().getLng()
					);
				}
				for (Task task : request.getTasks()) {
					if (task.getAddress() != null) {
						GeofencingBroadcastReceiver.addGeofence(
								activity,
								request.getId(),
								task.getAddress().getId(),
								task.getAddress().getLat(),
								task.getAddress().getLng()
						);
					}
				}
				setupUI();
			}

			@Override
			public void error() {
				super.error();
				Log.e("REQUEST", "Start: Error");
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("request", request.getId());
		if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_HOUR) {
			netRequest.putParam("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.getTime()));
		} else {
			netRequest.putNull("timestamp");
		}
		JSONObject location = new JSONObject();
		try {
			if (request.getAcceptedOffer().getPaymentType() == ServicePrefs.PAYMENT_TYPE_PER_KM) {
				location.put("latitude", lat);
				location.put("longitude", lng);
			} else {
				location.put("latitude", 0);
				location.put("longitude", 0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		netRequest.putParam("location", location);
		NetManager.add(netRequest);
	}

	void setRequest(Request request) {
		this.request = request;
		setupUI();
	}
}
