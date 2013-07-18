package com.dpendesigns.feudalwar.model;

import java.awt.Point;
import java.util.Vector;

public class Player {
	private String playerName = "default_player";
	private int connection_ID;
	private int[] colors = new int[]{0xb0b0b0, 0xcacaca, 0xe0e0e0};
	
	private int infantry; 
	private int generals;
	private Vector<int[]> provinces = new Vector<int[]>();
	private Vector<Point> attackerDepartingLocations = new Vector<Point>();
	private Vector<Point> attackerDestinations = new Vector<Point>();
	private Vector<Point> supporterBaseLocations = new Vector<Point>();
	private Vector<Point> supporterDefenseLocations = new Vector<Point>();
	
	public Player(){}
	
	public Player(User user){
		playerName = user.getName();
		connection_ID = user.getConnectionID();
	}
	
	
	public void setColors (int[] values){ colors = values; }
	public void addInfantry (int amount){ infantry+= amount;}
	public void addGeneral (int amount){ generals+= amount;}
	public void addProvince(int[] province){this.provinces.add(province);}
	
	public String getName(){ return playerName; }
	public int[] getColors() { return colors; }
	public int getConnectionID() { return connection_ID; }
	
	public int getInfantry() { return infantry; }
	public int getGenerals() { return generals; }
	public Vector<int[]> getProvinces() { return provinces; }
	
	public Vector<Point> getAttackerDepartingLocations(){
		return attackerDepartingLocations;
	}
	public void setAttackerDepartingLocations(Vector<Point> locations){ 
		this.attackerDepartingLocations = locations;
	}
	
	public Vector<Point> getAttackerDestinations(){
		return attackerDestinations;
	}
	public void setAttackerDestinations(Vector<Point> locations){ 
		this.attackerDestinations = locations;
	}
	
	public Vector<Point> getSupporterBaseLocations(){
		return supporterBaseLocations;
	}
	public void setSupporterBaseLocations(Vector<Point> locations){ 
		this.supporterBaseLocations = locations;
	}
	
	public Vector<Point> getSupporterDefenseLocations(){
		return supporterDefenseLocations;
	}
	public void setSupporterDefenseLocations(Vector<Point> locations){ 
		this.supporterDefenseLocations = locations;
	}
}
