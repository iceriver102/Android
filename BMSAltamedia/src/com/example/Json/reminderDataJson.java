package com.example.Json;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.example.appData.reminderData;

import android.annotation.SuppressLint;
import android.util.Log;

public class reminderDataJson {
	public int id;
	public String type;
	public String title;
	public String content;
	public boolean status;
	public int canComlete;
	private Date time;

	public static final String FIELD_ID = "id";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_TIME = "time";

	public reminderDataJson(String jsonString) {
		if (!jsonString.equals("")) {
			try {
				JSONObject json = new JSONObject(jsonString);
				this.id = json.getInt(FIELD_ID);
				this.title = json.getString(FIELD_TITLE);
				this.content = json.getString(FIELD_CONTENT);
				this.canComlete = json.getInt(FIELD_STATUS);
				this.setTime(json.getString(FIELD_TIME), "dd/MM/yyyy");
				this.type=json.getString(FIELD_TYPE);
				this.status = false;
			} catch (Exception ex) {
				Log.e("REMINDER", "không thể khởi tạo[" + jsonString + "]");
			}
			Log.d("REMINDER",this.toString());
			Log.d("JSON",jsonString);
		}
	}

	public reminderDataJson(JSONObject json) {
		try {
			//
			this.id = Integer.parseInt(json.getString(FIELD_ID));
			this.title = json.getString(FIELD_TITLE);
			this.content = json.getString(FIELD_CONTENT);
			this.canComlete = json.getInt(FIELD_STATUS);
			this.setTime(json.getString(FIELD_TIME), "dd/MM/yyyy");
			this.type=json.getString(FIELD_TYPE);
			this.status = false;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Log.d("REMINDER",this.toString());
	}

	public reminderDataJson() {
		this.status = false;
	}

	@SuppressLint("SimpleDateFormat")
	public String getTime(String format) {
		Format formatter = new SimpleDateFormat(format);
		return formatter.format(this.time);
	}

	public Date getTime() {
		return this.time;
	}

	@SuppressLint("SimpleDateFormat")
	public void setTime(String time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			this.time = (Date) dateFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e("Date pare", time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public reminderData convertReminder() {
		return new reminderData(this);
	}
	@Override
	public String toString(){
		return "REMINDER [id:"+this.id+", title:"+this.title+", content:"+this.content+", type:"+this.type+"]";
	}

}
