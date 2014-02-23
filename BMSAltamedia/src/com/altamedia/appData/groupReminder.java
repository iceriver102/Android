package com.altamedia.appData;

import java.util.ArrayList;
import java.util.List;

public class groupReminder {
	public List<reminderData> data;
	public String title;
	public String date;
	public int count(){
		return data.size();
	}
	public groupReminder(){
		data= new ArrayList<reminderData>();
	}
}
