package com.dpendesigns.network;

import java.awt.Point;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Vector;

import org.newdawn.slick.SlickException;

import com.dpendesigns.feudalwar.controllers.handlers.BeginGameHandler;
import com.dpendesigns.feudalwar.controllers.handlers.Map;
import com.dpendesigns.feudalwar.model.GameInstance;
import com.dpendesigns.feudalwar.model.General;
import com.dpendesigns.feudalwar.model.Infantry;
import com.dpendesigns.feudalwar.model.MilitaryUnit;
import com.dpendesigns.feudalwar.model.Player;
import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.network.data.GameList;
import com.dpendesigns.network.data.ProvinceData;
import com.dpendesigns.network.data.UserList;
import com.dpendesigns.network.requests.AddArmyRequest;
import com.dpendesigns.network.requests.BeginGameRequest;
import com.dpendesigns.network.requests.ChangeStateRequest;
import com.dpendesigns.network.requests.JoinGameRequest;
import com.dpendesigns.network.requests.LoginRequest;
import com.dpendesigns.network.requests.MovementPhaseRequest;
import com.dpendesigns.network.requests.PlacementPhaseRequest;
import com.dpendesigns.network.responses.LoginResponse;
import com.dpendesigns.network.responses.MovementPhaseResponse;
import com.dpendesigns.network.responses.PlacementPhaseResponse;
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
		
		//System.out.println("The Same: " + newUserName.equals("Enter Name"));
		
		int loginStatus = loginAccepted;
		
		for(User user: user_list){
			if ( (user.getName().equals(newUserName) && user.getConnectionID()!=c.getID()) || newUserName.equals("Enter Name") || newUserName.equals("default_player")){
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
	protected void parsePlacementPhaseRequest(Connection c, Object o){
		PlacementPhaseRequest placementPhaseRequest = (PlacementPhaseRequest)o;
		System.out.println("Request Received");
		for (GameInstance game : games_in_session){
			System.out.println(game.getGameName());
			System.out.println(placementPhaseRequest.getGameName());
			System.out.println("");
			if (game.getGameName().equals(placementPhaseRequest.getGameName())){
				//System.out.println("Found the game");
				for (Player player: game.getPlayers()){
					if (player.getName().equals(placementPhaseRequest.getUserName())){
						//System.out.println("Found the player");
						for (int[] position : placementPhaseRequest.getPlacedInfantry()){
							game.getMap().getProvinces()[position[0]][position[1]].addOccupyingUnit(new Infantry(player), true);
						}
						for (int[] position : placementPhaseRequest.getPlacedGenerals()){
							game.getMap().getProvinces()[position[0]][position[1]].addOccupyingUnit(new General(player), true);
						}
						game.removePlayerFromWaiting(placementPhaseRequest.getUserName());
					}
				}
				System.out.println(game.remainingWait());
				if (game.remainingWait() == 0){
					game.enterNextPhase();
					//game.enterNextPhase();
					for (User user : game.getUsers()){
						user.setState(loadGame);
						server.sendToTCP(user.getConnectionID(), game);
						server.sendToTCP(user.getConnectionID(), new PlacementPhaseResponse());
					}
				}
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
	
	protected void parseMovementRequest(Connection c, Object o){
		MovementPhaseRequest movementPhaseRequest = (MovementPhaseRequest)o;
		System.out.println("Request Received");
		for (GameInstance game : games_in_session){
			System.out.println(game.getGameName());
			System.out.println(movementPhaseRequest.getGameName());
			System.out.println("");
			if (game.getGameName().equals(movementPhaseRequest.getGameName())){
				//System.out.println("Found the game");
				for (Player player: game.getPlayers()){
					if (player.getName().equals(movementPhaseRequest.getUserName())){
						//System.out.println("Found the player");
						player.setAttackerDepartingLocations(movementPhaseRequest.getAttackerDepartingLocations());
						player.setAttackerDestinations(movementPhaseRequest.getAttackerDestinations());
						player.setSupporterBaseLocations(movementPhaseRequest.getSupporterBaseLocations());
						player.setSupporterDefenseLocations(movementPhaseRequest.getSupporterDefenseLocations());
						game.removePlayerFromWaiting(movementPhaseRequest.getUserName());
					}
				}
				System.out.println(game.remainingWait());
				if (game.remainingWait() == 0){
					resolveConflicts(game);
					game.enterNextPhase();
					for (User user : game.getUsers()){
						user.setState(loadGame);
						server.sendToTCP(user.getConnectionID(), game);
						server.sendToTCP(user.getConnectionID(), new MovementPhaseResponse());
					}
				}
			}
		}
	}
	
	private void resolveConflicts(GameInstance game){
		Vector<Player> players = game.getPlayers();
		Vector<Vector<Vector<MilitaryUnit>>> newUnresolvedPositions = new Vector<Vector<Vector<MilitaryUnit>>>();
		for(int i=0;i<game.getMap().getProvinces().length;i++){
			Vector<Vector<MilitaryUnit>> nextRow = new Vector<Vector<MilitaryUnit>>(); 
			for(int j=0;j<game.getMap().getProvinces()[i].length;j++){
				Vector<MilitaryUnit> addSet = new Vector<MilitaryUnit>();
				if(game.getMap().getProvinces()[i][j]!=null && game.getMap().getProvinces()[i][j].getOccupyingUnit()!=null){
					addSet.add(game.getMap().getProvinces()[i][j].getOccupyingUnit());
				}
				nextRow.add(addSet);
			}
			newUnresolvedPositions.add(nextRow);
		}
		Vector<MovementPair> noConflicts = new Vector<MovementPair>();
		Vector<Point> resolutionConflicts = new Vector<Point>();
		Vector<Vector<MovementPair>> allAttackerPaths = new Vector<Vector<MovementPair>>();
		for(int i=0;i<players.size();i++){
			Player player=players.get(i);
			Vector<MovementPair> playerAttackerPaths = new Vector<MovementPair>();
			for(Point base : player.getAttackerDepartingLocations()){
				Point dest = player.getAttackerDestinations().elementAt(player.getAttackerDepartingLocations().indexOf(base));
				int index = player.getAttackerDepartingLocations().indexOf(base);
				playerAttackerPaths.add(new MovementPair(dest,base,index));
			}
			allAttackerPaths.add(playerAttackerPaths);
		}
		for(int i=0;i<players.size();i++){
			PriorityQueue<Integer> indexList = new PriorityQueue<Integer>();
			for(int j=0;j<players.size();j++){
				for(int k=0;k<allAttackerPaths.get(i).size();k++){
					for(int l=0;l<allAttackerPaths.get(j).size();l++){
						if(allAttackerPaths.get(i).get(k).getBase().equals(allAttackerPaths.get(j).get(l).getDest()) &&
								allAttackerPaths.get(i).get(k).getDest().equals(allAttackerPaths.get(j).get(l).getBase())){
							indexList.add(-1*allAttackerPaths.get(i).get(k).getOriginalIndex());
						}
					}
				}
			}
			Vector<Point> attackerDepartingList = players.get(i).getAttackerDepartingLocations();
			Vector<Point> attackerDestinationList = players.get(i).getAttackerDestinations();
			for(int k=0;k<indexList.size();k++){
				attackerDepartingList.remove(-1*indexList.peek());
				attackerDestinationList.remove(-1*indexList.poll());
			}
			players.get(i).setAttackerDepartingLocations(attackerDepartingList);
			players.get(i).setAttackerDestinations(attackerDestinationList);
		}
		
		for(Player player : players){
			for(Point base : player.getAttackerDepartingLocations()){
				Point dest = player.getAttackerDestinations().elementAt(player.getAttackerDepartingLocations().indexOf(base));
				MilitaryUnit movedUnit = game.getMap().getProvinces()[base.x][base.y].getOccupyingUnit();
				newUnresolvedPositions.get(base.x).get(base.y).remove(movedUnit);
				newUnresolvedPositions.get(dest.x).get(dest.y).add(movedUnit);
				MovementPair pair = new MovementPair(dest,base);
				if(newUnresolvedPositions.get(dest.x).get(dest.y).size()>1){
					resolutionConflicts.add(dest);
					noConflicts.remove(pair);
				}
				else{
					noConflicts.add(pair);
				}
			}
		}
		for(MovementPair pair : noConflicts){
			Point dest = pair.getDest();
			Point base = pair.getBase();
			game.getMap().getProvinces()[dest.x][dest.y].addOccupyingUnit(game.getMap().getProvinces()[base.x][base.y].getOccupyingUnit(),false);
			game.getMap().getProvinces()[base.x][base.y].addOccupyingUnit(null,false);
			System.out.println("Moved");
		}
		for(Player player : players){
			for(int i=0;i < player.getSupporterBaseLocations().size();i++){
				Point base = player.getSupporterBaseLocations().get(i);
				Point target = player.getSupporterDefenseLocations().get(i);
				if(newUnresolvedPositions.get(base.x).get(base.y).size()!=1){
					player.getSupporterBaseLocations().remove(base);
					player.getSupporterDefenseLocations().remove(target);
				}
				else if(game.getMap().getProvinces()[target.x][target.y].getOccupyingUnit()!=null){
					game.getMap().getProvinces()[target.x][target.y].getOccupyingUnit().upSupportStrength(); 
				}
			}
		}
		while(!resolutionConflicts.isEmpty()){
			Point conflictPoint = resolutionConflicts.remove(resolutionConflicts.size()-1);
			Vector<MilitaryUnit> potentialVictors = new Vector<MilitaryUnit>();
			int maxStrength = 0;
			for(MilitaryUnit unit : newUnresolvedPositions.get(conflictPoint.x).get(conflictPoint.y)){
				if(unit.getStrength()+unit.getSupportStrength()>maxStrength){
					potentialVictors.clear();
					maxStrength=unit.getStrength()+unit.getSupportStrength();
					potentialVictors.add(unit);
				}
				else if(unit.getStrength()+unit.getSupportStrength()==maxStrength){
					potentialVictors.add(unit);
				}
				unit.resetSupportStrength();
			}
			newUnresolvedPositions.get(conflictPoint.x).get(conflictPoint.y).removeAllElements();
			if(potentialVictors.size()==1){
				Vector<Point> emptyAdjacentPoints = new Vector<Point>();
				Vector<Point> emptyAdjacentAlliedPoints = new Vector<Point>();
				Point source=null;
				for(Point adj : game.getMap().getProvinces()[conflictPoint.x][conflictPoint.y].getAdjacents()){
					if(game.getMap().getProvinces()[adj.x][adj.y].getOccupyingUnit()!=null && game.getMap().getProvinces()[adj.x][adj.y].getOccupyingUnit().equals(potentialVictors.get(0))){
						game.getMap().getProvinces()[adj.x][adj.y].addOccupyingUnit(null,false);
						source = adj;
					}
					else if(newUnresolvedPositions.get(adj.x).get(adj.y).isEmpty()){
						if(game.getMap().getProvinces()[adj.x][adj.y].getLastOwner().equals(
								game.getMap().getProvinces()[conflictPoint.x][conflictPoint.y].getLastOwner())){
							emptyAdjacentAlliedPoints.add(adj);
						}
						emptyAdjacentPoints.add(adj);
					}
				}
				if(emptyAdjacentPoints.size()!=0 && source!=null){
					Vector<Point> preference = new Vector<Point>();
					if(conflictPoint.x%2==0){
						if(conflictPoint.x==source.x){
							preference.add(new Point(conflictPoint.x,(2*conflictPoint.y-source.y)));
							if(conflictPoint.y<source.y){
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
							}
							else{
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y-1));
							}
						}
						else if(conflictPoint.x<source.x){
							if(conflictPoint.y==source.y){
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y-1));
							}
							else{
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
							}
						}
						else{
							if(conflictPoint.y==source.y){
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y-1));
							}
							else{
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
							}
						}
					}
					else{
						if(conflictPoint.x==source.x){
							preference.add(new Point(conflictPoint.x,(2*conflictPoint.y-source.y)));
							if(conflictPoint.y<source.y){
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y+1));
							}
							else{
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
							}
						}
						else if(conflictPoint.x<source.x){
							if(conflictPoint.y==source.y){
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
							}
							else{
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y+1));
							}
						}
						else{
							if(conflictPoint.y==source.y){
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y));
							}
							else{
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x+1,conflictPoint.y));
								preference.add(new Point(conflictPoint.x,conflictPoint.y+1));
								preference.add(new Point(conflictPoint.x,conflictPoint.y-1));
								preference.add(new Point(conflictPoint.x-1,conflictPoint.y+1));
							}
						}
					}
					Point pushTo = null;
					for(int i=preference.size()-1;i>=0;i--){
						if(emptyAdjacentAlliedPoints.contains(preference.get(i))){
							pushTo=preference.get(i);
						}
					}
					if(pushTo==null)
						for(int i=preference.size()-1;i>=0;i--){
						if(emptyAdjacentPoints.contains(preference.get(i))){
							pushTo=preference.get(i);
						}
					}
					
					game.getMap().getProvinces()[pushTo.x][pushTo.y].addOccupyingUnit(game.getMap().getProvinces()[conflictPoint.x][conflictPoint.y].getOccupyingUnit(),false);
				}
				game.getMap().getProvinces()[conflictPoint.x][conflictPoint.y].addOccupyingUnit(potentialVictors.get(0),false);
				newUnresolvedPositions.get(conflictPoint.x).get(conflictPoint.y).remove(potentialVictors.get(0));
			}
		}
	}
	
	private class MovementPair{
		private Point dest;
		private Point base;
		private int originalIndex;
		
		public MovementPair(){}
		public MovementPair(Point dest, Point base){
			this.dest=dest;
			this.base=base;
			originalIndex=-1;
		}
		public MovementPair(Point dest, Point base, int index){
			this.dest=dest;
			this.base=base;
			originalIndex=index;
		}
		
		public Point getDest(){return dest;}
		public Point getBase(){return base;}
		public int getOriginalIndex(){return originalIndex;}
		public void setDest(Point dest){this.dest=dest;}
		public void setBase(Point base){this.base=base;}
		
		public boolean equals(Object o){
			if(o instanceof MovementPair){
				if(((MovementPair) o).dest.equals(dest)) return true;
			}
			return false;
		}
	}
}