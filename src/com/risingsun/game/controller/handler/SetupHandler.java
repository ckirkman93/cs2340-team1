package com.risingsun.game.controller.handler;

import java.awt.Font;
import java.util.Random;

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
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

import com.risingsun.game.model.Player;

public class SetupHandler {
	
	private boolean finished=false;
	
	private TextField playerName;
	private String[] nameList = new String[6];
	private int playerCount=0;
	
	private SpriteSheet addPlayerSpriteSheet;
	private SpriteSheet beginSpriteSheet;
	private Image addPlayer;
	private Image begin;
	
	private Shape addPlayerLocation;
	private Shape beginLocation;
	
	private boolean leftClickDownState = false;
	
	private String fullList = "Allowed player amount is full, please begin the game.";
	
	private int gcMiddle;
	
	public SetupHandler () {
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gcMiddle = gc.getWidth()/2;
		
		Font font = new Font("Arial", Font.PLAIN, 30);
		TrueTypeFont ttf = new TrueTypeFont(font, true);
		
		playerName = new TextField(gc, ttf, 0,0,320,36, new ComponentListener() {
			public void componentActivated(AbstractComponent source) {
				playerName.setFocus(true);
			}
		});
		playerName.setBackgroundColor(Color.white);
		playerName.setTextColor(Color.black);
		playerName.setText("Name of Player");
		
		addPlayerSpriteSheet = new SpriteSheet("res/images/AddPlayerButton.png",192,32);
		beginSpriteSheet = new SpriteSheet("res/images/BeginButton.png",96,32);
		
		addPlayer = addPlayerSpriteSheet.getSubImage(0, 0);
		begin = beginSpriteSheet.getSubImage(0, 0);
		
		addPlayerLocation = new Rectangle(0,0,192,32);
		beginLocation = new Rectangle(0,0,96,32);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta, Player[] playerList) throws SlickException {
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		gcMiddle = gc.getWidth()/2;
		
		playerName.setLocation(gcMiddle - 260, 64);
		addPlayerLocation.setLocation(gcMiddle + 76, 66);
		beginLocation.setLocation(gcMiddle - 48, 212);
		
		if (input.isMouseButtonDown(0)) {leftClickDownState = true;}
		
		if (addPlayerLocation.contains(xpos, ypos) && playerCount < 6){
			if (input.isMouseButtonDown(0)) {
				addPlayer = addPlayerSpriteSheet.getSubImage(2, 0);
				}
			else {addPlayer = addPlayerSpriteSheet.getSubImage(1, 0);}
			
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				String enteredName = playerName.getText().trim();
				if (enteredName!="" && enteredName != "Name of Player"){
					nameList[playerCount] = enteredName;
					playerName.setText("");
					playerCount++;
				}
			}
		}
		else {addPlayer = addPlayerSpriteSheet.getSubImage(0, 0);}
		
		if (beginLocation.contains(xpos, ypos)){
			if (input.isMouseButtonDown(0)) {
				begin = beginSpriteSheet.getSubImage(2, 0);
				}
			else {begin = beginSpriteSheet.getSubImage(1, 0);}
			
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				
				Random rand = new Random();
				
				//playerList = new Player[playerCount];
				for (int i = 0; i < playerCount; i++){
					int newSlot = rand.nextInt(playerCount);
					while (playerList[newSlot] != null){
						newSlot = rand.nextInt(playerCount);
					}
					playerList[newSlot]=new Player(nameList[i]);
					
					playerList[newSlot].setInfantry(3);
					playerList[newSlot].setGenerals(1);
					
					if (newSlot==0){playerList[newSlot].setColors( new Color[]{new Color(0xFF0000), new Color(0xFF2626), new Color(0xFF5757)} );}
					else if (newSlot==1){playerList[newSlot].setColors(new Color[]{new Color(0xFFFF00), new Color(0xFFFF26), new Color(0xFFFF57)});}
					else if (newSlot==2){playerList[newSlot].setColors(new Color[]{new Color(0x00FF00), new Color(0x26FF26), new Color(0x57FF57)});}
					else if (newSlot==3){playerList[newSlot].setColors(new Color[]{new Color(0x0000FF), new Color(0x2626FF), new Color(0x5757FF)});}
					else if (newSlot==4){playerList[newSlot].setColors(new Color[]{new Color(0xFF00FF), new Color(0xFF26FF), new Color(0xFF57FF)});}
					else if (newSlot==5){playerList[newSlot].setColors(new Color[]{new Color(0x00FFFF), new Color(0x26FFFF), new Color(0x57FFFF)});}
				}
				
				finished = true;
				
			}
		}
		else {begin = beginSpriteSheet.getSubImage(0, 0);}
		
		if (!input.isMouseButtonDown(0)) {leftClickDownState = false;}
		
		if (input.isKeyPressed(Input.KEY_ENTER)){
			String enteredName = playerName.getText().trim();
			if (enteredName!="" && enteredName != "Name of Player"){
				nameList[playerCount] = enteredName;
				playerName.setText("");
				playerCount++;
			}
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		//g.setBackground(Color.white);
		if ( playerCount < 6 ){
			playerName.render(gc, g);
			addPlayer.draw(gcMiddle + 76, 66); 
		}
		else { g.drawString(fullList, gcMiddle - 240, 68);}
		
		for (int i = 0; i < playerCount; i++){
			int j = 0;
			int k = 0;
			if (i >= 3){ j = 160; k = 14*3;} 
			g.drawString(nameList[i], gcMiddle - 140 + j, 128 + (i*14) - k);
		}
		if (playerCount >=3){begin.draw(gcMiddle - 48, 212);}
	}
	
	public boolean isFinished(){ return finished; }
	public int getCount() {return playerCount;}
}
