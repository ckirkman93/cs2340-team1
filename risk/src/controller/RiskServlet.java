package controller;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import model.Game;
import model.Player;
 
public class RiskServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException{
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		ArrayList<Player> list = new ArrayList<Player>();
		for(int i = 0; i < 6;) {
			String str = JOptionPane.showInputDialog("Enter Player " + (i + 1) + "'s name.");
			if(str == null && i >= 3)
				break;
			list.add(new Player(str));
			i++;
		}
		Game game = new Game(list);
		String str = "";
		for(Player p : game.getPlayers())
			str += p.getName() + ", ";
		out.println("<h1> Order of players: " + str 
				+ " number of initial armies: " + game.getInitialArmies() + "<h1>");
		
		out.println("</body>");
		out.println("</html>");	
	}
}
