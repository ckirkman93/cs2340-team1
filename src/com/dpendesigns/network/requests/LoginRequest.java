package com.dpendesigns.network.requests;

public class LoginRequest {
	private static final long serialVersionUID = 9010L;
	private String requestedName;
	
	public LoginRequest (){}
	public LoginRequest (String name){requestedName = name;}
	public String getRequestedName(){return requestedName;}
}
