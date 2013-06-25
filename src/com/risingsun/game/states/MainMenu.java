package com.risingsun.game.states;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.*;

public class MainMenu extends BasicGameState{
	
	private Image mainMenuTitle;
	private SpriteSheet newGameSpriteSheet;
	private SpriteSheet exitSpriteSheet;
	private Image newGame;
	private Image exit;
	
	private Shape newGameLocation;
	private Shape exitLocation;
	
	private boolean leftClickDownState = false;
	
	private int gcMiddle;
	
	public MainMenu(int state){	
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		gcMiddle = gc.getWidth()/2;
		
		mainMenuTitle = new Image("res/images/MainMenuTitle.png");
		newGameSpriteSheet = new SpriteSheet("res/images/NewGameButton.png",160,32);
		exitSpriteSheet = new SpriteSheet("res/images/ExitButton.png",64,32);
		
		newGame = newGameSpriteSheet.getSubImage(0, 0);
		exit = exitSpriteSheet.getSubImage(0, 0);
		
		newGameLocation = new Rectangle(0,0,160,32);
		exitLocation = new Rectangle(0,0,64,32);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		gcMiddle = gc.getWidth()/2;
		
		newGameLocation.setLocation(gcMiddle - 80, 120);
		exitLocation.setLocation(gcMiddle - 32, 160);
		
		if (input.isMouseButtonDown(0)) {leftClickDownState = true;}
		
		if (newGameLocation.contains(xpos, ypos)){
			if (input.isMouseButtonDown(0)) {
				newGame = newGameSpriteSheet.getSubImage(2, 0);
				}
			else {newGame = newGameSpriteSheet.getSubImage(1, 0);}
			
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				sbg.enterState(1);
			}
		}
		else {newGame = newGameSpriteSheet.getSubImage(0, 0);}
		
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
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		mainMenuTitle.draw(gcMiddle - 128, 28);
		newGame.draw(gcMiddle - 80,120);
		exit.draw(gcMiddle - 32, 160);
		
		//g.draw(newGameLocation);
		//g.draw(exitLocation);
	}
	
	public int getID(){
		return 0;
	}

}
