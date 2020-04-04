package runners.errand.model;

import java.util.ArrayList;

public class Service {
    private int id;
    private String type, description;
    private boolean pictureRequired;
    private ArrayList<Task> tasks;

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
