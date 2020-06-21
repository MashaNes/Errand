package runners.errand.ui.newrequest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
				ListSelectDialog addressSelectDialog = new ListSelectDialog(activity, new AddressSelectItemAdapter(activity, activity.getUser().getAddresses()), activity.getString(R.string.newrequest_task_address_default) + ":") {
					@Override
					public void itemSelected(Object o, int index, int size) {
						super.itemSelected(o, index, size);
						if (index < size -1) {
							parent.getRequest().setDestination((Address) o);
							((TextView) destination).setText(((Address) o).getName());
						} else {
							MapDialog mapDialog = new MapDialog(activity, null) {
								@Override
								public void positive(AlertDialog dialog) {
									super.positive(dialog);
									if (getAddress() != null) {
										parent.getRequest().setDestination(getAddress());
										parent.addressChanged();
										((TextView) destination).setText(getAddress().getName());
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

		return root;
	}
}
