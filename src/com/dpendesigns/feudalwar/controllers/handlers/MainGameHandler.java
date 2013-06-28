package com.dpendesigns.feudalwar.controllers.handlers;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.Player;
import com.dpendesigns.feudalwar.model.Province;
import com.dpendesigns.feudalwar.model.ProvinceData;
import com.dpendesigns.network.requests.AddArmyRequest;

public class MainGameHandler {
	private GameInstance my_game;
	private String my_gameID;
	
	private Province[][] my_map;
	
	private static final int driftZone = 20;
	private static final int driftSpeed = 2;
	
	private static final int summaryWidth = 128;
	
	private TrueTypeFont ttf;
	
	private Image summaryBackground;
	
	//private Map map = new Map();
	
	private int xDrift;
	private int yDrift;
	
	private int mouseX;
	private int mouseY;
	
	public MainGameHandler(GameInstance game) throws SlickException {
		my_game = game;
		my_gameID = game.getGameName();
		Map mapHolder = my_game.getMap();
		ProvinceData[][] provinceHolder = mapHolder.getProvinces();
		my_map = new Province[mapHolder.height][mapHolder.width];
		
		for (int i = 0; i < my_map.length; i++){
			for (int j = 0; j < my_map[i].length; j++){
				if(provinceHolder[i][j] != null) {my_map[i][j] = new Province(provinceHolder[i][j]);}
			}
		}
		
		summaryBackground = new Image("res/images/SummaryBG.png");
		
		Font font = new Font("Arial", Font.BOLD, 14);
		ttf = new TrueTypeFont(font, true);
		
	}

	public Object update(GameContainer gc, GameInstance game) throws SlickException {
		if(game.getGameName().equals(my_game.getGameName()))
			my_game = game;
		else System.out.println("Received different game");
		mouseX = gc.getInput().getMouseX();
		mouseY = gc.getInput().getMouseY();
		
		calculateDrift(gc);
			
		my_game.getMap().setDrift(xDrift, yDrift);
		
		for(Province[] p : my_map) {
			for(Province province : p) {
				if(province != null) {
					province.setData(my_game.getMap().getProvinces()[province.getI()][province.getJ()]);
					province.setDrift(xDrift,yDrift); 
					Object addArmyRequest = province.update(gc);
					if(addArmyRequest instanceof AddArmyRequest) {
						((AddArmyRequest)addArmyRequest).setGameName(my_game.getGameName());
						return addArmyRequest;
					}
				}
			}
		}
		return null;
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		for(Province[] p : my_map)
			for(Province province : p)
				if(province != null) province.render(gc, g);
		
		g.setColor(Color.black);
		g.setFont(ttf);
		
		summaryBackground.draw(0,0);
		g.drawString("Game Summary:", 8, 16);
		int position = 0;
		for (Player player : my_game.getPlayers()){
			if (player!=null){
				g.setColor(new Color(player.getColors()[0]));
				g.drawString(player.toString() + ":",8,40+(position*48));
				g.setColor(Color.darkGray);
				g.drawString("Generals: " + player.getGenerals(),8,52+(position*48));
				g.drawString("Infantry: " + player.getInfantry(),8,64+(position*48));
				
				position++;
			}
		}
		
		//g.setColor(Color.white);
		//g.drawString("X: "+mouseX+", Y: "+ mouseY, 440, 8);
		//g.drawString("X drift: " + xDrift + ", Y drift: " + yDrift, 440, 8+16);
	}
	
	public void calculateDrift(GameContainer gc){
		if (mouseX <= (128 + driftZone) && xDrift <= -driftSpeed) {xDrift+=driftSpeed;} //The 128 is to account for the size of the Summary box
		if (mouseX >= (gc.getWidth() - driftZone) && xDrift > -80) {xDrift-=driftSpeed;}
		if (mouseY <= driftZone && yDrift <= -driftSpeed) {yDrift+=driftSpeed;}
		if (mouseY >= (gc.getHeight() - driftZone) && yDrift > -240) {yDrift-=driftSpeed;}
	}
}
