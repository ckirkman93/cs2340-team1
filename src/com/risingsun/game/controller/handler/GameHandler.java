package com.risingsun.game.controller.handler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
//import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.risingsun.game.model.Player;
import com.risingsun.game.model.Map;

public class GameHandler {

	private Map map = new Map();

	public GameHandler () {

	}

	public void init(GameContainer gc, StateBasedGame sbg, Player[] playerList) throws SlickException {
		//Input input = gc.getInput();
		//int xpos = input.getMouseX();
		//int ypos = input.getMouseY();

	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Player[] playerList) throws SlickException {
		map.update(gc, sbg, delta, playerList);
	}
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		map.render(gc, sbg, g);
	}
}