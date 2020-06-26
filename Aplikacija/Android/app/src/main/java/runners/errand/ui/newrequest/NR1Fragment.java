package runners.errand.ui.newrequest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.AddressSelectItemAdapter;
import runners.errand.model.Address;
import runners.errand.utils.dialogs.ListSelectDialog;
import runners.errand.utils.dialogs.MapDialog;

public class NR1Fragment extends Fragment {
	MainActivity activity;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_nr1, container, false);

		activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		final NewRequestFragment parent = ((NewRequestFragment) getParentFragment());
		if (parent == null) return root;

		((EditText) root.findViewById(R.id.newrequest_info_name)).addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				parent.getRequest().setName(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		((EditText) root.findViewById(R.id.newrequest_info_note)).addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				parent.getRequest().setNote(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		((EditText) root.findViewById(R.id.newrequest_info_max_distance)).addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().isEmpty()) parent.getRequest().setMaxDistance(Double.parseDouble(s.toString()));
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		((EditText) root.findViewById(R.id.newrequest_info_min_rating)).addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().isEmpty()) parent.getRequest().setMinRating(Double.parseDouble(s.toString()));
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		final TextView destination = root.findViewById(R.id.newrequest_info_destination_et);
		if (parent.getRequest().getDestination() != null) destination.setText(parent.getRequest().getDestination().getName());
		destination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((View) v.getParent()).callOnClick();
			}
		});
		((View) destination.getParent()).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ListSelectDialog addressSelectDialog = new ListSelectDialog(activity, new AddressSelectItemAdapter(activity, activity.getUser().getAddresses()), activity.getString(R.string.newrequest_task_address_default)) {
					@Override
					public void itemSelected(Object o, int index, int size) {
						super.itemSelected(o, index, size);
						if (index < size -1) {
							parent.getRequest().setDestination((Address) o);
							destination.setText(((Address) o).getName());
						} else {
							MapDialog mapDialog = new MapDialog(activity, null) {
								@Override
								public void positive(AlertDialog dialog) {
									super.positive(dialog);
									if (getAddress() != null) {
										parent.getRequest().setDestination(getAddress());
										parent.addressChanged();
										destination.setText(getAddress().getName());
									}
								}

								@Override
								public void negative(AlertDialog dialog) {
									super.negative(dialog);
								}
							};
							mapDialog.show();
						}
					}
				};
				addressSelectDialog.show();
			}
		});
		destination.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				((View) v.getParent()).performLongClick();
				return true;
			}
		});
		((View) destination.getParent()).setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (parent.getRequest().getDestination() != null) Toast.makeText(activity, parent.getRequest().getDestination().getName(), Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		if (parent.getRequest().getTime() == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis() + 30 * 60 * 1000); // +30min
			parent.getRequest().setTime(calendar.getTime());
		}

		final TextView time = root.findViewById(R.id.newrequest_info_time_et);
		time.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(parent.getRequest().getTime()));
		time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((View) v.getParent()).callOnClick();
			}
		});
		((View) time.getParent()).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				if (parent.getRequest().getTime() != null) {
					calendar.setTime(parent.getRequest().getTime());
				} else {
					calendar.setTimeInMillis(System.currentTimeMillis());
				}

				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);

				TimePickerDialog dialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						Calendar calendar = Calendar.getInstance();
						if (parent.getRequest().getTime() != null) {
							calendar.setTime(parent.getRequest().getTime());
						} else {
							calendar.setTimeInMillis(System.currentTimeMillis());
						}
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						parent.getRequest().setTime(calendar.getTime());
						time.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(parent.getRequest().getTime()));
					}
				}, hour, minute, true);
				dialog.show();
			}
		});

		final TextView date = root.findViewById(R.id.newrequest_info_date_et);
		date.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(parent.getRequest().getTime()));
		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((View) v.getParent()).callOnClick();
			}
		});
		((View) date.getParent()).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				if (parent.getRequest().getTime() != null) {
					calendar.setTime(parent.getRequest().getTime());
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
						if (parent.getRequest().getTime() != null) {
							calendar.setTime(parent.getRequest().getTime());
						} else {
							calendar.setTimeInMillis(System.currentTimeMillis());
						}
						calendar.set(Calendar.YEAR, year);
						calendar.set(Calendar.MONTH, month);
						calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						parent.getRequest().setTime(calendar.getTime());
						date.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(parent.getRequest().getTime()));
					}
				}, year, month, day);
				dialog.show();
			}
		});

		return root;
	}
}
