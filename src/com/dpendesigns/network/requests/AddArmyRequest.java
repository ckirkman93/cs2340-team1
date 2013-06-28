package com.dpendesigns.network.requests;

public class AddArmyRequest {
	private static final long serialVersionUID = 9999L;
	private String gameName;
	private int i;
	private int j;
	public AddArmyRequest(){}
	public AddArmyRequest(String gameName, int i, int j) {
		this.i = i;
		this.j = j;
		this.gameName = gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameName() {
		return gameName;
	}
	public int i() {
		return i;
	}
	public int j(){
		return j;
	}
}
