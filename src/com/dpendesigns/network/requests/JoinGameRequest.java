package com.dpendesigns.network.requests;

public class JoinGameRequest {
	private static final long serialVersionUID = 9020L;
	private String requestedGame;
	
	public JoinGameRequest (){}
	public JoinGameRequest (String gameName){requestedGame = gameName;}
	public String getRequestedGame(){return requestedGame;}
}
