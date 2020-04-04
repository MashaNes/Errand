package runners.errand.model;

import java.util.ArrayList;

public class Task {
    private int id;
    private String description;
    private Address address;
    private ArrayList<String> checklist;

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
}
