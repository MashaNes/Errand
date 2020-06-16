package runners.errand.model;

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
            STATUS_PAUSED = 1,
            STATUS_ACTIVE = 2,
            STATUS_COMPLETED = 3,
            STATUS_CANCELED = 4;

    private int id, status, locationStatus, requestType, directId;
    private double maxDistance, minRating;
    private Date time;
    private String name, note;
    private boolean pictureRequired;
    private boolean broadcast;
    private Service service;
    private Address destination;
    private ArrayList<String> pictures = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Offer> offers = new ArrayList<>();
    private Offer acceptedOffer;
    private User createdBy;
    private Rating rating;

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

        JSONObject rating = o.optJSONObject("rating");
        if (rating != null) this.rating = new Rating(rating);

        this.name = o.optString("name");
        this.status = o.optInt("status");
        this.locationStatus = o.optInt("location_status");
        try {
            this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(o.optString("time").replace("T", " ").substring(0, 19));
        } catch (ParseException e) {
            e.printStackTrace();
            this.time = null;
        }

        JSONObject destination = o.optJSONObject("destination");
        if (destination != null) this.destination = new Address(destination);

        this.pictureRequired = o.optBoolean("picture_required");
        this.broadcast = o.optBoolean("broadcast");
        this.note = o.optString("note");
        this.requestType = o.optInt("request_type");
        this.maxDistance = o.optDouble("max_dist");
        this.minRating = o.optDouble("min_rating");
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

    public int getDirectId() {
        return directId;
    }

    public void setDirectId(int directId) {
        this.directId = directId;
    }
}
