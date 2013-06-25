package com.risingsun.game.model;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Player {
	
	private String name;
	private Color color;
	private int infantry;
	private int generals;
	private GameContainer gc;
	private Map map;
	private ArrayList<Province> provinces;
	
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
	
	public Player(String name, GameContainer gc){
		this.name = name;
		this.gc = gc;
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
	
	private Province determineProvinceToAddArmy() {
		while(true) {
			Input input = gc.getInput();
			for(Province p : provinces) {
				if(p.getArea().contains(input.getMouseX(), input.getMouseY()));
					return p;
			}
		}
	}
	
}
