package algorithms;

import main.Game;

public interface TargetingAlgorithm {
	public void nextMove();
	public void run();
	public void setGame(Game game);
	public Game getGame();
}

