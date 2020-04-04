package runners.errand.model;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

public class Request {
    public static final int
            STATUS_PENDING = 0,
            STATUS_PAUSED = 1,
            STATUS_ACTIVE = 2,
            STATUS_COMPLETED = 3,
            STATUS_CANCELED = 4;

    private int id, status, locationStatus;
    private Date time;
    private String name, note;
    private boolean pictureRequired;
    private Service service;
    private ArrayList<Image> pictures;
    private ArrayList<Task> tasks;
    private ArrayList<Offer> offers;

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

    public void setPictures(ArrayList<Image> pictures) {
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

    public ArrayList<Image> getPictures() {
        return pictures;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }
}
