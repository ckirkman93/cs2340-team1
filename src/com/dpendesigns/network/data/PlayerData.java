package com.dpendesigns.network.data;

import com.dpendesigns.feudalwar.model.Player;
import com.dpendesigns.feudalwar.model.User;

public class PlayerData {
	private String playerName;
	private int connection_ID;
	private int[] colors = new int[]{0xc0c0c0, 0xd0d0d0, 0xf0f0f0};
	
	public PlayerData(){
		playerName = "null";
		colors = new int[]{0xc0c0c0, 0xd0d0d0, 0xf0f0f0};
	}
	
	public PlayerData(Player player){
		playerName = player.getName();
		connection_ID = player.getConnectionID();
		colors = player.getColors();
	}	
}
