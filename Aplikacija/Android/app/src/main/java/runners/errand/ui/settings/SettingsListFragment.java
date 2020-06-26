package runners.errand.ui.settings;

import android.os.Bundle;
import android.util.Log;
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
import runners.errand.model.Benefit;
import runners.errand.model.Notification;
import runners.errand.model.Service;
import runners.errand.model.ServicePrefs;
import runners.errand.model.Setting;
import runners.errand.model.WorkingHours;
import runners.errand.utils.Alarms;
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
				Notification.CATEGORY_REQUEST_DIRECT,
				getString(R.string.settings_notifications_request_direct),
				getString(R.string.settings_notifications_request_direct_desc),
				PreferenceManager.KEY_REQUEST_DIRECT,
				getNotificationSettingDrawableId(PreferenceManager.KEY_REQUEST_DIRECT)));

		settings.add(new Setting(
				Notification.CATEGORY_REQUEST_FAILED,
				getString(R.string.settings_notifications_request_failed),
				getString(R.string.settings_notifications_request_failed_desc),
				PreferenceManager.KEY_REQUEST_FAILED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_REQUEST_FAILED)));

		settings.add(new Setting(
				Notification.CATEGORY_REQUEST_SUCCESS,
				getString(R.string.settings_notifications_request_success),
				getString(R.string.settings_notifications_request_success_desc),
				PreferenceManager.KEY_REQUEST_SUCCESS,
				getNotificationSettingDrawableId(PreferenceManager.KEY_REQUEST_SUCCESS)));

		settings.add(new Setting(
				Notification.CATEGORY_OFFER_CREATED,
				getString(R.string.settings_notifications_offer_created),
				getString(R.string.settings_notifications_offer_created_desc),
				PreferenceManager.KEY_OFFER_CREATED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_OFFER_CREATED)));

		settings.add(new Setting(
				Notification.CATEGORY_OFFER_ACCEPTED,
				getString(R.string.settings_notifications_offer_accepted),
				getString(R.string.settings_notifications_offer_accepted_desc),
				PreferenceManager.KEY_OFFER_ACCEPTED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_OFFER_ACCEPTED)));

		settings.add(new Setting(
				Notification.CATEGORY_OFFER_CANCELED,
				getString(R.string.settings_notifications_offer_canceled),
				getString(R.string.settings_notifications_offer_canceled_desc),
				PreferenceManager.KEY_OFFER_CANCELED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_OFFER_CANCELED)));

		settings.add(new Setting(
				Notification.CATEGORY_EDIT_CREATED,
				getString(R.string.settings_notifications_edit_created),
				getString(R.string.settings_notifications_edit_created_desc),
				PreferenceManager.KEY_EDIT_CREATED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_EDIT_CREATED)));

		settings.add(new Setting(
				Notification.CATEGORY_EDIT_ACCEPTED,
				getString(R.string.settings_notifications_edit_accepted),
				getString(R.string.settings_notifications_edit_accepted_desc),
				PreferenceManager.KEY_EDIT_ACCEPTED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_EDIT_ACCEPTED)));

		settings.add(new Setting(
				Notification.CATEGORY_EDIT_CANCELED,
				getString(R.string.settings_notifications_edit_canceled),
				getString(R.string.settings_notifications_edit_canceled_desc),
				PreferenceManager.KEY_EDIT_CANCELED,
				getNotificationSettingDrawableId(PreferenceManager.KEY_EDIT_CANCELED)));

		settings.add(new Setting(
				Notification.CATEGORY_RATING,
				getString(R.string.settings_notifications_rating),
				getString(R.string.settings_notifications_rating_desc),
				PreferenceManager.KEY_RATING,
				getNotificationSettingDrawableId(PreferenceManager.KEY_RATING)));

		settings.add(new Setting(
				Notification.CATEGORY_ACHIEVEMENT,
				getString(R.string.settings_notifications_achievement),
				getString(R.string.settings_notifications_achievement_desc),
				PreferenceManager.KEY_ACHIEVEMENT,
				getNotificationSettingDrawableId(PreferenceManager.KEY_ACHIEVEMENT)));

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
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
						WorkingHours hours = activity.getUser().getWorkingHours(position);
						boolean update = true;
						if (hours == null) {
							update = false;
							hours = new WorkingHours(position);
						}
						final boolean finalUpdate = update;
						final WorkingHours finalHours = hours;
						SimpleDialog.buildEditWorkingHoursDialog(activity, finalHours, new Runnable() {
							@Override
							public void run() {
								if (finalUpdate) {
									apiUpdateWorkingHours(finalHours);
								} else {
									apiAddWorkingHours(finalHours);
								}
							}
						});
						break;
					case 3:
						if (position == 0) {
							final AutoBenefit autoBenefit;
							if (Float.isNaN(activity.getUser().getBenefitDiscount())) {
								autoBenefit = new AutoBenefit(3, 0.2f);
							} else {
								autoBenefit = new AutoBenefit(activity.getUser().getBenefitRequirements(), activity.getUser().getBenefitDiscount());
							}
							SimpleDialog.buildEditAutoBenefitDialog(activity, autoBenefit, new Runnable() {
								@Override
								public void run() {
									apiUpdateAutoBenefit(autoBenefit);
								}
							});
						} else {
							final Benefit benefit = activity.getUser().getBenefits().get(position - 1);
							SimpleDialog.buildEditBenefitDialog(activity, benefit, new Runnable() {
								@Override
								public void run() {
									apiUpdateBenefit(benefit, position - 1);
								}
							});
						}
						break;
				}
			}
		});
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
						final WorkingHours hours = activity.getUser().getWorkingHours(position);
						if (hours != null) {
							SimpleDialog.buildSelectDialog(activity, getString(R.string.settings_runner_hours_clear), getString(R.string.settings_runner_hours_clear_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
								@Override
								public void run() {
									apiDeleteWorkingHours(hours);
								}
							}, null);
						}
						break;
					case 3:
						if (position == 0) {
							SimpleDialog.buildSelectDialog(activity, getString(R.string.settings_runner_benefits_clear), getString(R.string.settings_runner_benefits_clear_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
								@Override
								public void run() {
									apiUpdateAutoBenefit(new AutoBenefit(-1, -1));
								}
							}, null);
						} else {
							final Benefit benefit = activity.getUser().getBenefits().get(position - 1);
							SimpleDialog.buildSelectDialog(activity, getString(R.string.settings_runner_benefits_clear), getString(R.string.settings_runner_benefits_clear_desc), getString(R.string.generic_yes), getString(R.string.generic_no), new Runnable() {
								@Override
								public void run() {
									apiDeleteBenefit(benefit, position - 1);
								}
							}, null);
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

		adapter.notifyDataSetChanged();
	}

	private void loadBenefitSettings() {
		othersPage = 3;

		back.setVisibility(View.VISIBLE);

		title.setText(R.string.settings_runner_benefits);

		settings.clear();

		String body = getString(R.string.settings_runner_benefits_auto_desc);
		body += "\n";
		if (Float.isNaN(activity.getUser().getBenefitDiscount())) {
			body += getString(R.string.settings_runner_benefits_not_setup);
		} else {
			body += getString(R.string.settings_runner_benefits_discount);
			body += ": ";
			body += String.format(Locale.getDefault(), "%.0f", activity.getUser().getBenefitDiscount() * 100);
			body += "%\n";
			body += getString(R.string.settings_runner_benefits_requirement);
			body += ": ";
			body += activity.getUser().getBenefitRequirements();
		}
		settings.add(new Setting(
				-1,
				getString(R.string.settings_runner_benefits_auto),
				body,
				null,
				R.drawable.ic_edit));

		for (Benefit benefit : activity.getUser().getBenefits()) {
			settings.add(new Setting(
					benefit.getId(),
					benefit.getUser().getFirstName() + " " + benefit.getUser().getLastName(),
					getString(R.string.settings_runner_benefits_discount) + ": " + String.format(Locale.getDefault(), "%.0f", benefit.getDiscount() * 100) + "%",
					null,
					R.drawable.ic_edit));
		}

		adapter.notifyDataSetChanged();
	}

	private void loadBlockedSettings() {
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

	private void apiUpdateBenefit(final Benefit benefit, final int position) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_BENEFIT_UPDATE, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
				activity.getUser().getBenefits().set(position, benefit);
				loadBenefitSettings();
			}

			@Override
			public void error() {
				super.error();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("benefit", benefit.getId());
		netRequest.putParam("discount", benefit.getDiscount());
		NetManager.add(netRequest);
	}

	private void apiDeleteBenefit(final Benefit benefit, final int position) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_BENEFIT_REMOVE, NetManager.DELETE) {
			@Override
			public void success() {
				super.success();
				activity.getUser().getBenefits().remove(position);
				loadBenefitSettings();
			}

			@Override
			public void error() {
				super.error();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("benefit_user", benefit.getUser().getId());
		NetManager.add(netRequest);
	}

	private void apiUpdateAutoBenefit(final AutoBenefit autoBenefit) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_BENEFIT_AUTO, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
				if (autoBenefit.req == -1) {
					activity.getUser().setBenefitDiscount(Float.NaN);
					activity.getUser().setBenefitRequirements(0);
				} else {
					activity.getUser().setBenefitDiscount(autoBenefit.disc);
					activity.getUser().setBenefitRequirements(autoBenefit.req);
				}
				loadBenefitSettings();
			}

			@Override
			public void error() {
				super.error();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		if (autoBenefit.req == -1) {
			netRequest.putNull("benefit_discount");
			netRequest.putNull("benefit_requirement");
		} else {
			netRequest.putParam("benefit_discount", autoBenefit.disc);
			netRequest.putParam("benefit_requirement", autoBenefit.req);
		}
		NetManager.add(netRequest);
	}

	private void apiAddWorkingHours(final WorkingHours hours) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_WORKING_HOURS_ADD, NetManager.POST) {
			@Override
			public void success() {
				super.success();
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					hours.setId(o.optInt("id"));
					activity.getUser().getWorkingHours().add(hours);
					loadWorkingHoursSettings();
					Alarms.add(activity, hours);
				} catch (JSONException e) {
					e.printStackTrace();
					SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
				}
			}

			@Override
			public void error() {
				super.error();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("day", hours.getDay());
		netRequest.putParam("work_from", hours.getFrom());
		netRequest.putParam("work_until", hours.getUntil());
		NetManager.add(netRequest);
	}

	private void apiUpdateWorkingHours(final WorkingHours hours) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_WORKING_HOURS_UPDATE, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
				for (WorkingHours h : activity.getUser().getWorkingHours()) {
					if (h.getDay() == hours.getDay()) h = hours;
				}
				Alarms.remove(activity, hours);
				Alarms.add(activity, hours);
				loadWorkingHoursSettings();
			}

			@Override
			public void error() {
				super.error();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("working_hours", hours.getId());
		netRequest.putParam("day", hours.getDay());
		netRequest.putParam("work_from", hours.getFrom());
		netRequest.putParam("work_until", hours.getUntil());
		NetManager.add(netRequest);
	}

	private void apiDeleteWorkingHours(final WorkingHours hours) {
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_WORKING_HOURS_REMOVE, NetManager.DELETE) {
			@Override
			public void success() {
				super.success();
				activity.getUser().getWorkingHours().remove(hours);
				Alarms.remove(activity, hours);
				loadWorkingHoursSettings();
			}

			@Override
			public void error() {
				super.error();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_generic_api), "", null);
			}
		};
		netRequest.putParam("created_by", activity.getUser().getId());
		netRequest.putParam("working_hours", hours.getId());
		NetManager.add(netRequest);
	}

	public static class AutoBenefit {
		public int req;
		public float disc;

		private AutoBenefit(int req, float disc) {
			this.req = req;
			this.disc = disc;
		}
	}
}
