package runners.errand.model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import runners.errand.utils.ImageUtils;

public class Achievement {
    private int id, level, max_level;
    private String name_en, description_en, name_sr, description_sr, icon_b64;
    private Bitmap icon_bmp;
    private ArrayList<Condition> conditions = new ArrayList<>();

    public Achievement(@NonNull JSONObject o) {
        this.id = o.optInt("id");
        JSONObject achievement = o.optJSONObject("achievement");
        if (achievement != null) {
            this.name_en = achievement.optString("name_en");
            this.name_sr = achievement.optString("name_sr");
            this.description_en = achievement.optString("description_en");
            this.description_sr = achievement.optString("description_sr");
            this.icon_b64 = achievement.optString("icon");
            Log.e("ICON", icon_b64);
            if (icon_b64.isEmpty()) {
                Log.e("ICON", "null");
                icon_bmp = null;
            } else {
                Log.e("ICON", "decode");
                icon_bmp = ImageUtils.decode(icon_b64);
            }
            this.max_level = achievement.optInt("levels");
            JSONArray conditions = achievement.optJSONArray("conditions");
            if (conditions != null) {
                Log.e("CON", conditions.toString());
                for (int i = 0; i < conditions.length(); i++) {
                    JSONObject condition = conditions.optJSONObject(i);
                    if (condition != null) this.conditions.add(new Condition(condition));
                }
            }
        }
        this.level = o.optInt("level");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMax_level() {
        return max_level;
    }

    public void setMax_level(int max_level) {
        this.max_level = max_level;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getName_sr() {
        return name_sr;
    }

    public void setName_sr(String name_sr) {
        this.name_sr = name_sr;
    }

    public String getDescription_sr() {
        return description_sr;
    }

    public void setDescription_sr(String description_sr) {
        this.description_sr = description_sr;
    }

    public String getIcon_b64() {
        return icon_b64;
    }

    public void setIcon_b64(String icon_b64) {
        this.icon_b64 = icon_b64;
    }

    public Bitmap getIcon_bmp() {
        return icon_bmp;
    }

    public void setIcon_bmp(Bitmap icon_bmp) {
        this.icon_bmp = icon_bmp;
    }

    public String getName() {
        if (Locale.getDefault().getLanguage().equals("sr")) {
            return name_sr;
        } else {
            return name_en;
        }
    }

    public String getDescription() {
        if (Locale.getDefault().getLanguage().equals("sr")) {
            return description_sr;
        } else {
            return description_en;
        }
    }

    public String getDescriptionFormatted() {
        String desc = getDescription();
        for (int i = 0; i < conditions.size(); i++) {
            desc = desc.replace("{" + (i + 1) + "}", "" + conditions.get(i).getNumber(level));
        }
        return desc;
    }

    static class Condition {
        int id;
        ArrayList<Integer> numbers = new ArrayList<>();

        Condition(@NonNull JSONObject o) {
            this.id = o.optInt("condition");
            JSONArray numbers = o.optJSONArray("condition_numbers");
            if (numbers != null) {
                for (int i = 0; i < numbers.length(); i++) {
                    JSONObject number = numbers.optJSONObject(i);
                    if (number != null) this.numbers.add(number.optInt("condition_number"));
                }
            }
        }

        int getNumber(int index) {
            return numbers.get(index - 1);
        }
    }
}
