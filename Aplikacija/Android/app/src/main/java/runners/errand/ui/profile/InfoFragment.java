package runners.errand.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Achievement;
import runners.errand.model.Address;
import runners.errand.model.User;
import runners.errand.utils.ImageUtils;
import runners.errand.utils.dialogs.MapDialog;
import runners.errand.utils.dialogs.SimpleDialog;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class InfoFragment extends Fragment {
	private static final int CALLBACK_PICTURE = 638;
	private User user;
	private MainActivity activity;
	private ImageView ivEdit, ivCancel, ivPicture;
	private EditText name, lastname, email, phone;
	private View ratingLayout, achievementsLayout;
	private TextView addAddress, achievements, rating;
	private String picture_b64 = null;
	private Bitmap picture_bmp;
	private ArrayList<Address> addresses = new ArrayList<>();
	private LinearLayout addressLayout;
	private boolean editing = false, pictureChanged = false, errorOccurred = false;

	public InfoFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.fragment_info, container, false);

		activity = ((MainActivity) getActivity());
		if (activity == null) return root;

		user = activity.getUser();

		ratingLayout = root.findViewById(R.id.profile_rating_layout);
		achievementsLayout = root.findViewById(R.id.profile_achievement_layout);

		addressLayout = root.findViewById(R.id.profile_info_addresses);

		addAddress = root.findViewById(R.id.profile_info_address_add);
		addAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MapDialog mapDialog = new MapDialog(activity, null) {
					@Override
					public void positive(AlertDialog dialog) {
						addresses.add(getAddress());
						fillAddresses();
						super.positive(dialog);
					}
				};
				mapDialog.show();
			}
		});

		name = root.findViewById(R.id.profile_info_name_et);
		lastname = root.findViewById(R.id.profile_info_lastname_et);
		email = root.findViewById(R.id.profile_info_email_et);
		phone = root.findViewById(R.id.profile_info_phone_et);

		rating = root.findViewById(R.id.profile_rating_value);
		root.findViewById(R.id.profile_rating_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				assert getParentFragment() != null;
				((ProfileFragment) getParentFragment()).navigateTo(1);
			}
		});
		achievements = root.findViewById(R.id.profile_achievement_value);
		root.findViewById(R.id.profile_achievement_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				assert getParentFragment() != null;
				((ProfileFragment) getParentFragment()).navigateTo(2);
			}
		});

		ivEdit = root.findViewById(R.id.profile_info_edit);
		ivCancel = root.findViewById(R.id.profile_info_cancel);

		ivPicture = root.findViewById(R.id.profile_info_picture);

		fillData();

		return root;
	}

	private void fillData() {
		String ratingValue = String.format(Locale.getDefault(), "%.1f", user.getRating()) + " (" + String.format(Locale.getDefault(), "%d", user.getRatings().size()) + ")";

		float averageLevel = 0;
		int achievementCount = 0;
		for (Achievement a : user.getAchievements()) {
			if (a.getLevel() > 0)
				achievementCount++;
			averageLevel += a.getLevel();
		}
		averageLevel /= user.getAchievements().size();
		String achievementsValue = String.format(Locale.getDefault(), "%.1f", averageLevel) + " (" + String.format(Locale.getDefault(), "%d", achievementCount) + "/" + String.format(Locale.getDefault(), "%d", user.getAchievements().size()) + ")";

		rating.setText(ratingValue);
		achievements.setText(achievementsValue);

//		ArrayList<Address> newAddresses = new ArrayList<>();
//		boolean updateAddresses = false;
//		for (int i = 0; i < user.getAddresses().size(); i++) {
//			Address address = user.getAddresses().get(i);
//			newAddresses.add(address.clone());
//			if (addresses.size() <= i || !address.equals(addresses.get(i))) {
//				Log.e("ADDRCMP", i + ": " + address.toJSON().toString() + "; " + (addresses.size() > i ? addresses.get(i).toJSON().toString() : "null"));
//				updateAddresses = true;
//			}
//		}
//		if (updateAddresses) {
//			addresses = newAddresses;
//			fillAddresses();
//		}
		addresses.clear();
//		addresses.addAll(user.getAddresses());
		for (Address address : user.getAddresses()) {
			addresses.add(address.clone());
		}
		fillAddresses();

		if (editing && picture_bmp != null) {
			ivPicture.setImageBitmap(picture_bmp);
		} else if (user.getPicture_bmp() != null) {
			ivPicture.setImageBitmap(user.getPicture_bmp());
		} else {
			ivPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_face));
		}

		if (editing) {
			edit();
		} else {
			tide();
			name.setText(user.getFirstName());
			lastname.setText(user.getLastName());
			email.setText(user.getEmail());
			phone.setText(user.getPhone());
		}
	}

	private View.OnClickListener getAddressClickListener(final int index, final Address address) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MapDialog mapDialog = new MapDialog(activity, address) {
					@Override
					public void positive(AlertDialog dialog) {
						Address a = getAddress();
						a.setId(addresses.get(index).getId());
						addresses.set(index, getAddress());
						fillAddresses();
						super.positive(dialog);
					}
				};
				mapDialog.show();
			}
		};
	}

	private void fillAddresses() {
		Log.e("ADDRESS", addresses.size() + " " + addressLayout.getChildCount());
		addressLayout.removeViews(0, addressLayout.getChildCount());
		Log.e("ADDRESS-B", addresses.size() + " " + addressLayout.getChildCount());

		for (int i = 0; i < addresses.size(); i++) {
			final int index = i;
			final Address address = addresses.get(index);
			final View view = LayoutInflater.from(activity).inflate(R.layout.item_address, addressLayout, false);

			TextView addressItemName = view.findViewById(R.id.item_address_et);
			addressItemName.setText(address.getName());
			addressItemName.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					view.callOnClick();
				}
			});
			addressItemName.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					view.performLongClick();
					return true;
				}
			});

			view.setOnClickListener(getAddressClickListener(index, address));
			view.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					Toast.makeText(activity, address.getName(), Toast.LENGTH_SHORT).show();
					return true;
				}
			});

			ImageView addressItemRemove = view.findViewById(R.id.item_address_remove);
			addressItemRemove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					addresses.remove(index);
					fillAddresses();
				}
			});

			final ImageView addressItemHome = view.findViewById(R.id.item_address_home);
			addressItemHome.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!address.isHome()) {
//						for (int i = 0; i < addresses.size(); i++) {
//							Address a = addresses.get(i);
//							if (a.isHome()) {
//								a.setHome(false);
//								((ImageView) addressLayout.getChildAt(i).findViewById(R.id.item_address_home)).setImageDrawable(getResources().getDrawable(R.drawable.ic_home_outline));
//								break;
//							}
//						}
						addressItemHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_filled));
						address.setHome(true);
					} else {
						addressItemHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_outline));
						address.setHome(false);
					}
				}
			});

			if (editing) {
				addressItemRemove.setVisibility(View.VISIBLE);
				addressItemHome.setVisibility(View.VISIBLE);
			}

			if (address.isHome()) {
				addressItemHome.setVisibility(View.VISIBLE);
				addressItemHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_filled));
			}

			addressLayout.addView(view);
		}
	}

	@Override
	public void onDestroy() {
		cancel();
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == CALLBACK_PICTURE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
			try {
				InputStream is = activity.getContentResolver().openInputStream(data.getData());
				if (is != null) {
					picture_b64 = ImageUtils.encode(is);
					picture_bmp = ImageUtils.decode(picture_b64);
					if (picture_bmp != null) {
						ivPicture.setImageBitmap(picture_bmp);
						pictureChanged = true;
					} else {
						ivPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_face));
						SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_image_load), "null/IF-L", null);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_image_load), "fnf/IF-L", null);
			}
		}
	}

	private void openUserUpdateErrorDialog(String errorCode) {
		if (!errorOccurred) {
			errorOccurred = true;
			SimpleDialog.buildMessageDialog(activity, getString(R.string.error), getString(R.string.error_user_update), errorCode, null);
		}
	}

	private void edit() {
		editing = true;
		//achievementsLayout.setVisibility(View.GONE);
		ratingLayout.setVisibility(View.GONE);
		addAddress.setVisibility(View.VISIBLE);
		ivEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
		ivEdit.setContentDescription(getString(R.string.image_cd_save));
		ivEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				errorOccurred = false;
				tide();

				apiUserUpdate();

				for (Address address : addresses) {
					Log.e("ADDR", address.toString());
					Address a = findAddress(address.getId(), user.getAddresses());
					if (a == null) {
						apiAddressAdd(address);
					} else {
						Log.e("ADDR F", a.toString() + "  --  " + a.equals(address));
						if (!a.equals(address)) {
							apiAddressUpdate(address);
						}
					}
				}

				for (Address address : user.getAddresses()) {
					Address a = findAddress(address.getId(), addresses);
					if (a == null) {
						apiAddressDelete(address);
					}
				}

				if (!errorOccurred) {
					tide();
					apiUserGet();
				} else {
					edit();
				}
			}
		});
		ivCancel.setVisibility(View.VISIBLE);
		ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		ivPicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_PICK);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), CALLBACK_PICTURE);
			}
		});
		name.setEnabled(true);
		lastname.setEnabled(true);
		email.setEnabled(true);
		phone.setEnabled(true);
		for (int i = 0; i < addressLayout.getChildCount(); i++) {
			addressLayout.getChildAt(i).findViewById(R.id.item_address_remove).setVisibility(View.VISIBLE);
			if (addresses.get(i).isHome())
				((ImageView) addressLayout.getChildAt(i).findViewById(R.id.item_address_home)).setImageDrawable(getResources().getDrawable(R.drawable.ic_home_filled));
			else
				((ImageView) addressLayout.getChildAt(i).findViewById(R.id.item_address_home)).setImageDrawable(getResources().getDrawable(R.drawable.ic_home_outline));
			addressLayout.getChildAt(i).findViewById(R.id.item_address_home).setVisibility(View.VISIBLE);
			addressLayout.getChildAt(i).setOnClickListener(getAddressClickListener(i, addresses.get(i)));
		}
	}

	private void tide() {
		editing = false;
		addAddress.setVisibility(View.GONE);
//		achievementsLayout.setVisibility(View.VISIBLE);
		ratingLayout.setVisibility(View.VISIBLE);
		ivEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
		ivEdit.setContentDescription(getString(R.string.image_cd_edit));
		ivEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				edit();
			}
		});
		ivCancel.setVisibility(View.INVISIBLE);
		ivCancel.setOnClickListener(null);
		ivPicture.setOnClickListener(null);
		name.setEnabled(false);
		lastname.setEnabled(false);
		email.setEnabled(false);
		phone.setEnabled(false);
		for (int i = 0; i < addressLayout.getChildCount(); i++) {
			addressLayout.getChildAt(i).findViewById(R.id.item_address_remove).setVisibility(View.GONE);
			if (!addresses.get(i).isHome()) addressLayout.getChildAt(i).findViewById(R.id.item_address_home).setVisibility(View.GONE);
			addressLayout.getChildAt(i).setOnClickListener(null);
		}
	}

	private void save() {
		tide();
		user.setFirstName(name.getText().toString());
		user.setLastName(lastname.getText().toString());
		user.setEmail(email.getText().toString());
		user.setPhone(phone.getText().toString());
		if (picture_b64 != null) {
			user.setPicture_b64(picture_b64);
			user.setPicture_bmp(picture_bmp);
		}
	}

	private void cancel() {
		tide();
		name.setText(user.getFirstName());
		lastname.setText(user.getLastName());
		email.setText(user.getEmail());
		phone.setText(user.getPhone());
		if (user.getPicture_bmp() != null)
			ivPicture.setImageBitmap(user.getPicture_bmp());
		else
			ivPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_face));
//		user.getAddresses().clear();
	}

	@Nullable
	private Address findAddress(int id, ArrayList<Address> a) {
		for (Address address : a)
			if (address.getId() == id) return address;

		return null;
	}

	private void apiUserGet() {
		NetRequest request = new NetRequest(NetManager.getApiServer() + NetManager.API_USERS + user.getId() + "/", NetManager.GET) {
			@Override
			public void success() {
				try {
					user = new User(new JSONObject(getResult().getMsg()));
					activity.setUser(user);
					fillData();
				} catch (JSONException e) {
					e.printStackTrace();
					openUserUpdateErrorDialog("AUG-JSONex");
				}
			}

			@Override
			public void error() {
				openUserUpdateErrorDialog("AUG-RE");
			}
		};
		NetManager.add(request);
	}

	private void apiUserUpdate() {
		NetRequest r = new NetRequest(NetManager.getApiServer() + NetManager.API_USER_UPDATE, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
			}

			@Override
			public void error() {
				super.error();
				openUserUpdateErrorDialog("AUU-RE");
				edit();
			}
		};
		r.putParam("created_by", user.getId());
		r.putParam("email", email.getText().toString());
		r.putParam("first_name", name.getText().toString());
		r.putParam("last_name", lastname.getText().toString());
		r.putNull("password");
		r.putParam("phone", phone.getText().toString());
		r.putParam("picture", pictureChanged ? picture_b64 : null);
		r.putParam("min_rating", user.getMinRating());
		r.putParam("max_dist", user.getMaxDistance());
		r.putParam("benefit_discount", user.getBenefitDiscount());
		r.putParam("benefit_requirement", user.getBenefitRequirements());
		NetManager.add(r);
		pictureChanged = false;
	}

	private void apiAddressAdd(final Address address) {
		NetRequest req = new NetRequest(NetManager.getApiServer() + NetManager.API_ADDRESS_ADD, NetManager.POST) {
			@Override
			public void success() {
				try {
					JSONObject json = new JSONObject(getResult().getMsg());
					int id = json.optInt("id", -1);
					if (id == -1) {
						openUserUpdateErrorDialog("AAA-I");
						edit();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					openUserUpdateErrorDialog("AAA-JSONex");
					edit();
				}
				super.success();
			}

			@Override
			public void error() {
				openUserUpdateErrorDialog("AAA-RE");
				edit();
				super.error();
			}
		};
		req.putParam("created_by", user.getId());
		req.putParam("name", address.getName());
		req.putParam("longitude", address.getLng());
		req.putParam("latitude", address.getLat());
		req.putParam("home", address.isHome());
		req.putParam("arrived", address.isArrived());
		NetManager.add(req);
	}

	private void apiAddressUpdate(final Address address) {
		NetRequest req = new NetRequest(NetManager.getApiServer() + NetManager.API_ADDRESS_UPDATE, NetManager.PUT) {
			@Override
			public void success() {
				try {
					JSONObject json = new JSONObject(getResult().getMsg());
					int id = json.optInt("id", -1);
					if (id == -1) {
						openUserUpdateErrorDialog("AAU-I");
						edit();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					openUserUpdateErrorDialog("AAU-JSONex");
					edit();
				}
				super.success();
			}

			@Override
			public void error() {
				openUserUpdateErrorDialog("AAU-RE");
				edit();
				super.error();
			}
		};
		req.putParam("created_by", user.getId());
		req.putParam("address", address.getId());
		req.putParam("name", address.getName());
		req.putParam("longitude", address.getLng());
		req.putParam("latitude", address.getLat());
		req.putParam("home", address.isHome());
		req.putParam("arrived", address.isArrived());
		NetManager.add(req);
	}

	private void apiAddressDelete(final Address address) {
		NetRequest req = new NetRequest(NetManager.getApiServer() + NetManager.API_ADDRESS_REMOVE, NetManager.DELETE) {
			@Override
			public void success() {
				try {
					JSONObject json = new JSONObject(getResult().getMsg());
					if (!json.optString("detail", "").equals("success")) {
						openUserUpdateErrorDialog("AAD-S");
						edit();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					openUserUpdateErrorDialog("AAD-JSONex");
					edit();
				}
				super.success();
			}

			@Override
			public void error() {
				openUserUpdateErrorDialog("AAD-RE");
				edit();
				super.error();
			}
		};
		req.putParam("created_by", user.getId());
		req.putParam("address", address.getId());
		NetManager.add(req);
	}
}
