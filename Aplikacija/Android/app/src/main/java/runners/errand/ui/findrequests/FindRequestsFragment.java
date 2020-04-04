package runners.errand.ui.findrequests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import runners.errand.MainActivity;
import runners.errand.R;

public class FindRequestsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_findrequests, container, false);

        MainActivity activity = ((MainActivity) getActivity());
        if (activity == null) return root;

        activity.noTabs();

        return root;
    }
}
