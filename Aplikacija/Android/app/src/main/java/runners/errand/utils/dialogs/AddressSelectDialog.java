package runners.errand.utils.dialogs;

import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.adapter.AddressSelectItemAdapter;
import runners.errand.model.Address;

public class AddressSelectDialog extends AlertDialog {

	protected AddressSelectDialog(MainActivity activity, final ArrayList<Address> addresses) {
		super(activity);
		ListView list = (ListView) View.inflate(activity, R.layout.dialog_address_select, null);

		AddressSelectItemAdapter adapter = new AddressSelectItemAdapter(activity, addresses);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= addresses.size()) {
					otherSelected();
				} else {
					itemSelected(addresses.get(position));
				}
			}
		});

		this.setView(list);
	}

	public void itemSelected(Address address) {
		dismiss();
	}

	public void otherSelected() {
		dismiss();
	}
}
