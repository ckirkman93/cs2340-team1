package com.risingsun.game.model;

import java.util.ArrayList;

import org.newdawn.slick.Color;
public class Player {
	
	private String name;
	private Color[] colors;
	private boolean hasTurn;	
	
	private int infantry;
	private int generals;
	private int freeArmies = 3;
	
	private ArrayList<Province> provinces;
	
	public Player() {
		provinces = new ArrayList<Province>();
		hasTurn = false;
	}
	
	public Player(String name){
		this();
		this.name = name;
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
	
	public void addProvince(Province p) {
		provinces.add(p);
	}
	
	public int getFreeArmies() {
		return freeArmies;
	}
	
	public void placeFreeArmy() {
		freeArmies--;
		infantry++;
	}
	
	public void takeTurn() {
		hasTurn = true;
		freeArmies = provinces.size()/3 + 3;
	}
	
	public void endTurn() {
		hasTurn = false;
	}
	
	public boolean hasTurn() {
		return hasTurn;
	}
}
