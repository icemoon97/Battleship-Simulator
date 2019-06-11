package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import main.Game;
import main.Ship;

public class ProbabilityTargeting extends TargetingAlgorithm {
	private Random rand;
	private ArrayList<Point> queue;
	private int trialsPerMove; //number of possible configurations generated per move. 
		//higher values are more accurate/make a better algorithm, but require more calculations

	public ProbabilityTargeting(Game game) {
		this.game = game;
		rand = new Random();
		queue = new ArrayList<Point>();
		trialsPerMove = 10; //good balance between accuracy and processing time
	}

	//constructor with option to set seed of random object
	public ProbabilityTargeting(Game game, long seed) {
		this(game);
		rand.setSeed(seed);
	}

	@Override
	public void nextMove() {
		if (!game.checkVictory()) {
			queue.retainAll(game.getAvailablePoints()); //removes any points in the queue that have already been guessed
			Point guess = new Point(-1, -1); //dummy point, will be reassigned
			if (queue.size() > 0) {
				guess = queue.get(0);
				queue.remove(0);
			} else { 
				int[][] table = generateCountsTable(trialsPerMove);
				int maxCount = 0;
				for (int i = 0; i < table.length; i++) {
					for (int j = 0; j < table.length; j++) {
						if (table[i][j] > maxCount) {
							maxCount = table[i][j];
							guess = new Point(i, j);
						}
					}
				}
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

	//returns a table the size of the battleship grid, each number respresenting the number of configurations where a ship occupied that space
	private int[][] generateCountsTable(int trials) {
		int[][] results = new int[game.getGridSize()][game.getGridSize()];
		for (int i = 0; i < trials; i++) {
			ArrayList<Ship> possibleConfig = generateShipConfig();
			for (Ship s: possibleConfig) {
				for (Point p: s.getPoints()) {
					results[p.x][p.y]++;
				}
			}
		}
		return results;
	}
	
	//returns a random, possible configuration of ships based on guesses and game state (which ships are sunk)
	private ArrayList<Ship> generateShipConfig() {
		Game possibleConfig = new Game(game.getGridSize());
		ArrayList<Integer> shipStatus = game.getShipStatus();
		Collections.reverse(shipStatus); //this way, ships are placed longest first, reduces infinite loops
		for (int n: shipStatus) {
			if (n > 0) {
				possibleConfig.addShip(generatePossibleShip(n, possibleConfig));
			}
		}
		return possibleConfig.getShips();
	}

	private Ship generatePossibleShip(int length, Game possibleConfig) {
		ArrayList<Point> available = game.getAvailablePoints(); //non-guessed points
		Ship ship = new Ship(length, true);
		while (!possibleConfig.isLegalLocation(ship) || !available.containsAll(ship.getPoints())) {
			//ship with random orientation, random point from available list
			ship = new Ship(length, rand.nextBoolean(), available.get(rand.nextInt(available.size())));
		}
		return ship;
	}

	@Override
	public void run() {
		while (!game.checkVictory()) {
			nextMove();
		}
	}
	
	//sets trailsPerMove
	public void setTrials(int trials) {
		trialsPerMove = trials;
	}
}
