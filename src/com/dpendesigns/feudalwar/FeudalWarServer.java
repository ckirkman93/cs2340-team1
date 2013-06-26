package com.dpendesigns.feudalwar;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import com.dpendesigns.feudalwar.model.BeginGameRequest;
import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.GameListPacket;
import com.dpendesigns.feudalwar.model.JoinGameRequest;
import com.dpendesigns.feudalwar.model.Player;
import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.feudalwar.model.UserListPacket;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class FeudalWarServer {
	public static Server server;
	public static Kryo kryo;
	
	private UserListPacket user_list;
	private GameListPacket game_list;
	private GameListPacket games_in_session;
	
	private StateHandler stateHandler;
	
	private JFrame serverView;
	private JLabel serverStatusText;
	private JLabel serverUserCount;
	
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
	
	public FeudalWarServer(){
		System.out.println("Setting Up Server...");
		user_list = new UserListPacket();
		game_list = new GameListPacket();
		games_in_session = new GameListPacket();
		
		stateHandler = new StateHandler();
		
		serverView = new JFrame("Server Overview");
		serverView.pack();
		serverView.setSize(256, 128);
		serverView.setVisible(true);
		serverView.setLocationRelativeTo(null);
		serverView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		serverStatusText = new JLabel("Starting server...");
		serverUserCount = new JLabel("Number of Users: " + user_list.size());
		
		serverView.getContentPane().setLayout(new GridLayout(4,2));
		serverView.getContentPane().add(serverStatusText,BorderLayout.NORTH);
		serverView.getContentPane().add(serverUserCount,BorderLayout.NORTH);

		System.out.println("Setting Up Completed");
		System.out.println("Booting Server...");
		try{
			
			server = new Server(16384,8192);
			
			kryo = server.getKryo();
			kryo.register(com.dpendesigns.feudalwar.model.User.class);
			kryo.register(com.dpendesigns.feudalwar.model.UserListPacket.class);
			kryo.register(com.dpendesigns.feudalwar.model.GameInstance.class);
			kryo.register(com.dpendesigns.feudalwar.model.GameListPacket.class);
			kryo.register(com.dpendesigns.feudalwar.model.JoinGameRequest.class);
			kryo.register(com.dpendesigns.feudalwar.model.BeginGameRequest.class);
			
			kryo.register(com.dpendesigns.feudalwar.controllers.handlers.Map.class);
			kryo.register(java.util.Vector.class);
			kryo.register(java.util.ArrayList.class);
			kryo.register(com.dpendesigns.feudalwar.model.ProvinceData[][].class);
			kryo.register(com.dpendesigns.feudalwar.model.ProvinceData[].class);
			kryo.register(com.dpendesigns.feudalwar.model.ProvinceData.class);
			kryo.register(org.newdawn.slick.geom.Polygon.class);
			kryo.register(java.awt.Point.class);
			kryo.register(float[].class);
			kryo.register(String[].class);
			kryo.register(com.dpendesigns.feudalwar.model.Player.class);
			kryo.register(int[].class);
			
			server.start();
			server.bind(54555, 54777);
			
			server.addListener(new ServerListener());
			
			serverStatusText.setText("Server Status: ON");

			System.out.println("Bootup Complete");
			System.out.println("Server Status: ON");
		} catch(Exception e){
			server.close();
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		FeudalWarServer gameServer = new FeudalWarServer();
	}
	public class ServerListener extends Listener{
		public void received (Connection c, Object o) {
			if (o instanceof Integer){
				int newState = (Integer)o;
				for(User user: user_list){
					if (user.getConnectionID() == c.getID()){
						if (user.getCurrentState() == preGame && newState == mainMenu){
							dropUser(user);
						}
						stateHandler.command(user, newState);
						if (user.getCurrentState()==joinGame){
							c.sendTCP(game_list); 
						} else if (user.getCurrentState()==hostGame){
							if (game_list.size() <= 2){game_list.add(new GameInstance(user));}
							else { stateHandler.command(user, joinGame); c.sendTCP(game_list);}
						}
					}
				}
				server.sendToAllTCP(user_list);
				server.sendToAllTCP(game_list);
			} else if (o instanceof User) {
				User newUser = (User)o;
				newUser.init(c.toString(),c.getID());
				user_list.add(newUser);
				
				serverUserCount.setText("Number of Users: " + user_list.size());
				
				stateHandler.command(newUser, preLogin);
				
				c.sendTCP(newUser);

				server.sendToAllTCP(user_list);
				server.sendToAllTCP(game_list);
			} else if (o instanceof String) {
				String newUserName = (String)o;
				
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
				
				c.sendTCP(loginStatus);
				server.sendToAllTCP(user_list);
				server.sendToAllTCP(game_list);
			} else if (o instanceof JoinGameRequest){
				JoinGameRequest joinGameRequest = (JoinGameRequest)o;
				String gameName = joinGameRequest.requestedGame;
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
							stateHandler.command(addedUser, joinGame);
						} else {stateHandler.command(addedUser, postMenu);}
					}
				}
				server.sendToAllTCP(user_list);
				server.sendToAllTCP(game_list);
			} else if (o instanceof BeginGameRequest){
				BeginGameRequest beginGameRequest = (BeginGameRequest)o;
				BeginGameHandler beginGameHandler = new BeginGameHandler();
				
				System.out.println("Received request to begin "+beginGameRequest.requestedGame);
				
				GameInstance requestedGame = new GameInstance();
				for (GameInstance game: game_list){
					if (game.getGameName().equals(beginGameRequest.requestedGame)){requestedGame = game;}
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
						stateHandler.command(user, loadGame);
						server.sendToTCP(user.getConnectionID(), requestedGame);
					}
				}
				server.sendToAllTCP(user_list);
				server.sendToAllTCP(game_list);
			}
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
						stateHandler.command(currentUser, mainMenu);
					}
				}	
			}
			game_list.remove(droppedGame);
			games_in_session.remove(droppedGame);
			user_list.remove(removedUser);
			
			serverUserCount.setText("Number of Users: " + user_list.size());
			
			server.sendToAllTCP(user_list);
			server.sendToAllTCP(game_list);
		}
	}
	
	private void dropUser(User user){
		GameInstance droppedGame = new GameInstance();
		for(GameInstance activeGame: game_list){ 
			if (activeGame.getUsers().contains(user)){ activeGame.getUsers().remove(user); }
			if (activeGame.getHost() == user){ droppedGame = activeGame; }
		}
		
		for(User currentUser: user_list){ if(droppedGame.isActive()){
				if (droppedGame.getUsers().contains(currentUser)){
					stateHandler.command(currentUser, mainMenu);
				}
			}	
		}
		game_list.remove(droppedGame);
	}
	
	private class StateHandler{
		private StateHandler(){}
		
		private void command(User user, int newState) {
			user.setState(newState);
		}

		private void nextState(User user){
			user.nextState();
		}
		private int getState(User user){
			return user.getCurrentState();
		}
	}
	
	private class BeginGameHandler{
		private BeginGameHandler(){}
		
		private void assignPlayers(GameInstance game) throws SlickException{
			
			Vector<Player> players = new Vector<Player>();
			
			for (int i = 0; i < game.getUsers().size(); i++){
				if (i==0){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors( new int[]{0xFF0000, 0xFF2626, 0xFF5757} );}
				else if (i==1){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0xFFFF00, 0xFFFF26, 0xFFFF57});}
				else if (i==2){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x00FF00, 0x26FF26, 0x57FF57});}
				else if (i==3){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x0000FF, 0x2626FF, 0x5757FF});}
				else if (i==4){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0xFF00FF, 0xFF26FF, 0xFF57FF});}
				else if (i==5){players.add(new Player(game.getUsers().get(i))); players.get(i).setColors(new int[]{0x00FFFF, 0x26FFFF, 0x57FFFF});}
			}
			
			game.init(players);
		}
	}
}
