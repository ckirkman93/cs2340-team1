package com.dpendesigns.network.requests;

public class BeginGameRequest {
	private static final long serialVersionUID = 9100L;
	private String requestedGame;
	
	public BeginGameRequest (){}
	public BeginGameRequest (String gameName){requestedGame = gameName;}
	public String getRequestedGame(){return requestedGame;}
}
