package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;

public class Task {
    private int id;
    private String name, description;
    private boolean pictureRequired;
    private Address address;
    private Service service;
    private ArrayList<String> checklist;

    public Task(@NonNull JSONObject o) {
        this.id = o.optInt("id");

        JSONObject address = o.optJSONObject("address");
        if (address != null) this.address = new Address(address);

        JSONObject service = o.optJSONObject("service");
        if (service != null) this.service = new Service(service);

        this.name = o.optString("name");
        this.description = o.optString("description");
        this.pictureRequired = o.optBoolean("picture_required");

        // TODO: Checklist, pictures
    }

    public Task(int id, String description, Address address) {
        this.id = id;
        this.description = description;
        this.address = address;
    }

    public void setChecklist(ArrayList<String> checklist) {
        this.checklist = checklist;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Address getAddress() {
        return address;
    }

    public ArrayList<String> getChecklist() {
        return checklist;
    }

    public String getName() {
        return name;
    }

    public boolean isPictureRequired() {
        return pictureRequired;
    }

    public Service getService() {
        return service;
    }
}
