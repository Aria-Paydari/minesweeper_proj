/*
 * ICS4U final project - minesweeper game
 * Aria Paydari Alamdari and Behrad Haghighi
 * January 2020
 * Mr. Benum's class
 * 
 * Tile.java 
 */
public class Tile {
	private int xCor, yCor;
	private boolean isBomb;//if true the tile has a bomb, otherwise it doesn't
	private int nearby=0; // displays how many bombs are nearby in the specific coordinates
	private boolean revealed;// if the space has been clicked and revealed it's true
	private boolean flagged; // becomes true if the player puts a flag on it
	public Tile(int x, int y) {
		this.xCor=x;
		this.yCor=y;
	}
	/**
	 * pre: Bomb has been randomly generated
	 * post: sets a bomb to the tile
	 * @param bomb
	 */
	public void setBomb(boolean bomb) {
		//sets the bomb state
		isBomb=bomb;
	}
	/**
	 * 
	 * @return
	 */
	public boolean hasBomb() {
		return isBomb;//returns whether it's a bomb or not
	}
	public int getNearby() {
		//returns the number of bombs around it
		return nearby;
	}
	public void setNearby(int amount) {
		//sets the number of nearby bombs
		nearby=amount;
	}
	public void addNearby(int amount) {
		//adds one to the number of nearby bombs
		nearby+=amount;
	}
	
	public void setRevealed(boolean state) {
		//reveales the tile when called with true
		revealed=state;
	}
	public boolean isRevealed() {
		return revealed; //checks whether the tile is revealed or not
	}
	public boolean isFlagged() {//checks whether the tile has a flag on it or not
		return flagged;
	}
	public void setFlagged(boolean state) {
		flagged=state; // sets or removes a flag when a tile is clicked
		//depending on whether
	}
}
