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
			if (i==0){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors( new int[]{0xFF0000, 0xFF2626, 0xFF5757} );}
			else if (i==1){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0xFFFF00, 0xFFFF26, 0xFFFF57});}
			else if (i==2){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x00FF00, 0x26FF26, 0x57FF57});}
			else if (i==3){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x0000FF, 0x2626FF, 0x5757FF});}
			else if (i==4){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0xFF00FF, 0xFF26FF, 0xFF57FF});}
			else if (i==5){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x00FFFF, 0x26FFFF, 0x57FFFF});}
		}
		
		game.init(players);
	}
}
