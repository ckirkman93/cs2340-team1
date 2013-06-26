package com.dpendesigns.feudalwar.model;

public class BeginGameRequest {
	private static final long serialVersionUID = 5011L;
	public String requestedGame;
	
	public BeginGameRequest (){}
	public void setRequest (String gameName){requestedGame = gameName;}
}
