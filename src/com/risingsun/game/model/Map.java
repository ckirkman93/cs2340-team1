package com.risingsun.game.model;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Map {

	private int xDrift,yDrift;
	private final Province[][] provinces = new Province[15][13];

	//blacklist is list of spaces intended to be empty on the board
	private final ArrayList<Point> blacklist = new ArrayList<Point>();

	public Map() {
		xDrift = 0;
		yDrift = 0;
		fillBlackList();		
		for(int i = 0; i < provinces.length; i++){
			for(int j = 0; j < provinces[i].length; j++) {
				if(!blacklist.contains(new Point(i, j))) {
					if(i % 2 == 0)
						provinces[i][j] = new Province(200 + j * Province.width, 
								25 + i * Province.choppedHeight);
					else provinces[i][j] = new Province(200 - Province.width / 2 
							+ j * Province.width, 
							25 + i * Province.choppedHeight);
				}
			}
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta, Player[] playerList) throws SlickException {
		for(Province[] p : provinces)
			for(Province province : p)
				if(province != null) {province.setDrift(xDrift,yDrift); province.update(gc, sbg, delta, playerList);}
	}
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for(Province[] p : provinces)
			for(Province province : p)
				if(province != null) province.render(gc, sbg, g);
	}
	
	public void setDrift(int xAmount, int yAmount){
		xDrift = xAmount;
		yDrift = yAmount;
	}
	
	public Province[][] getProvinces() {
		return provinces;
	}

	private void fillBlackList() {
		blacklist.add(new Point(0, 4));
		blacklist.add(new Point(0, 5));
		blacklist.add(new Point(0, 6));
		blacklist.add(new Point(0, 7));
		blacklist.add(new Point(0, 10));
		blacklist.add(new Point(0, 11));
		blacklist.add(new Point(0, 12));
		blacklist.add(new Point(1, 0));
		blacklist.add(new Point(1, 6));
		blacklist.add(new Point(1, 7));
		blacklist.add(new Point(1, 9));
		blacklist.add(new Point(1, 11));
		blacklist.add(new Point(1, 12));
		blacklist.add(new Point(2, 0));
		blacklist.add(new Point(2, 8));
		blacklist.add(new Point(2, 10));
		blacklist.add(new Point(2, 11));
		blacklist.add(new Point(2, 12));
		blacklist.add(new Point(3, 0));
		blacklist.add(new Point(3, 12));
		blacklist.add(new Point(4, 12));
		blacklist.add(new Point(5, 12));
		blacklist.add(new Point(6, 0));
		blacklist.add(new Point(6, 1));
		blacklist.add(new Point(6, 11));
		blacklist.add(new Point(6, 12));
		blacklist.add(new Point(7, 0));
		blacklist.add(new Point(7, 1));
		blacklist.add(new Point(7, 2));
		blacklist.add(new Point(7, 4));
		blacklist.add(new Point(7, 5));
		blacklist.add(new Point(7, 12));
		blacklist.add(new Point(8, 0));
		blacklist.add(new Point(8, 1));
		blacklist.add(new Point(8, 3));
		blacklist.add(new Point(8, 4));
		blacklist.add(new Point(8, 12));
		blacklist.add(new Point(9, 0));
		blacklist.add(new Point(9, 1));
		blacklist.add(new Point(9, 3));
		blacklist.add(new Point(9, 4));
		blacklist.add(new Point(10, 0));
		blacklist.add(new Point(10, 1));
		blacklist.add(new Point(10, 2));
		blacklist.add(new Point(10, 3));
		blacklist.add(new Point(10, 10));
		blacklist.add(new Point(10, 11));
		blacklist.add(new Point(10, 12));
		blacklist.add(new Point(11, 0));
		blacklist.add(new Point(11, 1));
		blacklist.add(new Point(11, 2));
		blacklist.add(new Point(11, 3));
		blacklist.add(new Point(11, 10));
		blacklist.add(new Point(11, 11));
		blacklist.add(new Point(11, 12));
		blacklist.add(new Point(12, 0));
		blacklist.add(new Point(12, 1));
		blacklist.add(new Point(12, 2));
		blacklist.add(new Point(12, 3));
		blacklist.add(new Point(12, 9));
		blacklist.add(new Point(12, 10));
		blacklist.add(new Point(12, 11));
		blacklist.add(new Point(12, 12));
		blacklist.add(new Point(13, 0));		
		blacklist.add(new Point(13, 1));		
		blacklist.add(new Point(13, 2));		
		blacklist.add(new Point(13, 3));		
		blacklist.add(new Point(13, 4));		
		blacklist.add(new Point(13, 5));		
		blacklist.add(new Point(13, 9));		
		blacklist.add(new Point(13, 10));		
		blacklist.add(new Point(13, 11));		
		blacklist.add(new Point(13, 12));		
		blacklist.add(new Point(14, 0));		
		blacklist.add(new Point(14, 1));		
		blacklist.add(new Point(14, 2));		
		blacklist.add(new Point(14, 3));		
		blacklist.add(new Point(14, 4));		
		blacklist.add(new Point(14, 5));		
		blacklist.add(new Point(14, 8));		
		blacklist.add(new Point(14, 9));		
		blacklist.add(new Point(14, 10));		
		blacklist.add(new Point(14, 11));		
		blacklist.add(new Point(14, 12));					
	}
}