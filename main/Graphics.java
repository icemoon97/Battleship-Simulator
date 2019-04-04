package main;

import java.awt.Point;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Graphics extends Application{
	public final int windowSize = 500;
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		stage.setTitle("Battleship simulator");
		
		Pane pane = new Pane();
		
		int gridSize = 10; //size of battleship grid
		StackPane[][] grid = new StackPane[gridSize][gridSize];
		initGrid(grid, pane);
		
		Game game = new Game(10);
		Random rand = new Random();
		
		PlacingAlgorithms.randomPlacement(game, rand);

		TargetingAlgorithms.basicHuntAndSink(game, rand);
		
		drawBoard(grid, game);
		
		System.out.println(game.getTurns());
		
		stage.setScene(new Scene(pane, windowSize, windowSize));
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
				} else {
					((Rectangle)grid[i][j].getChildren().get(0)).setFill(Color.WHITE);
				}
			}
			System.out.println();
		}
	}

	public void initGrid(StackPane[][] grid, Pane pane) {
		double rectSize = windowSize / (grid.length);
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = new StackPane();
				pane.getChildren().add(grid[i][j]);
				grid[i][j].relocate(i * rectSize, j * rectSize);
				Rectangle rect = new Rectangle(rectSize, rectSize);
				rect.setStroke(Color.BLACK);
				grid[i][j].getChildren().add(rect);
			}
		}
	}
}