package runners.errand.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import runners.errand.R;
import runners.errand.model.Notification;

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Notification> notifications;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notifications.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    	View view;

    	if (convertView == null) {
    		view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
		} else {
    		view = convertView;
		}

    	if (!notifications.get(position).isOpened()) {
    		((TextView) view.findViewById(R.id.notification_title)).setTypeface(null, Typeface.BOLD);
		} else {
			((TextView) view.findViewById(R.id.notification_title)).setTypeface(null, Typeface.NORMAL);
		}
		((TextView) view.findViewById(R.id.notification_title)).setText(notifications.get(position).getTitle());
		((TextView) view.findViewById(R.id.notification_time)).setText(new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault()).format(notifications.get(position).getTime()));
		((TextView) view.findViewById(R.id.notification_body)).setText(notifications.get(position).getBody());

        return view;
    }
}
