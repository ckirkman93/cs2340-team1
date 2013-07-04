package com.dpendesigns.feudalwar.controllers.handlers;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class ActionMenu {
	
	private boolean leftClickDownState = false;
	
	private int xPos;
	private int yPos;
	
	private Color color;
	
	private Shape holdLocation;	
	private final String hold = " Hold";
	
	private int currentStatus;
	private final int doNothingStatus = 0;
	private final int holdStatus = 1;
	private final int supportStatus = 2;
	private final int moveStatus = 3;
	
	public ActionMenu(int x, int y, int color) {
		xPos = x;
		yPos = y;
		this.color = new Color(color);
		holdLocation = new Rectangle(xPos, yPos, 100, 20);		
	}
	
	public int update(GameContainer gc) throws SlickException {
		currentStatus = doNothingStatus;
		
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		
		if (input.isMouseButtonDown(0)) {
			leftClickDownState = true;
		} else {
			leftClickDownState = false;
		}
		
		if (holdLocation.contains(xpos, ypos)) {
			if (!input.isMouseButtonDown(0) && leftClickDownState) {
				currentStatus = holdStatus;
			}
		}
		return currentStatus;
	}
	
	public void render(GameContainer gc, Graphics g, int x, int y) {
		((Rectangle) holdLocation).setBounds(x, y, 100, 18);
		g.setColor(Color.lightGray);
		g.fill(holdLocation);
		g.setColor(color);
		g.draw(holdLocation);
		g.drawString(hold, holdLocation.getX(), holdLocation.getY());	
	}
}
