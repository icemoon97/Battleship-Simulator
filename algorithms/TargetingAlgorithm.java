package algorithms;

import main.Game;

public abstract class TargetingAlgorithm {
	protected Game game;
	
	public abstract void nextMove();
	public abstract void run();
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
}
