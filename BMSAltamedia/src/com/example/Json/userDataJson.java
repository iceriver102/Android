package com.example.Json;

import org.json.JSONObject;

public class userDataJson {
	public boolean result;
	private String msg;
	private String fullName;
	public String ERR;
	private String user;
	private String pass;
	private int user_id;
	public String access_token;

	public static final String FIELD_FULL_NAME = "user_name";
	public static final String FIELD_RESULT = "result";
	public static final String FIELD_MSG = "msg";
	public static final String FIELD_ID = "user_id";
	public static final String FIELD_ACCESS = "user_access_token";

	public userDataJson(String datajson) {
		if (datajson.equals("")) {
			try {
				JSONObject json = new JSONObject(datajson);
				this.fullName = json.getString(FIELD_FULL_NAME);
				this.result = json.getBoolean(FIELD_RESULT);
				this.msg = json.getString(FIELD_MSG);
				this.ERR = "";
				this.user = "";
				this.pass = "";
				this.user_id = json.getInt(FIELD_ID);
				this.access_token = json.getString(FIELD_ACCESS);
			} catch (Exception ex) {
				result = false;
				ERR = ex.getMessage();
			}
		} else {
			result = false;
			ERR = "Chuỗi kết quả rỗng";
		}
	}

	public userDataJson(String datajson, String user, String pass) {
		try {
			JSONObject json = new JSONObject(datajson);
			this.user_id = json.getInt(FIELD_ID);
			this.fullName = json.getString(FIELD_FULL_NAME);
			this.result = json.getBoolean(FIELD_RESULT);
			this.msg = json.getString(FIELD_MSG);
			this.user = user;
			this.pass = pass;
			this.access_token = json.getString(FIELD_ACCESS);
			this.ERR = "";
		} catch (Exception ex) {
			result = false;
			ERR = ex.getMessage();
		}
	}

	public userDataJson() {
		this.ERR = "";
		this.result = false;
	}

	public void setUserId(int id) {
		this.user_id = id;
	}

	public int getUserId() {
		return this.user_id;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setFullName(String name) {
		this.fullName = name;
	}

	public String getUserName() {
		return this.user;
	}

	public String getPass() {
		return this.pass;
	}

	public void setUserName(String user) {
		this.user = user;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public String toString() {
		return "DATAJSON [user_id:" + this.user_id + ", user:" + this.user
				+ ", acess:" + this.access_token + ", fullName:"
				+ this.fullName + ", pass:" + this.pass + ", result:"
				+ this.result + ", msg:" + this.msg + "]";
	}
}
