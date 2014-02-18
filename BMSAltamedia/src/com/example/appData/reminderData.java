package com.example.appData;

import android.annotation.SuppressLint;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.Json.reminderDataJson;

public class reminderData {
	private int msg_id;
	public String title;
	public String content;
	private int status;
	private Date date;
	private String type;
	public int canComplete;

	public reminderData() {

	}

	public reminderData(int msg_id, String title, String content, int status,
			String type) {
		this.msg_id = msg_id;
		this.title = title;
		this.content = content;
		this.status = status;
		this.type = type;
	}
	public reminderData(reminderDataJson dataJson) {
		this.msg_id = dataJson.id;
		this.title = dataJson.title;
		this.content = dataJson.content;
		if (dataJson.status)
			this.status = 1;
		else
			this.status = 0;
		this.date = dataJson.getTime();
		this.type=dataJson.type;
		this.canComplete=dataJson.canComlete;
	}

	public void complete() {
		if (this.canComplete == 1)
			this.status = 1;
	}

	public boolean isComplete() {
		if (this.status == 1)
			return true;
		return false;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getID() {
		return this.msg_id;
	}

	public void setID(int id) {
		if (id > 0)
			this.msg_id = id;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	@SuppressLint("SimpleDateFormat")
	public String getDate(String fomat) {
		Format formatter = new SimpleDateFormat(fomat);
		return formatter.format(this.date);
	}

	public Date getDate() {
		return this.date;
	}

	@SuppressLint("SimpleDateFormat")
	public void setDate(String dateString, String fomat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fomat);
		try {
			this.date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "REMINDER[ id:" + this.msg_id + ", title:" + this.title
				+ ", content:" + this.content + ", status:" + this.status
				+ ", type:" + this.type + ", date:"
				+ this.getDate("dd/MM/yyyy") + "]";
	}

}
