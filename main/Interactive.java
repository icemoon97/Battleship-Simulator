package main;

import java.awt.Point;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import algorithms.*;

public class Interactive extends Application{
	public final int WINDOW_SIZE = 500;
	public Random rand;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		stage.setTitle("Battleship Simulator");
		stage.setResizable(false);

		Pane pane = new Pane();

		int gridSize = 10; //size of battleship grid
		StackPane[][] grid = new StackPane[gridSize][gridSize];
		initGrid(grid, pane);

		rand = new Random();
		Game game = new Game(10);

		game.addShip(new Ship(5, true, new Point(1, 1)));
		game.addShip(new Ship(4, true, new Point(3, 1)));
		game.addShip(new Ship(3, true, new Point(5, 1)));
		game.addShip(new Ship(3, true, new Point(7, 1)));
		game.addShip(new Ship(2, false, new Point(1, 8)));
		//BattleshipTesting.printShipLocations(game);

		BetterProbabilityTargeting algo = new BetterProbabilityTargeting(game);
		
		drawBoard(grid, game);
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				final int x = i;
				final int y = j;
				StackPane sp = grid[x][y];
				sp.setOnMousePressed(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
						if (!game.getGuesses()[x][y]) {
							game.guess(x, y);
						}
						drawBoard(grid, game);
						if (game.checkVictory()) {
							System.out.println("You Win!");
						}
					}
				});
			}
		}

		Button newConfigButton = new Button("Generate Possible Config");
		newConfigButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.out.println();
				Game possibleConfig = algo.createPossibleConfig();
				BattleshipTesting.printShipLocations(possibleConfig);

			}
		});

		Button countsTableButton = new Button("Generate Counts Table");
		countsTableButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.out.println();
				int[][] table = algo.generateCountsTable(25);
				printTable(table);
				int max = -1;
				Point guess = new Point(-1, -1);
				for (int i = 0; i < table.length; i++) {
					for (int j = 0; j < table.length; j++) {
						if (table[i][j] > max && !game.getGuesses()[j][i]) {
							max = table[i][j];
							guess = new Point(i, j);
						}
					}
				}
				System.out.println(guess);
			}
		});

		HBox controls = new HBox();
		controls.setPadding(new Insets(10, 10, 10, 10));
		controls.setSpacing(10);
		controls.relocate(0, 500);
		controls.getChildren().addAll(newConfigButton, countsTableButton);

		pane.getChildren().add(controls);

		stage.setScene(new Scene(pane, WINDOW_SIZE, WINDOW_SIZE + 30));
		stage.show();
	}

	public void drawBoard(StackPane[][] grid, Game game) {
		boolean[][] guesses = game.getGuesses();
		for (int i = 0; i < guesses.length; i++) {
			for (int j = 0; j < guesses.length; j++) {
				if (guesses[i][j] && game.isHit(new Point(i, j))) {
					((Rectangle)grid[i][j].getChildren().get(0)).setFill(Color.RED);
				} else if (guesses[i][j]){
					((Rectangle)grid[i][j].getChildren().get(0)).setFill(Color.GREY);
				} else if (game.isHit(new Point(i, j))) {
					((Rectangle)grid[i][j].getChildren().get(0)).setFill(Color.GREEN); //ONLY FOR TESTING
				} else {
					((Rectangle)grid[i][j].getChildren().get(0)).setFill(Color.WHITE);
				}
			}
		}
	}

	public void initGrid(StackPane[][] grid, Pane pane) {
		double rectSize = WINDOW_SIZE / (grid.length);
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = new StackPane();
				pane.getChildren().add(grid[i][j]);
				grid[i][j].relocate(i * rectSize, j * rectSize);
				Rectangle rect = new Rectangle(rectSize, rectSize);
				rect.setStroke(Color.BLACK);
				rect.setFill(Color.WHITE );
				grid[i][j].getChildren().add(rect);
			}
		}
	}

	public void printTable(int[][] table) {
		for (int[] row: table) {
			for (int n: row) {
				System.out.print(n + "  ");
			}
			System.out.println();
		}
	}
}
