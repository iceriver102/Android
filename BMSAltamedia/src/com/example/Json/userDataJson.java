package com.example.Json;

import org.json.JSONObject;

public class userDataJson {
	public boolean result;
	private String msg;
	private String fullName;
	public  String ERR;
	private String user;
	private String pass;
	
	public static final String FIELD_FULL_NAME="full_name";
	public static final String FIELD_RESULT="result";
	public static final String FIELD_MSG="msg";

	public userDataJson(String datajson) {
		try {
			JSONObject json = new JSONObject(datajson);
			this.fullName=json.getString(FIELD_FULL_NAME);
			this.result=json.getBoolean(FIELD_RESULT);
			this.msg=json.getString(FIELD_MSG);
			this.ERR="";
			this.user="";
			this.pass="";
		} catch (Exception ex) {
			result = false;
			ERR = ex.getMessage();
		}
	}
	public userDataJson(String datajson,String user,String pass) {
		try {
			JSONObject json = new JSONObject(datajson);
			this.fullName=json.getString(FIELD_FULL_NAME);
			this.result=json.getBoolean(FIELD_RESULT);
			this.msg=json.getString(FIELD_MSG);
			this.user=user;
			this.pass=pass;
			this.ERR="";
		} catch (Exception ex) {
			result = false;
			ERR = ex.getMessage();
		}
	}
	public userDataJson(){
		this.ERR="";
		this.result=false;
	}
	public String getFullName(){
		return this.fullName;
	}
	public String getMsg(){
		return this.msg;
	}
	public void setFullName(String name){
		this.fullName=name;
	}
	public String getUserName() {
		return this.user;
	}
	public String getPass() {
		return this.pass;
	}
	public void setUserName(String user) {
		this.user=user;		
	}
	public void setPass(String pass) {
		this.pass=pass;
	}
	@Override
	public String toString(){
		return "DATAJSON [user:"+this.user+", fullName:"+this.fullName+", pass:"+this.pass+", result:"+this.result+", msg:"+this.msg+"]";
	}
}
