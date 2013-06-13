package edu.risk;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")

public class Risk extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    	ArrayList<Player> list = new ArrayList<Player>();
    	list.add(new Player("Jim", 10));
    	list.add(new Player("John", 10));
    	list.add(new Player("Jake", 10));

        Game game = new Game(list);
        
    	
    }

}
