package com.dpendesigns.feudalwar.model;

import java.awt.Font;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import com.dpendesigns.feudalwar.controllers.handlers.Map;
import com.dpendesigns.feudalwar.model.Player;

public class GameInstance {
	private String gameName;
	private User host;
	private Vector<User> users;
	private Vector<Player> players;
	private int host_connection;
	
	private boolean active=false;
	private boolean inSession=false;
	
	private static final int driftZone = 20;
	private static final int driftSpeed = 2;
	
	private static final int summaryWidth = 128;
	
	private TrueTypeFont ttf;
	
	private Image summaryBackground;
	
	private Map map = new Map();
	
	private int xDrift;
	private int yDrift;
	
	private final int mainMenu = 20;
	
	public GameInstance(){}
	
	public GameInstance(User host) {
		this.gameName = "Game: " + host.getUserID();
		this.host = host;
		this.host_connection = host.getConnectionID();
		
		this.users=new UserListPacket();
		users.add(host);
		
		active = true;
	}
	
	public User getHost(){return host;}
	public Vector<User> getUsers(){return users;}
	public String getGameName(){return gameName;}
	public boolean isActive(){return active;}
	public boolean inSession(){return inSession;}
	public boolean addUser(User newUser){
		if (users.size()<6){users.add(newUser); return true;}
		else {return false;}
	}
	
	public void init(Vector<Player> players) throws SlickException{
		this.players = players;
		
		//summaryBackground = new Image("res/images/SummaryBG.png");
		
		Font font = new Font("Arial", Font.BOLD, 14);
		//ttf = new TrueTypeFont(font, true);
		
		map.init();
		
		xDrift = 0;
		yDrift = 0;
		
		if (users.size()>3){
			if(users.size()>4){
				if(users.size()>5){
					map.configure6(players);
				}
				else {map.configure5(players);}
			}
			else {map.configure4(players);}
		}
		else {map.configure3(players);}
	}
	//public void update(GameContainer gc) throws SlickException {
	//	
	//	calculateDrift(gc);
	//	
	//	map.setDrift(xDrift, yDrift);
	//	map.update(gc);
	//}
	/*public void render(GameContainer gc, Graphics g) throws SlickException {
		map.render(gc, g);
		
		summaryBackground.draw(0,0);
		g.setColor(Color.black);
		g.setFont(ttf);
		
		g.drawString("Game Summary:", 8, 16);
		int position = 0;
		for (Player player : players){
			if (player!=null){
				g.setColor(player.getColors()[0]);
				g.drawString(player.toString() + ":",8,40+(position*48));
				g.setColor(Color.darkGray);
				g.drawString("Generals: " + player.getGenerals(),8,52+(position*48));
				g.drawString("Infantry: " + player.getInfantry(),8,64+(position*48));
				
				position++;
			}
		}
	}*/
	
	public void calculateDrift(GameContainer gc){
		Input input = gc.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();
		
		if (xpos <= (128 + driftZone) && xDrift <= -driftSpeed) {xDrift+=driftSpeed;} //The 128 is to account for the size of the Summary box
		if (xpos >= (gc.getWidth() - driftZone) && xDrift > -80) {xDrift-=driftSpeed;}
		if (ypos <= driftZone && yDrift <= -driftSpeed) {yDrift+=driftSpeed;}
		if (ypos >= (gc.getHeight() - driftZone) && yDrift > -240) {yDrift-=driftSpeed;}
	}
	
	public Map getMap(){ return map; }
	
	public Vector<Player> getPlayers(){ return players; }
	
}
