package runners.errand.model;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Edit {
	private int id, requestId;
	private Date time;
	private ArrayList<Task> tasks = new ArrayList<>();

	public Edit(@NonNull JSONObject o) {
		Log.e("EDIT", o.toString());
		this.id = o.optInt("id");
		this.requestId = o.optInt("requestId");
		JSONArray tasks = o.optJSONArray("tasks");
		if (tasks != null) {
			for (int i = 0; i < tasks.length(); i++) {
				JSONObject task = tasks.optJSONObject(i);
				if (task != null) {
					this.tasks.add(new Task(task));
					this.tasks.get(this.tasks.size() - 1).setId(task.optInt("task"));
				}
			}
		}
		try {
			String t = o.optString("time");
			if (t.length() > 19) {
				t = t.replace("T", " ").substring(0, 19);
				this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(t);
			} else {
				this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(t);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			this.time = null;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
}
