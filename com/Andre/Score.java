package com.Andre;

/** Keeps track of, and display the user's score
 * 
 */


public class Score {

	private static int score = 0;			//FINDBUGS
	private int highScore = 0;	//FINDBUGS
	private static int increment = 1;		//FINDBUGS	//how many points for eating a kibble
	
	public Score(){
		//Possible TODO get more points for eating kibbles, the longer the snake gets?
	}
	
	public static void resetScore() {
		score = 0;	
	}
	
	public static void increaseScore() {
		
		score = score + increment;
		
	}


	//These methods are useful for displaying score at the end of the game
	
	public String getStringScore() {
		return Integer.toString(score);
	}

	public String newHighScore() {
		
		if (score > highScore) {
			highScore = score;
			return "New High Score!!";
		} else {
			return "";
	}
	}

	public String getStringHighScore() {
		return Integer.toString(highScore);
	}
	
}

