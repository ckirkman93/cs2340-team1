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
	}
	
	public void init (){
		xDrift = 0;
		yDrift = 0;
		fillBlackList();		
		for(int i = 0; i < provinces.length; i++){
			for(int j = 0; j < provinces[i].length; j++) {
				if(!blacklist.contains(new Point(i, j))) {
					if(i % 2 == 0){
						provinces[i][j] = new Province(200 + j * Province.width, 
								25 + i * Province.choppedHeight);
					provinces[i][j].setSeat(i,j);}
					else {provinces[i][j] = new Province(200 - Province.width / 2 
							+ j * Province.width, 
							25 + i * Province.choppedHeight);
					provinces[i][j].setSeat(i,j);}
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
	
	public void configure3(Player[] playerList){
		if (playerList[0]!=null){setupPosition1(playerList[0]);}
		if (playerList[1]!=null){setupPosition2(playerList[1]);}
		if (playerList[2]!=null){setupPosition3(playerList[2]);}
		
	}
	public void configure4(Player[] playerList){
		if (playerList[0]!=null){setupPosition1(playerList[0]);}
		if (playerList[1]!=null){setupPosition3(playerList[1]);}
		if (playerList[2]!=null){setupPosition5(playerList[2]);}
		if (playerList[3]!=null){setupPosition6(playerList[3]);}
	}
	public void configure5(Player[] playerList){
		configure3(playerList);
		if (playerList[3]!=null){setupPosition5(playerList[3]);}
		if (playerList[4]!=null){setupPosition6(playerList[4]);}
	}
	public void configure6(Player[] playerList){
		configure5(playerList);
		if (playerList[5]!=null){setupPosition4(playerList[5]);}
	}
	
	public void setupPosition1(Player player){
		provinces[10][9].setOwner(player);
		provinces[11][5].setOwner(player);
		provinces[11][6].setOwner(player);
		provinces[11][8].setOwner(player);
		provinces[11][9].setOwner(player);
		provinces[12][4].setOwner(player);
		provinces[12][5].setOwner(player);
		provinces[12][6].setOwner(player);
		provinces[12][7].setOwner(player);
		provinces[12][8].setOwner(player);
		provinces[13][6].setOwner(player);
		provinces[13][7].setOwner(player);
		provinces[13][8].setOwner(player);
		provinces[14][6].setOwner(player);
		provinces[14][7].setOwner(player);
	}
	public void setupPosition2(Player player){
		provinces[6][8].setOwner(player);
		provinces[7][8].setOwner(player);
		provinces[7][9].setOwner(player);
		provinces[8][7].setOwner(player);
		provinces[8][8].setOwner(player);
		provinces[9][7].setOwner(player);
		provinces[9][8].setOwner(player);
		provinces[10][6].setOwner(player);
		provinces[10][7].setOwner(player);
		provinces[10][8].setOwner(player);
		provinces[11][7].setOwner(player);
	}
	public void setupPosition3(Player player){
		provinces[5][9].setOwner(player);
		provinces[5][10].setOwner(player);
		provinces[6][9].setOwner(player);
		provinces[6][10].setOwner(player);
		provinces[7][10].setOwner(player);
		provinces[7][11].setOwner(player);
		provinces[8][9].setOwner(player);
		provinces[8][10].setOwner(player);
		provinces[8][11].setOwner(player);
		provinces[9][9].setOwner(player);
		provinces[9][10].setOwner(player);
		provinces[9][11].setOwner(player);
		provinces[9][12].setOwner(player);
	}
	public void setupPosition4(Player player){
		provinces[0][8].setOwner(player);
		provinces[0][9].setOwner(player);
		provinces[1][8].setOwner(player);
		provinces[1][10].setOwner(player);
		provinces[2][7].setOwner(player);
		provinces[2][9].setOwner(player);
		provinces[3][8].setOwner(player);
		provinces[3][9].setOwner(player);
		provinces[3][10].setOwner(player);
		provinces[4][8].setOwner(player);
		provinces[4][9].setOwner(player);
	}
	public void setupPosition5(Player player){
		provinces[3][1].setOwner(player);
		provinces[3][2].setOwner(player);
		provinces[4][0].setOwner(player);
		provinces[4][1].setOwner(player);
		provinces[4][2].setOwner(player);
		provinces[4][3].setOwner(player);
		provinces[5][0].setOwner(player);
		provinces[5][1].setOwner(player);
		provinces[5][2].setOwner(player);
		provinces[5][3].setOwner(player);
		provinces[5][4].setOwner(player);
		provinces[6][2].setOwner(player);
		provinces[6][3].setOwner(player);
	}
	public void setupPosition6(Player player){
		provinces[0][0].setOwner(player);
		provinces[0][1].setOwner(player);
		provinces[0][2].setOwner(player);
		provinces[1][1].setOwner(player);
		provinces[1][2].setOwner(player);
		provinces[1][3].setOwner(player);
		provinces[2][1].setOwner(player);
		provinces[2][2].setOwner(player);
		provinces[2][3].setOwner(player);
		provinces[2][4].setOwner(player);
		provinces[3][3].setOwner(player);
		provinces[3][4].setOwner(player);
	}
	
	public void setDrift(int xAmount, int yAmount){
		xDrift = xAmount;
		yDrift = yAmount;
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
		blacklist.add(new Point(9, 2));
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