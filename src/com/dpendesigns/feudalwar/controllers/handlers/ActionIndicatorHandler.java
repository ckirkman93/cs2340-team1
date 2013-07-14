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
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		System.out.println("render!");
		g.setColor(Color.black);
		for (Tuple a : attackActions) {
			g.drawLine(a.getFromX(), a.getFromY(), a.getToX(), a.getToY());
			g.setColor(Color.red);
			g.draw(new Circle(a.getToX(), a.getToY(), 3));
		}
		
		g.setColor(Color.white);
		for (Tuple s : supportActions) {
			g.drawLine(s.getFromX(), s.getFromY(), s.getToX(), s.getToY());
			g.setColor(Color.cyan);
			g.draw(new Circle(s.getToX(), s.getToY(), 3));
		}
	}
	
	public int computeActionType(Point from, Point to, boolean isAttack) {
		int shift;
		if (isAttack) {	shift = 0; }
		else { shift = 6; }
		/* 0 -> 5 corresponds to NW, NE, W, E, SW, SE directions, attacking/moving
		 * 6 -> 11 is the same, for supporting
		 */
		if (from.x - to.x == 1) {
			if (from.y - to.y == 1) {
				return 0+shift;
			} else if (from.y - to.y == 0) {
				return 1+shift;
			}
		} else if (from.x - to.x == 0) {
			if (from.y - to.y == 1) {
				return 2+shift;
			} else if (from.y - to.y == -1) {
				return 3+shift;
			}
		} else if (from.x - to.x == -1) {
			if (from.y - to.y == 1) {
				return 4+shift;
			} else if (from.y - to.y == -1) {
				return 5+shift;
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
		
		public int getFromX() { return from.xDefaultPosition(); }
		public int getFromY() { return from.yDefaultPosition(); }
		public int getToX() { return to.xDefaultPosition(); }
		public int getToY() { return to.yDefaultPosition(); }
	}
}
