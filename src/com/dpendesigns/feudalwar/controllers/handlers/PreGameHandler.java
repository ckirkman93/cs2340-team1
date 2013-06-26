package com.dpendesigns.feudalwar.controllers.handlers;

import java.util.Collections;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.dpendesigns.feudalwar.model.BeginGameRequest;
import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.feudalwar.model.Player;

public class PreGameHandler {
	
	private GameInstance my_game = null;
	private int my_connection;
	
	private boolean isHost = false;
	
	private SpriteSheet backSpriteSheet;
	private Image back;
	private Shape backLocation;
	
	private SpriteSheet beginSpriteSheet;
	private Image begin;
	private Shape beginLocation;
	
	private BeginGameRequest beginGameRequest = null;
	
	private boolean leftClickDownState = false;
	
	public PreGameHandler(int connection_ID, GameInstance game) throws SlickException{
		my_connection = connection_ID;
		my_game = game;
		
		backSpriteSheet = new SpriteSheet("res/images/BackButtonSpriteSheet.png",96,32);
		back = backSpriteSheet.getSubImage(0, 0);
		backLocation = new Rectangle(0,0,96,32);
		
		beginSpriteSheet = new SpriteSheet("res/images/BeginButtonSpriteSheet.png",96,32);
		begin = beginSpriteSheet.getSubImage(0, 0);
		beginLocation = new Rectangle(0,0,96,32);
	}
	public boolean update(GameContainer gc, GameInstance game) throws SlickException {
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		
		boolean main_menu = false;
		
		my_game = game;
		if (my_game.getHost().getConnectionID() == my_connection){isHost = true;}
		else {isHost = false;}
		
		backLocation.setLocation(gc.getWidth()-8-96, gc.getHeight()-8-32);
		
		if (backLocation.contains(mouseX,mouseY)){
			if (input.isMouseButtonDown(0)) {
				back = backSpriteSheet.getSubImage(2, 0);
			}
			else {back = backSpriteSheet.getSubImage(1, 0);}
				
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				main_menu = true;
			}
		} else {back = backSpriteSheet.getSubImage(0, 0);}
		
		beginLocation.setLocation(gc.getWidth()-8-96, gc.getHeight()-16-64);
		
		if (beginLocation.contains(mouseX,mouseY) && my_game.getUsers().size() >=3 && isHost){
			if (input.isMouseButtonDown(0)) {
				begin = beginSpriteSheet.getSubImage(2, 0);
			}
			else {begin = beginSpriteSheet.getSubImage(1, 0);}
				
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				//MAY THE ODDS BE EVER IN YOUR FAVOR
				beginGameRequest = new BeginGameRequest();
				beginGameRequest.setRequest(my_game.getGameName());
			}
		} else {begin = beginSpriteSheet.getSubImage(0, 0);}
		
		if (gc.getInput().isMouseButtonDown(0)) {leftClickDownState = true;}
		else {leftClickDownState = false;}
		
		return main_menu;
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (my_game!=null){
			g.drawString(my_game.getGameName(), 8, 8);
			g.drawString("Players: ", 8, 30);
		
			my_game.getUsers();
			for (int i = 0; i < my_game.getUsers().size(); i++){ g.drawString(my_game.getUsers().get(i).getName(), 8, 46 + (i*16));}
		}
		
		back.draw(gc.getWidth()-8-96, gc.getHeight()-8-32);
		if (my_game.getUsers().size() >=3 && isHost){ begin.draw(gc.getWidth()-8-96, gc.getHeight()-16-64); }
	}
	
	public BeginGameRequest getRequest(){ return beginGameRequest; }
}
