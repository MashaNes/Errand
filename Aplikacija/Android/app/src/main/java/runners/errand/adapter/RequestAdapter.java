package runners.errand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import runners.errand.R;
import runners.errand.model.Request;
import runners.errand.model.Task;

public class RequestAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Request> requests;

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return requests.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
		} else {
			view = convertView;
		}
		
		Request request = requests.get(position);

        ((TextView) view.findViewById(R.id.item_request_name)).setText(request.getName());
        ((TextView) view.findViewById(R.id.item_request_date)).setText(new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault()).format(request.getTime()));
        ((TextView) view.findViewById(R.id.item_request_note)).setText(request.getNote());;

        ImageView image = view.findViewById(R.id.item_request_status);

        LinearLayout services = view.findViewById(R.id.item_request_services);
        if (services.getChildCount() > 0) services.removeViews(0, services.getChildCount());
        ArrayList<Integer> serviceIds = new ArrayList<>();
        for (Task task : request.getTasks()) {
            if (serviceIds.indexOf(task.getService().getId()) == -1) {
                serviceIds.add(task.getService().getId());
                View v = LayoutInflater.from(context).inflate(R.layout.item_service, services, false);
                ((TextView) v.findViewById(R.id.item_request_service)).setText(task.getService().getType());
                services.addView(v);
            }
        }

        switch (request.getStatus()) {
            case Request.STATUS_PAUSED:
            case Request.STATUS_PENDING:
                image.setImageResource(R.drawable.ic_status_pending);
                image.setContentDescription(context.getString(R.string.image_cd_request_status) + ": pending");
                break;
            case Request.STATUS_ACTIVE:
                image.setImageResource(R.drawable.ic_status_run);
                image.setContentDescription(context.getString(R.string.image_cd_request_status) + ": active");
                break;
            case Request.STATUS_CANCELED:
                image.setImageResource(R.drawable.ic_status_canceled);
                image.setContentDescription(context.getString(R.string.image_cd_request_status) + ": canceled");
                break;
            case Request.STATUS_COMPLETED:
                image.setImageResource(R.drawable.ic_status_completed);
                image.setContentDescription(context.getString(R.string.image_cd_request_status) + ": completed");
                break;
        }

        return view;
    }
}
