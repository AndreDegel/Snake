package com.Andre;


import java.awt.*;
import java.awt.Image;
import java.util.LinkedList;

import javax.swing.*;

/** This class responsible for displaying the graphics, so the snake, grid, kibble, instruction text and high score
 * 
 * @author Clara
 *
 */
public class DrawSnakeGamePanel extends JPanel {
	
	private static int gameStage = SnakeGame.BEFORE_GAME;  //use this to figure out what to paint
	
	private Snake snake;
	private Kibble kibble;
	private Score score;

	//Initalize images for game graphics
	private Image apple;
	private Image header;
	private Image dot;
	private Image win;
	private Image start;
	private Image gameOver;
	
	DrawSnakeGamePanel(Snake s, Kibble k, Score sc){
		this.snake = s;
		this.kibble = k;
		this.score = sc;
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(SnakeGame.xPixelMaxDimension, SnakeGame.yPixelMaxDimension);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        /* Where are we at in the game? 4 phases.. 
         * 1. Before game starts
         * 2. During game
         * 3. Game lost aka game over
         * 4. or, game won
         */

        gameStage = SnakeGame.getGameStage();
        
        switch (gameStage) {
        case 1: {
        	displayInstructions(g);
        	break;
        } 
        case 2 : {
			//hide the menue during the game
			SnakeGame.menu.setVisible(false);
        	displayGame(g);
        	break;
        }
        case 3: {
			//make the menue visible during game over
			SnakeGame.menu.setVisible(true);
        	displayGameOver(g);
        	break;
        }
        case 4: {
        	displayGameWon(g);
        	break;
        }
        }
        
        
        
    }

	private void displayGameWon(Graphics g) {
		//Draw the winning image
		win = getImage("win.png");
		g.drawImage(win, 100,100,350,350, this);
		
	}
	private void displayGameOver(Graphics g) {
		// Have the game over screen display an image as well
		gameOver = getImage("gameOver.jpg");
		g.drawImage(gameOver, 0, 0, SnakeGame.xPixelMaxDimension, SnakeGame.yPixelMaxDimension, this);
		
		String textScore = score.getStringScore();
		String textHighScore = score.getStringHighScore();
		String newHighScore = score.newHighScore();
		g.setColor(Color.RED);
		g.drawString("SCORE = " + textScore, 150, 400);
		
		g.drawString("HIGH SCORE = " + textHighScore, 150, 420);
		g.drawString(newHighScore, 150, 450);

	}

	private void displayGame(Graphics g) {
		displaySnake(g);
		displayKibble(g);
	}

	private void displayKibble(Graphics g) {


		//Get the changing kibble coordinates
		int x = kibble.getKibbleX() * SnakeGame.squareSize;
		int y = kibble.getKibbleY() * SnakeGame.squareSize;

		//Draw the kibble as an apple image
		//food image is smaller than snake.
		apple = getImage("apple.png");
		g.drawImage(apple, x+1, y+1,SnakeGame.squareSize-20, SnakeGame.squareSize-20, this);
	}

	private void displaySnake(Graphics g) {

		LinkedList<Point> coordinates = snake.segmentsToDraw();
		Point head = coordinates.pop();
		//Draw the Snake head as a blue dot image
		header = getImage("head.png");
		g.drawImage(header,(int)head.getX(), (int)head.getY(),SnakeGame.squareSize, SnakeGame.squareSize, this);

		//Draw rest of snake as green dot images
		for (Point p : coordinates) {
			dot = getImage("dot.png");
			g.drawImage(dot,(int)p.getX(), (int)p.getY(),SnakeGame.squareSize, SnakeGame.squareSize, this);

		}

	}

	private void displayInstructions(Graphics g) {
		//Have a nicer start screen with an image
		start = getImage("startSnake.png");
		g.drawImage(start, 0, 0, SnakeGame.xPixelMaxDimension, SnakeGame.yPixelMaxDimension, this);
    	}

	//method to get images from the project to use in game
	private Image getImage(String imagePNG) {
		Image image;
		ImageIcon iia = new ImageIcon(this.getClass().getResource(imagePNG));
		image = iia.getImage();
		return image;
	}
    
}

