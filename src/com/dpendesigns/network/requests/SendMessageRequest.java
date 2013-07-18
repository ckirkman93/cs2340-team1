package com.dpendesigns.network.requests;

public class SendMessageRequest {
	
	private String message;
	private String from;
	private String to;
	
	public SendMessageRequest() {}
	
	public SendMessageRequest(String message, String from, String to) {
		this.message = message;
		this.from = from;
		this.to = to;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
}
