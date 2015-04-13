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

	private Image apple;
	private Image header;
	private Image dot;
	private Image win;
	
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
        	displayGame(g);
        	break;
        }
        case 3: {
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
		// TODO Replace this with something really special!
		g.clearRect(100,100,350,350);
		//g.drawString("YOU WON SNAKE!!!", 150, 150);

		//Draw the winning image
		win = getImage("win.png");
		g.drawImage(win, 100,100,350,350, this);
		
	}
	private void displayGameOver(Graphics g) {

		g.clearRect(100,100,350,350);
		g.drawString("GAME OVER", 150, 150);
		
		String textScore = score.getStringScore();
		String textHighScore = score.getStringHighScore();
		String newHighScore = score.newHighScore();
		
		g.drawString("SCORE = " + textScore, 150, 250);
		
		g.drawString("HIGH SCORE = " + textHighScore, 150, 300);
		g.drawString(newHighScore, 150, 400);
		
		g.drawString("press a key to play again", 150, 350);
		g.drawString("Press q to quit the game",150,400);		
    			
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
		apple = getImage("apple.png");
		g.drawImage(apple, x+1, y+1,SnakeGame.squareSize, SnakeGame.squareSize, this);
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
        g.drawString("Press any key to begin!",100,200);		
        g.drawString("Press q to quit the game",100,300);		
    	}

	//method to get images from the project to use in game
	private Image getImage(String imagePNG) {
		Image image;
		ImageIcon iia = new ImageIcon(this.getClass().getResource(imagePNG));
		image = iia.getImage();
		return image;
	}
    
}

