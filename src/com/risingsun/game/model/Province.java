package com.risingsun.game.model;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Province {

	private Shape area;
	
	private static final int width = 54;
	private static final int height = 64;
	
	private Color currentColor;
	private Color neutral = new Color(0xc1c1c1);
	private Color highlighted = new Color(0xe1e1e1);
	
	private boolean leftClickDownState = false;
	
	public Province () {
		
		float yMin = 0;
		float yQuart = 16;
		float yMost = 48;
		float yMax = 64;
		
		float xHalf = 32;
		float xMin = 5;
		float xMax = 59;
		
		area = new Polygon(new float[]{xHalf,yMin,xMax,yQuart,xMax,yMost,xHalf,yMax,xMin,yMost,xMin,yQuart});
		area.setLocation(200, 150);
		currentColor = neutral;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Player[] playerList) throws SlickException {
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		
		if (input.isMouseButtonDown(0)) {leftClickDownState = true;}
		
		if (area.contains(xpos, ypos)){
			if (input.isMouseButtonDown(0)) {
				currentColor = highlighted;
				}
			else {currentColor = neutral;}
			
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				
			}
		}
		
		if (!input.isMouseButtonDown(0)) {leftClickDownState = false;}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(currentColor);
		g.fill(area);
	}

}
