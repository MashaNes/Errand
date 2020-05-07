package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Task {
    private int id;
    private String name, description;
    private boolean pictureRequired;
    private Address address;
    private Service service;
    private ArrayList<ChecklistItem> checklist = new ArrayList<>();

    public Task() {}

    public Task(int id, String name, String description, boolean pictureRequired, Address address, Service service) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pictureRequired = pictureRequired;
        this.address = address;
        this.service = service;
    }

    public Task(@NonNull JSONObject o) {
        this.id = o.optInt("id");

        JSONObject address = o.optJSONObject("address");
        if (address != null) this.address = new Address(address);

        JSONObject service = o.optJSONObject("service_type");
        if (service != null) this.service = new Service(service);

        this.name = o.optString("name");
        this.description = o.optString("description");
        this.pictureRequired = o.optBoolean("picture_required");

        JSONArray checklist = o.optJSONArray("checklist");
        if (checklist != null) {
            for (int i = 0; i < checklist.length(); i++) {
                JSONObject checklistItem = checklist.optJSONObject(i);
                if (checklistItem != null)
                    this.checklist.add(new ChecklistItem(
                        checklistItem.optInt("id"),
                        checklistItem.optString("check_list")
                    ));
            }
        }
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

    public ArrayList<ChecklistItem> getChecklist() {
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

    public String getBody() {
        StringBuilder result = new StringBuilder();
        result.append("");
        result.append(description);
        result.append("\n");
        for (ChecklistItem c : checklist) {
            result.append(" • ");
            result.append(c.s);
            result.append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    static class ChecklistItem {
        public int id;
        public String s;

        public ChecklistItem(int id, String s) {
            this.id = id;
            this.s = s;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictureRequired(boolean pictureRequired) {
        this.pictureRequired = pictureRequired;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setChecklist(ArrayList<ChecklistItem> checklist) {
        this.checklist = checklist;
    }
}
