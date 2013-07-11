package com.dpendesigns.feudalwar.controllers.handlers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ActionIndicatorHandler {
	
	public static final int SUPPORT_NW = 0;
	public static final int SUPPORT_NE = 1;
	public static final int SUPPORT_E = 2;
	public static final int SUPPORT_SE = 3;
	public static final int SUPPORT_SW = 4;
	public static final int SUPPORT_W = 5;
	
	public static final int MOVE_NW = 6;
	public static final int MOVE_NE = 7;
	public static final int MOVE_E = 8;
	public static final int MOVE_SE = 9;
	public static final int MOVE_SW = 10;
	public static final int MOVE_W = 11;
	
	private int actionType;
	private Image actionImage;
	
	public ActionIndicatorHandler(int actionType) throws SlickException {
		this.actionType = actionType;
		actionImage = new Image("res/images/action-indicator/" + Integer.toString(actionType) + ".png");
	}
	
	public void render(GameContainer gc, Graphics g, int x, int y) throws SlickException {
		actionImage.draw(x, y);
	}

}
