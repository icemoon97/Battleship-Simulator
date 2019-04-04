package main;

import java.awt.Point;
import java.util.Random;

public class PlacingAlgorithms {
	//randomly places standard set of ships
	public static void randomPlacement(Game game, Random rand) {
		addRandomShip(game, 2, rand);
		addRandomShip(game, 3, rand);
		addRandomShip(game, 3, rand);
		addRandomShip(game, 4, rand);
		addRandomShip(game, 5, rand);
	}
	
	//creates a new ship and adds it to the game in a legal, random location
	public static void addRandomShip(Game game, int length, Random rand) {
		Ship ship = new Ship(length, rand.nextBoolean(), new Point(-1, -1)); //dummy point
		while (!game.isLegalLocation(ship)) {
			int x = rand.nextInt(game.getGridSize());
			int y = rand.nextInt(game.getGridSize());
			ship.place(new Point(x, y));
		}
		game.addShip(ship);
	}
}
