package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import main.Game;

public class RandomTargeting extends TargetingAlgorithm {
	private Random rand;
	private ArrayList<Point> available;

	public RandomTargeting(Game game) {
		this.game = game;
		rand = new Random();
		available = game.getAvailablePoints();
	}

	//constructor with option to set seed of random object
	public RandomTargeting(Game game, long seed) {
		this(game);
		rand.setSeed(seed);
	}

	@Override
	public void nextMove() {
		if (!game.checkVictory()) {
			int randIndex = rand.nextInt(available.size());
			Point guess = available.get(randIndex);
			available.remove(randIndex);
			game.guess(guess);
		}
	}

	@Override
	public void run() {
		while (!game.checkVictory()) {
			nextMove();
		}
	}
}

