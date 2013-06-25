package com.dpendesigns.feudalwar.model;

public class JoinGameRequest {
	private static final long serialVersionUID = 5010L;
	public String requestedGame;
	
	public JoinGameRequest (){}
	
	public void setRequest (String gameName){requestedGame = gameName;}
	
}
