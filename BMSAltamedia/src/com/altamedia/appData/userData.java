package com.altamedia.appData;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

public class userData {
	private int id;
	public int user_id;
	private String userName;
	public String fullName;
	private String pass;
	public Date date;
	public String access_token;
	
	public String getUserName() {
		return this.userName;
	}
	public String getPass() {
		return this.pass;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		if (id > 0)
			this.id = id;
	}
	public void setUserName(String userName) {
		this.userName=userName;
	}
	public void setPass(String pass) {
		this.pass=pass;
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getDate(String fomat){
		Format formatter = new SimpleDateFormat(fomat);
		return formatter.format(this.date);
	}
	
	public Date getDate(){
		return this.date;
	}
	
	@SuppressLint("SimpleDateFormat")
	public void setDate(String dateString,String fomat){
		SimpleDateFormat dateFormat = new SimpleDateFormat(fomat);	   
	    try {
	        this.date = (Date) dateFormat.parse(dateString);
	    } catch (ParseException e) {
	        // TODO Auto-generated catch block
	       Log.e("Date pare",dateString);
	    }catch(Exception ex){
	    	 ex.printStackTrace();
	    }
	}
	public void setDate(Date date){
		this.date=date;
	}
	
	@Override
	public String toString() {
		return "USER [id=" + id + ", username=" + this.userName+", access_token: "+this.access_token + ", pass=" + this.pass +", full name= "+this.fullName+", Date=" +this.getDate("dd/MM/yyyy")
				+ "]";
	}

}
