<<<<<<< HEAD:src/main/java/model/Player.java
package model;
=======
package edu.risk;
>>>>>>> 03505cace99079411c101e8e0de00a4e391c8cb1:riskdemo/WEB-INF/classes/edu/risk/Player.java

/**
 * @author Aris Santas<asantas93@gmail.com>
 * @version alpha 0.023
 */
public class Player {
	
	private String name;
	private int armies;
	
	public Player(String name, int armies) {
		this.name = name;
		this.armies = armies;
	}
	
	public Player(String name) {
		this(name, 0);
	}
	
	public String getName() {
		return name;
	}

	public int getArmies() {
		return armies;
	}
	
<<<<<<< HEAD:src/main/java/model/Player.java
	public void setArmies(int armies) {
		this.armies = armies;
	}	
=======
	public void setArmies(int i) {
		armies = i;
	}
	
>>>>>>> 03505cace99079411c101e8e0de00a4e391c8cb1:riskdemo/WEB-INF/classes/edu/risk/Player.java
}
