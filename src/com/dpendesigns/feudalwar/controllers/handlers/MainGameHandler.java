package com.dpendesigns.feudalwar.controllers.handlers;

import java.awt.Font;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.Infantry;
import com.dpendesigns.feudalwar.model.Player;
import com.dpendesigns.feudalwar.model.Province;
import com.dpendesigns.network.data.ProvinceData;
import com.dpendesigns.network.requests.AddArmyRequest;

public class MainGameHandler {
	private GameInstance my_game;
	private String my_gameID;
	
	private String my_name;
	
	private Province[][] my_map;
	
	private int myTurnPhase;
	
	private int availableInfantry = 0;
	private int availableGenerals = 0;
	
	private Vector<int[]> placedInfantry = new Vector<int[]>(); 
	private Vector<int[]> placedGenerals = new Vector<int[]>();
	
	private static final int driftZone = 20;
	private static final int driftSpeed = 2;
	
	private static final int summaryWidth = 128;
	
	private TrueTypeFont h1, h2, p1;
	private boolean leftClickDownState = false;
	private boolean rightClickDownState = false;
	private boolean turnPhaseFinished = false;
	
	private Image mainGameBackground, mainGameBorder, selfSummaryBackground, gameSummaryBackground;
	private SpriteSheet turnPhaseSpriteSheet, endTurnSpriteSheet;
	private Image turnPhase, endTurn;
	
	private Shape endTurnLocation;
	
	private int xDrift;
	private int yDrift;
	
	private int mouseX;
	private int mouseY;
	
	//private AddArmyRequest addArmyRequest;
	
	public MainGameHandler(GameInstance game, String name) throws SlickException {
		myTurnPhase = game.getTurnPhase();
		
		mainGameBackground = new Image("res/images/MainGameBG.png");
		mainGameBorder = new Image("res/images/MainGameBorder.png");
		selfSummaryBackground = new Image("res/images/SelfSummaryBG.png");
		gameSummaryBackground = new Image("res/images/GameSummaryBG.png");
		
		turnPhaseSpriteSheet = new SpriteSheet("res/images/TurnPhaseSpriteSheet.png", 124, 32 );
		endTurnSpriteSheet = new SpriteSheet("res/images/EndTurnButtonSpriteSheet.png", 96, 64);
		
		turnPhase = turnPhaseSpriteSheet.getSubImage(myTurnPhase, 0);
		endTurn = endTurnSpriteSheet.getSubImage(0, 0);
		
		endTurnLocation = new Rectangle(0,0,96,64);
		
		my_name = name;
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
		
		
		h1 = new TrueTypeFont(new Font(Font.MONOSPACED, Font.BOLD, 16), true);
		h2 = new TrueTypeFont(new Font(Font.MONOSPACED, Font.BOLD, 12), true);
		p1 = new TrueTypeFont(new Font(Font.MONOSPACED, Font.PLAIN, 12), true);
		
		placementPhaseSetup();
	}

	public void updateMap(GameInstance updatedGame){
		myTurnPhase = updatedGame.getTurnPhase();
		
		if(updatedGame.getGameName().equals(my_game.getGameName())){ my_game = updatedGame;}
		
		for(Province[] provinceArray : my_map) {
			for(Province province : provinceArray) {
				if(province != null) {
					province.setData(my_game.getMap().getProvinces()[province.iPosition()][province.jPosition()]);	
				}
			}
		}
		if (myTurnPhase == 0){ placementPhaseSetup(); }
	}
	
	public void placementPhaseSetup(){
		for (Player player : my_game.getPlayers()){
			if (player!=null){
				if (player.getName().equals(my_name)){
					availableInfantry += player.getProvinces().size()/8;
				}
			}
		}
	}
	
	
	public void update(GameContainer gc) throws SlickException {
		endTurnLocation.setLocation(4, gc.getHeight() - 68);
		turnPhaseFinished = false;
		
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		
		mouseX = gc.getInput().getMouseX();
		mouseY = gc.getInput().getMouseY();
		
		calculateDrift(gc);
		
		for(Province[] provinceArray : my_map) {
			for(Province province : provinceArray) {
				if(province != null) {
					province.setDrift(xDrift,yDrift); 
					
					int provinceClickedStatus = province.update(gc, my_name, leftClickDownState, rightClickDownState);
					
					if (provinceClickedStatus == 1 && availableInfantry > 0 && !province.isOccupied()){
						placedInfantry.add(province.getThisLocation());
						availableInfantry--;
						province.addOccupyingUnit(new Infantry());
						System.out.println("Left Clicked");
					}
					else if (provinceClickedStatus == 2 && availableGenerals > 0  && !province.isOccupied()){
						placedGenerals.add(province.getThisLocation());
						availableGenerals--;
						System.out.println("Right Clicked");
					}
					
				}
			}
		}
		
		if (endTurnLocation.contains(xpos, ypos) && myTurnPhase!=1){
			if (input.isMouseButtonDown(0)) {
				endTurn = endTurnSpriteSheet.getSubImage(2, 0);
			}
			else {endTurn = endTurnSpriteSheet.getSubImage(1, 0);}
			
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				turnPhaseFinished = true;
				setTurnPhase(1);
			}
		}
		else {endTurn = endTurnSpriteSheet.getSubImage(0, 0);}
		
		if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {leftClickDownState = false;}
		else {leftClickDownState = true;}
		
		if (!input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {rightClickDownState = false;}
		else {rightClickDownState = true;}
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		mainGameBackground.draw(0,0);
		for(Province[] p : my_map)
			for(Province province : p)
				if(province != null) province.render(gc, g);
		
		g.setColor(Color.black);
		g.setFont(h1);
		
		//g.drawString("Game Summary:", 8, 16);
		
		//g.setColor(Color.white);
		//g.drawString("X: "+mouseX+", Y: "+ mouseY, 440, 8);
		//g.drawString("X drift: " + xDrift + ", Y drift: " + yDrift, 440, 8+16);
		selfSummaryBackground.draw(0,0);
		turnPhase.draw(4, 100);
		
		gameSummaryBackground.draw(gc.getWidth()-160, gc.getHeight()-68);
		
		int position = 0;
		for (Player player : my_game.getPlayers()){
			if (player!=null){
				if (player.getName().equals(my_name)){
					g.setFont(h1); g.setColor(new Color(player.getColors()[0]));
					g.drawString(player.getName(), 8, 0);
					g.setFont(h2);
					g.setColor(Color.darkGray);
					g.drawString("Provinces: " + player.getProvinces().size(), 8, 18);
					g.drawString("Infantry: " + player.getInfantry(), 8 , 30);
					g.drawString("Generals: " + player.getGenerals(), 8 , 42);
					g.drawString("Free Inf: " + availableInfantry, 8 , 66);
					g.drawString("Free Gen: " + availableGenerals, 8 , 78);
				} else {
					int xPlacement = gc.getWidth() - 156;
					int yPlacement = gc.getHeight() - 66 + (position * 12);
					
					g.setFont(h2);
					g.setColor(new Color(player.getColors()[0]));
					g.drawString(player.getName(),xPlacement,yPlacement);
					g.setFont(h2);
					g.setColor(Color.darkGray);
					//g.drawString("" + player.getInfantry() + " INF,",xPlacement + 72, yPlacement);
					//g.drawString("" + player.getGenerals() + " GEN",xPlacement + 110, yPlacement);
					position++;
				}
			}
		}
		
		endTurn.draw(4, gc.getHeight()-68);
		mainGameBorder.draw(0,0);
	}
	
	public void calculateDrift(GameContainer gc){
		if (mouseX <= driftZone && mouseY <= (gc.getHeight()-68) && mouseY >= 132 && xDrift <= -driftSpeed) {xDrift+=driftSpeed;} //132 is to give room for the self summary
		if (mouseX >= (gc.getWidth() - driftZone) && mouseY <= (gc.getHeight()-102) && xDrift > -80) {xDrift-=driftSpeed;} //102 is to give room for the game summary
		if (mouseY <= driftZone && mouseX >= 132 && yDrift <= -driftSpeed) {yDrift+=driftSpeed;}
		if (mouseY >= (gc.getHeight() - driftZone) && mouseX >= 104 && mouseX <= (gc.getWidth() - 164)  && yDrift > -280) {yDrift-=driftSpeed;}
	}
	
	public void setTurnPhase(int phase){
		myTurnPhase = phase;
		turnPhase = turnPhaseSpriteSheet.getSubImage(myTurnPhase, 0);
	}
	
	public boolean getTurnPhaseStatus(){return turnPhaseFinished;}

	public Vector<int[]> getInfantryPlacements() {
		return placedInfantry;
	}

	public Vector<int[]> getGeneralPlacements() {
		return placedGenerals;
	}

	public void clearPlacementChoices() {
		placedInfantry = new Vector<int[]>(); 
		placedGenerals = new Vector<int[]>();
	}
}
