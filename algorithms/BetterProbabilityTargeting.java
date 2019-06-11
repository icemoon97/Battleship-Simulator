package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import main.Game;
import main.Ship;

public class BetterProbabilityTargeting extends TargetingAlgorithm {
	private Random rand;
	private int trialsPerMove; //number of possible configurations generated per move. 
	//higher values are more accurate/make a better algorithm, but require more calculations

	public BetterProbabilityTargeting(Game game) {
		this.game = game;
		rand = new Random();
		trialsPerMove = 10; //good balance between accuracy and processing time
	}

	//constructor with option to set seed of random object
	public BetterProbabilityTargeting(Game game, long seed) {
		this(game);
		rand.setSeed(seed);
	}

	@Override
	public void nextMove() {
		if (!game.checkVictory()) {
			Point guess = new Point(-1, -1); //dummy point, will be reassigned 
			int[][] table = generateCountsTable(trialsPerMove);
			int maxCount = -1;
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table.length; j++) {
					if (table[i][j] > maxCount && !game.getGuesses()[j][i]) {
						maxCount = table[i][j];
						guess = new Point(j, i);
					}
				}
			}

			game.guess(guess); 
		}
	}

	//returns a table the size of the battleship grid, each number respresenting the number of configurations where a ship occupied that space
	public int[][] generateCountsTable(int trials) {
		int[][] table = new int[game.getGridSize()][game.getGridSize()];
		for (int i = 0; i < trials; i++) {
			Game possibleConfig = createPossibleConfig();
			for (Ship ship: possibleConfig.getShips()) {
				for (Point p: ship.getPoints()) {
					table[p.y][p.x]++;
				}
			}
		}
		return table;
	}

	public Game createPossibleConfig() {
		Game possibleConfig = new Game(game.getGridSize());
		ArrayList<Integer> shipStatus = game.getShipStatus();
		for (int i = shipStatus.size() - 1; i >= 0; i--) {
			if (shipStatus.get(i) < 0) {
				shipStatus.remove(i);
			}
		}

		Collections.sort(shipStatus);
		Collections.reverse(shipStatus);

		ArrayList<Point> hitsToFill = getNonDestroyedHits();

		for (int shipSize: shipStatus) {
			boolean testOrientation = rand.nextBoolean(); //attempts to place ship with random orientation
			ArrayList<Ship> possible = getPossibleShips(shipSize, testOrientation);
			
			if (hitsToFill.size() > 0) { //if there are undestroyed hits
				retainHitOverlaps(possible, hitsToFill);
				if (possible.size() == 0) { //means nothing for that orientation
					possible = getPossibleShips(shipSize, !testOrientation);
					retainHitOverlaps(possible, hitsToFill);
					if (possible.size() == 0) { //means no possible placements that overlap a hit
						possible = getPossibleShips(shipSize, testOrientation);
					}
				}
			} 
			
			if (possible.size() == 0) { //means nothing for that orientation
				testOrientation = !testOrientation;
				possible = getPossibleShips(shipSize, testOrientation);
			}
			
			removeOverlapping(possible, possibleConfig);
			if (possible.size() == 0) {
				possible = getPossibleShips(shipSize, !testOrientation);
				removeOverlapping(possible, possibleConfig);
			}
			
			if (possible.size() == 0) { //happens very rarely (about 1/1000 trials) but is unavoidable
				return possibleConfig;
			}
			
			int n = rand.nextInt(possible.size());
			Ship testShip = possible.get(n);
			possibleConfig.addShip(testShip);
			hitsToFill.removeAll(testShip.getPoints());
		}
		return possibleConfig;
	}
	
	//removes all ships that do not overlap with a hit
	private void retainHitOverlaps(ArrayList<Ship> possible, ArrayList<Point> hitsToFill) {
		for (int i = possible.size() - 1; i >= 0; i--) {
			if (!containsHit(possible.get(i), hitsToFill)) {
				possible.remove(i);
			}
		}
	}
	
	//removes all ships that overlap with other ships in the current possible config
	private void removeOverlapping(ArrayList<Ship> possible, Game config) {
		for (int i = possible.size() - 1; i >= 0; i--) {
			if (!config.isLegalLocation(possible.get(i))) {
				possible.remove(i);
			}
		}
	}

	//returns true if ship contains a point from the given list of points
	private boolean containsHit(Ship ship, ArrayList<Point> hits) {
		for (Point hit: hits) {
			if (ship.getPoints().contains(hit)) {
				return true;
			}
		}
		return false;
	}

	//returns a list of all possible ships that could fit on the given game with the given size and orientation
	public ArrayList<Ship> getPossibleShips(int size, boolean orientation) {
		ArrayList<Point> empty = game.getAvailablePoints(); //all non-guessed and hits
		empty.addAll(getNonDestroyedHits());

		ArrayList<Ship> result = new ArrayList<Ship>();
		for (Point possible: empty) {
			Ship testShip = new Ship(size, orientation, possible);
			if (empty.containsAll(testShip.getPoints())) {
				result.add(testShip);
			}
		}
		return result;
	}

	private ArrayList<Point> getNonDestroyedHits() {
		ArrayList<Point> hits = game.getHits();
		for (Ship s: game.getDestroyedShips()) {
			hits.removeAll(s.getPoints());
		}
		return hits;
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