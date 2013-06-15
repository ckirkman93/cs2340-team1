package com.feudalwar.model;
/**
 * @author Aris Santas<asantas93@gmail.com>
 * @version alpha 0.023
 */
public class Player extends Object{
	
	private String name;
	private String color;
	private int infantry;
	private int generals;
	
	public Player(){
	}
	
	public void setName (String value){
		name = value;
	}
	public void setColor (String value){
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
	
	public Player(String name, String color){
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
		return color;
	}
	
	public int getInfantry() {
		return infantry;
	}
	
	public int getGenerals() {
		return generals;
	}
	
}
