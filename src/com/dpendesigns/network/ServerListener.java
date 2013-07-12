package com.dpendesigns.network;

import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.network.data.GameList;
import com.dpendesigns.network.data.UserList;
import com.dpendesigns.network.requests.BeginGameRequest;
import com.dpendesigns.network.requests.ChangeStateRequest;
import com.dpendesigns.network.requests.ConnectRequest;
import com.dpendesigns.network.requests.JoinGameRequest;
import com.dpendesigns.network.requests.LoginRequest;
import com.dpendesigns.network.requests.PlacementPhaseRequest;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerListener extends Listener{
	
	private Server server;
	
	private UserList user_list;
	private GameList game_list;
	private GameList games_in_session;
	
	private ServerListenerParser requestParser = new ServerListenerParser();
	
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
	
	public ServerListener(){}
	
	public ServerListener(Server server, UserList user_list, GameList game_list, GameList games_in_session){
		this.server = server;
		this.user_list = user_list;
		this.game_list = game_list;
		this.games_in_session = games_in_session;
		
		requestParser = new ServerListenerParser(server, user_list, game_list, games_in_session);
	}
	
	public void received (Connection c, Object o) {
		System.out.println(System.currentTimeMillis() + " " + o);
		if (o instanceof ConnectRequest){ requestParser.parseConnectRequest(c); }
		else if (o instanceof ChangeStateRequest){ requestParser.parseChangeStateRequest(c, o); }
		else if (o instanceof LoginRequest) { requestParser.parseLoginRequest(c, o);}
		else if (o instanceof JoinGameRequest){ requestParser.parseJoinGameRequest(c, o);}
		else if (o instanceof BeginGameRequest){ requestParser.parseBeginGameRequest(c, o);}
		else if (o instanceof PlacementPhaseRequest){ requestParser.parsePlacementPhaseRequest(c, o);}
		
		server.sendToAllTCP(user_list);
		server.sendToAllTCP(game_list);
	}
	
	public void disconnected (Connection c) {
		User removedUser = new User();
		for(User currentUser: user_list){if (currentUser.getName().equals(c.toString())){removedUser=currentUser;}}
		
		GameInstance droppedGame = new GameInstance();
		for(GameInstance activeGame: game_list){ 
			if (activeGame.getUsers().contains(removedUser)){ activeGame.getUsers().remove(removedUser); }
			if (activeGame.getHost() == removedUser){ droppedGame = activeGame; }
		}
		for(GameInstance activeGame: games_in_session){ 
			if (activeGame.getUsers().contains(removedUser)){ activeGame.getUsers().remove(removedUser); }
			if (activeGame.getHost() == removedUser){ droppedGame = activeGame; }
		}
		
		for(User currentUser: user_list){ if(droppedGame.isActive()){
				if (droppedGame.getUsers().contains(currentUser)){
					currentUser.setState(mainMenu);
				}
			}	
		}
		game_list.remove(droppedGame);
		games_in_session.remove(droppedGame);
		user_list.remove(removedUser);
		
		server.sendToAllTCP(user_list);
		server.sendToAllTCP(game_list);
	}
}
