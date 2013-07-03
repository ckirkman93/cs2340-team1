package com.dpendesigns.feudalwar.controllers.handlers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class HostGameHandler {
	
	public HostGameHandler(){}
	
	public void init(GameContainer gc) throws SlickException {}
	public void update(GameContainer gc) throws SlickException {}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.drawString("This is the host game handler screen", 8, 8);
	}
}
