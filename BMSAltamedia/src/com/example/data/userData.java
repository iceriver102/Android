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
	public String getPass(){
		return this.pass;
	}
	public void setEmail(String email){
		if(Validate.isEmail(email))
			this.email=email;
	}
	public String getEmail(){
		return this.email;
	}
	public void setPhone(String phone){
		if(Validate.isPhone(phone))
			this.phone_number=phone;
	}
	public String getPhone(){
		return this.phone_number;
	}
	public void setIME(String ime){
		this.IME=ime;
	}
	public String getIME(){
		return this.IME;
	}
	public void setAddress(String add){
		this.Address=add;
	}
	public String getAddress(){
		return this.Address;
	}

}
