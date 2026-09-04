package tetris.main;

import java.io.IOException;
import tetris.game.Game;

public class App {
	public static void main(String[] args) {
		try {
			Game g = new Game();
			g.initGame();
		} catch (IOException e) {
			System.err.println("An unexpected error occurred."); 
			e.printStackTrace();
		}
	}
}
