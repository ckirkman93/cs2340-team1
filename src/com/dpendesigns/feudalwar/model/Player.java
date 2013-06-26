package com.dpendesigns.feudalwar.model;

import org.newdawn.slick.Color;

public class Player {
	private String playerName;
	private int connection_ID;
	private int[] colors = new int[]{0xc0c0c0, 0xd0d0d0, 0xf0f0f0};
	
	private int infantry;
	private int generals;
	
	public Player(){}
	
	public Player(User user){
		playerName = user.getName();
		connection_ID = user.getConnectionID();
	}
	
	
	public void setColors (int[] values){ colors = values; }
	public void setInfantry (int value){ infantry = value; }
	public void setGenerals (int value){ generals = value; }
	
	public String toString(){ return playerName; }
	public int[] getColors() { return colors; }
	public int getConnectionID() { return connection_ID; }
	
	public int getInfantry() { return infantry; }
	public int getGenerals() { return generals; }
}
