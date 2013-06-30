package com.dpendesigns.feudalwar.model;

public abstract class MilitaryUnit {
	private static final long serialVersionUID = 9400L;
	
	private Player owner;
	private int strength;
	private boolean active = false;
	
	public MilitaryUnit(){}
	public MilitaryUnit(Player owner, int strength){ 
		this.owner = owner;
		this.strength = strength;
		active = true;
		}
	
	public Player getOwner(){ return owner;}
	public int getStrength(){ return strength;}
	public boolean isActive(){return active;}
}
