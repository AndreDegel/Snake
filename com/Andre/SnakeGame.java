package com.Andre;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import javax.swing.*;


public class SnakeGame {

	public final static int xPixelMaxDimension = 501;  //Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
	public final static int yPixelMaxDimension = 501;

	public static int xSquares ;
	public static int ySquares ;

	public static int squareSize = 70;	//sqaresize to snake fields: 30=16x16 40=12x12, 50=10x10, 60=8x8, 70=7x7 80=6x6

	public static void setSquareSize(int squareSize) {
		SnakeGame.squareSize = squareSize;
	}

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

	public static void setClockInterval(long clockInterval) {
		SnakeGame.clockInterval = clockInterval;
	}


	static JFrame snakeFrame;

	static DrawSnakeGamePanel snakePanel;
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html
	static JTextArea scoreViewer;
	static JMenuBar menu = setJMenuBar();

	private static void createAndShowGUI() {
		//Create and set up the window.
		snakeFrame = new JFrame("Snake");
		snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		snakeFrame.setJMenuBar(menu);



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

	public static JMenuBar setJMenuBar() {

		JMenuBar menue = new JMenuBar();

		JMenu game = new JMenu("Game");
		final JMenuItem newgame = new JMenuItem("New Game");
		JMenuItem exit = new JMenuItem("Exit");
		newgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (SnakeGame.getGameStage() == SnakeGame.BEFORE_GAME) {
					//Start the game
					setGameStage(SnakeGame.DURING_GAME);
					newGame();
					snakePanel.repaint();
				}
				else {
					SnakeGame.setGameStage(SnakeGame.DURING_GAME);
					newGame();
					snake.reset();
					Score.resetScore();
					snakePanel.repaint();
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		game.add(newgame);
		game.addSeparator();
		game.add(exit);
		menue.add(game);

		JMenu type = new JMenu("Warp Walls");
		JRadioButtonMenuItem rbMenuItem;
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("On");

		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				snake.setHasWarp(true);
			}
		});
		group.add(rbMenuItem);
		type.add(rbMenuItem);


		rbMenuItem = new JRadioButtonMenuItem("Off");
		//set no warp walls as default
		rbMenuItem.setSelected(true);
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				snake.setHasWarp(false);
			}
		});
		group.add(rbMenuItem);
		type.add(rbMenuItem);

		menue.add(type);


		JMenu level = new JMenu("Size");
		ButtonGroup group2 = new ButtonGroup();
//TODO: Not correctly working yet, need way to update size correctly
		rbMenuItem = new JRadioButtonMenuItem("7x7");
		//have the smallest as default
		rbMenuItem.setSelected(true);
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSquareSize(70);
			}
		});
		group2.add(rbMenuItem);
		level.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("10x10");
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSquareSize(50);
			}
		});
		group2.add(rbMenuItem);
		level.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("16x16");
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSquareSize(30);
				snake.reset();
			}
		});
		group2.add(rbMenuItem);
		level.add(rbMenuItem);

		menue.add(level);


		JMenu speed = new JMenu("Speed");
		ButtonGroup group3 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Slow");
		//have the slowest as default
		rbMenuItem.setSelected(true);
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setClockInterval(500);

			}
		});
		group3.add(rbMenuItem);
		speed.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Medium");
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setClockInterval(300);
			}
		});
		group3.add(rbMenuItem);
		speed.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Fast");

		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setClockInterval(150);
			}
		});
		group3.add(rbMenuItem);
		speed.add(rbMenuItem);
		menue.add(speed);

		JMenu help = new JMenu("Help");
		JMenuItem creator = new JMenuItem("Creator");
		JMenuItem instruction = new JMenuItem("Instruction");
		creator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Author: Andre Degel\nVersion: 2.0 \n Initial Version: https://github.com/minneapolis-edu/Snake");
			}
		});
		instruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Welcome to Snake\nTo control the snake use the arrow keys(Up, Down, Left, Right) accordingly\n" +
						"As the snake finds food, it eats the food, and thereby grows larger.\n" +
						"The game ends when the snake either moves off the screen or moves into itself.\nThe goal is to make the snake as large as possible before that happens.\n" +
						"The settings can be changed in the menu bar above before the game or after Game Over,\nto do so just click on the option you want and start a new Game");
			}
		});

		help.add(creator);
		help.add(instruction);

		menue.add(help);

		return menue;

	}

}
