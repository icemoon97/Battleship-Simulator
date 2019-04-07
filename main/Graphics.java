package main;

import java.awt.Point;
import algorithms.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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
		PlacingAlgorithm placeAlgo = new RandomPlacement(game);
		placeAlgo.run();
		TargetingAlgorithm targetAlgo = new CheckerboardHuntAndSink(game);
		
		Button newButton = new Button("New");
		newButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Game newGame = new Game(10);
				placeAlgo.setGame(newGame);
				placeAlgo.run();
				targetAlgo.setGame(newGame);
				drawBoard(grid, targetAlgo.getGame());
			}
		});
		Button nextMoveButton = new Button("Next Move");
		nextMoveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				targetAlgo.nextMove();
				drawBoard(grid, targetAlgo.getGame());
			}
		});
		Button runButton = new Button("Sim to End");
		runButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				targetAlgo.run();
				drawBoard(grid, targetAlgo.getGame());
			}
		});
		
		HBox controls = new HBox();
		controls.setPadding(new Insets(10, 10, 10, 10));
		controls.setSpacing(10);
		controls.relocate(0, 500);
		controls.getChildren().addAll(newButton, nextMoveButton, runButton);
		pane.getChildren().add(controls);
		
		stage.setScene(new Scene(pane, windowSize, windowSize + 40));
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
				rect.setFill(Color.WHITE );
				grid[i][j].getChildren().add(rect);
			}
		}
	}
}