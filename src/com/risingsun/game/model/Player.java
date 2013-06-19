package com.risingsun.game.model;

import org.newdawn.slick.Color;

public class Player {
	
	private String name;
	private Color color;
	private int infantry;
	private int generals;
	
	public Player(){
	}
	
	public void setName (String value){
		name = value;
	}
	public void setColor (Color value){
		color = value;
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
	
	public Player(String name, Color color){
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getInfantry() {
		return infantry;
	}
	
	public int getGenerals() {
		return generals;
	}
	
}
