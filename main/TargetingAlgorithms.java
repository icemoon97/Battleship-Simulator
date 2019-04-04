package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class TargetingAlgorithms {
	//algorithm that guesses guesses adjacent squares after getting a hit, is otherwise random
	public static void basicHuntAndSink(Game game, Random rand) {
		ArrayList<Point> queue = new ArrayList<Point>();
		while (!game.checkVictory()) {
			queue.retainAll(game.getAvailablePoints()); //removes any points in the queue that have already been guessed
			Point guess = new Point(-1, -1);
			if (queue.size() > 0) {
				guess = queue.get(0);
				queue.remove(0);
			} else { //if there is nothing in the queue, a random point is picked
				ArrayList<Point> available = game.getAvailablePoints();
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

	public static void checkerboardHuntAndSink(Game game, Random rand) {
		ArrayList<Point> queue = new ArrayList<Point>();
		while (!game.checkVictory()) {
			queue.retainAll(game.getAvailablePoints()); //removes any points in the queue that have already been guessed
			Point guess = new Point(-1, -1);
			if (queue.size() > 0) {
				guess = queue.get(0);
				queue.remove(0);
			} else { //if there is nothing in the queue, a random point is picked
				ArrayList<Point> available = game.getAvailablePoints();
				for (int i = available.size() - 1; i >= 0; i--) {
					if (available.get(i).x % 2 != available.get(i).y % 2) {
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

	//algorithm that guesses completely randomly (is really bad)
	public static void randomAlgorithm(Game game, Random rand) {
		ArrayList<Point> available = game.getAvailablePoints();
		while (!game.checkVictory()) {
			int randIndex = rand.nextInt(available.size());
			Point guess = available.get(randIndex);
			available.remove(randIndex);
			game.guess(guess);
		}
	}
}
