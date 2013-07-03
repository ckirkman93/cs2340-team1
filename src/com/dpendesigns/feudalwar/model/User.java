package com.dpendesigns.feudalwar.model;

public class User {
	
	public int currentState;
	
	private String name;
	private String user_id;
	private int connection_id;
	
	public User(){}
	
	public void init(String connection_name,int connection_id){
		this.name = connection_name;
		this.user_id = connection_name+"@"+connection_id;
		this.connection_id=connection_id;
		currentState = 0;
	}
	
	public String getName(){return name;}
	public String getUserID(){return user_id;}
	public int getConnectionID(){return connection_id;}
	
	public int getCurrentState(){return currentState;}
	public void nextState(){currentState++; System.out.println("State: "+currentState);}
	public void prevState(){currentState--;}
	
	public void setName(String newName){
		this.name = newName;
		this.user_id = newName+"@"+connection_id;
	}
	public void setState(int newState){currentState=newState;}
}
