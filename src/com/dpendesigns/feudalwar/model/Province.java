package com.dpendesigns.feudalwar.model;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Province {

	private Shape area;

	public static final int width = 39;
	public static final int height = 48;
	public static final int choppedHeight = 36;
	
	private int xDefaultPosition;
	private int yDefaultPosition;
	
	private int xDrift;
	private int yDrift;
	
	private int ipos,jpos;
	
	private int numberOfArmies;
	
	private Player owner;

	private Color currentColor;

	private boolean leftClickDownState = false;
	
	public Province (ProvinceData data){
		owner = data.getOwner();
		xDefaultPosition = data.getXDefault();
		yDefaultPosition = data.getYDefault();
		
		ipos = data.iPos();
		jpos = data.jPos();	
		
		xDrift = data.getXDrift();
		yDrift = data.getYDrift();
		
		numberOfArmies = data.getInfantry();
		
		float yMin = 0;
		float yQuart = 12;
		float yMost = 36;
		float yMax = 48;

		float xHalf = 24;
		float xMin = 5;
		float xMax = 43;

		area = new Polygon(new float[]{xHalf,yMin,xMax,yQuart,xMax,yMost,xHalf,yMax,xMin,yMost,xMin,yQuart});
		area.setLocation(xDefaultPosition, yDefaultPosition);
		currentColor = new Color(owner.getColors()[0]);
	}

	public void update(GameContainer gc) throws SlickException {
		area.setLocation(xDefaultPosition + xDrift, yDefaultPosition + yDrift);
		
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();

		if (input.isMouseButtonDown(0)) {leftClickDownState = true;}

		if (area.contains(xpos, ypos)){
			if (input.isMouseButtonDown(0)) {
				currentColor = new Color(owner.getColors()[2]);
			}
			else {currentColor = new Color(owner.getColors()[1]);}

			if ( !input.isMouseButtonDown(0) && leftClickDownState == true) {

			}
			
			if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
				//this.setOwner(playerList[0]);
				//numberOfArmies++;
			}
		}
		else {currentColor = new Color(owner.getColors()[0]);}

		if (!input.isMouseButtonDown(0)) {leftClickDownState = false;}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(currentColor);
		g.fill(area);
		g.setColor(Color.black);
		g.draw(area);
		
		//g.drawString("" + ipos,  xDefaultPosition +xDrift, yDefaultPosition + yDrift + height/2 - 16);
		//g.drawString("" + jpos,  xDefaultPosition +xDrift, yDefaultPosition + yDrift + height/2 - 2);
		
		if(numberOfArmies > 9)
			g.drawString("" + numberOfArmies, 
					xDefaultPosition + xDrift + width/2 - 10, 
					yDefaultPosition + yDrift + height/2 - 8);
		else if(numberOfArmies > 0)
			g.drawString("" + numberOfArmies, 
					xDefaultPosition + xDrift + width/2 - 6, 
					yDefaultPosition + yDrift + height/2 - 8);
	}
	
	public void setDrift(int xAmount, int yAmount){
		xDrift = xAmount;
		yDrift = yAmount;
	}
	
	//public void setOwner(Player player){
	//	neutral = player.getColors()[0];
	//	highlighted = player.getColors()[1];
	//	clicked = player.getColors()[2];
	//}
	//
	public void setSeat(int ipos, int jpos){
		this.ipos=ipos;
		this.jpos=jpos;
	}

}