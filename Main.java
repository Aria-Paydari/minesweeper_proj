/*
 * ICS4U final project - minesweeper game
 * Aria Paydari Alamdari and Behrad Haghighi
 * January 2020
 * Mr. Benum's class
 * Main.java 
 * 
 */
public class Main implements Runnable {
	
	GameClass game = new GameClass();
	//starts thread
	public static void main2(String[] args) {
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

