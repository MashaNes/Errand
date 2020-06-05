package runners.errand.ui.newrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

	// TODO: JUST ONE DIRECT, CAN BE BROADCAST AND DIRECT

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_nr3, container, false);

		MainActivity activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		final NewRequestFragment parent = ((NewRequestFragment) getParentFragment());
		if (parent == null) return root;

		final TextView broadcast = root.findViewById(R.id.newrequest_offers_broadcast);
		broadcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((View) v.getParent()).callOnClick();
			}
		});
		((View) broadcast.getParent()).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.getRequest().setBroadcast(!parent.getRequest().isBroadcast());
				broadcast.setText(parent.getRequest().isBroadcast() ? getString(R.string.generic_enabled) : getString(R.string.generic_disabled));
			}
		});

		directLayout = root.findViewById(R.id.newrequest_offers_direct_layout);

		root.findViewById(R.id.newrequest_offers_direct_add).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: Add direct
				// 	If added successfully, hide the add button since only one is allowed
				// 	Needs a new fragment for a user list, and a modification to the Profile page to load other users
			}
		});

		return root;
	}

	private void loadDirect() {

	}
}
