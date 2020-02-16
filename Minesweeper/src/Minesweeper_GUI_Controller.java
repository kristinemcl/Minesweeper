/* Kristine McLaughlin, Period 6, 4.7.19
 * This lab took me around 20 hours (this is an estimate- I didn't really count).
 */

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Minesweeper_GUI_Controller extends Application { // Controller
	private final Image BLANK = new Image("file:minesweeper_images/blank.gif");
	private final Image FLAGGED = new Image("file:minesweeper_images/bomb_flagged.gif");
	private final Image QUESTION = new Image("file:minesweeper_images/bomb_question.gif");
	private final Image REVEALED = new Image("file:minesweeper_images/bomb_revealed.gif");
	private final Image BOMB_DEATH = new Image("file:minesweeper_images/bomb_death.gif");
	private final Image NUM_0 = new Image("file:minesweeper_images/num_0.gif");
	private final Image NUM_1 = new Image("file:minesweeper_images/num_1.gif");
	private final Image NUM_2 = new Image("file:minesweeper_images/num_2.gif");
	private final Image NUM_3 = new Image("file:minesweeper_images/num_3.gif");
	private final Image NUM_4 = new Image("file:minesweeper_images/num_4.gif");
	private final Image NUM_5 = new Image("file:minesweeper_images/num_5.gif");
	private final Image NUM_6 = new Image("file:minesweeper_images/num_6.gif");
	private final Image NUM_7 = new Image("file:minesweeper_images/num_7.gif");
	private final Image NUM_8 = new Image("file:minesweeper_images/num_8.gif");
	
	private Minesweeper_Model model;
	private View view;
	private BorderPane borderPane;
	private Group viewGroup;
	private int move = 0;
	private Text minesRemaining;
	private HBox bottom;
	private ScrollPane scrollPane;
	
	private int gameLostRow;
	private int gameLostCol;
	
	private MenuItem setNumMines;
	private MenuItem howToPlay;
	private MenuItem about;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		model = new Minesweeper_Model();
		
		borderPane = new BorderPane();
		
		stage.setResizable(true);
		
		// set up the game
		model.setGame(10, 10, 10);
		viewGroup = new Group();
		view = new View();
		viewGroup.getChildren().add(view);
		viewGroup.setOnMouseClicked(new MouseListener());
		borderPane.setCenter(viewGroup);
		
		// game menu
		MenuBar menuBar = new MenuBar();
		Menu menu1 = new Menu("Game");
		MenuItem newGame = new MenuItem("New Beginner Game");
		newGame.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e) {
				model.setGame(8,8,10);
				move = 0;
				view = new View(8,8);
				viewGroup = new Group();
				viewGroup.getChildren().add(view);
				viewGroup.setOnMouseClicked(new MouseListener());
				borderPane.setCenter(viewGroup);
				updateView();
		}});
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.exit(0);
				
		}});
		
		// options menu
		Menu menu2 = new Menu("Options");
		setNumMines = new MenuItem("Set Number of Mines");
		setNumMines.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				TextInputDialog input = new TextInputDialog();
				int userNum;
				boolean first = true;
				do{
					if(!first) {
						Alert invalid = new Alert(AlertType.ERROR, "That is not a valid number of mines. Try again.", ButtonType.OK);
						invalid.showAndWait();
					} else first = false;
					input.setHeaderText("How many mines would you like?");
					input.showAndWait();
					userNum = Integer.parseInt(input.getEditor().getText());
				} while (userNum < 0 || userNum >= model.getNumCols() * model.getNumRows());
				
				model.setGame(model.getNumRows(), model.getNumCols(), userNum);
				move = 0;
				view = new View(model.getNumRows(), model.getNumCols());
				viewGroup = new Group();
				viewGroup.getChildren().add(view);
				viewGroup.setOnMouseClicked(new MouseListener());
				borderPane.setCenter(viewGroup);
				updateView();
		}});
		
		// help menu
		Menu menu3 = new Menu("Help");
		howToPlay = new MenuItem("How To Play");
		howToPlay.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				scrollPane = new ScrollPane();
				WebView abtPage = new WebView();
				WebEngine engine = abtPage.getEngine();
				engine.load("file:///" + new File("about.html").getAbsolutePath());
				scrollPane.setContent(abtPage);
				borderPane.setCenter(scrollPane);
				stage.setScene(new Scene(borderPane, 500, 500));
		}});
		about = new MenuItem("About");
		about.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				scrollPane = new ScrollPane();
				WebView howToPlay = new WebView();
				WebEngine engine = howToPlay.getEngine();
				engine.load("file:///" + new File("howToPlay.html").getAbsolutePath());
				scrollPane.setContent(howToPlay);
				borderPane.setCenter(scrollPane);
				stage.setScene(new Scene(borderPane, 500, 500));
		}});
		
		menu1.getItems().addAll(newGame, exit);
		menu2.getItems().add(setNumMines);
		menu3.getItems().addAll(howToPlay, about);
		menuBar.getMenus().addAll(menu1, menu2, menu3);
		borderPane.setTop(menuBar);
		
		// set up timer and mines remaining
		bottom = new HBox();
		String minesLeft = "Mines Remaining: " + model.getTotalMines();
		minesRemaining = new Text(minesLeft);
		minesRemaining.setFont(new Font("American Typewriter", 23));
		bottom.getChildren().add(minesRemaining);
		bottom.setPadding(new Insets(20, 20, 20, 20)); 
		bottom.setStyle("-fx-background-color: #CCF2FF");
		Label timeElapsed = new Label("Time elapsed: ");
		long beginning = System.currentTimeMillis();
		new AnimationTimer() {
			public void handle(long now) {
				timeElapsed.setText("Time elapsed: " + (System.currentTimeMillis() - beginning)/1000);
			}
		}.start();
		bottom.getChildren().add(timeElapsed);
		timeElapsed.setFont(new Font("American Typewriter", 23));
		timeElapsed.setPadding(new Insets(0, 10, 10, 10));
		
		borderPane.setBottom(bottom);
		
		Scene scene = new Scene(borderPane, 500, 500);
		stage.setScene(scene);
		stage.show();
		
	}
	
	private class MouseListener implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent e) {
			if(!model.gameOver() && !model.gameWon()) {
				
				int col = view.colForXPos(e.getX());
				int row = view.rowForYPos(e.getY());
				
				// set the mines on the first move
				if(move == 0) {
					move++;
					model.setGridMines(row, col);
				}
				
				if(isInBounds(row, col)) {
					if(e.getButton() == MouseButton.PRIMARY && e.getSource() == viewGroup) { // left click
						if(!model.isRevealed(row, col)) {
							if(model.minePresent(row, col)) {
								// you lose
								model.setRevealed(row, col, true);
								gameLostRow = row;
								gameLostCol = col;
							} else model.revealNeighbors(row, col);
						}
					} else if(e.getButton() == MouseButton.SECONDARY && e.getSource() == viewGroup) { // right click
						if(!model.isRevealed(row, col)) model.setFlag(row, col);
					}
				}
				
				updateView();
			}
			
			if(model.gameWon()) {
				winActions();
			} else if(model.gameOver()) {
				loseActions();
			}
		}
	}
	
	private boolean isInBounds(int r, int c) {
		return (r >= 0 && r < model.getNumRows() && c >= 0 && c < model.getNumCols());
	}
	
	private void updateView() {
		for(int row = 0; row < model.getNumRows(); row++) {
			for(int col = 0; col < model.getNumCols(); col++) {
				if(model.isRevealed(row, col)) {
					if(model.minePresent(row, col)) view.changeImage(row, col, REVEALED);
					else if(model.getNumNeighborMines(row, col) == 0) view.changeImage(row, col, NUM_0);
					else if(model.getNumNeighborMines(row, col) == 1) view.changeImage(row, col, NUM_1);
					else if(model.getNumNeighborMines(row, col) == 2) view.changeImage(row, col, NUM_2);
					else if(model.getNumNeighborMines(row, col) == 3) view.changeImage(row, col, NUM_3);
					else if(model.getNumNeighborMines(row, col) == 4) view.changeImage(row, col, NUM_4);
					else if(model.getNumNeighborMines(row, col) == 5) view.changeImage(row, col, NUM_5);
					else if(model.getNumNeighborMines(row, col) == 6) view.changeImage(row, col, NUM_6);
					else if(model.getNumNeighborMines(row, col) == 7) view.changeImage(row, col, NUM_7);
					else if(model.getNumNeighborMines(row, col) == 8) view.changeImage(row, col, NUM_8);
				} else {
					if(model.hasQuestion(row, col)) view.changeImage(row, col, QUESTION);
					else if(model.isFlagged(row, col)) view.changeImage(row, col, FLAGGED);
					else view.changeImage(row, col, BLANK);
				}
			}
		}
		minesRemaining.setText("Mines Remaining: " + model.getNumMinesLeft());
	}
	
	private void winActions() {
		Alert youWin = new Alert(AlertType.NONE, "Congratulations! You win! :)", ButtonType.OK);
		youWin.showAndWait();
		viewGroup.setOnMouseClicked(null);
	}
	
	private void loseActions() {
		for(int r = 0; r < model.getNumRows(); r++) {
			for(int c = 0; c < model.getNumCols(); c++) {
				if(model.minePresent(r, c)) {
					if(r == gameLostRow && c == gameLostCol) view.changeImage(r, c, BOMB_DEATH);
					else view.changeImage(r, c, REVEALED);
				} 
			}
		}
		Alert youLose = new Alert(AlertType.NONE, "Sorry, you lost. Maybe next time! <3", ButtonType.OK);
		youLose.showAndWait();
		viewGroup.setOnMouseClicked(null);
	}
	

}
