package algorithms;

import main.Game;

public abstract class PlacingAlgorithm {
	protected Game game;
	
	public abstract void run();

	public void setGame(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
}
