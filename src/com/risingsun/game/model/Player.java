package com.risingsun.game.model;

import org.newdawn.slick.Color;

public class Player {
	
	private String name;
	private Color[] colors;
	
	
	private int infantry;
	private int generals;
	
	public Player(){
	}
	
	public void setName (String value){
		name = value;
	}
	public void setColors (Color[] values){
		colors = values;
	}
	public void setInfantry (int value){
		infantry = value;
	}
	public void setGenerals (int value){
		generals = value;
	}
	
	public String toString(){
		return name;
	}
	
	public Player(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Color[] getColors() {
		return colors;
	}
	
	public int getInfantry() {
		return infantry;
	}
	
	public int getGenerals() {
		return generals;
	}
	
}
