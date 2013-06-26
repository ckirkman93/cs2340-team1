package com.dpendesigns.feudalwar.model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Polygon;

public class ProvinceData {

	public static final int width = 39;
	public static final int height = 48;
	public static final int choppedHeight = 36;
	
	private int xDefaultPosition;
	private int yDefaultPosition;
	
	private int xDrift;
	private int yDrift;
	
	private int ipos,jpos;

	private Player owner = new Player();
	private int infantry = 0;
	
	public ProvinceData(){}
	
	public ProvinceData (int x, int y) {

		xDefaultPosition = x;
		yDefaultPosition = y;
		
		xDrift = 0;
		yDrift = 0;
	}
	
	public void setDrift(int xAmount, int yAmount){
		xDrift = xAmount;
		yDrift = yAmount;
	}
	
	public void setOwner(Player player){
		owner = player;
	}
	
	public void setSeat(int ipos, int jpos){
		this.ipos=ipos;
		this.jpos=jpos;
	}
	
	public void addInfantry(int amount){
		infantry+=amount;
	}
	
	public int getXDefault(){ return xDefaultPosition; }
	public int getYDefault(){ return yDefaultPosition; }
	public Player getOwner(){ return owner; }
	public int iPos(){ return ipos; }
	public int jPos(){ return jpos; }
	public int getXDrift(){ return xDrift; }
	public int getYDrift(){ return yDrift; }
	public int getInfantry(){ return infantry; }
}
