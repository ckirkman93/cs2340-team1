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
	private int currentConversationIndex;
	private String my_name;
	private TextField field;
	private boolean enterDown;
	private boolean leftMouseDown;
	private boolean tabDown;
	
	public ChatWindow(Vector<User> users, String my_name, GameContainer gc) {
		this.my_name = my_name;
		for(User u : users)
			if(u != null && !u.getName().equals(my_name))
				conversations.add(new Conversation(u.getName()));
		Font font = new Font("Arial", Font.PLAIN, 16);
		TrueTypeFont ttf = new TrueTypeFont(font, true);
		field = new TextField(gc, ttf, 25, 315, 590, 20);
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
		if(visible) {
			int mouseX = gc.getInput().getMouseX();
			int mouseY = gc.getInput().getMouseY();
			if(gc.getInput().isMouseButtonDown(0))
				leftMouseDown = true;
			else if(leftMouseDown) {
				for(int i = 0; i < conversations.size(); i++)
					if(mouseX < 25 + (i+1)*100 && mouseX > 25 + i*100
							&& mouseY > 25 && mouseY < 43)
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
			g.fillRect(25, 25, 590, 310);
			g.setColor(Color.black);
			g.drawRect(25, 25, 590, 310);
			conversations.get(currentConversationIndex).render(g);
			field.render(gc, g);
			int lengthSum = 25;
			for(Conversation c : conversations) {
				g.drawRect(lengthSum, 25, 100, 18);
				if(conversations.indexOf(c) == currentConversationIndex) {
					g.setColor(new Color(150, 150, 150, 150));
					g.fillRect(lengthSum, 25, 100, 18);
					g.setColor(Color.black);
				}
				g.drawString(c.otherName, lengthSum + 10, 25);
				lengthSum += 100;
			}
		}
	}	

	private class Conversation {
		
		private Vector<String> textBody = new Vector<String>();
		private String otherName;
		
		private Conversation(String otherName) {
			this.otherName = otherName;
		}
		
		private void addMessage(String message, boolean isOtherUser) {
			if(isOtherUser)
				textBody.add(otherName + ": " + message);
			else textBody.add(my_name + ": " + message);
			if(textBody.size() > 17)
				textBody.remove(0);
		}
		
		private void render(Graphics g) {
			g.setColor(Color.black);
			for(int i = 0; i < textBody.size(); i++)
				g.drawString(textBody.get(i), 35, 50 + i*16);
		}
	}
}
