package com.example.data;

public class userData {
	public String full_name;
	public String user_name;
	private String pass;
	private String phone_number;
	private String email;
	private String Address;
	private String IME;
	
	public void setPass(String pass){
		this.pass=encodeMD5.md5(pass);
	}
	
	public String setPass(){
		return this.pass;
	}
	
	public void setEmail(String email){
		this.email=email;
	}
	
	public String getEmail(){
		return this.email;
	}
	public void setPhone(String phone){
		
	}


}
