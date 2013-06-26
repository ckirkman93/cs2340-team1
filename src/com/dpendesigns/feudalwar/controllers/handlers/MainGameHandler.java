package com.dpendesigns.feudalwar.controllers.handlers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MainGameHandler {
	public MainGameHandler() {}

	public void render(GameContainer gc, Graphics g) {
		g.drawString("Current State: MainGameHandler", 8, 8);
	}
}
