package com.dpendesigns.feudalwar;

import java.awt.Point;
import java.io.IOException;
import java.util.Vector;

import org.newdawn.slick.*;

import com.dpendesigns.feudalwar.controllers.handlers.HostGameHandler;
import com.dpendesigns.feudalwar.controllers.handlers.JoinGameHandler;
import com.dpendesigns.feudalwar.controllers.handlers.MainGameHandler;
import com.dpendesigns.feudalwar.controllers.handlers.MainMenuHandler;
import com.dpendesigns.feudalwar.controllers.handlers.PreGameHandler;
import com.dpendesigns.feudalwar.controllers.handlers.SetupHandler;
import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.network.data.GameList;
import com.dpendesigns.network.data.UserList;
import com.dpendesigns.network.requests.MovementPhaseRequest;
import com.dpendesigns.network.requests.BeginGameRequest;
import com.dpendesigns.network.requests.ChangeStateRequest;
import com.dpendesigns.network.requests.ConnectRequest;
import com.dpendesigns.network.requests.JoinGameRequest;
import com.dpendesigns.network.requests.LoginRequest;
import com.dpendesigns.network.requests.PlacementPhaseRequest;
import com.dpendesigns.network.requests.SendMessageRequest;
import com.dpendesigns.network.responses.LoginResponse;
import com.dpendesigns.network.responses.MovementPhaseResponse;
import com.dpendesigns.network.responses.PlacementPhaseResponse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class FeudalWarClient extends BasicGame {
	
	public static final String title = "Feudal War: Sengoku Jidai";
	private boolean connected = false;
	
	private UserList user_list = new UserList();
	private GameList game_list = new GameList();
	
	private boolean game_in_session = false;
	
	private User self = new User();
	private int my_connection;
	private GameInstance my_game = null;
	
	private SetupHandler setupHandler;
	private MainMenuHandler mainMenuHandler;
	private JoinGameHandler joinGameHandler;
	private HostGameHandler hostGameHandler;
	private PreGameHandler preGameHandler;
	private MainGameHandler mainGameHandler;
	
	private String ip = "127.0.0.1";
	private int port = 54555;
	
	private boolean waitForResponse = false;
	private boolean waitForLoginResponse = false;
	private boolean waitForUserList = false;
	private boolean waitForGameList = false;
	private boolean waitForGameUpdate = false;
	
	private boolean joinGameBounced = false;
	
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
	private final int loadGame = 51;
	private final int mainGame = 52;
	private final int postGame = 53;

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
			client = new Client(16384,16384);
			
			kryo = client.getKryo();
			kryo.register(User.class);
			kryo.register(UserList.class);
			kryo.register(GameInstance.class);
			kryo.register(GameList.class);
			
			// Register the request classes for serialization
			kryo.register(ConnectRequest.class);
			kryo.register(ChangeStateRequest.class);
			kryo.register(LoginRequest.class);
			kryo.register(JoinGameRequest.class);
			kryo.register(BeginGameRequest.class);
			kryo.register(PlacementPhaseRequest.class);
			kryo.register(MovementPhaseRequest.class);
			
			// Register the response classes for serialization
			kryo.register(com.dpendesigns.network.responses.LoginResponse.class);
			kryo.register(com.dpendesigns.network.responses.PlacementPhaseResponse.class);
			kryo.register(com.dpendesigns.network.responses.MovementPhaseResponse.class);
			
			kryo.register(com.dpendesigns.feudalwar.controllers.handlers.Map.class);
			kryo.register(java.util.Vector.class);
			kryo.register(java.util.ArrayList.class);
			
			kryo.register(com.dpendesigns.network.data.ProvinceData[][].class);
			kryo.register(com.dpendesigns.network.data.ProvinceData[].class);
			kryo.register(com.dpendesigns.network.data.ProvinceData.class);
			kryo.register(org.newdawn.slick.geom.Polygon.class);
			kryo.register(java.awt.Point.class);
			kryo.register(float[].class);
			kryo.register(String[].class);
			kryo.register(com.dpendesigns.feudalwar.model.Player.class);
			kryo.register(int[].class);
			
			kryo.register(com.dpendesigns.feudalwar.model.Infantry.class);
			kryo.register(com.dpendesigns.feudalwar.model.General.class);
			kryo.register(com.dpendesigns.feudalwar.model.MilitaryUnit.class);
			kryo.register(com.dpendesigns.network.requests.SendMessageRequest.class);
			
			client.start();
			client.connect(5000, "127.0.0.1", 54555, 54777);
			client.setTimeout(0);
			
			connected = true;
			my_connection = client.getID();
			
			client.addListener(new ClientListener());
			client.sendTCP(new ConnectRequest());
		
		} catch (IOException e) {
			e.printStackTrace();
			client.close();
			connected = false;
		}
		
		setupHandler = new SetupHandler();
	}
	public void update(GameContainer gc, int delta) throws SlickException {
		
		
		if (client.isConnected()){
			if(!waitForUserList && !waitForGameList && !waitForGameUpdate){
				if (self.getCurrentState() == preLogin){
					setupHandler = new SetupHandler();
					setupHandler.init(gc, self.getName());
					waitForUserList = true;
					waitForGameList = true;
					client.sendTCP(new ChangeStateRequest(login));
				} else if (self.getCurrentState() == login){
					if (setupHandler.update(gc)){
						waitForUserList = true;
						waitForGameList = true;
						//waitForLoginResponse = true;
						client.sendTCP(new ChangeStateRequest(postLogin));
						client.sendTCP(new LoginRequest(setupHandler.getNewName()));
						loginStatus = loginWaiting;
						}
				} else if (self.getCurrentState() == postLogin){
					if (loginStatus == loginAccepted){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(new ChangeStateRequest(mainMenu));
						mainMenuHandler = new MainMenuHandler();
						mainMenuHandler.init(gc);
					} else if (loginStatus == loginRejected){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(new ChangeStateRequest(login));
						setupHandler.loginRejected(true);
					} else {}
				} else if (self.getCurrentState() == mainMenu){
					joinGameHandler = new JoinGameHandler(client);
					hostGameHandler = new HostGameHandler();
					hostGameHandler.init(gc);
					int menuStatus = mainMenuHandler.update(gc);
					if (menuStatus == 1){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(new ChangeStateRequest(joinGame));
					} else if (menuStatus == 2){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(new ChangeStateRequest(hostGame));
					} else if (menuStatus == 0){}//Do Nothing
				} else if (self.getCurrentState() == postMenu){
					waitForUserList = true;
					waitForGameList = true;
					client.sendTCP(new ChangeStateRequest(preGame));
					preGameHandler = new PreGameHandler(my_connection, my_game);
				} else if (self.getCurrentState() == joinGame){
					String joinGameName = joinGameHandler.update(gc, game_list, joinGameBounced);
					if (joinGameName!="null"){
						JoinGameRequest joinGameRequest = new JoinGameRequest(joinGameName);
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(joinGameRequest);
					}
				} else if (self.getCurrentState() == hostGame){
					waitForUserList = true;
					waitForGameList = true;
					client.sendTCP(new ChangeStateRequest(postMenu));
				} else if (self.getCurrentState() == preGame){
					if (preGameHandler.update(gc, my_game)){
						waitForUserList = true;
						waitForGameList = true;
						client.sendTCP(new ChangeStateRequest(mainMenu));
					} 
					BeginGameRequest begin = preGameHandler.getRequest();
					
					if(begin!=null){
						waitForUserList = true;
						waitForGameList = true;
						waitForGameUpdate = true;
						client.sendTCP(begin);
					}
				} else if (self.getCurrentState() == loadGame){
					mainGameHandler = new MainGameHandler(my_game, self.getName());
					waitForUserList = true;
					waitForGameList = true;
					client.sendTCP(new ChangeStateRequest(mainGame));
				} else if (self.getCurrentState() == mainGame){
					mainGameHandler.update(gc);
					SendMessageRequest sendMessageRequest = mainGameHandler.getSendMessageRequest();
					if(sendMessageRequest != null) {
						client.sendTCP(sendMessageRequest);
						mainGameHandler.closeOutgoingMessage();
					}
					boolean turnPhaseFinished = mainGameHandler.getTurnPhaseStatus();
					if (turnPhaseFinished){
						if (my_game.getTurnPhase() == 0){ 
							waitForUserList = true;
							waitForGameList = true;
							client.sendTCP(new PlacementPhaseRequest(my_game.getGameName(), self.getName(),mainGameHandler.getInfantryPlacements(),mainGameHandler.getGeneralPlacements()));
							mainGameHandler.clearPlacementChoices();
							System.out.println("Request Sent");
							}
						else if (my_game.getTurnPhase() == 2) {
							waitForUserList = true;
							waitForGameList = true;
							Vector<Vector<Point>> locations = new Vector<Vector<Point>>();
							locations.add(mainGameHandler.getAttackerDepartingLocations());
							locations.add(mainGameHandler.getAttackerDestinations());
							locations.add(mainGameHandler.getSupporterBaseLocations());
							locations.add(mainGameHandler.getSupporterSupportLocations());
							client.sendTCP(new MovementPhaseRequest(my_game.getGameName(), self.getName(), locations));
							mainGameHandler.clearMovementPhaseInfo();
							System.out.println("Move Request Sent");
						}
					}
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
			} else if (self.getCurrentState() == hostGame){
				hostGameHandler.render(gc, g);
			} else if (self.getCurrentState() == preGame){
				preGameHandler.render(gc, g);
			} else if (self.getCurrentState() == mainGame){
				mainGameHandler.render(gc, g);
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
			} else if (o instanceof UserList) {
				user_list = (UserList)o;
				for (User user : user_list){
					if (user.getConnectionID() == my_connection){
						self = user;
					}
				}
				waitForUserList = false;
			} else if (o instanceof GameList) {
				game_list = (GameList)o;
				if(!game_in_session){
					for (GameInstance game : game_list){
						for (User user : game.getUsers()){ 
							if (user.getConnectionID() == my_connection){ my_game = game; }
						}
					}
				}
				waitForGameList = false;
			} else if (o instanceof GameInstance){
				GameInstance sentGame = (GameInstance)o;
				for (User user : sentGame.getUsers()){ 
					if (user.getConnectionID() == my_connection){ my_game = sentGame; game_in_session = true; }
				}
				waitForGameUpdate = false;
			} else if (o instanceof LoginResponse) {
				LoginResponse response = (LoginResponse)o;
				loginStatus = response.getLoginResponse();
				waitForLoginResponse = false;
			} else if (o instanceof JoinGameRequest){
				joinGameBounced = true;
				waitForResponse = false;
			} else if (o instanceof PlacementPhaseResponse){
				if (mainGameHandler!=null){
					mainGameHandler.updateMap(my_game);
					}
				System.out.println("Response Received");
			}
			else if (o instanceof MovementPhaseResponse){
				if (mainGameHandler!=null){
					mainGameHandler.updateMap(my_game);
					}
				System.out.println("Response Received");
			}
			else if (o instanceof SendMessageRequest) {
				SendMessageRequest messageData = (SendMessageRequest) o;
				if(!messageData.getFrom().equals(mainGameHandler.getName()) 
						&& messageData.getTo().equals(mainGameHandler.getName()))
					mainGameHandler.sendMessageToChat(messageData.getMessage(), messageData.getFrom());
			}
		}
	}
}
