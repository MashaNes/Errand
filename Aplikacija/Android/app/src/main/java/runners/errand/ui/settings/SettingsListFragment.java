package runners.errand.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.SettingsAdapter;
import runners.errand.model.Notification;
import runners.errand.model.Service;
import runners.errand.model.ServicePrefs;
import runners.errand.model.Setting;
import runners.errand.model.WorkingHours;
import runners.errand.utils.PreferenceManager;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class SettingsListFragment extends Fragment {
	private static final String SIS_KEY_INDEX = "runners.errand.ui.settings.index";

	private MainActivity activity;
	private int index, othersPage = 0;
	private ArrayList<Setting> settings = new ArrayList<>();
	private SettingsAdapter adapter;
	private ImageView back;
	private TextView title;
	private ListView list;

	SettingsListFragment(int index) {
		this.index = index;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_settings_list, container, false);

		if (savedInstanceState != null)
			index = savedInstanceState.getInt(SIS_KEY_INDEX);

		activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		back = root.findViewById(R.id.page_title_back);
		title = root.findViewById(R.id.page_title);
		list = root.findViewById(R.id.list_view);
		adapter = new SettingsAdapter(activity, settings);

		switch (index) {
			case 0:
				loadNotificationSettings();
				break;
			case 1:
				loadOtherSettings();
				break;
			case 2:
				loadBlockedSettings();
				break;
		}

		list.setAdapter(adapter);

		return root;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putInt(SIS_KEY_INDEX, index);
		super.onSaveInstanceState(outState);
	}

	private void loadNotificationSettings() {
		settings.clear();
		title.setText(R.string.settings_notifications);

		settings.add(new Setting(
				Notification.CATEGORY_NEW_RATING,
				getString(R.string.settings_notifications_new_rating),
				getString(R.string.settings_notifications_new_rating_desc),
				PreferenceManager.KEY_NEW_RATING,
				getNotificationSettingDrawableId(PreferenceManager.KEY_NEW_RATING)));

		settings.add(new Setting(
				Notification.CATEGORY_ACHIEVEMENT,
				getString(R.string.settings_notifications_achievement),
				getString(R.string.settings_notifications_achievement_desc),
				PreferenceManager.KEY_ACHIEVEMENT,
				getNotificationSettingDrawableId(PreferenceManager.KEY_ACHIEVEMENT)));

		settings.add(new Setting(
				Notification.CATEGORY_OFFER_ACCEPTED,
				getString(R.string.settings_notifications_offer_accepted),
				getString(R.string.settings_notifications_offer_accepted_desc),
				PreferenceManager.KEY_OFFER_ACCEPTED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_OFFER_ACCEPTED)));

		settings.add(new Setting(
				Notification.CATEGORY_OFFER_DECLINED,
				getString(R.string.settings_notifications_offer_declined),
				getString(R.string.settings_notifications_offer_declined_desc),
				PreferenceManager.KEY_OFFER_DECLINED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_OFFER_DECLINED)));

		settings.add(new Setting(
				Notification.CATEGORY_OFFER_REQUEST_CANCELED,
				getString(R.string.settings_notifications_offer_request_canceled),
				getString(R.string.settings_notifications_offer_request_canceled_desc),
				PreferenceManager.KEY_OFFER_REQUEST_CANCELED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_OFFER_REQUEST_CANCELED)));

		settings.add(new Setting(
				Notification.CATEGORY_OFFER_DIRECT_REQUEST,
				getString(R.string.settings_notifications_request_direct),
				getString(R.string.settings_notifications_request_direct_desc),
				PreferenceManager.KEY_OFFER_DIRECT_REQUEST,
				getNotificationSettingDrawableId(PreferenceManager.KEY_OFFER_DIRECT_REQUEST)));

		settings.add(new Setting(
				Notification.CATEGORY_REQUEST_SUCCESS,
				getString(R.string.settings_notifications_request_success),
				getString(R.string.settings_notifications_request_success_desc),
				PreferenceManager.KEY_REQUEST_SUCCESS,
				getNotificationSettingDrawableId(PreferenceManager.KEY_REQUEST_SUCCESS)));

		settings.add(new Setting(
				Notification.CATEGORY_REQUEST_FAILURE,
				getString(R.string.settings_notifications_request_failure),
				getString(R.string.settings_notifications_request_failure_desc),
				PreferenceManager.KEY_REQUEST_FAILURE,
				getNotificationSettingDrawableId(PreferenceManager.KEY_REQUEST_FAILURE)));

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Setting setting = adapter.getItem(position);
				boolean value = !PreferenceManager.getBoolean(activity, PreferenceManager.GROUP_SETTINGS, setting.getPrefKey());
				setting.setDrawableId(value ? R.drawable.ic_setting_on : R.drawable.ic_setting_off);
				PreferenceManager.putBoolean(activity, PreferenceManager.GROUP_SETTINGS, setting.getPrefKey(), value);
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void loadOtherSettings() {
		othersPage = 0;

		back.setVisibility(View.GONE);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadOtherSettings();
			}
		});

		title.setText(R.string.settings_runner);

		settings.clear();

		settings.add(new Setting(
				0,
				getString(R.string.settings_runner_service_defaults),
				getString(R.string.settings_runner_service_defaults_desc),
				null,
				R.drawable.ic_goto_24));

		settings.add(new Setting(
				0,
				getString(R.string.settings_runner_working_hours),
				getString(R.string.settings_runner_working_hours_desc),
				null,
				R.drawable.ic_goto_24));

		settings.add(new Setting(
				0,
				getString(R.string.settings_runner_benefits),
				getString(R.string.settings_runner_benefits_desc),
				null,
				R.drawable.ic_goto_24));

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (othersPage) {
					case 0:
						// Main page
						switch (position) {
							case 0:
								loadServiceDefaultsSettings();
								break;
							case 1:
								loadWorkingHoursSettings();
								break;
							case 2:
								loadBenefitSettings();
								break;
						}
						break;
					case 1:
						Service service = activity.getServices().get(position);
						ServicePrefs prefs = new ServicePrefs(service.getId());
						for (ServicePrefs servicePrefs : activity.getUser().getServicePrefs())
							if (servicePrefs.getService() == service.getId()) {
								prefs = servicePrefs.copy();
								break;
							}
						final ServicePrefs finalPrefs = prefs;
						SimpleDialog.buildEditServicePrefDialog(activity, finalPrefs, service, new Runnable() {
							@Override
							public void run() {
								apiUpdateServicePrefs(finalPrefs);
							}
						});
						break;
					case 2:
						// Working hours item click
						break;
					case 3:
						// Benefits item click
						break;
				}
			}
		});
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				switch (othersPage) {
					case 1:
						Service service = activity.getServices().get(position);
						ServicePrefs prefs = null;
						for (ServicePrefs servicePrefs : activity.getUser().getServicePrefs())
							if (servicePrefs.getService() == service.getId()) {
								prefs = servicePrefs;
								break;
							}
						if (prefs != null) {
							final ServicePrefs finalPrefs = prefs;
							SimpleDialog.buildSelectDialog(activity, getString(R.string.settings_runner_service_defaults_clear), getString(R.string.settings_runner_service_defaults_clear_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
								@Override
								public void run() {
									apiDeleteServicePrefs(finalPrefs);
								}
							}, null);
						}
						break;
					case 2:
						// Delete working hours
						break;
					case 3:
						// First item is user info benefits requirement & discount for auto add
						if (position > 0) {
							// Remove item from benefits
						}
						break;
				}
				return true;
			}
		});

		adapter.notifyDataSetChanged();
	}

	private void loadServiceDefaultsSettings() {
		othersPage = 1;

		back.setVisibility(View.VISIBLE);

		title.setText(R.string.settings_runner_service_defaults);

		settings.clear();

		for (Service service : activity.getServices()) {
			String body = service.getDescription();
			body += "\n";
			ServicePrefs prefs = null;
			for (ServicePrefs servicePrefs : activity.getUser().getServicePrefs())
				if (servicePrefs.getService() == service.getId()) prefs = servicePrefs;
			if (prefs == null) {
				body += getString(R.string.settings_runner_service_defaults_no_defaults);
			} else {
				double d;
				body += getString(R.string.settings_runner_service_defaults_payment_type);
				body += ": ";
				body += ServicePrefs.getPaymentTypeString(activity, prefs.getPaymentType());
				body += "\n";
				body += getString(R.string.settings_runner_service_defaults_payment_amount);
				body += ": ";
				d = prefs.getPaymentAmount();
				if (d == (long) d) {
					body += String.format(Locale.getDefault(), "%d", (long) d);
				} else {
					body += String.format(Locale.getDefault(), "%s", d);
				}
				body += "\n";
				body += getString(R.string.settings_runner_service_defaults_max_distance);
				body += ": ";
				d = prefs.getMaxDistance();
				if (d == (long) d) {
					body += String.format(Locale.getDefault(), "%d", (long) d);
				} else {
					body += String.format(Locale.getDefault(), "%s", d);
				}
				body += "m\n";
				body += getString(R.string.settings_runner_service_defaults_min_rating);
				body += ": ";
				d = prefs.getMinRating();
				if (d == (long) d) {
					body += String.format(Locale.getDefault(), "%d", (long) d);
				} else {
					body += String.format(Locale.getDefault(), "%s", d);
				}
			}
			settings.add(new Setting(
					service.getId(),
					service.getType(),
					body,
					null,
					R.drawable.ic_edit));
		}

		adapter.notifyDataSetChanged();
	}

	private void loadWorkingHoursSettings() {
		othersPage = 2;

		back.setVisibility(View.VISIBLE);

		title.setText(R.string.settings_runner_working_hours);

		settings.clear();

		ArrayList<String> times = new ArrayList<>(7);
		for (int i = 0; i < 7; i++) times.add("");

		for (WorkingHours h : activity.getUser().getWorkingHours()) {
			int i = h.getDay();
			String add = times.get(i);
			if (!add.isEmpty()) add += "\n";
			add += h.getFrom() + " - " + h.getUntil();
			times.set(i, add);
		}

		for (int i = 0; i < times.size(); i++) {
			if (times.get(i).isEmpty()) times.set(i, getString(R.string.settings_runner_working_hours_empty));
			settings.add(new Setting(
					i,
					getResources().getStringArray(R.array.settings_runner_working_hours_days)[i],
					times.get(i),
					null,
					R.drawable.ic_edit));
		}

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SimpleDialog.buildEditWorkingHoursDialog(activity);
			}
		});

		adapter.notifyDataSetChanged();
	}

	private void loadBenefitSettings() {
		othersPage = 3;

		back.setVisibility(View.VISIBLE);

		title.setText(R.string.settings_runner_benefits);

		settings.clear();

		String body = getString(R.string.settings_runner_benefits_auto_desc);
		body += "\n";
		body += getString(R.string.settings_runner_benefits_discount);
		body += ": ";
		body += activity.getUser().getBenefitDiscount();
		body += "\n";
		body += getString(R.string.settings_runner_benefits_requirement);
		body += ": ";
		body += activity.getUser().getBenefitRequirements();
		settings.add(new Setting(
				-1,
				getString(R.string.settings_runner_benefits_auto),
				body,
				null,
				R.drawable.ic_edit));

		// TODO

		adapter.notifyDataSetChanged();
	}

	private void loadBlockedSettings() {
		// TODO: Add blocked to user
		// activity.getUser().getBlocked();

		title.setText(R.string.settings_blocked);

		settings.clear();

		adapter = new SettingsAdapter(activity, settings);

		adapter.notifyDataSetChanged();
	}

	private int getNotificationSettingDrawableId(String key) {
		return PreferenceManager.getBoolean(activity, PreferenceManager.GROUP_SETTINGS, key) ? R.drawable.ic_setting_on : R.drawable.ic_setting_off;
	}

	private void apiUpdateServicePrefs(final ServicePrefs prefs) {
		NetRequest netRequest;
		if (prefs.getId() == -1) {
			netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USER_SERVICES_ADD, NetManager.POST) {
				@Override
				public void success() {
					try {
						JSONObject o = new JSONObject(getResult().getMsg());
						prefs.setId(o.optInt("id"));
						activity.getUser().getServicePrefs().add(prefs);
						loadServiceDefaultsSettings();
					} catch (JSONException e) {
						e.printStackTrace();
						SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_json), "", null);
					}
					super.success();
				}

				@Override
				public void error() {
					super.error();
					SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
				}
			};
			netRequest.putParam("service", prefs.getService());
		} else {
			netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USER_SERVICES_UPDATE, NetManager.PUT) {
				@Override
				public void success() {
					for (int i = 0; i < activity.getUser().getServicePrefs().size(); i++)
						if (activity.getUser().getServicePrefs().get(i).getId() == prefs.getId()) {
							activity.getUser().getServicePrefs().set(i, prefs);
							break;
						}
					loadServiceDefaultsSettings();
					super.success();
				}

				@Override
				public void error() {
					super.error();
					SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
				}
			};
			netRequest.putParam("user_service", prefs.getId());
		}
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("payment_type", prefs.getPaymentType());
		netRequest.putParam("payment_ammount", prefs.getPaymentAmount());
		netRequest.putParam("max_dist", prefs.getMaxDistance());
		netRequest.putParam("min_rating", prefs.getMinRating());
		NetManager.add(netRequest);
	}

	private void apiDeleteServicePrefs(final ServicePrefs prefs) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USER_SERVICES_REMOVE, NetManager.DELETE) {
			@Override
			public void success() {
				for (int i = 0; i < activity.getUser().getServicePrefs().size(); i++)
					if (activity.getUser().getServicePrefs().get(i).getId() == prefs.getId()) {
						activity.getUser().getServicePrefs().remove(i);
						break;
					}
				loadServiceDefaultsSettings();
				super.success();
			}

			@Override
			public void error() {
				super.error();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("service", prefs.getId());
		NetManager.add(netRequest);
	}
}
