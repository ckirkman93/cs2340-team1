package com.dpendesigns.network.responses;

public class LoginResponse {
	private static final long serialVersionUID = 9011L;
	private int loginResponse;
	
	public LoginResponse (){}
	public LoginResponse (int response){loginResponse = response;}
	public int getLoginResponse(){return loginResponse;}
}
