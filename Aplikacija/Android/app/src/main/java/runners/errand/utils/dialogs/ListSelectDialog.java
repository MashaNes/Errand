package runners.errand.utils.dialogs;

import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import runners.errand.MainActivity;
import runners.errand.R;

public class ListSelectDialog extends AlertDialog {

	protected ListSelectDialog(MainActivity activity, final BaseAdapter adapter, String title) {
		super(activity);
		View root = View.inflate(activity, R.layout.dialog_simple_list, null);
		
		ListView list = root.findViewById(R.id.dialog_select_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				itemSelected(adapter.getItem(position), position, adapter.getCount());
			}
		});

		((TextView) root.findViewById(R.id.dialog_select_title)).setText(title);

		root.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		this.setView(root);
	}

	public void itemSelected(Object o, int index, int size) {
		dismiss();
	}
}
