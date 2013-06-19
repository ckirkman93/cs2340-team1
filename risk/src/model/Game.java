package model;

import java.util.ArrayList;
import java.util.Random;

/**
 *A game instance. Will eventually be able to hold all the logic required for a game.
 *
 *@author Kyle Zimmerman
 *@version 1.0 6-12-13
 */
public class Game{
	private Player[] players; //Order in the array determines turn order
	//private Board board;
	public static final int INITIAL_ARMIES_FOR_3=40;
	public static final int INITIAL_ARMIES_FOR_4=30;
	public static final int INITIAL_ARMIES_FOR_5=25;
	public static final int INITIAL_ARMIES_FOR_6=20;
	private int initialArmies;
	
	/**
	 *Constructs a game from an arraylist of 3-6 players and a board.
	 *
	 *@param list arraylist containg the players
	 *@param board
	 */
	@SuppressWarnings("unchecked")
	public Game(ArrayList<Player> list){
		ArrayList<Player> playerList=(ArrayList<Player>)(list.clone());
		//this.board=board;
		if(playerList.size()==3){
			initialArmies = INITIAL_ARMIES_FOR_3;
			players = new Player[3];
		} else if(playerList.size()==4){
			initialArmies = INITIAL_ARMIES_FOR_4;
			players=new Player[4];
		} else if(playerList.size()==5){
			initialArmies = INITIAL_ARMIES_FOR_5;
			players=new Player[5];
		} else if(playerList.size()==6){
			initialArmies = INITIAL_ARMIES_FOR_6;
			players=new Player[6];
		} else{
			throw new IllegalArgumentException("Invalid number of players.");
		}
		for(Player pl : playerList) pl.setArmies(initialArmies);
		
		Random rand = new Random();
		for(int i=0;i<players.length;i++)
		players[i]=playerList.remove(rand.nextInt(players.length-i));
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public int getInitialArmies() {
		return initialArmies;
	}
}
