package com.dpendesigns.network;

import java.util.Collections;

import org.newdawn.slick.SlickException;

import com.dpendesigns.feudalwar.controllers.handlers.BeginGameHandler;
import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.network.data.GameList;
import com.dpendesigns.network.data.UserList;
import com.dpendesigns.network.requests.BeginGameRequest;
import com.dpendesigns.network.requests.ChangeStateRequest;
import com.dpendesigns.network.requests.JoinGameRequest;
import com.dpendesigns.network.requests.LoginRequest;
import com.dpendesigns.network.responses.LoginResponse;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class ServerListenerParser {
	
	private Server server;
	
	private UserList user_list;
	private GameList game_list;
	private GameList games_in_session;
	
	private final int boot = 0;
	private final int preLogin = 10;
	private final int login = 11;
	private final int postLogin = 12;
	private final int mainMenu = 20;
	private final int postMenu = 21;
	private final int hostGame = 30;
	private final int joinGame = 40;
	private final int preGame = 50;
	private final int loadGame = 51;
	private final int mainGame = 52;
	private final int postGame = 53;
	
	protected ServerListenerParser(){}
	protected ServerListenerParser(Server server, UserList user_list, GameList game_list, GameList games_in_session){
		this.server = server;
		this.user_list = user_list;
		this.game_list = game_list;
		this.games_in_session = games_in_session;
	}
	
	/* ---------------------------------------------------
	 * 
	 *                   DATA PARSERS
	 * 
	 * ---------------------------------------------------
	 * 
	 * 
	 * The following methods are all parsers to be used by
	 * the ServerListener subclass within FeudalWarServer.
	 * 
	 * Generic Object data is received through a connection
	 * and then it is filtered through the ServerListener
	 * which decides what type of data it is.
	 * 
	 * Finally, depending on what type of data is received
	 * it is passed to these parsers for type-specific handling. 
	 * 
	 * Responses are sent back accordingly.
	 *  
	 */
	
	protected void parseConnectRequest(Connection c){
		User newUser = new User();
		newUser.init(c.toString(),c.getID());
		user_list.add(newUser);
		
		newUser.setState(preLogin);
		
		c.sendTCP(newUser);
	}
	
	/*
	 * --------------------------------
	 *  PARSER 00 SerialVersionID 9000 
	 * --------------------------------
	 * 
	 * Determines which user is sending the request
	 * and then checks to see if they just left a
	 * game while it was in pre-game state. If they
	 * did, then it will drop them from the game.
	 * 
	 * Additionally, if they attempt to host a game
	 * when the allotted amount of games is full,
	 * it will automatically bump them to the join
	 * game screen.
	 * 
	 */
	protected void parseChangeStateRequest(Connection c, Object o){
		ChangeStateRequest stateRequest = (ChangeStateRequest)o;
		int newState = stateRequest.getRequestedState();
		for(User user: user_list){
			if (user.getConnectionID() == c.getID()){
				if (user.getCurrentState() == preGame && newState == mainMenu){
					dropUser(user);
				}
				user.setState(newState);
				if (user.getCurrentState()==hostGame){
					if (game_list.size() < 2){game_list.add(new GameInstance(user));}
					else { user.setState(joinGame);}
				}
			}
		}
	}
	
	/*
	 * --------------------------------
	 *  PARSER 01 SerialVersionID 9010 
	 * --------------------------------
	 * 
	 * Determines if any user, other than the 
	 * requesting user, currently has the requesting
	 * user's desired name. 
	 * 
	 * The parser automatically sends a LoginResponse
	 * to the connection who sent the request.
	 * 
	 */
	protected void parseLoginRequest(Connection c, Object o){
		LoginRequest loginRequest = (LoginRequest)o;
		String newUserName = loginRequest.getRequestedName();
		
		final int loginAccepted = 1;
		final int loginRejected = -1;
		
		int loginStatus = loginAccepted;
		
		for(User user: user_list){
			if (user.getName().equals(newUserName) && user.getConnectionID()!=c.getID()){
				loginStatus = loginRejected;
			}
		}
		
		if(loginStatus == loginAccepted) {
			c.setName(newUserName);
			for(User user: user_list){
				if (user.getConnectionID() == c.getID()){
					user.setName(newUserName);
				}
			}
		}
		
		c.sendTCP(new LoginResponse(loginStatus));
	}
	
	/*
	 * --------------------------------
	 *  PARSER 02 SerialVersionID 9020 
	 * --------------------------------
	 * 
	 * Received by a user who requests to join a game
	 * that is currently in the pre-game waiting period.
	 * 
	 * If the game is full, it bumps them back to the
	 * join game menu, but if the game has room, then
	 * it adds the user to the game and moves them to 
	 * the pre-game waiting state.
	 * 
	 */
	protected void parseJoinGameRequest(Connection c, Object o){
		JoinGameRequest joinGameRequest = (JoinGameRequest)o;
		String gameName = joinGameRequest.getRequestedGame();
		User addedUser = new User();
		for (User user : user_list){
			if (user.getConnectionID() == c.getID()){
				addedUser = user;
			} 
		}
		for (GameInstance game : game_list){
			if (game.getGameName().equals(gameName)){
				boolean joinAccepted = game.addUser(addedUser);
				if(!joinAccepted){
					c.sendTCP(joinGameRequest);
					addedUser.setState(joinGame);
				} else {addedUser.setState(postMenu);}
			}
		}
	}
	/*
	 * --------------------------------
	 *  PARSER 03 SerialVersionID 9100 
	 * --------------------------------
	 * 
	 * This is the first parser that handles actual
	 * game-related requests.
	 * 
	 * First it finds the game that is being requested
	 * to have start, then it double checks to make sure
	 * that the found game is actually active. If that
	 * passes, then it moves the game to the "in-session"
	 * list, initializes the game, and then moves all the
	 * users in the game onto the main-game state.
	 * 
	 */
	protected void parseBeginGameRequest(Connection c, Object o){
		BeginGameRequest beginGameRequest = (BeginGameRequest)o;
		BeginGameHandler beginGameHandler = new BeginGameHandler();
		
		System.out.println("Received request to begin "+beginGameRequest.getRequestedGame());
		
		GameInstance requestedGame = new GameInstance();
		for (GameInstance game: game_list){
			if (game.getGameName().equals(beginGameRequest.getRequestedGame())){requestedGame = game;}
		}
		
		System.out.println("Found "+requestedGame.getGameName());
		
		if (requestedGame.isActive()){
			Collections.shuffle(requestedGame.getUsers());
			try { beginGameHandler.assignPlayers(requestedGame); } 
			catch (SlickException e) { e.printStackTrace(); }
		
			for (User masterUser : user_list){
				for (User user : requestedGame.getUsers()){
					if (user.getConnectionID() == masterUser.getConnectionID()){
						masterUser.setState(loadGame);
						//user.setState(mainGame);
					}
				} 
			}
			games_in_session.add(requestedGame);
			game_list.remove(requestedGame);
			
			for (User user : requestedGame.getUsers()){
				user.setState(loadGame);
				server.sendToTCP(user.getConnectionID(), requestedGame);
			}
		}
	}
	
	protected void dropUser(User user){
		GameInstance droppedGame = new GameInstance();
		for(GameInstance activeGame: game_list){ 
			if (activeGame.getUsers().contains(user)){ activeGame.getUsers().remove(user); }
			if (activeGame.getHost() == user){ droppedGame = activeGame; }
		}
		
		for(User currentUser: user_list){ if(droppedGame.isActive()){
				if (droppedGame.getUsers().contains(currentUser)){
					currentUser.setState(mainMenu);
				}
			}	
		}
		game_list.remove(droppedGame);
	}
}
