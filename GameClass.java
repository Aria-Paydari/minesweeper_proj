
/*
 * ICS4U final project - minesweeper game
 * Aria Paydari Alamdari and Behrad Haghighi
 * January 2020
 * Mr. Benum's class
 * 
 * GameClass.java 
 * (contained nested classed Board, Move, Click)
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameClass extends JFrame {
	
	boolean victory = false;
	
	Date startDate = new Date();
	
	Date winDate;
	// tracks the time
	static int time;
	private int hundreds;
	private int tens;
	private boolean checkEndMessage = true; // to prevent the final panel from displaying infinitely
	private int ones;
	// size of playing field
	private final int ROWS=16;
	private final int COLS=12;
	// variable that store the x and y coordinates of the place that has been clicked
	private int xCoord = -100;
	private int yCoord = -100;
	//flag symbol coordinates
	private int flaggerX = 500;
	private int flaggerY = 25;
	//smiley face coordiantes
	private int smileyX = 365;
	private int smileyY = 15;
	//clock coordinates
	private int clockX = 600;
	private int clockY = 25;
	// coordinates for displaying victory message
	private int vicX = -250;
	private int vicY = 25;
	private int vicX2 = -250;
	//do ta kam mishe
	
	Tile[][] tilesArray= new Tile[ROWS][COLS];
//	private int[][] table = new int[ROWS][COLS];//if table [i][j] = 1 it has a bomb, if = 0 it doesn't
//	private int[][] nearby = new int[ROWS][COLS]; // displays how many bombs are nearby in the specific coordinates
//	private boolean[][] revealed = new boolean[ROWS][COLS];// if the space has been clicked it's true
//	private boolean[][] flagged = new boolean[ROWS][COLS]; // becomes true if the player puts a flag on it
	
	Random rand = new Random();// used to make a random number of mines
	
	private boolean flagger = false;//become true if player clicked the flag, returns to false if clicked again
	
	private boolean happy = true;//becomes unhappy if a mine is selected
	
	public GameClass() {
		// initializes the bombs
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				tilesArray[i][j]=new Tile(i,j);
				tilesArray[i][j].setBomb(false);
				//tilesArray[i][j].setNearby(0);
				tilesArray[i][j].setRevealed(false);
				tilesArray[i][j].setFlagged(false);
				if (rand.nextInt(100) <Menu.difficulty && j > 1) {
					//randomly generates mines - adjust difficulty
					//Table array means it has a bomb at i and j
					tilesArray[i][j].setBomb(true);
				}
			}
		}
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				for (int m = 0; m < ROWS; m++) {
					for (int n = 0; n < COLS; n++) {
						if (Math.abs(i-m) <= 1 && Math.abs(j-n) <= 1 && (i != m || j != n) && tilesArray[m][n].hasBomb()) {
							tilesArray[i][j].addNearby(1);
							//Saves the number that is to be drawn on each square- how many mines it has around it
						}
					}
				}
				//System.out.println(tilesArray[i][j].getNearby());
			
			}
		}
		// initializes the Board, Click and Move classes
		Board board = new Board();
		Move move = new Move();
		Click click = new Click();
		
		this.setSize(806,630);
		this.setTitle("Minesweeper");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
		this.setContentPane(board);
		
		board.addMouseMotionListener(move);
		board.addMouseListener(click);
	}
	/*
	 * Board class - draws graphics on a board for gameplay
	 */
	public class Board extends JPanel {
		//this method is run automatically when repaint is called - repaints the board every time
		public void paintComponent (Graphics g) {
			//background
			g.fillRect(0, 0, 800, 600);
			for (int i = 0; i < ROWS; i++) {
				for (int j = 2; j < COLS; j++) {
					g.setColor(Color.GRAY); // square color when not yet clicked on
					if (tilesArray[i][j].isRevealed()) {
						g.setColor(Color.WHITE);
						if (tilesArray[i][j].getNearby() > 0) {
							g.setColor(Color.LIGHT_GRAY); // color is it doesn't have a mine
						}
						if (tilesArray[i][j].hasBomb()) {
							g.setColor(Color.RED); // square colour if a mine is selected
						}
						
					}
					
					
					
					g.fillRect(2 + 50 * i, 2 + 50 * j, 46, 46);
					
					if (tilesArray[i][j].isFlagged()) {
						//draws flag symbol
						g.setColor(Color.BLACK);
						g.fillRect(i * 50 + 24, j * 50 + 10, 3, 30);
						g.fillRect(i * 50 + 20, j * 50 + 35, 11, 5);
						g.fillRect(i * 50 + 16, j * 50 + 37, 19, 3);
						g.setColor(Color.RED);
						g.fillRect(i * 50 + 15, j * 50 + 10, 9, 9);
					}
					//draws numbers if there are bombs around
					if (tilesArray[i][j].getNearby() > 0 && !tilesArray[i][j].hasBomb() && tilesArray[i][j].isRevealed()) {
						int k = tilesArray[i][j].getNearby();
						if (k == 1) {
							//draws blue 1
							g.setColor(Color.BLUE);
							g.fillRect(i * 50 + 23, j * 50 + 10, 4, 30);
							g.fillRect(i * 50 + 19, j * 50 + 36, 12, 4);
							g.fillRect(i * 50 + 19, j * 50 + 10, 8, 4);
						} else if (k == 2) {
							//draws green 2
							g.setColor(new Color(0,180,0));
							g.fillRect(i * 50 + 15, j * 50 + 10, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 23, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 36, 20, 4);
							g.fillRect(i * 50 + 31, j * 50 + 10, 4, 17);
							g.fillRect(i * 50 + 15, j * 50 + 23, 4, 17);
						} else if (k == 3) {
							g.setColor(Color.RED);
							//draws red 3
							g.fillRect(i * 50 + 15, j * 50 + 10, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 23, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 36, 20, 4);
							g.fillRect(i * 50 + 31, j * 50 + 10, 4, 30);
						} else if (k == 4) {
							g.setColor(new Color(15,15,112));//draws purple 4
							g.fillRect(i * 50 + 30, j * 50 + 10, 4, 30);
							g.fillRect(i * 50 + 16, j * 50 + 10, 4, 14);
							g.fillRect(i * 50 + 16, j * 50 + 20, 20, 4);
						} else if (k == 5) {
							//draws orange-ish 5
							g.setColor(new Color(165,42,42));
							g.fillRect(i * 50 + 15, j * 50 + 10, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 23, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 36, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 10, 4, 17);
							g.fillRect(i * 50 + 31, j * 50 + 23, 4, 17);
						} else if (k == 6) {
							//draw 6
							g.setColor(new Color(32,178,170));
							g.fillRect(i * 50 + 15, j * 50 + 10, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 23, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 36, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 10, 4, 17);
							g.fillRect(i * 50 + 31, j * 50 + 23, 4, 17);
							g.fillRect(i * 50 + 15, j * 50 + 23, 4, 17);
						} else if (k == 7) {
							//draws 7
							g.setColor(Color.MAGENTA);
							g.fillRect(i * 50 + 15, j * 50 + 10, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 10, 4, 5);
							g.fillRect(i * 50 + 31, j * 50 + 10, 4, 8);
							g.fillRect(i * 50 + 29, j * 50 + 18, 4, 4);
							g.fillRect(i * 50 + 27, j * 50 + 22, 4, 4);
							g.fillRect(i * 50 + 25, j * 50 + 26, 4, 4);
							g.fillRect(i * 50 + 23, j * 50 + 30, 4, 4);
							g.fillRect(i * 50 + 21, j * 50 + 34, 4, 4);
						} else if (k == 8) {
							//draw 8
							g.setColor(new Color(139,139,131));
							g.fillRect(i * 50 + 15, j * 50 + 10, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 23, 20, 4);
							g.fillRect(i * 50 + 15, j * 50 + 36, 20, 4);
							g.fillRect(i * 50 + 31, j * 50 + 10, 4, 30);
							g.fillRect(i * 50 + 15, j * 50 + 10, 4, 30);
						}
					}
				}
			}
			
			// draws corner flag box
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(flaggerX, flaggerY, 50, 50);
			if (flagger == true) {
				// if the flag is clicked
				// draws a red border around the box
				g.setColor(Color.BLACK);
				g.fillRect(flaggerX + 24, flaggerY + 10, 3, 30);
				g.fillRect(flaggerX + 20, flaggerY + 35, 11, 5);
				g.fillRect(flaggerX + 16, flaggerY + 37, 19, 3);
				g.setColor(Color.RED);
				g.fillRect(flaggerX + 15, flaggerY + 10, 9, 9);
				g.fillRect(flaggerX, flaggerY, 50, 4);
				g.fillRect(flaggerX, flaggerY + 46, 50, 4);
				g.fillRect(flaggerX, flaggerY, 4, 50);
				g.fillRect(flaggerX + 46, flaggerY, 4, 50);
			} else {
				// draws the box without the border around
				g.setColor(Color.BLACK);
				g.fillRect(flaggerX + 24, flaggerY + 10, 3, 30);
				g.fillRect(flaggerX + 20, flaggerY + 35, 11, 5);
				g.fillRect(flaggerX + 16, flaggerY + 37, 19, 3);
				g.setColor(Color.RED);
				g.fillRect(flaggerX + 15, flaggerY + 10, 9, 9);
			}
			//draws smiley face
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(smileyX, smileyY, 70, 70);
			g.setColor(Color.YELLOW);
			g.fillOval(smileyX + 10, smileyY + 10, 50, 50);
			g.setColor(Color.BLACK);
			g.fillOval(smileyX + 20, smileyY + 25, 10, 10);
			g.fillOval(smileyX + 40, smileyY + 25, 10, 10);
			if (happy == true) {
				g.fillRect(smileyX + 26, smileyY + 46, 19, 4);
				g.fillRect(smileyX + 23, smileyY + 42, 5, 5);
				g.fillRect(smileyX + 43, smileyY + 42, 5, 5);
				//facial expressions - happy and unhappy
			} else {
				g.fillRect(smileyX + 26, smileyY + 44, 19, 4);
				g.fillRect(smileyX + 23, smileyY + 46, 5, 5);
				g.fillRect(smileyX + 43, smileyY + 46, 5, 5);
			}
			g.setColor(Color.LIGHT_GRAY);
			//draws clock box
			g.fillRect(clockX, clockY, 200, 50);
			time = (int) (new Date().getTime() - startDate.getTime()) / 1000;
			if (happy == true && victory == false) {
				ones = (int) time % 10;
				tens = (int) (time % 100 - ones) / 10;
				hundreds = (int) (time % 1000 - time % 100) / 100;
			}
			g.setColor(Color.BLACK);
			if (!happy) {
				g.setColor(Color.RED);
			}
			if (victory) {
				g.setColor(Color.GREEN);
			}
			// HUNDREDS NUMBER DISPLAY
			if (hundreds == 0) {
				g.fillRect(clockX + 15, clockY + 10, 4, 30);
				g.fillRect(clockX + 31, clockY + 10, 4, 30);
				g.fillRect(clockX + 15, clockY + 10, 20, 4);
				g.fillRect(clockX + 15, clockY + 36, 20, 4);
			}
			if (hundreds == 1) {
				g.fillRect(clockX + 23, clockY + 10, 4, 30);
				g.fillRect(clockX + 19, clockY + 36, 12, 4);
				g.fillRect(clockX + 19, clockY + 10, 8, 4);
			} else if (hundreds == 2) {
				g.fillRect(clockX + 15, clockY + 10, 20, 4);
				g.fillRect(clockX + 15, clockY + 23, 20, 4);
				g.fillRect(clockX + 15, clockY + 36, 20, 4);
				g.fillRect(clockX + 31, clockY + 10, 4, 17);
				g.fillRect(clockX + 15, clockY + 23, 4, 17);
			} else if (hundreds == 3) {
				g.fillRect(clockX + 15, clockY + 10, 20, 4);
				g.fillRect(clockX + 15, clockY + 23, 20, 4);
				g.fillRect(clockX + 15, clockY + 36, 20, 4);
				g.fillRect(clockX + 31, clockY + 10, 4, 30);
			} else if (hundreds == 4) {
				g.fillRect(clockX + 30, clockY + 10, 4, 30);
				g.fillRect(clockX + 16, clockY + 10, 4, 14);
				g.fillRect(clockX + 16, clockY + 20, 20, 4);
			} else if (hundreds == 5) {
				g.fillRect(clockX + 15, clockY + 10, 20, 4);
				g.fillRect(clockX + 15, clockY + 23, 20, 4);
				g.fillRect(clockX + 15, clockY + 36, 20, 4);
				g.fillRect(clockX + 15, clockY + 10, 4, 17);
				g.fillRect(clockX + 31, clockY + 23, 4, 17);
			} else if (hundreds == 6) {
				g.fillRect(clockX + 15, clockY + 10, 20, 4);
				g.fillRect(clockX + 15, clockY + 23, 20, 4);
				g.fillRect(clockX + 15, clockY + 36, 20, 4);
				g.fillRect(clockX + 15, clockY + 10, 4, 17);
				g.fillRect(clockX + 31, clockY + 23, 4, 17);
				g.fillRect(clockX + 15, clockY + 23, 4, 17);
			} else if (hundreds == 7) {
				g.fillRect(clockX + 15, clockY + 10, 20, 4);
				g.fillRect(clockX + 15, clockY + 10, 4, 5);
				g.fillRect(clockX + 31, clockY + 10, 4, 8);
				g.fillRect(clockX + 29, clockY + 18, 4, 4);
				g.fillRect(clockX + 27, clockY + 22, 4, 4);
				g.fillRect(clockX + 25, clockY + 26, 4, 4);
				g.fillRect(clockX + 23, clockY + 30, 4, 4);
				g.fillRect(clockX + 21, clockY + 34, 4, 4);
			} else if (hundreds == 8) {
				g.fillRect(clockX + 15, clockY + 10, 20, 4);
				g.fillRect(clockX + 15, clockY + 23, 20, 4);
				g.fillRect(clockX + 15, clockY + 36, 20, 4);
				g.fillRect(clockX + 31, clockY + 10, 4, 30);
				g.fillRect(clockX + 15, clockY + 10, 4, 30);
			} else if (hundreds == 9) {
				g.fillRect(clockX + 15, clockY + 10, 20, 4);
				g.fillRect(clockX + 15, clockY + 23, 20, 4);
				g.fillRect(clockX + 15, clockY + 36, 20, 4);
				g.fillRect(clockX + 31, clockY + 10, 4, 30);
				g.fillRect(clockX + 15, clockY + 10, 4, 17);
			}
			// TENS NUMBER DISPLAY
			if (tens == 0) {
				g.fillRect(clockX + 15 + 30, clockY + 10, 4, 30);
				g.fillRect(clockX + 31 + 30, clockY + 10, 4, 30);
				g.fillRect(clockX + 15 + 30, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 36, 20, 4);
			}
			if (tens == 1) {
				g.fillRect(clockX + 23 + 30, clockY + 10, 4, 30);
				g.fillRect(clockX + 19 + 30, clockY + 36, 12, 4);
				g.fillRect(clockX + 19 + 30, clockY + 10, 8, 4);
			} else if (tens == 2) {
				g.fillRect(clockX + 15 + 30, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 36, 20, 4);
				g.fillRect(clockX + 31 + 30, clockY + 10, 4, 17);
				g.fillRect(clockX + 15 + 30, clockY + 23, 4, 17);
			} else if (tens == 3) {
				g.fillRect(clockX + 15 + 30, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 36, 20, 4);
				g.fillRect(clockX + 31 + 30, clockY + 10, 4, 30);
			} else if (tens == 4) {
				g.fillRect(clockX + 30 + 30, clockY + 10, 4, 30);
				g.fillRect(clockX + 16 + 30, clockY + 10, 4, 14);
				g.fillRect(clockX + 16 + 30, clockY + 20, 20, 4);
			} else if (tens == 5) {
				g.fillRect(clockX + 15 + 30, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 36, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 10, 4, 17);
				g.fillRect(clockX + 31 + 30, clockY + 23, 4, 17);
			} else if (tens == 6) {
				g.fillRect(clockX + 15 + 30, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 36, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 10, 4, 17);
				g.fillRect(clockX + 31 + 30, clockY + 23, 4, 17);
				g.fillRect(clockX + 15 + 30, clockY + 23, 4, 17);
			} else if (tens == 7) {
				g.fillRect(clockX + 15 + 30, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 10, 4, 5);
				g.fillRect(clockX + 31 + 30, clockY + 10, 4, 8);
				g.fillRect(clockX + 29 + 30, clockY + 18, 4, 4);
				g.fillRect(clockX + 27 + 30, clockY + 22, 4, 4);
				g.fillRect(clockX + 25 + 30, clockY + 26, 4, 4);
				g.fillRect(clockX + 23 + 30, clockY + 30, 4, 4);
				g.fillRect(clockX + 21 + 30, clockY + 34, 4, 4);
			} else if (tens == 8) {
				g.fillRect(clockX + 15 + 30, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 36, 20, 4);
				g.fillRect(clockX + 31 + 30, clockY + 10, 4, 30);
				g.fillRect(clockX + 15 + 30, clockY + 10, 4, 30);
			} else if (tens == 9) {
				g.fillRect(clockX + 15 + 30, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 30, clockY + 36, 20, 4);
				g.fillRect(clockX + 31 + 30, clockY + 10, 4, 30);
				g.fillRect(clockX + 15 + 30, clockY + 10, 4, 17);
			}
			// ONES NUMBER DISPLAY
			if (ones == 0) {
				g.fillRect(clockX + 15 + 60, clockY + 10, 4, 30);
				g.fillRect(clockX + 31 + 60, clockY + 10, 4, 30);
				g.fillRect(clockX + 15 + 60, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 36, 20, 4);
			}
			if (ones == 1) {
				g.fillRect(clockX + 23 + 60, clockY + 10, 4, 30);
				g.fillRect(clockX + 19 + 60, clockY + 36, 12, 4);
				g.fillRect(clockX + 19 + 60, clockY + 10, 8, 4);
			} else if (ones == 2) {
				g.fillRect(clockX + 15 + 60, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 36, 20, 4);
				g.fillRect(clockX + 31 + 60, clockY + 10, 4, 17);
				g.fillRect(clockX + 15 + 60, clockY + 23, 4, 17);
			} else if (ones == 3) {
				g.fillRect(clockX + 15 + 60, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 36, 20, 4);
				g.fillRect(clockX + 31 + 60, clockY + 10, 4, 30);
			} else if (ones == 4) {
				g.fillRect(clockX + 30 + 60, clockY + 10, 4, 30);
				g.fillRect(clockX + 16 + 60, clockY + 10, 4, 14);
				g.fillRect(clockX + 16 + 60, clockY + 20, 20, 4);
			} else if (ones == 5) {
				g.fillRect(clockX + 15 + 60, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 36, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 10, 4, 17);
				g.fillRect(clockX + 31 + 60, clockY + 23, 4, 17);
			} else if (ones == 6) {
				g.fillRect(clockX + 15 + 60, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 36, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 10, 4, 17);
				g.fillRect(clockX + 31 + 60, clockY + 23, 4, 17);
				g.fillRect(clockX + 15 + 60, clockY + 23, 4, 17);
			} else if (ones == 7) {
				g.fillRect(clockX + 15 + 60, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 10, 4, 5);
				g.fillRect(clockX + 31 + 60, clockY + 10, 4, 8);
				g.fillRect(clockX + 29 + 60, clockY + 18, 4, 4);
				g.fillRect(clockX + 27 + 60, clockY + 22, 4, 4);
				g.fillRect(clockX + 25 + 60, clockY + 26, 4, 4);
				g.fillRect(clockX + 23 + 60, clockY + 30, 4, 4);
				g.fillRect(clockX + 21 + 60, clockY + 34, 4, 4);
			} else if (ones == 8) {
				g.fillRect(clockX + 15 + 60, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 36, 20, 4);
				g.fillRect(clockX + 31 + 60, clockY + 10, 4, 30);
				g.fillRect(clockX + 15 + 60, clockY + 10, 4, 30);
			} else if (ones == 9) {
				g.fillRect(clockX + 15 + 60, clockY + 10, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 23, 20, 4);
				g.fillRect(clockX + 15 + 60, clockY + 36, 20, 4);
				g.fillRect(clockX + 31 + 60, clockY + 10, 4, 30);
				g.fillRect(clockX + 15 + 60, clockY + 10, 4, 17);
			}
			// checks if the player has won or not
			winCheck();
			//displays victory message in green if player has won
			
			if (victory) {
				int L = (int) (-winDate.getTime() + new Date().getTime()) / 10;
				if (L > 300) {
					L = 300;
				}
				vicX = L - 250;
				vicX2 = vicX + 150;
				g.setColor(Color.GREEN);
				// LETTER "Y"
				for (int m = 0; m < 11; m++) {
					g.fillRect(vicX+m+10, vicY+m+10, 7, 5);
					g.fillRect(vicX+33-m, vicY+m+10, 7, 5);
				}
				g.fillRect(vicX+21, vicY+23, 8, 17);
				// LETTER "O"
				g.fillRect(vicX+50, vicY+10, 30, 5);
				g.fillRect(vicX+50, vicY+35, 30, 5);
				g.fillRect(vicX+50, vicY+10, 5, 30);
				g.fillRect(vicX+75, vicY+10, 5, 30);
				// LETTER"U"
				g.fillRect(vicX+90, vicY+35, 30, 5);
				g.fillRect(vicX+90, vicY+10, 5, 30);
				g.fillRect(vicX+115, vicY+10, 5, 30);
				// LETTER "W"
				for (int m = 0; m < 10; m++) {
					g.fillRect(vicX2+6+m, vicY+10+m*3, 4, 4);
					g.fillRect(vicX2+40-m, vicY+10+m*3, 4, 4);
					if (m < 5) {
						g.fillRect(vicX2+22-m, vicY+22+m*3, 4, 4);
						g.fillRect(vicX2+24+m, vicY+22+m*3, 4, 4);
					}
				}
				// LETTER "I"
				g.fillRect(vicX2+60, vicY+10, 5, 30);
				// LETTER "N"
				for (int m = 0; m < 27; m++) {
					g.fillRect(vicX2+80+m, vicY+10+m, 4, 4);
					g.fillRect(vicX2+80, vicY+10, 5, 30);
					g.fillRect(vicX2+105, vicY+10, 5, 30);
				}
				if(checkEndMessage) {//boolean condition necassery to prevent an infinite loop
					Menu.gameEnded();
					
				}
				// only brings ending panel once				
				checkEndMessage=false;
			}
			
		}
		
	}
	/**
	 * pre: Have to have clicked in the board
	 * post: Returns a boolean based on whether it's in a box or not
	 * @param xC
	 * @param yC
	 * @param cols
	 * @param rows
	 * @return
	 */
	
	public boolean isIn(int xC, int yC) {
		for (int x = 0; x < ROWS; x++) {
			for (int y = 2; y < COLS; y++) {
				if (xC >= x * 50 + 2 && xC < (x + 1) * 50 - 2 && yC >= y * 50 + 2 && yC < (y + 1) * 50 - 2) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * pre: Have to have clicked in a box
	 * post: Returns the x value of the box based on where was clicked on the board
	 * @param xC
	 * @param yC
	 * @return
	 */
	public int getXBox(int xC, int yC) {
		for (int x = 0; x < ROWS; x++) {
			for (int y = 2; y < COLS; y++) {
				if (xC >= x * 50 + 2 && xC < (x + 1) * 50 - 2 && yC >= y * 50 + 2 && yC < (y + 1) * 50 - 2) {
					return x;
				}
			}
		}
		return 0;
	}
	/**
	 * pre: Have to have clicked in a box
	 * post: Returns the y value of the box based on where was clicked on the board
	 * @param xC
	 * @param yC
	 * @return
	 */
	public int getYBox(int xC, int yC) {
		for (int x = 0; x < ROWS; x++) {
			for (int y = 2; y < COLS; y++) {
				if (xC >= x * 50 + 2 && xC < (x + 1) * 50 - 2 && yC >= y * 50 + 2 && yC < (y + 1) * 50 - 2) {
					return y;
				}
			}
		}
		return 0;
	}
	/**
	 * pre: Clicked on a mine
	 * post: Reveals all of the mines and makes the smiley face unhappy
	 */
	public void unhappy() {
		
		for (int x = 0; x < ROWS; x++) {
			for (int y = 2; y < COLS; y++) {
				
				if (tilesArray[x][y].hasBomb()) {
					tilesArray[x][y].setRevealed(true);//reveals each bomb
				}
			}
		}
		happy = false;
		
	}

	/**
	 * pre: Clicked on a space with no mines around it  (blank)
	 * post: Reveals all of the spaces immediately around it
	 * and is called recursively to reveal all of the blank spaces attached to one another
	 * @param a
	 * @param b
	 */
	public void openClear(int a, int b, boolean checkFirst) {
		if(a<0 || a>= ROWS||b<0|| b>=COLS) {
			return;
		}
		if(tilesArray[a][b].isRevealed() && !checkFirst) {			
			return;
		}
		
		if(tilesArray[a][b].hasBomb()) {
			return;
			
		}//doesn't register nearby for b=0!!!!!!!1
		tilesArray[a][b].setRevealed(true);
		if(tilesArray[a][b].getNearby()!=0 ) {
			return;
		}
		checkFirst=false;
		if(b!=2) {
			
			openClear(a-1,b-1,checkFirst);
			openClear(a-1,b,checkFirst);
			openClear(a-1,b+1,checkFirst);
			openClear(a,b-1,checkFirst);
			openClear(a,b+1,checkFirst);
			openClear(a+1,b-1,checkFirst);
			openClear(a+1,b,checkFirst);
			openClear(a+1,b+1,checkFirst);
		}
		
	}
		
		
	/**
	 * pre: Clicked on smiley face
	 * post: Rests the game
	 */
	public void reset() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				//tilesArray[i][j]=new Tile(i,j);
				tilesArray[i][j].setBomb(false);
				tilesArray[i][j].setNearby(0);
				tilesArray[i][j].setRevealed(false);// sets all of the squares to not revealed
				tilesArray[i][j].setFlagged(false);
				if (rand.nextInt(100) <Menu.difficulty && j > 1) {
					tilesArray[i][j].setBomb(true);// generates new bombs
				}
			}
			startDate = new Date(); //resets time
		}

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				for (int m = 0; m < ROWS; m++) {
					for (int n = 0; n < COLS; n++) {
						if (Math.abs(i-m) <= 1 && Math.abs(j-n) <= 1 && (i != m || j != n) && tilesArray[m][n].hasBomb()) {
							tilesArray[i][j].addNearby(1);
							//Adds to the number that is to be drawn on each square- how many mines it has around it
						}
					}
				}
			
			}
		}
		flagger = false;
		happy = true;
		victory = false;
	}
	/*
	 * Click class - handles what happens when mouse is clicked in the board
	 */
	public class Click implements MouseListener {

		@Override
		/**
		 * pre: mouse is clicked
		 * post: adjusts for the new variables and events, then repaints
		 */
		public void mouseClicked(MouseEvent e) {
			//saves the x and y coordinates of where has bee clicked
			xCoord = e.getX();
			yCoord = e.getY();
			if (isIn(xCoord, yCoord) == true) {// if it's in a box
				int boxXCor = getXBox(xCoord, yCoord);
				int boxYCor =getYBox(xCoord, yCoord);
			
				//System.out.println(boxXCor +","+boxYCor);
				if (flagger == false) {// if the flag icon hasn't been clicked
					if (!tilesArray[boxXCor][boxYCor].isFlagged()) {
						tilesArray[boxXCor][boxYCor].setRevealed(true);; // reveals the box
						tilesArray[boxXCor][boxYCor].setFlagged(false);;
						if (tilesArray[boxXCor][boxYCor].hasBomb()) { //has a bomb
							happy = false;
							unhappy();
						} else if (tilesArray[boxXCor][boxYCor].getNearby() == 0) {
							
							openClear(boxXCor,boxYCor,true);
						}
					}
				} else {
					//if you've selected a flag
					if (!tilesArray[boxXCor][boxYCor].isRevealed()) {
						if(!tilesArray[boxXCor][boxYCor].isFlagged()) {
							tilesArray[boxXCor][boxYCor].setFlagged(true);// puts a flag on the selected square
						} else {
							tilesArray[boxXCor][boxYCor].setFlagged(false);;// removes flag if it exists
						}
					}
				}
			}
			if (xCoord >= flaggerX && xCoord < flaggerX + 50 && yCoord >= flaggerY && yCoord < flaggerY + 50) {
				// if the flag icon is clicked
				if (flagger == false) {
					flagger = true; //flagger is true, so when you click now it will place a flag
				} else {
					flagger = false;//turns flag placer off
				}
			}
			if (xCoord >= smileyX && xCoord < smileyX + 70 && yCoord >= smileyY && yCoord < smileyY + 70) {
				reset(); // if you click the smiley sign it resest
			}
		
			repaint();// repaints when clicked
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		/**
		 * pre: mouse is released (clicked)
		 * post: gets coordinates
		 */
		public void mouseReleased(MouseEvent e) {
			xCoord = e.getX();
			yCoord = e.getY();
			repaint();
		}
		
	}
	/*
	 * Handles actions when the mouse is moved
	 */
	public class Move implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// gets the coordinates and 
			xCoord = e.getX();
			yCoord = e.getY();
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			xCoord = e.getX();
			yCoord = e.getY();
			repaint();
		}
		
	}
	/**
	 * pre: Repaints when the method is called in the main class
	 * post: calls paintcomponent method automatically
	 */
	public void doIt() {
		repaint();
	}
	/**
	 * pre: painting has been done, checks to see if the player has won
	 * post: if the player has won it sets victory to true
	 * which will cause the victory message display and congratulating panel
	 */
	public void winCheck() {
	
		int mines = 0;
		int reveals = 0;
		for (int a = 0; a < ROWS; a++) {
			for (int b = 2; b < COLS; b++) {
				if (tilesArray[a][b].hasBomb()) {
					mines++;
				}
				if (tilesArray[a][b].isRevealed()) {
					reveals++;
				}
			}
		}
		if (160 - mines - reveals == 0 && happy == true && victory == false) {
			// all of the squares without bombs have been revealed
			winDate = new Date();
			victory = true;
		}
//		if(happy == false) {

	}
	

	/**
 	* pre: none
 	* post: Returns the time
 	* specifically used to display the message at the ending
 	* @return
 	*/
	public static int getTime(){
			return time;
		}

}






