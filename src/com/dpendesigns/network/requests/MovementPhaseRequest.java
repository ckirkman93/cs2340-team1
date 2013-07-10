package com.dpendesigns.network.requests;

import java.awt.Point;
import java.util.Vector;

public class MovementPhaseRequest {
	
	private Vector<Point> attackerDepartingLocations = new Vector<Point>();
	private Vector<Point> attackerDestinations = new Vector<Point>();
	private Vector<Point> supporterBaseLocations = new Vector<Point>();
	private Vector<Point> supporterDefenseLocations = new Vector<Point>();
	
	public MovementPhaseRequest(){}
	
	public MovementPhaseRequest(Vector<Vector<Point>> locations) {
		this.attackerDepartingLocations = locations.get(0);
		this.attackerDestinations = locations.get(1);
		this.supporterBaseLocations = locations.get(2);
		this.supporterDefenseLocations = locations.get(3);
	}

	public Vector<Point> getAttackerDepartingLocations() {
		return attackerDepartingLocations;
	}

	public Vector<Point> getAttackerDestinations() {
		return attackerDestinations;
	}

	public Vector<Point> getSupporterBaseLocations() {
		return supporterBaseLocations;
	}

	public Vector<Point> getSupporterDefenseLocations() {
		return supporterDefenseLocations;
	}
	
}
