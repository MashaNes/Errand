package runners.errand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import runners.errand.R;
import runners.errand.model.Offer;
import runners.errand.model.Request;
import runners.errand.model.ServicePrefs;
import runners.errand.model.Setting;
import runners.errand.model.Task;
import runners.errand.model.User;

public class OfferAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Offer> offers;
	private Request request;

	public OfferAdapter(Context context, ArrayList<Offer> offers, Request request) {
		this.context = context;
		this.offers = offers;
		this.request = request;
	}

	@Override
	public int getCount() {
		return offers.size();
	}

	@Override
	public Offer getItem(int position) {
		return offers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return offers.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_offer, parent, false);
		} else {
			view = convertView;
		}

		Offer offer = offers.get(position);
		User user = offer.getCreatedBy();

		// User
		if (user.getPicture_bmp() != null) {
			((ImageView) view.findViewById(R.id.item_user_image)).setImageBitmap(user.getPicture_bmp());
		} else {
			((ImageView) view.findViewById(R.id.item_user_image)).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_face));
		}
		String name = user.getFirstName() + " " + user.getLastName();
		((TextView) view.findViewById(R.id.item_user_name)).setText(name);
		String rating = Float.isNaN(user.getRating()) ? context.getString(R.string.generic_unrated) : String.format(Locale.getDefault(), "%.1f", user.getRating());
		((TextView) view.findViewById(R.id.item_user_rating)).setText(rating);
		// Offer
		String payment = "";
		payment += offer.getPaymentAmount();
		payment += " RSD ";
		payment += ServicePrefs.getPaymentTypeString(context, offer.getPaymentType());
		((TextView) view.findViewById(R.id.item_offer_payment_amount)).setText(payment);
		// Edit
		if (offer.getEdit() == null || (offer.getEdit().getTime() == null && (offer.getEdit().getTasks() == null || offer.getEdit().getTasks().size() == 0))) {
			view.findViewById(R.id.item_offer_edit_label).setVisibility(View.GONE);
			view.findViewById(R.id.item_offer_edit_request).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.item_offer_edit_label).setVisibility(View.VISIBLE);
			view.findViewById(R.id.item_offer_edit_request).setVisibility(View.VISIBLE);
			StringBuilder s = new StringBuilder();
			if (offer.getEdit().getTime() != null) {
				s.append(context.getString(R.string.newrequest_info_time)).append(": ").append(new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(offer.getEdit().getTime())).append("\n");
			}
			for (Task task : offer.getEdit().getTasks()) {
				for (Task task1 : request.getTasks()) {
					if (task.getId() == task1.getId()) {
						s.append(task1.getName()).append(": ").append(task.getAddress().getName()).append("\n");
						break;
					}
				}
			}
			s.delete(s.length() - 1, s.length());
			((TextView) view.findViewById(R.id.item_offer_edit_request)).setText(s.toString());
		}

		return view;
	}
}
