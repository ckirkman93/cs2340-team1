package com.dpendesigns.feudalwar.controllers.handlers;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class ActionMenu {
	
	private boolean leftClickDownState = false;
	
	private int xPos;
	private int yPos;
	
	private Color color;
	private Color bgColor;
	private Color highlightColor;
	
	private Shape holdLocation;
	private Shape supportLocation;
	private Shape moveLocation;
	
	private final String hold = " Hold";
	private final String support = " Support";
	private final String move = " Move";
	
	private boolean highlightHold= false;
	private boolean highlightSupport = false;
	private boolean highlightMove = false;
	
	private int currentStatus;
	public static final int INACTIVE_STATUS = 0;
	public static final int HOLD_STATUS = 1;
	public static final int SUPPORT_STATUS = 2;
	public static final int MOVE_STATUS = 3;
	public static final int WAITING_STATUS = 4;
	
	public ActionMenu(int x, int y, int color) {
		xPos = x;
		yPos = y;
		this.color = new Color(color);
		bgColor = new Color(50, 50, 50, 150);
		highlightColor = new Color(150, 150, 150, 150);
		moveLocation = new Rectangle(xPos, yPos, 100, 18);
		supportLocation = new Rectangle(xPos, yPos+18, 100, 18);
		holdLocation = new Rectangle(xPos, yPos+36, 100, 18);		
	}
	
	public int update(GameContainer gc, boolean isVisible) throws SlickException {
		highlightMove = false;
		highlightSupport = false;
		highlightHold = false;
		currentStatus = INACTIVE_STATUS;
		if(isVisible) {
			currentStatus = WAITING_STATUS;
			Input input = gc.getInput();
			int xpos = input.getMouseX();
			int ypos = input.getMouseY();
	
			if (moveLocation.contains(xpos, ypos)) {
				if (!input.isMouseButtonDown(0) && leftClickDownState) {
					currentStatus = MOVE_STATUS;
				}
				highlightMove = true;
			} else if (supportLocation.contains(xpos, ypos)) {
				if (!input.isMouseButtonDown(0) && leftClickDownState) {
					currentStatus = SUPPORT_STATUS;
				}
				highlightSupport = true;
			} else if (holdLocation.contains(xpos, ypos)) {
				if (!input.isMouseButtonDown(0) && leftClickDownState) {
					currentStatus = HOLD_STATUS;
				}
				highlightHold = true;
			} else if (!input.isMouseButtonDown(0) && leftClickDownState) {
				currentStatus = INACTIVE_STATUS;
			}
			
			if (input.isMouseButtonDown(0)) {
				leftClickDownState = true;
			} else {
				leftClickDownState = false;
			}
		}
		
		return currentStatus;
	}
	
	public void render(GameContainer gc, Graphics g, int x, int y) {		
		((Rectangle) moveLocation).setBounds(x, y, 100, 18);
		if (highlightMove) { 
			g.setColor(highlightColor);
		} else {
			g.setColor(bgColor);
		}
		g.fill(moveLocation);
		g.setColor(color);
		g.draw(moveLocation);
		g.drawString(move, moveLocation.getX(), moveLocation.getY());
		
		((Rectangle) supportLocation).setBounds(x, y+18, 100, 18);
		if (highlightSupport) {
			g.setColor(highlightColor);
		} else {
			g.setColor(bgColor);
		}
		g.fill(supportLocation);
		g.setColor(color);
		g.draw(supportLocation);
		g.drawString(support, supportLocation.getX(), supportLocation.getY());
		
		((Rectangle) holdLocation).setBounds(x, y+36, 100, 18);
		if (highlightHold) {
			g.setColor(highlightColor);
		} else {
			g.setColor(bgColor);
		}
		g.fill(holdLocation);
		g.setColor(color);
		g.draw(holdLocation);
		g.drawString(hold, holdLocation.getX(), holdLocation.getY());
	}
	
	public void setStatus(int status) {
		this.currentStatus = status;
	}
}
