package com.risingsun.game.controller.handler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.risingsun.game.model.Player;
import com.risingsun.game.model.Province;

public class GameHandler {
	
	private Province province = new Province();
	
	public GameHandler () {
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg, Player[] playerList) throws SlickException {
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		
	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Player[] playerList) throws SlickException {
		province.update(gc, sbg, delta, playerList);
	}
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		province.render(gc, sbg, g);
	}
}
