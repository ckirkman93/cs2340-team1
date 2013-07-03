package com.dpendesigns.feudalwar.model;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import com.dpendesigns.network.data.ProvinceData;
import com.dpendesigns.network.requests.AddArmyRequest;

public class Province {

	private Shape area;

	public static final int width = 43;
	public static final int height = 48;
	public static final int choppedHeight = 36;
	
	private int xDefaultPosition, yDefaultPosition;
	private int iPosition, jPosition;
	
	private int[] thisLocation;
	
	private int xDrift;
	private int yDrift;
	
	private Player lastOwner;
	private MilitaryUnit occupyingUnit;
	
	private Color currentColor;
	
	//private AddArmyRequest addArmyRequest;
	
	public Province (ProvinceData data){
		
		lastOwner = data.getLastOwner();
		occupyingUnit = data.getOccupyingUnit();
		
		xDefaultPosition = data.getXDefault();
		yDefaultPosition = data.getYDefault();
		
		iPosition = data.iPosition();
		jPosition = data.jPosition();	
		
		thisLocation = data.getThisLocation();
		
		xDrift = data.getXDrift();
		yDrift = data.getYDrift();
		
		float yMin = 0;
		float yQuart = height/4;
		float yMost = 3*yQuart;
		float yMax = height;

		float xHalf = height/2;
		float xMin = height-width;
		float xMax = width;

		area = new Polygon(new float[]{xHalf,yMin,xMax,yQuart,xMax,yMost,xHalf,yMax,xMin,yMost,xMin,yQuart});
		area.setLocation(xDefaultPosition, yDefaultPosition);
		currentColor = new Color(data.getLastOwner().getColors()[0]);
	}

	public void setData(ProvinceData data) {
		lastOwner = data.getLastOwner();
		occupyingUnit = data.getOccupyingUnit();
	}
	
	public int update(GameContainer gc, String observer, boolean leftClickDownState, boolean rightClickDownState) throws SlickException {
		int clickStatus = 0;
		
		area.setLocation(xDefaultPosition + xDrift, yDefaultPosition + yDrift);
		
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();

		if (area.contains(xpos, ypos) && lastOwner.getName().equals(observer)){
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				currentColor = new Color(lastOwner.getColors()[2]);
			}
			else {currentColor = new Color(lastOwner.getColors()[1]);}

			if ( !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && leftClickDownState == true) {
				currentColor = new Color(lastOwner.getColors()[0]);
				clickStatus = 1;
				
			}
			
			if (!input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && rightClickDownState == true) {
				currentColor = new Color(lastOwner.getColors()[2]);
				clickStatus = 2;
			}
		}
		else {currentColor = new Color(lastOwner.getColors()[0]);}
		
		return clickStatus;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(currentColor);
		g.fill(area);
		g.setColor(Color.black);
		g.draw(area);
		
		//g.drawString("" + ipos,  xDefaultPosition +xDrift, yDefaultPosition + yDrift + height/2 - 16);
		//g.drawString("" + jpos,  xDefaultPosition +xDrift, yDefaultPosition + yDrift + height/2 - 2);
		
		if (occupyingUnit instanceof Infantry){
		g.drawString("INF", 
					xDefaultPosition + xDrift + width/2 - 16, 
					yDefaultPosition + yDrift + height/2 - 8);
		}
		
		if (occupyingUnit instanceof General){
			g.drawString("GEN", 
					xDefaultPosition + xDrift + width/2 - 16, 
					yDefaultPosition + yDrift + height/2 - 8);
		}
	}
	
	public void setDrift(int xAmount, int yAmount){
		xDrift = xAmount;
		yDrift = yAmount;
	}
	
	public int iPosition(){ return iPosition; }
	public int jPosition(){ return jPosition; }
	
	public int[] getThisLocation(){return thisLocation;}
	
	public boolean isOccupied(){
		if (occupyingUnit!=null){return true;}
		else {return false;}
	}
	
	public void addOccupyingUnit(MilitaryUnit unit, boolean created){ 
		occupyingUnit = unit; 
		if (unit.isActive()){
			lastOwner = unit.getOwner();
			if(!lastOwner.getProvinces().contains(thisLocation)){
				lastOwner.getProvinces().add(thisLocation);
			}
		}
		if (created){
			if (unit instanceof Infantry){lastOwner.addInfantry(1);}
			else if (unit instanceof General){lastOwner.addGeneral(1);}
		}
	}
}
