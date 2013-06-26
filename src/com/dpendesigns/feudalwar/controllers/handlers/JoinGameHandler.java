package com.dpendesigns.feudalwar.controllers.handlers;


import java.awt.Font;
import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.GameListPacket;
import com.esotericsoftware.kryonet.Client;

public class JoinGameHandler {
	Vector<JoinGameData> joinGameData;
	
	private Client client;
	
	private SpriteSheet backSpriteSheet;
	private Image back;
	private Shape backLocation;
	
	private final int mainMenu = 20;
	
	private boolean joinGameBounced = false;
	
	private boolean leftClickDownState = false;
	
	Font font = new Font("Arial", Font.PLAIN, 30);
	TrueTypeFont ttf = new TrueTypeFont(font, true);
	
	public JoinGameHandler(Client client) throws SlickException {
		this.client = client;
		
		joinGameData = new Vector<JoinGameData>();
		
		backSpriteSheet = new SpriteSheet("res/images/BackButtonSpriteSheet.png",96,32);
		back = backSpriteSheet.getSubImage(0, 0);
		backLocation = new Rectangle(0,0,96,32);
	}
	
	public String update(GameContainer gc, GameListPacket game_list, boolean joinGameBounced) throws SlickException {
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		
		String joinGame = "null";
		this.joinGameBounced = joinGameBounced;
		joinGameData = new Vector<JoinGameData>();
		
		backLocation.setLocation(gc.getWidth()-8-96, gc.getHeight()-8-32);
		
		if (backLocation.contains(mouseX,mouseY)){
			if (input.isMouseButtonDown(0)) {
				back = backSpriteSheet.getSubImage(2, 0);
			}
			else {back = backSpriteSheet.getSubImage(1, 0);}
				
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				client.sendTCP(mainMenu);
			}
		} else {back = backSpriteSheet.getSubImage(0, 0);}
		
		for (int i = 0; i < game_list.size(); i++){
			if (game_list.get(i).getUsers().size() < 6){
				joinGameData.add(i, new JoinGameData());
				String joinChoice = joinGameData.get(i).update(game_list.get(i), input, 8, 42 + (i*32), leftClickDownState);
				if (joinChoice!="null"){ joinGame = joinChoice; }
			}
		}
		
		if (gc.getInput().isMouseButtonDown(0)) {leftClickDownState = true;}
		else {leftClickDownState = false;}
		
		return joinGame;
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setFont(ttf);
		if (!joinGameBounced){ g.drawString("Join a Game:", 8, 6);}
		else { g.drawString("Game Unavailable, Please Try Again:", 8, 6); }
		for (JoinGameData joinOption: joinGameData){
			joinOption.render(g);
		}
		back.draw(gc.getWidth()-8-96, gc.getHeight()-8-32);
	}
	
	private class JoinGameData {
		int xpos,ypos;

		private SpriteSheet joinSpriteSheet;
		private Image join;
		private Shape joinLocation;
		
		private String gameName;
		
		private JoinGameData() throws SlickException{
			joinSpriteSheet = new SpriteSheet("res/images/JoinButtonSpriteSheet.png",96,32);
			join = joinSpriteSheet.getSubImage(0, 0);
			joinLocation = new Rectangle(0,0,96,32);
		}
		public String update(GameInstance game, Input input, int xpos, int ypos, boolean leftClickDownState) throws SlickException {
			gameName = game.getGameName();
			this.xpos = xpos;
			this.ypos = ypos;
			
			int mouseX = input.getMouseX();
			int mouseY = input.getMouseY();
			
			joinLocation.setLocation(xpos , ypos);
			
			if (joinLocation.contains(mouseX,mouseY)){
				if (input.isMouseButtonDown(0)) {
					join = joinSpriteSheet.getSubImage(2, 0);
				}
				else {join = joinSpriteSheet.getSubImage(1, 0);}
					
				if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
					return gameName;
				}
			} else {join = joinSpriteSheet.getSubImage(0, 0);}
			
			return "null";
		}
		public void render(Graphics g) throws SlickException{
			g.drawString(gameName, xpos + 102, ypos -2);
			join.draw(xpos,ypos);
		}
	}
}
