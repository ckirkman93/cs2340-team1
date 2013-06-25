package com.dpendesigns.feudalwar;

import java.io.IOException;
import java.util.Vector;

import org.newdawn.slick.*;

import com.dpendesigns.feudalwar.controllers.handlers.HostGameHandler;
import com.dpendesigns.feudalwar.controllers.handlers.JoinGameHandler;
import com.dpendesigns.feudalwar.controllers.handlers.MainMenuHandler;
import com.dpendesigns.feudalwar.controllers.handlers.SetupHandler;
import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.GameListPacket;
import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.feudalwar.model.UserListPacket;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class FeudalWarClient extends BasicGame {
	
	public static final String title = "Feudal War: Sengoku Jidai";
	private boolean connected = false;
	
	private UserListPacket user_list = new UserListPacket();
	private GameListPacket game_list = new GameListPacket();
	
	private User self;
	private int my_connection;
	
	private SetupHandler setupHandler;
	private MainMenuHandler mainMenuHandler;
	private JoinGameHandler joinGameHandler;
	private HostGameHandler hostGameHandler;
	
	private String ip = "127.0.0.1";
	private int port = 54555;
	
	private boolean waitForResponse = false;
	private boolean waitForUserList = false;
	private boolean waitForGameList = false;
	
	private Client client;
	private Kryo kryo;
	
	private final int moveStateFoward = 1;
	private final int keepStateStill = 0;
	private final int moveStateBack = -1;
	
	private final int loginAccepted = 1;
	private final int loginWaiting = 0;
	private final int loginRejected = -1;
	
	private int loginStatus;
	
	private final int boot = 0;
	private final int preLogin = 10;
	private final int login = 11;
	private final int postLogin = 12;
	private final int mainMenu = 20;
	private final int postMenu = 21;
	private final int hostGame = 30;
	private final int joinGame = 40;
	private final int preGame = 50;
	private final int mainGame = 51;
	private final int postGame = 52;
	
	public FeudalWarClient(){
		super(title);
		
	}
	
	public FeudalWarClient(String title){
		super(title);
	}
	
	
	
	public void init(GameContainer gc) throws SlickException {
		gc.setAlwaysRender(true);
		gc.setTargetFrameRate(60);
		gc.setShowFPS(false);
		
		try {
			client = new Client();
			
			kryo = client.getKryo();
			kryo.register(com.dpendesigns.feudalwar.model.User.class);
			kryo.register(com.dpendesigns.feudalwar.model.UserListPacket.class);
			kryo.register(com.dpendesigns.feudalwar.model.GameInstance.class);
			kryo.register(com.dpendesigns.feudalwar.model.GameListPacket.class);
			kryo.register(java.util.Vector.class);
			
			client.start();
			client.connect(5000, "127.0.0.1", 54555, 54777);
			connected = true;
			
			self = new User();
			my_connection = client.getID();
			
			client.sendTCP(self);
			client.addListener(new ClientListener());
		
		} catch (IOException e) {
			e.printStackTrace();
			client.close();
			connected = false;
		}
		
		setupHandler = new SetupHandler();
	}
	public void update(GameContainer gc, int delta) throws SlickException {
		
		
		if (client.isConnected()){
			if(!waitForUserList && !waitForGameList){
				if (self.getCurrentState() == preLogin){
					setupHandler = new SetupHandler();
					setupHandler.init(gc, self.getName());
					waitForUserList = true;
					waitForGameList = true;
					client.sendTCP(login);
				} else if (self.getCurrentState() == login){
					if (setupHandler.update(gc)){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(postLogin);
						client.sendTCP(setupHandler.getNewName());
						loginStatus = loginWaiting;
						}
				} else if (self.getCurrentState() == postLogin){
					if (loginStatus == loginAccepted){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(mainMenu);
						mainMenuHandler = new MainMenuHandler();
						mainMenuHandler.init(gc);
					} else if (loginStatus == loginRejected){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(login);
						setupHandler.loginRejected(true);
					} else {}
				} else if (self.getCurrentState() == mainMenu){
					int menuStatus = mainMenuHandler.update(gc);
					if (menuStatus == 1){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(postMenu);
						joinGameHandler = new JoinGameHandler();
					} else if (menuStatus == 2){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(hostGame);
						hostGameHandler = new HostGameHandler();
						hostGameHandler.init(gc);
					} else if (menuStatus == 0){}//Do Nothing
				} else if (self.getCurrentState() == postMenu){
					joinGameHandler.init(gc, game_list);
					waitForUserList = true;
					waitForGameList = true;
					client.sendTCP(joinGame);
				} else if (self.getCurrentState() == joinGame){
					joinGameHandler.update(gc, game_list);
				} else if (self.getCurrentState() == hostGame){
					waitForUserList = true;
					waitForGameList = true;
					client.sendTCP(preGame);
				}
			}
		}
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (self.getCurrentState() == boot){
			
			g.drawString("X: "+gc.getInput().getMouseX()+", Y: "+gc.getInput().getMouseY(), 8, 8);
			g.drawString("Waiting for response: " + waitForResponse, 8, 22);
	   
			g.drawString("Connected: " + connected, 240, 60);
			g.drawString("Current State: " + self.getCurrentState(), 240, 76);
			g.drawString("User Count: " + user_list.size(), 240, 92);
		
		
			g.drawString("User: " + self.getName(), 240, 164);
			g.drawString("User ID: " + self.getUserID(), 240, 180);
			g.drawString("Connection ID: " + client.getID(), 240, 196);
			g.drawString("Local Connection ID: " + my_connection, 240, 212);
			
		} else if (self.getCurrentState() == login){
			setupHandler.render(gc, g); 
		} else if (self.getCurrentState() == mainMenu){
			mainMenuHandler.render(gc, g); 
		} else if (self.getCurrentState() == joinGame){
			joinGameHandler.render(gc, g);
		}
	}
	
	public static void main(String[] args) {
		AppGameContainer appgc;
		try {
			appgc = new AppGameContainer(new FeudalWarClient(title));
			appgc.setDisplayMode(640, 360, false);
			appgc.start();
		}
		catch(SlickException e){e.printStackTrace();}
	}
	
	private class ClientListener extends Listener{
		public void received (Connection c, Object o) {
			if (o instanceof User) {
				self = (User)o;
				waitForResponse = false;
			} else if (o instanceof UserListPacket) {
				user_list = (UserListPacket)o;
				for (User user : user_list){
					if (user.getConnectionID() == my_connection){
						self = user;
					}
				}
				waitForUserList = false;
			} else if (o instanceof GameListPacket) {
				game_list = (GameListPacket)o;
				waitForGameList = false;
			} else if (o instanceof Integer) {
				loginStatus = (Integer)o;
				waitForResponse = false;
			}
		}
	}
}
