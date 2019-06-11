package main;

import java.awt.Point;
import java.util.Arrays;
import java.util.Random;
import algorithms.*;

public class BattleshipTesting {
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		int trials = 100;
		int[] record = new int[trials];
		for (int i = 0; i < trials; i++) {
			Game game = new Game(10);

			Random rand = new Random();
			long seed = rand.nextLong();
			//long seed = -2286453305364611319l;
			//System.out.println("Seed: " + seed);

			PlacingAlgorithm placeAlgo = new RandomPlacement(game, seed);
			placeAlgo.run();

			//TargetingAlgorithm targetAlgo = new RandomTargeting(game, seed);
			//TargetingAlgorithm targetAlgo = new BasicHuntAndSink(game, seed);
			//TargetingAlgorithm targetAlgo = new CheckerboardHuntAndSink(game, seed);
			//TargetingAlgorithm targetAlgo = new ProbabilityTargeting(game, seed);
			TargetingAlgorithm targetAlgo = new BetterProbabilityTargeting(game, seed);

			targetAlgo.run();

			record[i] = game.getTurns();
			System.out.println("Trial " + (i + 1) + ": Sunk all ships in " + game.getTurns() + " turns");
		}
		
		long endTime = System.nanoTime();
		double simulationTime = (endTime - startTime) / 1000000000.0; //in seconds
		
		int sum = 0;
		for (int n: record) {
			sum += n;
		}
		
		Arrays.sort(record);
		System.out.println("Average turns to win: " + (double)sum / trials);
		System.out.println("Best game: " + record[0] + " turns");
		System.out.println("Worst game: " + record[record.length - 1] + " turns");
		System.out.println("Simulation time: " + simulationTime + " seconds");
		System.out.println("Simulation time per trial: " + simulationTime / trials);
	}

	public static void printBoard(Game game) {
		boolean[][] guesses = game.getGuesses();
		for (int i = 0; i < guesses.length; i++) {
			for (int j = 0; j < guesses.length; j++) {
				if (guesses[j][i] && game.isHit(new Point(j, i))) {
					System.out.print("X ");
				} else if (guesses[j][i]){
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
				ships[p.y][p.x] = n;
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
