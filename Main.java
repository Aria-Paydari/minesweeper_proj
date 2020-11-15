/*
 * minesweeper game
 * Aria Paydari Alamdari 
 * January 2020
 * Main.java 
 */
public class Main implements Runnable {
	
	GameClass game = new GameClass();
	//starts thread
	public static void main(String[] args) {
		(new Thread(new Main())).start();
	}


	@Override
	/**
	 * pre: Button has been chosen from menu
	 * post:Runs GameClass
	 */
	public void run() {
			game.doIt();
	}

}

