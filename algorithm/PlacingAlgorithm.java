package algorithms;

import main.Game;

public interface PlacingAlgorithm {
	public void run();
	public void setGame(Game game);
	public Game getGame();
}
