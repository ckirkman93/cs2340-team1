package com.dpendesigns.feudalwar;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import com.dpendesigns.feudalwar.controllers.handlers.BeginGameHandler;
import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.Player;
import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.network.ServerListener;
import com.dpendesigns.network.data.GameList;
import com.dpendesigns.network.data.UserList;
import com.dpendesigns.network.requests.AddArmyRequest;
import com.dpendesigns.network.requests.BeginGameRequest;
import com.dpendesigns.network.requests.ChangeStateRequest;
import com.dpendesigns.network.requests.ConnectRequest;
import com.dpendesigns.network.requests.JoinGameRequest;
import com.dpendesigns.network.requests.LoginRequest;
import com.dpendesigns.network.responses.LoginResponse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class FeudalWarServer {
	public static Server server;
	public static Kryo kryo;
	
	private UserList user_list;
	private GameList game_list;
	private GameList games_in_session;
	
	private JFrame serverView;
	private JLabel serverStatusText;
	
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
		user_list = new UserList();
		game_list = new GameList();
		games_in_session = new GameList();
		
		serverView = new JFrame("Server Overview");
		serverView.pack();
		serverView.setSize(256, 128);
		serverView.setVisible(true);
		serverView.setLocationRelativeTo(null);
		serverView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		serverStatusText = new JLabel("Starting server...");
		
		serverView.getContentPane().setLayout(new GridLayout(4,2));
		serverView.getContentPane().add(serverStatusText,BorderLayout.NORTH);

		System.out.println("Setting Up Completed");
		System.out.println("Booting Server...");
		try{
			
			server = new Server(16384,8192);
			
			kryo = server.getKryo();
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
			kryo.register(AddArmyRequest.class);
			
			// Register the response classes for serialization
			kryo.register(com.dpendesigns.network.responses.LoginResponse.class);
			
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
			
			server.addListener(new ServerListener(server, user_list, game_list, games_in_session));
			
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
	
	public UserList getUserList(){return user_list;}
	public GameList getGameList(){return game_list;}
	public GameList getGamesInSession(){return game_list;}
}
