package com.dpendesigns.feudalwar.controllers.handlers;

import java.awt.Font;
import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;

import com.dpendesigns.feudalwar.model.User;
import com.dpendesigns.network.requests.SendMessageRequest;

public class ChatWindow {
	
	private boolean visible;
	private Vector<Conversation> conversations = new Vector<Conversation>(5);
	private boolean[] unread = new boolean[6];
	private int currentConversationIndex;
	private String my_name;
	private TextField field;
	private boolean enterDown;
	private boolean leftMouseDown;
	private boolean tabDown;
	
	private static final int TAB_WIDTH = 118;
	private static final int TAB_HEIGHT = 18;
	private static final int X_POSITION = 25;
	private static final int Y_POSITION = 25;
	private static final int NAME_BUFFER = 5;
	private static final int GAME_WIDTH = 640;
	private static final int GAME_HEIGHT = 360;
	private static final int FIELD_HEIGHT = 20;
	
	
	public ChatWindow(Vector<User> users, String my_name, GameContainer gc) {
		this.my_name = my_name;
		for(User u : users)
			if(u != null && !u.getName().equals(my_name))
				conversations.add(new Conversation(u.getName()));
		Font font = new Font("Arial", Font.PLAIN, 16);
		TrueTypeFont ttf = new TrueTypeFont(font, true);
		field = new TextField(gc, ttf, X_POSITION, GAME_HEIGHT - FIELD_HEIGHT - Y_POSITION, GAME_WIDTH - X_POSITION*2, FIELD_HEIGHT);
	}

	public void addMessage(String message, String name, boolean isOtherUser) {
		for(Conversation c : conversations) {
			if(c.otherName.equals(name)) {
				c.addMessage(message, isOtherUser);
			}				
		}
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public SendMessageRequest update(GameContainer gc) {
		SendMessageRequest sendMessageRequest = null;
		if(gc.getInput().isKeyDown(Input.KEY_TAB))
			tabDown = true;
		else if(tabDown) {
			tabDown = false;
			visible = !visible;
		}
		for(int i = 0; i < conversations.size(); i++) {
			if(!conversations.get(i).textBody.isEmpty()) {
				String message = conversations.get(i).textBody.remove(conversations.get(i).textBody.size() - 1);
				if(message.charAt(message.length() - 1) == '*') {
					conversations.get(i).textBody.add(message.substring(0, message.length() - 1));
					unread[i] = true;
				} else conversations.get(i).textBody.add(message);
			}
		}
		if(visible) {
			field.setFocus(true);
			int mouseX = gc.getInput().getMouseX();
			int mouseY = gc.getInput().getMouseY();
			if(gc.getInput().isMouseButtonDown(0))
				leftMouseDown = true;
			else if(leftMouseDown) {
				for(int i = 0; i < conversations.size(); i++)
					if(mouseX < X_POSITION + (i+1)*TAB_WIDTH && mouseX > X_POSITION + i*TAB_WIDTH
							&& mouseY > Y_POSITION && mouseY < Y_POSITION + TAB_HEIGHT)
						currentConversationIndex = i;
				leftMouseDown = false;
			}
			if(gc.getInput().isKeyDown(Input.KEY_ENTER))
				enterDown = true;
			else if (enterDown) {
				enterDown = false;
				String outgoingMessage = field.getText();
				sendMessageRequest = new SendMessageRequest(
						outgoingMessage, my_name, 
						conversations.get(currentConversationIndex).otherName);
				conversations.get(currentConversationIndex).
						addMessage(outgoingMessage, false);
				field.setText("");
			}
		}
		return sendMessageRequest;
	}
	
	public void render(GameContainer gc, Graphics g) {
		if(visible) {
			g.setColor(new Color(255, 255, 255, 150));
			g.fillRect(X_POSITION, Y_POSITION, GAME_WIDTH - X_POSITION*2, GAME_HEIGHT - Y_POSITION*2);
			g.setColor(Color.black);
			g.drawRect(X_POSITION, Y_POSITION, GAME_WIDTH - X_POSITION*2, GAME_HEIGHT - Y_POSITION*2);
			conversations.get(currentConversationIndex).render(g);
			unread[currentConversationIndex] = false;
			field.render(gc, g);
			int lengthSum = X_POSITION;
			for(Conversation c : conversations) {
				g.drawRect(lengthSum, Y_POSITION, TAB_WIDTH, TAB_HEIGHT);
				if(conversations.indexOf(c) == currentConversationIndex) {
					g.setColor(new Color(150, 150, 150, 150));
					g.fillRect(lengthSum, Y_POSITION, TAB_WIDTH, TAB_HEIGHT);
					g.setColor(Color.black);
					g.drawString(c.otherName, lengthSum + NAME_BUFFER, Y_POSITION);
				} else if(unread[conversations.indexOf(c)]) {
					g.drawString(c.otherName + " (new)", lengthSum + NAME_BUFFER, Y_POSITION);
				} else	g.drawString(c.otherName, lengthSum + NAME_BUFFER, Y_POSITION);
				lengthSum += TAB_WIDTH;
			}
		}
	}	

	private class Conversation {
		
		private Vector<String> textBody = new Vector<String>();
		private String otherName;
		private static final int Y_MESSAGE_BUFFER = 16;
		private static final int Y_BODY_BUFFER = 25;
		private static final int X_BODY_BUFFER = 10;
		
		private Conversation(String otherName) {
			this.otherName = otherName;
		}
		
		private void addMessage(String message, boolean isOtherUser) {
			if(isOtherUser)
				textBody.add(otherName + ": " + message + "*");
			else textBody.add(my_name + ": " + message + " ");
			if(textBody.size() > 17)
				textBody.remove(0);
		}
		
		private void render(Graphics g) {
			g.setColor(Color.black);
			for(int i = 0; i < textBody.size(); i++)
				g.drawString(textBody.get(i), X_POSITION + X_BODY_BUFFER, 
						Y_POSITION + Y_BODY_BUFFER + i*Y_MESSAGE_BUFFER);
		}
	}
}
