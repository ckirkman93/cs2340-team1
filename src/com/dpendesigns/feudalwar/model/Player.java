package com.dpendesigns.feudalwar.model;

import org.newdawn.slick.Color;

public class Player {
	
	public int currentState;
	
	private String name;
	private Color[] colors;
	
	private int player_id;
	
	private int infantry;
	private int generals;
	
	public Player(){
	}
	
	public Player(String name){
		this.name = name;
	}
	
	public void init(int player_id,String connection_id){
		this.player_id = player_id;
		this.name=connection_id;
		currentState = 0;
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
	
	public int getCurrentState(){
		return currentState;
	}
	public void setCurrentState(int newState){
		currentState = newState;
	}
	public int getID(){
		return player_id;
	}
	
	public void nextState(){
		currentState++;
	}
	
}
