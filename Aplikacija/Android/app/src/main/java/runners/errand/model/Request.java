package runners.errand.model;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Request {
    public static final int
            STATUS_PENDING = 0,
            STATUS_ACTIVE = 1,
            STATUS_COMPLETED = 2,
            STATUS_CANCELED = 3;

    private int id, status, locationStatus, requestType, dist, directId;
    private double maxDistance, minRating;
    private Date time;
    private String name, note;
    private boolean pictureRequired, ratedCreatedBy, ratedWorkingWith, finishedCreatedBy, finishedWorkingWith;
    private boolean broadcast = true;
    private Service service;
    private Address destination;
    private ArrayList<String> pictures = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Offer> offers = new ArrayList<>();
    private ArrayList<Edit> edits = new ArrayList<>();
    private Offer acceptedOffer, myOffer;
    private User createdBy, workingWith, direct;
    private Rating rating;
    private double priceLat, priceLng, price;
    private Date priceTime;

    public Request() {};

    public Request(@NonNull JSONObject o) {
        this.id = o.optInt("id");

        JSONObject service = o.optJSONObject("service_type");
        if (service != null) this.service = new Service(service);

        JSONObject createdBy = o.optJSONObject("created_by");
        if (createdBy != null) {
            JSONObject tmp = new JSONObject();
            try {
                tmp.put("user", createdBy);
                this.createdBy = new User(tmp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONArray tasks = o.optJSONArray("tasklist");
        if (tasks != null) {
            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.optJSONObject(i);
                if (task != null) this.tasks.add(new Task(task));
            }
        }

        JSONObject acceptedOffer = o.optJSONObject("accepted_offer");
        if (acceptedOffer != null) {
            this.acceptedOffer = new Offer(acceptedOffer);
        } else {
            JSONArray offers = o.optJSONArray("offers");
            if (offers != null) {
                for (int i = 0; i < offers.length(); i++) {
                    JSONObject offer = offers.optJSONObject(i);
                    if (offer != null) this.offers.add(new Offer(offer));
                }
            }
        }

        JSONArray edits = o.optJSONArray("edits");
        if (edits != null) {
            for (int i = 0; i < edits.length(); i++) {
                JSONObject edit = edits.optJSONObject(i).optJSONObject("request_edit");
                if (edit != null) this.edits.add(new Edit(edit));
            }
        }

        JSONObject workingWith = o.optJSONObject("working_with");
        if (workingWith != null) this.workingWith = new User(workingWith);

        JSONObject directUser = o.optJSONObject("direct_user");
        if (directUser != null) this.direct = new User(directUser);

        JSONObject rating = o.optJSONObject("rating");
        if (rating != null) this.rating = new Rating(rating);

        this.name = o.optString("name");
        this.status = o.optInt("status");
        this.locationStatus = o.optInt("location_status");

        if (!o.isNull("time")) {
            try {
                this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(o.optString("time").replace("T", " ").substring(0, 19));
            } catch (ParseException e) {
                e.printStackTrace();
                this.time = null;
            }
        } else {
            this.time = null;
        }

        JSONObject destination = o.optJSONObject("destination");
        if (destination != null) this.destination = new Address(destination);

        this.pictureRequired = o.optBoolean("picture_required");
        this.broadcast = o.optBoolean("broadcast");
        this.ratedCreatedBy = o.optBoolean("rated_created_by");
        this.ratedWorkingWith = o.optBoolean("rated_working_with");
        this.finishedCreatedBy = o.optBoolean("finished_created_by");
        this.finishedWorkingWith = o.optBoolean("finished_working_with");
        this.note = o.optString("note");
        this.requestType = o.optInt("request_type");
        this.maxDistance = o.optDouble("max_dist");
        this.minRating = o.optDouble("min_rating");
        this.dist = o.optInt("dist");

        if (!o.isNull("timestamp")) {
            try {
                this.priceTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(o.optString("timestamp").replace("T", " ").substring(0, 19));
            } catch (ParseException e) {
                e.printStackTrace();
                this.priceTime = null;
            }
        } else {
            this.priceTime = null;
        }

        JSONObject priceLocation = o.optJSONObject("location");
        if (priceLocation != null) {
            priceLat = priceLocation.optDouble("latitude");
            priceLng = priceLocation.optDouble("longitude");
        } else {
            priceLat = Double.NaN;
            priceLng = Double.NaN;
        }

        price = o.optDouble("price");
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public Service getService() {
        return service;
    }

    public Date getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getMinRating() {
        return minRating;
    }

    public void setMinRating(double minRating) {
        this.minRating = minRating;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    public Offer getAcceptedOffer() {
        return acceptedOffer;
    }

    public void setAcceptedOffer(Offer acceptedOffer) {
        this.acceptedOffer = acceptedOffer;
    }

    public User getWorkingWith() {
        return workingWith;
    }

    public void setWorkingWith(User workingWith) {
        this.workingWith = workingWith;
    }

    public User getDirect() {
        return direct;
    }

    public void setDirect(User direct) {
        this.direct = direct;
    }

    public int getDirectId() {
        return directId;
    }

    public void setDirectId(int directId) {
        this.directId = directId;
    }

    public Offer getMyOffer() {
        return myOffer;
    }

    public void setMyOffer(Offer myOffer) {
        this.myOffer = myOffer;
    }

    public boolean isRatedCreatedBy() {
        return ratedCreatedBy;
    }

    public void setRatedCreatedBy(boolean ratedCreatedBy) {
        this.ratedCreatedBy = ratedCreatedBy;
    }

    public boolean isRatedWorkingWith() {
        return ratedWorkingWith;
    }

    public void setRatedWorkingWith(boolean ratedWorkingWith) {
        this.ratedWorkingWith = ratedWorkingWith;
    }

    public boolean isFinishedCreatedBy() {
        return finishedCreatedBy;
    }

    public void setFinishedCreatedBy(boolean finishedCreatedBy) {
        this.finishedCreatedBy = finishedCreatedBy;
    }

    public boolean isFinishedWorkingWith() {
        return finishedWorkingWith;
    }

    public void setFinishedWorkingWith(boolean finishedWorkingWith) {
        this.finishedWorkingWith = finishedWorkingWith;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTimeString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(time);
    }

    public double getPriceLat() {
        return priceLat;
    }

    public void setPriceLat(double priceLat) {
        this.priceLat = priceLat;
    }

    public double getPriceLng() {
        return priceLng;
    }

    public void setPriceLng(double priceLng) {
        this.priceLng = priceLng;
    }

    public Date getPriceTime() {
        return priceTime;
    }

    public void setPriceTime(Date priceTime) {
        this.priceTime = priceTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Edit> getEdits() {
        return edits;
    }

    public void setEdits(ArrayList<Edit> edits) {
        this.edits = edits;
    }
}
