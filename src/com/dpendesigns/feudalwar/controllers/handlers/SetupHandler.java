package com.dpendesigns.feudalwar.controllers.handlers;

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

import com.dpendesigns.feudalwar.model.Player;



public class SetupHandler {
	
	private TextField playerNameInput;
	private String currentName, newName;
	
	private boolean nameTaken = false;
	
	private SpriteSheet loginSpriteSheet;
	private Image login;
	private Shape loginLocation;
	
	private boolean leftClickDownState = false;
	
	private String loginStatus = "Allowed player amount is full, please begin the game.";
	
	private int gcMiddle;
	
	public SetupHandler () {
		
	}
	
	public void init(GameContainer gc, String oldName) throws SlickException {
		
		currentName = oldName;
		newName = oldName;
		
		gcMiddle = gc.getWidth()/2;
		
		Font font = new Font("Arial", Font.PLAIN, 30);
		TrueTypeFont ttf = new TrueTypeFont(font, true);
		
		playerNameInput = new TextField(gc, ttf, 0,0,320,36, new ComponentListener() {
			public void componentActivated(AbstractComponent source) {
				playerNameInput.setFocus(true);
			}
		});
		playerNameInput.setBackgroundColor(Color.white);
		playerNameInput.setTextColor(Color.black);
		playerNameInput.setText(currentName);
		
		loginSpriteSheet = new SpriteSheet("res/images/AddPlayerButton.png",192,32);
		
		login = loginSpriteSheet.getSubImage(0, 0);
		
		loginLocation = new Rectangle(0,0,192,32);
	}
	
	public boolean update(GameContainer gc) throws SlickException {
		
		boolean submitLogin = false;
		
		if (playerNameInput.hasFocus()){newName = playerNameInput.getText();}
		
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		gcMiddle = gc.getWidth()/2;
		
		playerNameInput.setLocation(gcMiddle - 260, 64);
		loginLocation.setLocation(gcMiddle + 76, 66);
		
		if (input.isMouseButtonDown(0)) {leftClickDownState = true;}
		
		if (loginLocation.contains(xpos, ypos)){
			if (input.isMouseButtonDown(0)) {
				login = loginSpriteSheet.getSubImage(2, 0);
			}
			else {login = loginSpriteSheet.getSubImage(1, 0);}
				
			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {
				String enteredName = playerNameInput.getText().trim();
				if (enteredName!=""){
					newName = enteredName;
					playerNameInput.setText("");
					submitLogin = true;
				}
			}
		}
		else {login = loginSpriteSheet.getSubImage(0, 0);}
		
		if (!input.isMouseButtonDown(0)) {leftClickDownState = false;}
		
		if (input.isKeyPressed(Input.KEY_ENTER)){
			String enteredName = playerNameInput.getText().trim();
			if (enteredName!=""){
				newName = enteredName;
				playerNameInput.setText("");
				submitLogin = true;
			}
		}
		
		return submitLogin;
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		//g.setBackground(Color.white);
		playerNameInput.render(gc, g);
		login.draw(gcMiddle + 76, 66); 
		if (nameTaken){g.drawString("Username already taken, please try again...", gcMiddle - 260, 48);}
		else {g.drawString("Please enter your desired username:", gcMiddle - 260, 48);}
		g.drawString("Current Name: " + currentName, gcMiddle - 260, 128);
		g.drawString("New Name: " + newName, gcMiddle - 260, 144);
	}
	
	public String getNewName(){
		return newName;
	}
	public void loginRejected(boolean rejected){
		nameTaken = rejected;
	}
}
