package com.dpendesigns.feudalwar.model;

import org.newdawn.slick.Color;

public class Player {
	private String playerName;
	private int connection_ID;
	private String[] colors;
	
	private int infantry;
	private int generals;
	
	public Player(){}
	
	public Player(User user){
		playerName = user.getName();
		connection_ID = user.getConnectionID();
	}
	
	
	public void setColors (String[] values){ colors = values; }
	public void setInfantry (int value){ infantry = value; }
	public void setGenerals (int value){ generals = value; }
	
	public String toString(){ return playerName; }
	public String[] getColors() { return colors; }
	public int getConnectionID() { return connection_ID; }
	
	public int getInfantry() { return infantry; }
	public int getGenerals() { return generals; }
}
