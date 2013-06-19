package com.risingsun.game.states;

import com.risingsun.game.model.Player;
import com.risingsun.game.controller.GameController;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainGame extends BasicGameState{
	
	private GameController gameController;
	
	int stateID;

	public MainGame(int stateID) {
		this.stateID=stateID;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gameController = new GameController();
		gameController.init(gc,sbg);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		gameController.update(gc,sbg,delta);
	}

	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		gameController.render(gc, sbg, g);
	}


	
	public int getID() {
		return 1;
	}

}