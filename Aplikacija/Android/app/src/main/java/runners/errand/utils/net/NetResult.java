package runners.errand.utils.net;


import androidx.annotation.NonNull;

public class NetResult {
	public static final int
			TYPE_ERROR_LOCAL = 0,
			TYPE_ERROR_REMOTE = 1,
			TYPE_SUCCESS = 2;

	private int type;
	private String msg;

	NetResult(int type, String msg) {
		this.type = type;
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	@NonNull
	public String toString() {
		String type;
		switch (this.type) {
			case TYPE_ERROR_LOCAL:
				type = "TYPE_ERROR_LOCAL";
				break;
			case TYPE_ERROR_REMOTE:
				type = "TYPE_ERROR_REMOTE";
				break;
			case TYPE_SUCCESS:
				type = "TYPE_SUCCESS";
				break;
			default:
				type = "TYPE_UNKNOWN";
		}

		return "Result{" +
				"type=" + type +
				", msg='" + msg + '\'' +
				'}';
	}
}
