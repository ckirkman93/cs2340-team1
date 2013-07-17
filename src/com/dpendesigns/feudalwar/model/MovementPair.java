package com.dpendesigns.feudalwar.model;

import java.awt.Point;

public class MovementPair {
	private Point baseLocation;
	private Point destination;
	
	public MovementPair(Point baseLocation, Point destination){
		this.baseLocation = baseLocation;
		this.destination = destination;
	}
	
	public Point getBaseLocation(){
		return baseLocation;
	}
	
	public Point getDestination(){
		return destination;
	}
}