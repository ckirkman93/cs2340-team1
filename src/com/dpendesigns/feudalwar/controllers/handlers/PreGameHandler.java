package com.dpendesigns.feudalwar.controllers.handlers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.dpendesigns.feudalwar.model.GameInstance;
import com.esotericsoftware.kryonet.Client;

public class PreGameHandler {
	
	private Client client;
	private GameInstance my_game = null;
	
	private SpriteSheet backSpriteSheet;
	private Image back;
	private Shape backLocation;
	
	private final int mainMenu = 20;
	
	private boolean leftClickDownState = false;
	
	public PreGameHandler(Client client, GameInstance game) throws SlickException{
		my_game = game;
		backSpriteSheet = new SpriteSheet("res/images/BackButtonSpriteSheet.png",96,32);
		back = backSpriteSheet.getSubImage(0, 0);
		backLocation = new Rectangle(0,0,96,32);
	}
	public boolean update(GameContainer gc, GameInstance game) throws SlickException {
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		
		boolean main_menu = false;
		
		my_game = game;
		
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
	}
}
