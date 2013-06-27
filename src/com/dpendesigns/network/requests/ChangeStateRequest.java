package com.dpendesigns.network.requests;

public class ChangeStateRequest {
	private static final long serialVersionUID = 9000L;
	private int requestedState;
	
	public ChangeStateRequest (){}
	public ChangeStateRequest (int state){requestedState = state;}
	public int getRequestedState(){return requestedState;}
}
