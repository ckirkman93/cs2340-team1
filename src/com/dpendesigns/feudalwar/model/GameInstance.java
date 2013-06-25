package com.dpendesigns.feudalwar.model;

import java.util.Vector;

public class GameInstance {
	private String gameName;
	private User host;
	private UserListPacket users;
	private int host_connection;
	
	private boolean active=false;
	
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
	public UserListPacket getUsers(){return users;}
	public String getGameName(){return gameName;}
	public boolean isActive(){return active;}
	public boolean addUser(User newUser){
		if (users.size()<6){users.add(newUser); return true;}
		else {return false;}
	}
}
