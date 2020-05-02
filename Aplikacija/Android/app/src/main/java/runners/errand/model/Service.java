package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;

public class Service {
    private int id;
    private String type, description;
    private boolean pictureRequired;
    private ArrayList<Task> tasks;

    public Service(@NonNull JSONObject o) {
        this.id = o.optInt("id");
        this.type = o.optString("service_type");
        this.description = o.optString("description");
        this.pictureRequired = o.optBoolean("picture_required");
    }

    public Service(int id, String type, String description, boolean pictureRequired) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.pictureRequired = pictureRequired;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPictureRequired() {
        return pictureRequired;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
