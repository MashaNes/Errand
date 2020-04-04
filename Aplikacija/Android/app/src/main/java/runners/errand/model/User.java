package runners.errand.model;

import android.media.Image;

import java.util.ArrayList;

// In the DB there is also a list of blocked users, there is no need to have a copy locally at any
// point because DB queries will be already filtered.
public class User {
	public static final int
		STATUS_NOT_RUNNING = 0,
		STATUS_RUNNING = 1;

	private int id, status, benefitRequirements;
	private String firstName, lastName, email, phone;
	private Image picture;
	private float rating, minRating, maxDistance, benefitDiscount;
	private ArrayList<Achievement> achievements;
	private ArrayList<Address> addresses;
	private ArrayList<Benefit> benefits;
	private ArrayList<Rating> ratings;
	private ArrayList<Request> requests;
	private ArrayList<Notification> notifications;

	public User(int id, int status, int benefitRequirements, String firstName, String lastName, String email, String phone, Image picture, float rating, float minRating, float maxDistance, float benefitDiscount) {
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

	public Image getPicture() {
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
