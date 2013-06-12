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
	

}
