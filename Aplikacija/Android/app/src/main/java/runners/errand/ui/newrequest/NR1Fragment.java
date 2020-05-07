package runners.errand.ui.newrequest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import runners.errand.MainActivity;
import runners.errand.R;

public class NR1Fragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_nr1, container, false);

		MainActivity activity = ((MainActivity) getActivity());
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

		return root;
	}
}
