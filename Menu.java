/*
 * ICS4U final project - minesweeper game
 * Aria Paydari Alamdari and Behrad Haghighi
 * January 2020
 * Mr. Benum's class
 * Menu.java 
 * 
 */
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
//Main class - generally controls the actions
	public class Menu implements ActionListener {
		private static JLabel enterName;
		private static JLabel rules;
		private static JTextField name;
		private static JButton easy;
		private static JButton normal;
		private static JButton hard;
		private static JButton insane;
		private static JLabel highScoreLabel;
		private JFrame intro;
		private static JFrame frame;
		private static String playerName;
		public static int difficulty;//difficulty set when player selects a button
		public static File highScore;
		public static int compareScore=1000;
		static String topPlayer="";
		static Scanner sc=null;
		
		public Menu() {
			intro=new JFrame();
			intro.setLayout(null);
  
			//ask how to get name
			//close window
			easy=new JButton("easy");
			normal=new JButton("Normal");
			hard=new JButton("Hard");
			insane=new JButton("Insane");
			name=new JTextField();
			rules=new JLabel("<html>  Welcome to the minesweeper game,<br><br>  The game contains a window that is filled with small squares containing either a mine or a number underneath it.<br>  The difficulty of the game ranges from easy to hard.<br><br>  The rules : <br><br>  1. The first step is the hardest. Clicking on a random place(s) and hoping you’re not blown to bits.<br><br>  2. If you click on a non-bomb area, the square will either open up to be blank, or will contain a number from 1 to 8.<br><br>  3. These numbers specify the number of bombs that are adjacent to that block. 1 means there is only 1 bomb adjacent to it, while 8 would mean all blocks adjacent to it are bombs.<br><br>  4. Next, you need to do a bit of calculations. You need to find out which block will contain the bomb(s). These calculations are to be performed based on multiple blocks that are either clear or contain other numbers.<br><br>  5. Most Minesweeper games have the functionality to mark where bombs are with a flag, so you can remember easily.<br><br>  choose your difficulty and play on.<br><br>PRESS THE FLAG ICON TO MARK THE FLAGS AND WHEN YOU LOSE PRESS ON THE SAD FACE TO PLAY AGAIN</html>");
			enterName = new JLabel("Enter name here :");
			rules.setBounds(0, 50, 500, 460);
			enterName.setBounds(0, 0, 130, 25);
			//add buttons and labels
			name.setBounds(115, 0, 330, 30);
			easy.setBounds(0, 530, 100, 100);
			normal.setBounds(125, 530, 100, 100);
			hard.setBounds(250, 530, 100, 100);
			insane.setBounds(375, 530, 100, 100);
			easy.addActionListener(this);
			normal.addActionListener(this);
			hard.addActionListener(this);
			insane.addActionListener(this);
			intro.add(rules);
			intro.add(easy);
			intro.add(normal);
			intro.add(hard);
			intro.add(insane);
			intro.add(name);
			highScore=new File("HighScore.txt");
			//checks for the file with the high scores
			if(highScore.exists()){
				try {
					sc=new Scanner(highScore);
					//attaches scanner to the highScore file to check the lines
				} catch (FileNotFoundException e) {
					System.out.println("Doesn't exist.");
				}
				/*this loop checks every two lines in the highScore file
				 * The first line is the player name
				 * second line is the score
				 */
				while(sc.hasNextLine()){
 	
					String player=sc.next();
					int score=sc.nextInt();
					/*if the score is bigger than the previous
 			It will be the high score
 			This is repeated until the true high score is stored
					 */
					if(score<compareScore) {
						compareScore=score;
						topPlayer=player;
					}
					//Places cursor at the beginning of the next line if it exists
					if(sc.hasNextLine())
						sc.nextLine();
				}
  
			}
			else{
				//happens in case there is no text file
				System.out.println("error");
			}
			final int DIMENSION_ONE = 580;
			final int DIMENSION_TWO = 750;
			intro.add(enterName);
			intro.setPreferredSize(new Dimension(DIMENSION_ONE,DIMENSION_TWO));
			intro.pack();
			intro.setVisible(true);
			intro.setLocationRelativeTo(null);
  
			}	
		/**
		 * main method
		 * @param args
		 */
		public static void main(String[] args) {
			new Menu();
		}
		/**
		 * executed when game is over
		 * removes game frame and creates the JFrame
		 * This shows the user's score
		 * score and name also saved to highScore
		 * pre:The previous game has ended (executed in stopMoving() in the GameInterface class)
		 * post:new (final) panel is created, the user's name and score are recorded in the text file.
		 */
		public static void gameEnded(){
			
			int time = GameClass.getTime();
			
			JFrame endFrame  = new JFrame();
			if(time<compareScore || compareScore==0) {
				JLabel highScoreMessage=new JLabel("<html><h1>Congratulations "+playerName+"! you set out a new record of "+"<br>"+time +"!</html>");
				highScoreMessage.setBounds(150, 0, 250,250);
				endFrame.add(highScoreMessage);
			}
			else {
				highScoreLabel= new JLabel("<html><h2><br><br>High score: "+ compareScore + " by " +topPlayer +"</html>");
				highScoreLabel.setBounds(150, 100, 200, 200);
				endFrame.add(highScoreLabel);
				JLabel finalScore=new JLabel("<html><h1>Game Over!<br>Player: "+playerName+"<br>Score: "+time +"</html>");
				finalScore.setBounds(150, 0, 250,250);
				endFrame.add(finalScore);
			}
			endFrame.setLayout(null);
			endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			endFrame.setPreferredSize(new Dimension(500,400));
			endFrame.pack();
			endFrame.setVisible(true);
			endFrame.setLocationRelativeTo(null);
			//Writes name and score on the text files
			Writer writer=null;
			try {
				writer=new FileWriter("HighScore.txt",true);
	
			}
			catch(IOException ex) {
				System.out.println("Can't write the desired.");
			}
 
			PrintWriter output=new PrintWriter(writer);

			output.println("\n"+playerName);
			System.out.println("Kir");
			output.print(time);
			output.close();
		}
		/**
		 * executes when one of the buttons of the intro page is pressed
		 * Opens a frame with the gamepanel JPanel
		 * This contains the actual game mechanics
		 * pre:none (button pressed)
		 * post:game frame with snake game is created
		 */
		public void gamePlayed() {
  
			playerName=name.getText();
			this.intro.dispose();
			frame  = new JFrame();
			new Main();
		}
		@Override
		/**
		 * actionlistener which executes when button is pressed
		 * Based on which button is pressed the difficulty level is chosen
		 * pre: A button is pressed
		 * post:difficulty is obtained based on the one chosen, executes gamePlayed to create the actual snake game frame
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==easy)
				difficulty=7;
			else if(e.getSource()==normal) 
				difficulty=15;
			else if (e.getSource()==hard)
				difficulty=20;
			else if (e.getSource()==insane)
				difficulty=25;
			gamePlayed();
  
		}
		
	}