package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import main.Game;

public class CheckerboardHuntAndSink extends TargetingAlgorithm {
	private ArrayList<Point> queue;
	private Random rand;

	public CheckerboardHuntAndSink(Game game) {
		this.game = game;
		queue = new ArrayList<Point>();
		rand = new Random();
	}

	//constructor with option to set seed of random object
	public CheckerboardHuntAndSink(Game game, long seed) {
		this(game);
		rand.setSeed(seed);
	}

	@Override
	public void nextMove() {
		if (!game.checkVictory()) {
			queue.retainAll(game.getAvailablePoints()); //removes any points in the queue that have already been guessed
			Point guess = new Point(-1, -1);
			if (queue.size() > 0) {
				guess = queue.get(0);
				queue.remove(0);
			} else { //if there is nothing in the queue, a random point on checkerboard is picked
				ArrayList<Point> available = game.getAvailablePoints();
				for (int i = available.size() - 1; i >= 0; i--) { 
					if (available.get(i).x % 2 != available.get(i).y % 2) { //removes any points not on checkerboard
						available.remove(i);
					}
				}
				int randIndex = rand.nextInt(available.size());
				guess = available.get(randIndex);
			}
			if (game.isHit(guess)) {
				queue.add(new Point(guess.x + 1, guess.y));
				queue.add(new Point(guess.x - 1, guess.y));
				queue.add(new Point(guess.x, guess.y + 1));
				queue.add(new Point(guess.x, guess.y - 1));
			}
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
