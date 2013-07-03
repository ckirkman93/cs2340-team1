package com.dpendesigns.feudalwar.controllers.handlers;

import java.util.Vector;

import org.newdawn.slick.SlickException;

import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.Player;

public class BeginGameHandler{
	public BeginGameHandler(){}
	
	public void assignPlayers(GameInstance game) throws SlickException{
		
		Vector<Player> players = new Vector<Player>();
		
		for (int i = 0; i < game.getUsers().size(); i++){
			if (i==0){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors( new int[]{0xCC3333, 0xCC6666, 0xCCAAAA} );}
			else if (i==1){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0xCCCC33, 0xCCCC66, 0xCCCCAA});}
			else if (i==2){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x33CC33, 0x66CC66, 0xAACCAA});}
			else if (i==3){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x3333CC, 0x6666CC, 0xAAAACC});}
			else if (i==4){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0xCC33CC, 0xCC66CC, 0xCCAACC});}
			else if (i==5){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x33CCCC, 0x66CCCC, 0xAACCCC});}
		}
		
		game.init(players);
	}
}
