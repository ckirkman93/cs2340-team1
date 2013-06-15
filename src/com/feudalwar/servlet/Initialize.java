package com.feudalwar.servlet;
import com.feudalwar.model.Player;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


import javax.servlet.Servlet;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Initialize extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;
	
	public Initialize(){
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		request.getRequestDispatcher("/FeudalWar.jsp").forward(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Collection<Player> playerlist = (Collection)session.getAttribute("playerlist");
		for (Player player : playerlist){
			player.setInfantry(3);
			player.setGenerals(1);
		}
		session.setAttribute("playerlist", playerlist);
		request.getRequestDispatcher("/FeudalWar.jsp").forward(request, response);
	}
	
}
