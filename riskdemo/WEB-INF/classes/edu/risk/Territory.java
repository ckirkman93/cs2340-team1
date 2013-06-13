<<<<<<< HEAD:src/main/java/model/Territory.java
package model;
=======
package edu.risk;
>>>>>>> 03505cace99079411c101e8e0de00a4e391c8cb1:riskdemo/WEB-INF/classes/edu/risk/Territory.java

/**
 * @author Aris Santas<asantas93@gmail.com>
 * @version alpha 0.023
 */
public class Territory {
	
	private int armiesOnTerritory;
	private Player owner;

	public Territory() {}
	
	public Territory(Player owner) {
		this.owner = owner;
	}
	
	public void setOwner(Player p) {
		owner = p;
	}
	
	public void addArmy() {
		armiesOnTerritory++;
	}
	
	public void removeArmy() {
		if(armiesOnTerritory > 0)
			armiesOnTerritory--;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setArmies(int i) {
		armiesOnTerritory = i;
	}
	

}
