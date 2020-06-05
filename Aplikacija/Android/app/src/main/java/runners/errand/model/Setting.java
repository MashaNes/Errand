package runners.errand.model;

public class Setting {
	private int id, drawableId;
	private String name, description, prefKey;

	public Setting(int id, String name, String description, String prefKey, int drawableId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.prefKey = prefKey;
		this.drawableId = drawableId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPrefKey() {
		return prefKey;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}
}
