package model;

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
	
	public void setArmies(int i) {
		armies = i;
	}
}
