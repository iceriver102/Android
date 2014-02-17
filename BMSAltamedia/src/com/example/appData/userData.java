package com.example.appData;

import java.sql.Date;

public class userData {
	private int id;
	private String userName;
	public String fullName;
	private String pass;
	public Date date;

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
	@Override
	public String toString() {
		return "USER [id=" + id + ", username=" + this.userName + ", pass=" + this.pass +", full name= "+this.fullName+", Date=" +date.toString()
				+ "]";
	}

}
