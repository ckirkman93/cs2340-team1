package com.dpendesigns.network.data;

import java.awt.Point;
import java.util.Vector;

import com.dpendesigns.feudalwar.model.General;
import com.dpendesigns.feudalwar.model.Infantry;
import com.dpendesigns.feudalwar.model.MilitaryUnit;
import com.dpendesigns.feudalwar.model.Player;

public class ProvinceData {

	public static final int width = 38;
	public static final int height = 48;
	public static final int choppedHeight = 35;
	
	private int xDefaultPosition, yDefaultPosition;
	private int iPosition, jPosition;
	private int[] thisLocation;
	
	private int xDrift;
	private int yDrift;
	private Vector<Point> adjacents = new Vector<Point>();

	private Player lastOwner = new Player();
	
	private MilitaryUnit occupyingUnit;
	
	public ProvinceData(){}
	
	public ProvinceData (int xDefaultPosition, int yDefaultPosition, int iPosition, int jPosition) {
		this.xDefaultPosition = xDefaultPosition;
		this.yDefaultPosition = yDefaultPosition;
		
		this.iPosition = iPosition;
		this.jPosition = jPosition;
		
		thisLocation = new int[]{iPosition,jPosition};
		
		xDrift = 0;
		yDrift = 0;
	}
	
	public void addAdjacent(Point adjacent) {
		adjacents.add(adjacent);
	}
	
	public Vector<Point> getAdjacents() {
		return adjacents;
	}
	
	public void setDrift(int xAmount, int yAmount){
		xDrift = xAmount;
		yDrift = yAmount;
	}
	
	public void setLastOwner(Player player){ 
		lastOwner = player;
		if(!lastOwner.getProvinces().contains(thisLocation)){
			lastOwner.getProvinces().add(thisLocation);
		}
	}
	public void addOccupyingUnit(MilitaryUnit unit, boolean created){ 
		occupyingUnit = unit; 
		if (unit.isActive()){
			lastOwner = unit.getOwner();
			if(!lastOwner.getProvinces().contains(thisLocation)){
				lastOwner.getProvinces().add(thisLocation);
			}
		}
		if (created){
			if (unit instanceof Infantry){lastOwner.addInfantry(1);}
			else if (unit instanceof General){lastOwner.addGeneral(1);}
		}
	}
	
	public int getXDefault(){ return xDefaultPosition; }
	public int getYDefault(){ return yDefaultPosition; }
	public Player getLastOwner(){ return lastOwner; }
	public int iPosition(){ return iPosition; }
	public int jPosition(){ return jPosition; }
	public int getXDrift(){ return xDrift; }
	public int getYDrift(){ return yDrift; }
	
	public int[] getThisLocation(){return thisLocation;}
	
	public MilitaryUnit getOccupyingUnit(){ return occupyingUnit;}
}
