package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class BattleshipMain {
	public static void main(String[] args) {
		int trials = 100;
		int sum = 0;
		for (int i = 0; i < trials; i++) {
			Game game = new Game(10);

			addRandomShip(game, 2);
			addRandomShip(game, 3);
			addRandomShip(game, 3);
			addRandomShip(game, 4);
			addRandomShip(game, 5);

			basicHuntAndSink(game);
			sum += game.getTurns();
			
			System.out.println("Sunk all ships in " + game.getTurns() + " turns");
		}
		System.out.println("Average turns to win: " + (double)sum / trials);
	}

	//algorithm that guesses guesses adjacent squares after getting a hit, is otherwise random
	public static void basicHuntAndSink(Game game) {
		Random rand = new Random();
		ArrayList<Point> queue = new ArrayList<Point>();
		while (!game.checkVictory()) {
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
			queue.retainAll(game.getAvailablePoints()); //removes any points in the queue that have already been guessed
			game.guess(guess);
		}
	}

	//algorithm that guesses completely randomly (is really bad)
	public static void randomAlgorithm(Game game) {
		Random rand = new Random();
		ArrayList<Point> available = game.getAvailablePoints();
		while (!game.checkVictory()) {
			int randIndex = rand.nextInt(available.size());
			Point guess = available.get(randIndex);
			available.remove(randIndex);
			game.guess(guess);
		}
	}

	//creates a new ship and adds it to the game in a legal, random location
	public static void addRandomShip(Game game, int length) {
		Random rand = new Random();
		Ship ship = new Ship(length, rand.nextBoolean(), new Point(-1, -1)); //dummy point
		while (!game.isLegalLocation(ship)) {
			int x = rand.nextInt(game.getGridSize());
			int y = rand.nextInt(game.getGridSize());
			ship.place(new Point(x, y));
		}
		game.addShip(ship);
	}

	public static void printBoard(Game game) {
		boolean[][] guesses = game.getGuesses();
		for (int i = 0; i < guesses.length; i++) {
			for (int j = 0; j < guesses.length; j++) {
				if (guesses[i][j] && game.isHit(new Point(i, j))) {
					System.out.print("X ");
				} else if (guesses[i][j]){
					System.out.print("O ");
				} else {
					System.out.print("- ");
				}
			}
			System.out.println();
		}
	}

	public static void printShipLocations(Game game) {
		int[][] ships = new int[game.getGridSize()][game.getGridSize()];
		int n = 1;
		for (Ship ship: game.getShips()) {
			for (Point p: ship.getPoints()) {
				ships[p.x][p.y] = n;
			}
			n++;
		}
		for (int i = 0; i < game.getGridSize(); i++) {
			for (int j = 0; j < game.getGridSize(); j++) {
				if (ships[i][j] == 0) {
					System.out.print("- ");
				} else {
					System.out.print(ships[i][j] + " ");
				}
			}
			System.out.println();
		}
	}
}
