package runners.errand.ui.newrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.User;

public class NR3Fragment extends Fragment {
	private LinearLayout directLayout;
	private ArrayList<User> directUsers = new ArrayList<>();
	private ArrayList<User> availableUsers = new ArrayList<>();

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_nr3, container, false);

		MainActivity activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		NewRequestFragment parent = ((NewRequestFragment) getParentFragment());
		if (parent == null) return root;

		directLayout = root.findViewById(R.id.newrequest_offers_direct_layout);

		root.findViewById(R.id.newrequest_offers_direct_add).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
			}
		});

		return root;
	}

	private void loadDirect() {

	}
}
