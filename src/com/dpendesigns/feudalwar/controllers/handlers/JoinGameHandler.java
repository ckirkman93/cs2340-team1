package com.dpendesigns.feudalwar.controllers.handlers;


import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.GameListPacket;

public class JoinGameHandler {
	GameListPacket game_list;
	
	public JoinGameHandler(){}
	
	public void init(GameContainer gc, GameListPacket game_list) throws SlickException {
		this.game_list = game_list;
	}
	public void update(GameContainer gc, GameListPacket game_list) throws SlickException {this.game_list = game_list;}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.drawString("Join a Game:", 8, 8);
		for (int i = 0; i < game_list.size(); i++){
			g.drawString(game_list.get(i).getGameName(), 8, 22 + (i*16));
		}
	}
}
