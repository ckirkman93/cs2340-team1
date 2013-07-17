package com.dpendesigns.network.requests;

import java.awt.Point;
import java.util.Vector;

import com.dpendesigns.feudalwar.model.MovementPair;

public class MovementPhaseRequest {
	
	private String gameName;
	private String userName;
	private Vector<Vector<MovementPair>> locations = new Vector<Vector<MovementPair>>();
	
	public MovementPhaseRequest(){}
	public MovementPhaseRequest(String gameName, String userName, Vector<Vector<MovementPair>> locations){
		this.locations = locations;
		this.gameName = gameName;
		this.userName = userName; 
	}
	
	public Vector<Vector<MovementPair>> getLocations(){
		return locations;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getGameName(){
		return gameName;
	}
}
