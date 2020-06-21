package runners.errand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import runners.errand.R;
import runners.errand.model.Offer;
import runners.errand.model.Request;
import runners.errand.model.ServicePrefs;
import runners.errand.model.Setting;
import runners.errand.model.User;

public class OfferAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Offer> offers;

	public OfferAdapter(Context context, ArrayList<Offer> offers) {
		this.context = context;
		this.offers = offers;
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
		((ImageView) view.findViewById(R.id.item_user_image)).setImageBitmap(user.getPicture_bmp());
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

		return view;
	}
}
