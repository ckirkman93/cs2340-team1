package com.feudalwar.servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.feudalwar.model.Player;

public class Setup extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;
	private Collection<Player> playerlist = new ArrayList<Player>();
	
	public Setup(){
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);

		session.setAttribute("playercount", playerlist.size());
		request.getRequestDispatcher("/Setup.jsp").forward(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		
		if (request.getParameter("newplayer")!=null && request.getParameter("name").replaceAll("\\s","")!=""){playerlist.add(new Player(request.getParameter("name"),request.getParameter("color")));}
		
		session.setAttribute("playercount", playerlist.size());
		session.setAttribute("playerlist", playerlist);
		request.getRequestDispatcher("/Setup.jsp").forward(request, response);
	}
	
}
