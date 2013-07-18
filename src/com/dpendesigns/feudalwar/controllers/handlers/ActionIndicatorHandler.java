package com.dpendesigns.feudalwar.controllers.handlers;

import java.awt.Point;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

import com.dpendesigns.feudalwar.model.Province;

public class ActionIndicatorHandler {
	
	private Vector<Tuple> attackActions;
	private Vector<Tuple> supportActions;
	
	private Province[][] map;
	
	public ActionIndicatorHandler(Province[][] map) throws SlickException {
		this.map = map;
		attackActions = new Vector<Tuple>();
		supportActions = new Vector<Tuple>();
	}
	
	public void update(GameContainer gc, Vector<Point> fromLocations, Vector<Point> toLocations, boolean isAttack) throws SlickException {		
		Province from, to;
		if (isAttack) {
			attackActions.clear();
		} else {
			supportActions.clear();
		}
		for (int i = 0; i < fromLocations.size(); i++) {
			from = map[fromLocations.elementAt(i).x][fromLocations.elementAt(i).y];
			to = map[toLocations.elementAt(i).x][toLocations.elementAt(i).y];
			if (isAttack) {
				attackActions.add(new Tuple(from, to));
			} else {
				supportActions.add(new Tuple(from, to));
			}			
		}
	}
	
	public void render(GameContainer gc, Graphics g, int xDrift, int yDrift) throws SlickException {
		g.setColor(Color.black);
		for (Tuple a : attackActions) {
			g.drawLine(a.getFromX()+xDrift, a.getFromY()+yDrift, a.getToX()+xDrift, a.getToY()+yDrift);
			g.setColor(Color.red);
			g.draw(new Circle(a.getToX()+xDrift, a.getToY()+yDrift, 3));
		}
		
		g.setColor(Color.white);
		for (Tuple s : supportActions) {
			g.drawLine(s.getFromX()+xDrift, s.getFromY()+yDrift, s.getToX()+xDrift, s.getToY()+yDrift);
			g.setColor(Color.cyan);
			g.draw(new Circle(s.getToX()+xDrift, s.getToY()+yDrift, 3));
		}
	}
	
	public int computeActionType(Point from, Point to) {
		/* 0 -> 5 corresponds to NW, NE, W, E, SW, SE directions */
		if (from.x - to.x == 1) {
			if (from.y - to.y == 1) {
				return 0;
			} else if (from.y - to.y == 0) {
				return 1;
			}
		} else if (from.x - to.x == 0) {
			if (from.y - to.y == 1) {
				return 2;
			} else if (from.y - to.y == -1) {
				return 3;
			}
		} else if (from.x - to.x == -1) {
			if (from.y - to.y == 1) {
				return 4;
			} else if (from.y - to.y == -1) {
				return 5;
			}
		}
		return /*hacks*/ 666;
	}
	
	private class Tuple {
		private Province from;
		private Province to;
		
		public Tuple(Province from, Province to) {
			this.from = from;
			this.to = to;
		}
		
		public int getFromX() { return from.centeredXDefaultPosition(); }
		public int getFromY() { return from.centeredYDefaultPosition(); }
		public int getToX() { return to.centeredXDefaultPosition(); }
		public int getToY() { return to.centeredYDefaultPosition(); }
	}
}
