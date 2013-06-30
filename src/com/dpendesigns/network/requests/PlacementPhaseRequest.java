package com.dpendesigns.network.requests;

import java.util.Vector;

public class PlacementPhaseRequest {
	private String gameName;
	private String userName;
	private Vector<int[]> placedInfantry = new Vector<int[]>(); 
	private Vector<int[]> placedGenerals = new Vector<int[]>(); 
	
	public PlacementPhaseRequest(){}
	public PlacementPhaseRequest(String gameName, String userName, Vector<int[]> placedInfantry, Vector<int[]> placedGenerals){
		this.gameName = gameName;
		this.userName = userName; 
		this.placedInfantry = placedInfantry; 
		this.placedGenerals = placedGenerals;
	}
	
	public String getUserName(){return userName;}
	public String getGameName(){return gameName;}
	public Vector<int[]> getPlacedInfantry(){return placedInfantry;}
	public Vector<int[]> getPlacedGenerals(){return placedGenerals;}
}
