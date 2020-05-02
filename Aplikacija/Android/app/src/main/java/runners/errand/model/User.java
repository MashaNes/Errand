package runners.errand.model;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
	public static final int
		STATUS_NOT_RUNNING = 0,
		STATUS_RUNNING = 1;

	private int id, status, benefitRequirements;
	private String firstName, lastName, email, phone;
	private String picture;
	private float rating, minRating, maxDistance, benefitDiscount;
	private ArrayList<Achievement> achievements = new ArrayList<>();
	private ArrayList<Address> addresses = new ArrayList<>();
	private ArrayList<Benefit> benefits = new ArrayList<>();
	private ArrayList<Rating> ratings = new ArrayList<>();
	private ArrayList<Request> requests = new ArrayList<>();
	private ArrayList<Notification> notifications = new ArrayList<>();
	private ArrayList<ServicePrefs> servicePrefs = new ArrayList<>();
	private ArrayList<WorkingHours> workingHours = new ArrayList<>();
	private ArrayList<User> blockedUsers = new ArrayList<>();

	public User(@NonNull JSONObject o) {
		JSONObject user = o.optJSONObject("user");
		if (user != null) {
			this.id = user.optInt("id");
			this.email = user.optString("email");
			this.firstName = user.optString("first_name");
			this.lastName = user.optString("last_name");
			this.phone = user.optString("phone");
			this.picture = user.optString("picture");
			this.rating = (float) user.optDouble("avg_rating");
		}

		JSONArray achievements = o.optJSONArray("achievements");
		if (achievements != null) {
			for (int i = 0; i < achievements.length(); i++) {
				JSONObject achievement = achievements.optJSONObject(i);
				if (achievement != null) this.achievements.add(new Achievement(achievement));
			}
		}

		JSONArray addresses = o.optJSONArray("addresses");
		if (addresses != null) {
			for (int i = 0; i < addresses.length(); i++) {
				JSONObject address = addresses.optJSONObject(i);
				if (address != null) this.addresses.add(new Address(address));
			}
		}

		JSONArray benefits = o.optJSONArray("benefitlist");
		if (benefits != null) {
			for (int i = 0; i < benefits.length(); i++) {
				JSONObject benefit = benefits.optJSONObject(i);
				if (benefit != null) this.benefits.add(new Benefit(benefit));
			}
		}

		JSONArray ratings = o.optJSONArray("ratings");
		if (ratings != null) {
			for (int i = 0; i < ratings.length(); i++) {
				JSONObject rating = ratings.optJSONObject(i);
				if (rating != null) this.ratings.add(new Rating(rating));
			}
		}

		JSONArray requests = o.optJSONArray("requests");
		if (requests != null) {
			for (int i = 0; i < requests.length(); i++) {
				JSONObject request = requests.optJSONObject(i);
				if (request != null) this.requests.add(new Request(request));
			}
		}

		JSONArray notifications = o.optJSONArray("notifications");
		if (notifications != null) {
			for (int i = 0; i < notifications.length(); i++) {
				JSONObject notification = notifications.optJSONObject(i);
				if (notification != null) this.notifications.add(new Notification(notification));
			}
		}

		JSONArray services = o.optJSONArray("services");
		if (services != null) {
			for (int i = 0; i < services.length(); i++) {
				JSONObject service = services.optJSONObject(i);
				if (service != null) this.servicePrefs.add(new ServicePrefs(service));
			}
		}

		JSONArray workingHours = o.optJSONArray("working_hours");
		if (workingHours != null) {
			for (int i = 0; i < workingHours.length(); i++) {
				JSONObject workingHour = workingHours.optJSONObject(i);
				if (workingHour != null) this.workingHours.add(new WorkingHours(workingHour));
			}
		}

		JSONArray blockedUsers = o.optJSONArray("blocked");
		if (blockedUsers != null) {
			for (int i = 0; i < blockedUsers.length(); i++) {
				JSONObject blockedUser = blockedUsers.optJSONObject(i);
				if (blockedUser != null) {
					JSONObject tmp = new JSONObject();
					try {
						tmp.put("user", blockedUser);
						this.blockedUsers.add(new User(tmp));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

		this.benefitDiscount = (float) o.optDouble("benefit_discount");
		this.benefitRequirements = o.optInt("benefit_requirement");
	}

	public User(int id, int status, int benefitRequirements, String firstName, String lastName, String email, String phone, String picture, float rating, float minRating, float maxDistance, float benefitDiscount) {
		this.id = id;
		this.status = status;
		this.benefitRequirements = benefitRequirements;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.picture = picture;
		this.rating = rating;
		this.minRating = minRating;
		this.maxDistance = maxDistance;
		this.benefitDiscount = benefitDiscount;
	}

	public void setAchievements(ArrayList<Achievement> achievements) {
		this.achievements = achievements;
	}

	public void setAddresses(ArrayList<Address> addresses) {
		this.addresses = addresses;
	}

	public void setBenefits(ArrayList<Benefit> benefits) {
		this.benefits = benefits;
	}

	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
	}

	public void setRequests(ArrayList<Request> requests) {
		this.requests = requests;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setNotifications(ArrayList<Notification> notifications) {
		this.notifications = notifications;
	}

	public int getId() {
		return id;
	}

	public int getStatus() {
		return status;
	}

	public int getBenefitRequirements() {
		return benefitRequirements;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getPicture() {
		return picture;
	}

	public float getRating() {
		return rating;
	}

	public float getMinRating() {
		return minRating;
	}

	public float getMaxDistance() {
		return maxDistance;
	}

	public float getBenefitDiscount() {
		return benefitDiscount;
	}

	public ArrayList<Achievement> getAchievements() {
		return achievements;
	}

	public ArrayList<Address> getAddresses() {
		return addresses;
	}

	public ArrayList<Benefit> getBenefits() {
		return benefits;
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	public ArrayList<Request> getRequests() {
		return requests;
	}

	public ArrayList<Notification> getNotifications() {
		return notifications;
	}
}
