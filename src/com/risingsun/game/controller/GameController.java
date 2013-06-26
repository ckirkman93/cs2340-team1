package com.risingsun.game.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.risingsun.game.controller.handler.*;
import com.risingsun.game.model.Player;

public class GameController {
	
	private Player[]  playerList;
	
	private SetupHandler setupHandler;
	private SummaryHandler summaryHandler;
	private GameHandler gameHandler;
	private boolean setupFinished = false;
	private boolean gameStarted = false;
	private boolean gameFinished = false;
	
	public GameController() {
		
	}
		
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		playerList = new Player[6];
		
		setupHandler = new SetupHandler();
		gameHandler = new GameHandler();
		summaryHandler = new SummaryHandler();
		setupHandler.init(gc , sbg);
	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (!setupFinished) {
			setupHandler.update(gc , sbg , delta , playerList); 
			setupFinished = setupHandler.isFinished();
			if (setupFinished){
				gameHandler.init(gc, sbg, playerList, setupHandler.getCount());
				summaryHandler.init(gc, sbg, playerList);
			}
		}
		else if (!gameStarted){ gameStarted = true;}
		else if (!gameFinished && gameStarted){
			gameHandler.update(gc, sbg, delta, playerList);
			summaryHandler.update(gc, sbg, delta, playerList);
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (!setupFinished) { setupHandler.render(gc, sbg, g); }
		else if (!gameFinished && gameStarted){
			gameHandler.render(gc, sbg, g);
			summaryHandler.render(gc, sbg, g, playerList);
		}
	}
}
