package com.dpendesigns.feudalwar.controllers.handlers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.dpendesigns.feudalwar.model.GameInstance;

public class PreGameHandler {
	
	GameInstance my_game = null;
	
	public PreGameHandler(){}
	
	public void init(GameContainer gc, GameInstance game) throws SlickException {
		my_game = game;
	}
	public void update(GameContainer gc, GameInstance game) throws SlickException {
		my_game = game;
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (my_game!=null){
			g.drawString(my_game.getGameName(), 8, 8);
			g.drawString("Players: ", 8, 30);
		
			my_game.getUsers();
			for (int i = 0; i < my_game.getUsers().size(); i++){ g.drawString(my_game.getUsers().get(i).getName(), 8, 46 + (i*16));}
		}
	}
}
