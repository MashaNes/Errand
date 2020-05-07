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

    private int id, status, locationStatus, requestType;
    private double maxDistance, minRating;
    private Date time;
    private String name, note;
    private boolean pictureRequired;
    private Service service;
    private ArrayList<String> pictures = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Offer> offers = new ArrayList<>();
    private User createdBy;

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

        // TODO: Offers

        this.name = o.optString("name");
        this.status = o.optInt("status");
        this.locationStatus = o.optInt("location_status");
        try {
            this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(o.optString("time").replace("T", " ").substring(0, 19));
        } catch (ParseException e) {
            e.printStackTrace();
            this.time = null;
        }
        this.pictureRequired = o.optBoolean("picture_required");
        this.note = o.optString("note");
        this.requestType = o.optInt("request_type");
        this.maxDistance = o.optDouble("max_dist");
        this.minRating = o.optDouble("min_rating");
    }

    public Request(int id, int status, int locationStatus, Date time, String name, String note, boolean pictureRequired, Service service) {
        this.id = id;
        this.status = status;
        this.locationStatus = locationStatus;
        this.time = time;
        this.name = name;
        this.note = note;
        this.pictureRequired = pictureRequired;
        this.service = service;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public int getLocationStatus() {
        return locationStatus;
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

    public boolean isPictureRequired() {
        return pictureRequired;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public int getRequestType() {
        return requestType;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public double getMinRating() {
        return minRating;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLocationStatus(int locationStatus) {
        this.locationStatus = locationStatus;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setMinRating(double minRating) {
        this.minRating = minRating;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPictureRequired(boolean pictureRequired) {
        this.pictureRequired = pictureRequired;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
