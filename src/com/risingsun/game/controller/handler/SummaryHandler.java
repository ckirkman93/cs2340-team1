package com.risingsun.game.controller.handler;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import com.risingsun.game.model.Player;

public class SummaryHandler {
	
	private static final int width = 128;
	
	private TrueTypeFont ttf;
	
	private Image background;
	
	public SummaryHandler () {
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg, Player[] playerList) throws SlickException {
		background = new Image("res/images/SummaryBG.png");
		
		Font font = new Font("Arial", Font.BOLD, 14);
		ttf = new TrueTypeFont(font, true);
	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Player[] playerList) throws SlickException {
		
	}
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g, Player[] playerList) throws SlickException {
		background.draw(0,0);
		g.setColor(Color.black);
		g.setFont(ttf);
		
		g.drawString("Game Summary:", 8, 16);
		
		for (Player player: playerList) {
			if (player != null && player.hasTurn()) {
				g.setColor(player.getColors()[0]);
				g.drawString(player.getName() + ": " + player.getFreeArmies(), 8, 28);
			}
		}
		int position = 0;
		for (Player player : playerList){
			if (player!=null){
				g.setColor(player.getColors()[0]);
				g.drawString(player.getName() + ":",8,40+(position*48));
				g.setColor(Color.darkGray);
				g.drawString("Generals: " + player.getGenerals(),8,52+(position*48));
				g.drawString("Infantry: " + player.getInfantry(),8,64+(position*48));
				
				position++;
			}
		}
	}
	
	public int getWidth(){
		return width;
	}
}
