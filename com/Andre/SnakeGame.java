package com.Andre;

import java.awt.*;
import java.util.Timer;

import javax.swing.*;


public class SnakeGame {

	public final static int xPixelMaxDimension = 501;  //Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
	public final static int yPixelMaxDimension = 501;

	public static int xSquares ;
	public static int ySquares ;

	public final static int squareSize = 80;

	protected static Snake snake ;

	protected static Kibble kibble;

	protected static Score score;

	static final int BEFORE_GAME = 1;
	static final int DURING_GAME = 2;
	static final int GAME_OVER = 3;
	static final int GAME_WON = 4;   //The values are not important. The important thing is to use the constants 
	//instead of the values so you are clear what you are setting. Easy to forget what number is Game over vs. game won
	//Using constant names instead makes it easier to keep it straight. Refer to these variables 
	//using statements such as SnakeGame.GAME_OVER 

	private static int gameStage = BEFORE_GAME;  //use this to figure out what should be happening. 
	//Other classes like Snake and DrawSnakeGamePanel will need to query this, and change it's value

	protected static long clockInterval = 500; //controls game speed
	//Every time the clock ticks, the snake moves
	//This is the time between clock ticks, in milliseconds
	//1000 milliseconds = 1  second.

	static JFrame snakeFrame;

	static DrawSnakeGamePanel snakePanel;
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html
	static JTextArea scoreViewer;
	static JMenuBar menue = setJMenueBar();

	private static void createAndShowGUI() {
		//Create and set up the window.
		snakeFrame = new JFrame("Snake");
		snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//snakeFrame.setJMenuBar(menue);
		/*scoreViewer = new JTextArea("Score ==>" + score);
		scoreViewer.setEnabled(false);
		scoreViewer.setBackground(Color.BLACK);
		scoreViewer.setLayout(new GridLayout(0, 1));
		snakeFrame.add(scoreViewer);*/

		snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
		snakeFrame.setUndecorated(true); //hide title bar
		snakeFrame.setVisible(true);
		snakeFrame.setResizable(false);

		snakePanel = new DrawSnakeGamePanel(snake, kibble, score);
		snakePanel.setFocusable(true);
		snakePanel.requestFocusInWindow(); //required to give this component the focus so it can generate KeyEvents

		snakeFrame.add(snakePanel);
		snakePanel.addKeyListener(new GameControls(snake));

		setGameStage(BEFORE_GAME);

		snakeFrame.setVisible(true);
	}

	private static void initializeGame() {
		//set up score, snake and first kibble
		xSquares = xPixelMaxDimension / squareSize;
		ySquares = yPixelMaxDimension / squareSize;

		snake = new Snake(xSquares, ySquares, squareSize);
		kibble = new Kibble(snake);
		score = new Score();

		gameStage = BEFORE_GAME;
	}

	protected static void newGame() {
		Timer timer = new Timer();
		GameClock clockTick = new GameClock(snake, kibble, score, snakePanel);
		timer.scheduleAtFixedRate(clockTick, 0 , clockInterval);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initializeGame();
				createAndShowGUI();
			}
		});
	}



	public static int getGameStage() {
		return gameStage;
	}

	public static boolean gameEnded() {
		if (gameStage == GAME_OVER || gameStage == GAME_WON){
			return true;
		}
		return false;
	}

	public static void setGameStage(int gameStage) {
		SnakeGame.gameStage = gameStage;
	}

	public static JMenuBar setJMenueBar() {

		JMenuBar menue = new JMenuBar();

		JMenu game = new JMenu("Game");
		JMenuItem newgame = new JMenuItem("New Game");
		JMenuItem exit = new JMenuItem("Exit");
		/*newgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startNewGame();
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});*/
		game.add(newgame);
		game.addSeparator();
		game.add(exit);
		menue.add(game);

		JMenu type = new JMenu("Type");
		JMenuItem noMaze = new JMenuItem("No Maze");
		/*noMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedGameType = GAME_TYPE.NO_MAZE;
				startNewGame();
			}
		});*/
		JMenuItem border = new JMenuItem("Border Maze");
		/*border.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedGameType = GAME_TYPE.BORDER;
				startNewGame();
			}
		});*/
		type.add(noMaze);
		type.add(border);

		menue.add(type);

		JMenu level = new JMenu("Level");
		JMenuItem level1 = new JMenuItem("Level 1");
		/*level1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedSpeed = SNAKE_RUNNING_SPEED_FAST;
				startNewGame();
			}
		});
		JMenuItem level2 = new JMenuItem("Level 2");
		level2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedSpeed = SNAKE_RUNNING_SPEED_FASTER;
				startNewGame();
			}
		});
		JMenuItem level3 = new JMenuItem("Level 3");
		level3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedSpeed = SNAKE_RUNNING_SPEED_FASTEST;
				startNewGame();
			}
		});*/
		level.add(level1);
		//level.add(level2);
		//level.add(level3);

		menue.add(level);

		JMenu help = new JMenu("Help");
		JMenuItem creator = new JMenuItem("Creator");
		JMenuItem instruction = new JMenuItem("Instraction");
		/*creator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Author: Abdullah al hasb\nVersion: 1.0.1 \n Blog: http://imhasib.wordpress.com/");
			}
		});*/

		help.add(creator);
		help.add(instruction);

		menue.add(help);

		return menue;

	}

}
