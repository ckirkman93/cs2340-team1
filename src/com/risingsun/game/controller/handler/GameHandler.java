package com.risingsun.game.controller.handler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
//import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import com.risingsun.game.model.Player;
import com.risingsun.game.model.Map;

public class GameHandler {

	private Map map = new Map();
	
	private int xDrift;
	private int yDrift;
	
	private static final int driftZone = 20;
	private static final int driftSpeed = 2;

	public GameHandler () {

	}

	public void init(GameContainer gc, StateBasedGame sbg, Player[] playerList) throws SlickException {
		
		xDrift = 0;
		yDrift = 0;

	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Player[] playerList) throws SlickException {
		
		calculateDrift(gc);
		
		map.setDrift(xDrift, yDrift);
		map.update(gc, sbg, delta, playerList);
	}
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		map.render(gc, sbg, g);
	}
	
	public void calculateDrift(GameContainer gc){
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		
		if (xpos <= (128 + driftZone) && xDrift <= -driftSpeed) {xDrift+=driftSpeed;} //The 128 is to account for the size of the Summary box
		if (xpos >= (gc.getWidth() - driftZone) && xDrift > -80) {xDrift-=driftSpeed;}
		if (ypos <= driftZone && yDrift <= -driftSpeed) {yDrift+=driftSpeed;}
		if (ypos >= (gc.getHeight() - driftZone) && yDrift > -240) {yDrift-=driftSpeed;}
	}
}
