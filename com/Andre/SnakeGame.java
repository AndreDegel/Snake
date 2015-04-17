package com.Andre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import javax.swing.*;


public class SnakeGame {

	public final static int xPixelMaxDimension = 501;  //Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
	public final static int yPixelMaxDimension = 501;

	private static int xSquares ;		//FINDBUGS
	private static int ySquares ;		//FINDBUGS

	//Getters and setters to change square location and size, which are needed if we want to change the
	//game fields size without changing the windows size.
	public static int getxSquares() {
		return xSquares;
	}

	public static void setxSquares() {
		SnakeGame.xSquares = xPixelMaxDimension / squareSize;
	}

	public static int getySquares() {
		return ySquares;
	}

	public static void setySquares() {
		SnakeGame.ySquares = yPixelMaxDimension / squareSize;
	}

	public static int getSquareSize() {
		return squareSize;
	}

	private static int squareSize = 80;	//FINDBUGS

	//Setter to change the size of the snake and the game field
	public static void setSquareSize(int squareSize) {
		SnakeGame.squareSize = squareSize;
	}

	private static Snake snake ;	//FINDBUGS

	private static Kibble kibble;	//FINDBUGS

	private static Score score;		//FINDBUGS


	static final int BEFORE_GAME = 1;
	static final int DURING_GAME = 2;
	static final int GAME_OVER = 3;
	static final int GAME_WON = 4;   //The values are not important. The important thing is to use the constants 
	//instead of the values so you are clear what you are setting. Easy to forget what number is Game over vs. game won
	//Using constant names instead makes it easier to keep it straight. Refer to these variables 
	//using statements such as SnakeGame.GAME_OVER 

	private static int gameStage = BEFORE_GAME;  //use this to figure out what should be happening. 
	//Other classes like Snake and DrawSnakeGamePanel will need to query this, and change it's value

	private static long clockInterval = 500; //controls game speed	//FINDBUGS
	//Every time the clock ticks, the snake moves
	//This is the time between clock ticks, in milliseconds
	//1000 milliseconds = 1  second.

	//setter to change the game's speed
	public static void setClockInterval(long clockInterval) {
		SnakeGame.clockInterval = clockInterval;
	}


	static JFrame snakeFrame;

	static DrawSnakeGamePanel snakePanel;
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html

	//Instantiate a new menu bar using the set method
	static JMenuBar menu = setJMenuBar();



	private static void createAndShowGUI() {
		//Create and set up the window.
		snakeFrame = new JFrame();
		snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		snakeFrame.setJMenuBar(menu);


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
		GameClock clockTick = new GameClock(snake, kibble, snakePanel);
		timer.scheduleAtFixedRate(clockTick, 0, clockInterval);

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


	public static void setGameStage(int gameStage) {
		SnakeGame.gameStage = gameStage;
	}

	//Method to create the menu for the game
	public static JMenuBar setJMenuBar() {

		JMenuBar menue = new JMenuBar();
		//Create the first option field with the items to start a new game or exit the game
		JMenu game = new JMenu("Game");
		final JMenuItem newgame = new JMenuItem("New Game");
		JMenuItem exit = new JMenuItem("Exit");
		//If the new game item is pressed, start a new game
		newgame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//Start the game
					setGameStage(SnakeGame.DURING_GAME);
					//start a new timer
					newGame();
					//we have to reset the snake for the initial(1st) game because
					//the player might have changed the field size.
					snake.reset();
					//If it is not the initial(1st) game reset the score
					if (SnakeGame.getGameStage() == SnakeGame.DURING_GAME) {
						Score.resetScore();
					}
					snakePanel.repaint();

			}
		});
		//If exit is pressed, exit the game
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().exit(0); 	//FINDBUGS
			}
		});
		//Add everything to the first option and then to the menu
		game.add(newgame);
		//put a seperator between the two options for better look
		game.addSeparator();
		game.add(exit);
		menue.add(game);

		//Initialiye the options for the warp walls
		JMenu type = new JMenu("Warp Walls");
		//Make them radio buttons and group them together so that just one can be selected
		JRadioButtonMenuItem rbMenuItem;
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("On");
		//if the radio button is selected turn the warp walls on bz setting the boolean from the snake game class to true
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				snake.setHasWarp(true);
			}
		});
		//add item to group
		group.add(rbMenuItem);
		//add item to option
		type.add(rbMenuItem);

		//same as on button
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

		//add the option to the menu bar
		menue.add(type);

		//Initialiye a new option for the menu to change the field siye of the game
		JMenu level = new JMenu("Size");
		//Here again using grouped radio buttons to just select one at a time
		ButtonGroup group2 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("6x6");
		//have the smallest as default
		rbMenuItem.setSelected(true);
		//add an action listener to change the square siye and call the update method if this item is selected
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSquareSize(80);
				updateField();
			}
		});
		//add item to group and to option
		group2.add(rbMenuItem);
		level.add(rbMenuItem);

		//Same as befor just decrease the sqaresize further
		rbMenuItem = new JRadioButtonMenuItem("10x10");
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSquareSize(50);
				updateField();
			}
		});
		group2.add(rbMenuItem);
		level.add(rbMenuItem);

		//Same as befor just decrease the sqaresize further
		rbMenuItem = new JRadioButtonMenuItem("16x16");
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSquareSize(30);
				updateField();
			}
		});
		group2.add(rbMenuItem);
		level.add(rbMenuItem);
		//add the size option to the menu
		menue.add(level);

		//Initialize the option to change the speed of the game
		JMenu speed = new JMenu("Speed");
		//here again grouped radio buttons
		ButtonGroup group3 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Slow");
		//have the slowest as default
		rbMenuItem.setSelected(true);
		//If the item is selected set the clock interval that controls the speed
		//to a half second tick (every half second refresh)
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setClockInterval(500);

			}
		});
		group3.add(rbMenuItem);
		speed.add(rbMenuItem);


		rbMenuItem = new JRadioButtonMenuItem("Medium");
		//change to about a third of a second
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setClockInterval(300);
			}
		});
		group3.add(rbMenuItem);
		speed.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Fast");
		//change to about a tenth of a second
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setClockInterval(150);
			}
		});
		group3.add(rbMenuItem);
		speed.add(rbMenuItem);
		menue.add(speed);

		//Create a help option to show information about the game
		//as well as the instructions
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

	//Method to update the game field.
	public static void updateField() {
		//Since we can change the field size as much as we want, we have to update everytime
		//the snakefield, also we have to re-move the kibbel to avoid it getting lost
		setxSquares();
		setySquares();
		snake.setSquareSize(getSquareSize());
		snake.setMaxX(getxSquares());
		snake.setMaxY(getySquares());
		snake.snakeSquares = new int[getxSquares()][getySquares()];		//FINDBUGS
		kibble.moveKibble(snake);
	}

}
