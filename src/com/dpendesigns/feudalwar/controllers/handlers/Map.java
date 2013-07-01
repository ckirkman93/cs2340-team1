package com.dpendesigns.feudalwar.controllers.handlers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.dpendesigns.feudalwar.model.Infantry;
import com.dpendesigns.feudalwar.model.MilitaryUnit;
import com.dpendesigns.feudalwar.model.Player;
import com.dpendesigns.network.data.ProvinceData;

public class Map {

	public final int height = 15;
	public final int width = 13;
	
	private final ProvinceData[][] provinceDatas = new ProvinceData[height][width];

	//blacklist is list of spaces intended to be empty on the board
	private final ArrayList<Point> blacklist = new ArrayList<Point>();

	public Map() {}
	
	public void init (){
		fillBlackList();		
		for(int i = 0; i < provinceDatas.length; i++){
			for(int j = 0; j < provinceDatas[i].length; j++) {
				if(!blacklist.contains(new Point(i, j))) {
					if(i % 2 == 0){
						provinceDatas[i][j] = new ProvinceData(200 + j * ProvinceData.width, 
								25 + i * ProvinceData.choppedHeight, i ,j);
					} else {provinceDatas[i][j] = new ProvinceData(200 - ProvinceData.width / 2 
							+ j * ProvinceData.width, 
							25 + i * ProvinceData.choppedHeight, i, j);
					}
				}
			}
		}
	}

	//public void update(GameContainer gc) throws SlickException {
	//	for(ProvinceData[] p : provinceDatas)
	//		for(ProvinceData provinceData : p)
	//			if(provinceData != null) {provinceData.setDrift(xDrift,yDrift); provinceData.update(gc);}
	//}
	//public void render(GameContainer gc, Graphics g) throws SlickException {
	//	for(ProvinceData[] p : provinceDatas)
	//		for(ProvinceData provinceData : p)
	//			if(provinceData != null) provinceData.render(gc, g);
	//}
	
	public void configure3(Vector<Player> playerList){
		if (playerList.get(0)!=null){setupPosition1(playerList.get(0));}
		if (playerList.get(1)!=null){setupPosition2(playerList.get(1));}
		if (playerList.get(2)!=null){setupPosition3(playerList.get(2));}
		
	}
	public void configure4(Vector<Player>  playerList){
		if (playerList.get(0)!=null){setupPosition1(playerList.get(0));}
		if (playerList.get(1)!=null){setupPosition3(playerList.get(1));}
		if (playerList.get(2)!=null){setupPosition5(playerList.get(2));}
		if (playerList.get(3)!=null){setupPosition6(playerList.get(3));}
	}
	public void configure5(Vector<Player>  playerList){
		configure3(playerList);
		if (playerList.get(3)!=null){setupPosition5(playerList.get(3));}
		if (playerList.get(4)!=null){setupPosition6(playerList.get(4));}
	}
	public void configure6(Vector<Player>  playerList){
		configure5(playerList);
		if (playerList.get(5)!=null){setupPosition4(playerList.get(5));}
	}
	
	public void setupPosition1(Player player){
		provinceDatas[10][9].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[11][5].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[11][6].setLastOwner(player); 
		provinceDatas[11][8].setLastOwner(player); 
		provinceDatas[11][9].setLastOwner(player);
		provinceDatas[12][4].setLastOwner(player);
		provinceDatas[12][5].setLastOwner(player);
		provinceDatas[12][6].setLastOwner(player);
		provinceDatas[12][7].setLastOwner(player);
		provinceDatas[12][8].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[13][6].setLastOwner(player);
		provinceDatas[13][7].setLastOwner(player);
		provinceDatas[13][8].setLastOwner(player);
		provinceDatas[14][6].setLastOwner(player);
		provinceDatas[14][7].setLastOwner(player);
	}
	public void setupPosition2(Player player){
		provinceDatas[6][8].setLastOwner(player);
		provinceDatas[7][8].setLastOwner(player);
		provinceDatas[7][9].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[8][7].setLastOwner(player);
		provinceDatas[8][8].setLastOwner(player);
		provinceDatas[9][7].setLastOwner(player);
		provinceDatas[9][8].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[10][6].setLastOwner(player);
		provinceDatas[10][7].setLastOwner(player);
		provinceDatas[10][8].setLastOwner(player);
		provinceDatas[11][7].addOccupyingUnit(new Infantry(player), true);
	}
	public void setupPosition3(Player player){
		provinceDatas[5][9].setLastOwner(player);
		provinceDatas[5][10].setLastOwner(player);
		provinceDatas[6][9].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[6][10].setLastOwner(player);
		provinceDatas[7][10].setLastOwner(player);
		provinceDatas[7][11].setLastOwner(player);
		provinceDatas[8][9].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[8][10].setLastOwner(player);
		provinceDatas[8][11].setLastOwner(player);
		provinceDatas[9][9].setLastOwner(player);
		provinceDatas[9][10].setLastOwner(player);
		provinceDatas[9][11].setLastOwner(player);
		provinceDatas[9][10].addOccupyingUnit(new Infantry(player), true);
	}
	public void setupPosition4(Player player){
		provinceDatas[0][8].setLastOwner(player);
		provinceDatas[0][9].setLastOwner(player); 
		provinceDatas[1][8].setLastOwner(player);
		provinceDatas[1][10].setLastOwner(player); 
		provinceDatas[2][7].setLastOwner(player);
		provinceDatas[2][9].setLastOwner(player);
		provinceDatas[3][8].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[3][9].setLastOwner(player);
		provinceDatas[3][10].setLastOwner(player);
		provinceDatas[4][8].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[4][9].addOccupyingUnit(new Infantry(player), true);
	}
	public void setupPosition5(Player player){
		provinceDatas[3][1].setLastOwner(player);
		provinceDatas[3][1].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[4][0].setLastOwner(player);
		provinceDatas[4][1].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[4][2].setLastOwner(player);
		provinceDatas[4][3].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[5][0].setLastOwner(player);
		provinceDatas[5][1].setLastOwner(player);
		provinceDatas[5][2].setLastOwner(player);
		provinceDatas[5][3].setLastOwner(player);
		provinceDatas[5][4].setLastOwner(player);
		provinceDatas[6][2].setLastOwner(player);
		provinceDatas[6][3].setLastOwner(player);
	}
	public void setupPosition6(Player player){
		provinceDatas[0][0].setLastOwner(player);
		provinceDatas[0][1].setLastOwner(player);
		provinceDatas[0][2].setLastOwner(player);
		provinceDatas[1][1].setLastOwner(player);
		provinceDatas[1][2].setLastOwner(player);
		provinceDatas[1][3].setLastOwner(player);
		provinceDatas[2][1].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[2][2].setLastOwner(player);
		provinceDatas[2][3].addOccupyingUnit(new Infantry(player), true);
		provinceDatas[2][4].setLastOwner(player); 
		provinceDatas[3][3].setLastOwner(player);
		provinceDatas[3][4].addOccupyingUnit(new Infantry(player), true);
	}
	
	public ProvinceData[][] getProvinces(){ return provinceDatas; }

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