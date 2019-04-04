package main;

import java.awt.Point;
import java.util.Arrays;
import java.util.Random;

public class BattleshipTesting {
	public static void main(String[] args) {
		int trials = 500;
		int[] record = new int[trials];
		for (int i = 0; i < trials; i++) {
			Game game = new Game(10);
			Random rand = new Random();

			PlacingAlgorithms.randomPlacement(game, rand);

			//TargetingAlgorithms.randomAlgorithm(game);
			//TargetingAlgorithms.basicHuntAndSink(game, rand);
			TargetingAlgorithms.checkerboardHuntAndSink(game, rand);

			//printBoard(game);

			record[i] = game.getTurns();
			System.out.println("Sunk all ships in " + game.getTurns() + " turns");
		}
		int sum = 0;
		for (int n: record) {
			sum += n;
		}
		Arrays.sort(record);
		System.out.println("Average turns to win: " + (double)sum / trials);
		System.out.println("Best game: " + record[0] + " turns");
		System.out.println("Worst game: " + record[record.length - 1] + " turns");
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
