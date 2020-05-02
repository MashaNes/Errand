package runners.errand.model;

import android.media.Image;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Achievement {
    private int id, level;
    private String name, description;
    private Image icon;

    public Achievement(@NonNull JSONObject o) {

    }

    public Achievement(int id, int level, String name, String description) {
        this.id = id;
        this.level = level;
        this.name = name;
        this.description = description;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Image getIcon() {
        return icon;
    }
}
