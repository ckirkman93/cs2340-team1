package com.risingsun.game;

import com.risingsun.game.model.Player;
import com.risingsun.game.states.*;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class RisingSun extends StateBasedGame {

	public static final String title = "Rising Sun: Warring States!";
			
	public static final int mainmenu = 0;
	public static final int maingame = 1;
	
	public Player[] playerList;
	
	public RisingSun(){
		super(title);
		this.addState(new MainMenu(mainmenu));
		this.addState(new MainGame(maingame));
	}
	
	public RisingSun(String title){
		super(title);
		this.addState(new MainMenu(mainmenu));
		this.addState(new MainGame(maingame));
	}
	
	public void initStatesList(GameContainer gc) throws SlickException{
		gc.setShowFPS(false);
		gc.setTargetFrameRate(60);
		
		this.getState(mainmenu).init(gc, this);
		this.getState(maingame).init(gc, this);
		this.enterState(mainmenu);
	}
	/**
	*public static void main(String[] args) {
	*	AppGameContainer appgc;
	*	try {
	*		appgc = new AppGameContainer(new RisingSun(title));
	*		appgc.setDisplayMode(640, 360, false);
	*		appgc.setAlwaysRender(true);
	*		appgc.start();
	*	}
		catch(SlickException e){e.printStackTrace();}
	*} **/

}
