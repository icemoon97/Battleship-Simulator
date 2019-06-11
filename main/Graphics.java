package main;

import java.awt.Point;
import algorithms.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

public class Graphics extends Application{
	public final int WINDOW_SIZE = 500;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		stage.setTitle("Battleship Simulator");

		Pane pane = new Pane();

		int gridSize = 10; //size of battleship grid
		StackPane[][] grid = new StackPane[gridSize][gridSize];
		initGrid(grid, pane);

		PlacingAlgorithm placeAlgo = new RandomPlacement(new Game(10));
		placeAlgo.run();
		
		//TargetingAlgorithm targetAlgo = new RandomTargeting(placeAlgo.getGame());
		//TargetingAlgorithm targetAlgo = new BasicHuntAndSink(placeAlgo.getGame());
		//TargetingAlgorithm targetAlgo = new CheckerboardHuntAndSink(placeAlgo.getGame());
		//TargetingAlgorithm targetAlgo = new ProbabilityTargeting(placeAlgo.getGame());
		TargetingAlgorithm targetAlgo = new BetterProbabilityTargeting(placeAlgo.getGame());

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(25), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (targetAlgo.getGame().checkVictory()) {
					placeAlgo.setGame(new Game(10));
					placeAlgo.run();
					targetAlgo.setGame(placeAlgo.getGame());
					drawBoard(grid, targetAlgo.getGame());
				}
				targetAlgo.nextMove();
				drawBoard(grid, targetAlgo.getGame());				
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		Button newButton = new Button("New");
		newButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				timeline.pause();
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
				timeline.pause();
				targetAlgo.nextMove();
				drawBoard(grid, targetAlgo.getGame());
			}
		});
		Button runButton = new Button("Sim to End");
		runButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				timeline.pause();
				targetAlgo.run();
				drawBoard(grid, targetAlgo.getGame());
			}
		});
		Button playButton = new Button("Play Animation");
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				timeline.play();
			}
		});
		Button pauseButton = new Button("Pause Animation");
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				timeline.pause();
			}
		});


		HBox controls = new HBox();
		controls.setPadding(new Insets(10, 10, 10, 10));
		controls.setSpacing(10);
		controls.relocate(0, 500);
		controls.getChildren().addAll(newButton, nextMoveButton, runButton, playButton, pauseButton);
		pane.getChildren().add(controls);

		stage.setScene(new Scene(pane, WINDOW_SIZE, WINDOW_SIZE + 40));
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
}