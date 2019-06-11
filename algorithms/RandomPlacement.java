package algorithms;

import java.awt.Point;
import java.util.Random;
import main.Game;
import main.Ship;

public class RandomPlacement implements PlacingAlgorithm {
	private Game game;
	private Random rand;

	public RandomPlacement(Game game) {
		this.game = game;
		rand = new Random();
	}

	//constructor with option to set seed of random object
	public RandomPlacement(Game game, long seed) {
		this(game);
		rand.setSeed(seed);
	}

	@Override
	public void run() {
		//randomly places standard set of ships
		addRandomShip(2);
		addRandomShip(3);
		addRandomShip(3);
		addRandomShip(4);
		addRandomShip(5);
	}

	//creates a new ship and adds it to the game in a legal, random location
	private void addRandomShip(int length) {
		Ship ship = new Ship(length, rand.nextBoolean()); 
		while (!game.isLegalLocation(ship)) {
			int x = rand.nextInt(game.getGridSize());
			int y = rand.nextInt(game.getGridSize());
			ship.place(new Point(x, y));
		}
		game.addShip(ship);
	}
	
	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public Game getGame() {
		return game;
	}
}

