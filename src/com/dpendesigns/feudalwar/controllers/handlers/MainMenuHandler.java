package com.dpendesigns.feudalwar.controllers.handlers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuHandler {
	private Image mainMenuTitle;
	private SpriteSheet joinGameSpriteSheet;
	private SpriteSheet hostGameSpriteSheet;
	private SpriteSheet exitSpriteSheet;
	private Image joinGame;
	private Image hostGame;
	private Image exit;
	
	private Shape joinGameLocation;
	private Shape hostGameLocation;
	private Shape exitLocation;
	
	private boolean leftClickDownState = false;
	
	private int gcMiddle;
	
	private final int joinGameStatus = 1;
	private final int hostGameStatus = 2;
	private final int doNothingStatus = 0;
	
	public MainMenuHandler(){}
	
	public void init(GameContainer gc) throws SlickException{
		gcMiddle = gc.getWidth()/2;
		
		mainMenuTitle = new Image("res/images/MainMenuTitle.png");
		joinGameSpriteSheet = new SpriteSheet("res/images/NewGameButton.png",160,32);
		hostGameSpriteSheet = new SpriteSheet("res/images/NewGameButton.png",160,32);
		exitSpriteSheet = new SpriteSheet("res/images/ExitButton.png",64,32);
		
		joinGame = joinGameSpriteSheet.getSubImage(0, 0);
		hostGame = hostGameSpriteSheet.getSubImage(0, 0);
		exit = exitSpriteSheet.getSubImage(0, 0);
		
		joinGameLocation = new Rectangle(0,0,160,32);
		hostGameLocation = new Rectangle(0,0,160,32);
		exitLocation = new Rectangle(0,0,64,32);
	}
	
	public int update(GameContainer gc) throws SlickException{
		int currentStatus = doNothingStatus;
		
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		gcMiddle = gc.getWidth()/2;
		
		joinGameLocation.setLocation(gcMiddle - 80, 120);
		hostGameLocation.setLocation(gcMiddle - 80, 160);
		exitLocation.setLocation(gcMiddle - 32, 200);
		
		if (input.isMouseButtonDown(0)) {leftClickDownState = true;}
		
		if (joinGameLocation.contains(xpos, ypos)){
			if (input.isMouseButtonDown(0)) {
				joinGame = joinGameSpriteSheet.getSubImage(2, 0);
				}
			else {joinGame = joinGameSpriteSheet.getSubImage(1, 0);}
			
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				currentStatus = joinGameStatus;
				//System.out.println("JOIN GAME");
			}
		}
		else {joinGame = joinGameSpriteSheet.getSubImage(0, 0);}
		
		if (hostGameLocation.contains(xpos, ypos)){
			if (input.isMouseButtonDown(0)) {
				hostGame = hostGameSpriteSheet.getSubImage(2, 0);
				}
			else {hostGame = hostGameSpriteSheet.getSubImage(1, 0);}
			
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				currentStatus = hostGameStatus;
				//System.out.println("HOST GAME");
			}
		}
		else {hostGame = hostGameSpriteSheet.getSubImage(0, 0);}
		
		if (exitLocation.contains(xpos, ypos)){
			if (input.isMouseButtonDown(0)) {
				exit = exitSpriteSheet.getSubImage(2, 0);
				}
			else {exit = exitSpriteSheet.getSubImage(1, 0);}
			
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				
				System.exit(0);
			}
		}
		else {exit = exitSpriteSheet.getSubImage(0, 0);}
		
		if (!input.isMouseButtonDown(0)) {leftClickDownState = false;}
		
		return currentStatus;
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException{
		g.drawString("X: "+gc.getInput().getMouseX()+", Y: "+gc.getInput().getMouseY(), 8, 8);
		
		mainMenuTitle.draw(gcMiddle - 128, 28);
		joinGame.draw(gcMiddle - 80,120);
		hostGame.draw(gcMiddle - 80,160);
		exit.draw(gcMiddle - 32, 200);
		
		//g.draw(newGameLocation);
		//g.draw(exitLocation);
	}
}
