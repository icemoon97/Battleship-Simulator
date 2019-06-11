package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Game {
	private ArrayList<Ship> ships;
	private int gridSize;
	private boolean[][] guesses;
	private int turns;

	public Game(int gridSize) {
		ships = new ArrayList<Ship>();
		this.gridSize = gridSize;
		guesses = new boolean[gridSize][gridSize];
		turns = 0;
	}

	public void addShip(Ship ship) {
		if (isLegalLocation(ship)) {
			ships.add(ship);
		} else {
			throw new IllegalArgumentException("ship out of bounds or overlapping");
		}
	}

	public void guess(Point guess) {
		if (guess.x < 0 || guess.x >= gridSize || guess.y < 0 || guess.y >= gridSize) {
			throw new IllegalArgumentException("guess out of bounds");
		}
		if (guesses[guess.x][guess.y]) {
			throw new IllegalArgumentException("that location has already been guessed");
		} else {
			guesses[guess.x][guess.y] = true;
			for (Ship ship: ships) {
				ship.guess(guess);
			}
		}
		turns++;
	}
	
	public void guess(int x, int y) {
		guess(new Point(x, y));
	}

	public boolean isHit(Point guess) {
		for (Ship ship: ships) {
			if (ship.isHit(guess)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isHit(int x, int y) {
		return isHit(new Point(x, y));
	}

	public boolean isLegalLocation(Ship ship) { 
		//checks to make sure there is no overlap between ships
		for (Ship other: ships) {
			if (isOverlap(ship, other)) {
				return false;
			}
		}
		//checks if ship will be within grid
		for (Point p: ship.getPoints()) {
			if (p.x < 0 || p.x >= gridSize || p.y < 0 || p.y >= gridSize) {
				return false;
			}
		}
		return true;
	}

	private boolean isOverlap(Ship ship1, Ship ship2) {
		Set<Point> overlap = new HashSet<Point>();
		for (Point p: ship1.getPoints()) {
			overlap.add(p);
		}		
		overlap.retainAll(ship2.getPoints());
		return overlap.size() > 0;
	}

	public boolean checkVictory() {
		for (Ship ship: ships) {
			if (!ship.isDestroyed()) {
				return false;
			}
		}
		return true;
	}

	//returns arraylist of all non-guessed points
	public ArrayList<Point> getAvailablePoints() { 
		ArrayList<Point> result = new ArrayList<Point>();
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (!guesses[i][j]) {
					result.add(new Point(i, j));
				}
			}
		}
		return result;
	}

	public int getGridSize() {
		return gridSize;
	}

	public ArrayList<Ship> getShips() {
		return ships;
	}

	public boolean[][] getGuesses() {
		return guesses;
	}

	public int getTurns() {
		return turns;
	}
	
	//returns an arraylist of ship lengths, positive if they still exist, negative if they have been sunk
	public ArrayList<Integer> getShipStatus() {
		ArrayList<Integer> shipStatus = new ArrayList<Integer>();
		for (Ship s: ships) {
			if (s.isDestroyed()) {
				shipStatus.add(s.size() * -1);
			} else {
				shipStatus.add(s.size());
			}
		}
		return shipStatus;
	}
	
	//returns a set of all non-destroyed ships
	public Set<Ship> getAliveShips() {
		Set<Ship> alive = new HashSet<Ship>();
		for (Ship s: ships) {
			if (!s.isDestroyed()) {
				alive.add(s);
			}
		}
		return alive;
	}
	
	//returns a set of all destroyed ships
	public Set<Ship> getDestroyedShips() {
		Set<Ship> dead = new HashSet<Ship>();
		for (Ship s: ships) {
			if (s.isDestroyed()) {
				dead.add(s);
			}
		}
		return dead;
	}
	
	//returns an arraylist of all points that have been guessed and are hits
	public ArrayList<Point> getHits() {
		ArrayList<Point> hits = new ArrayList<Point>();
		for (int i = 0; i < guesses.length; i++) {
			for (int j = 0; j < guesses.length; j++) {
				if (guesses[i][j] && isHit(i, j)) {
					hits.add(new Point(i, j));
				}
			}
		}
		return hits;
	}
}
