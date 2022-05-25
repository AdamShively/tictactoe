/*
* The TicTacToe program implements an application that
* lets a human play against AI in a game of tic-tac-toe
* on varying levels of difficulty.
*
* @author  Adam Shively
* @version 1.0
* @since   04-10-2022
*/


package application;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TicTacToe extends Application{
	
	private static Text[][] board;  //Stores entries to board.
	private static String humanPlayer;	//Human player mark.
	private static String compPlayer;	//Computer player mark.
	private static boolean gameOver;	//Game active or over.
	private static boolean normalDifficulty;	//Normal difficulty setting on or off.
	private static boolean playerStarts;	//Which player goes first.
	
	public TicTacToe() {
	    board = new Text[3][3];
	    humanPlayer = "x";
	    compPlayer =  "o";
	    gameOver = false;
	    normalDifficulty = true;
	    playerStarts = true;
	}
	
	/**
	* This method is for the human player clicking on a cell they have chosen.
	* The human player's mark will be placed, immediately followed by the computer 
	* players turn if game is not over. The board is checked for a winning combination
	* after each players turn. If game is over or cell is full method returns. A check
	* for a tied game occurs as well.
	* @param p Mouse click on will cause event.
	* @param row  The row the cell is located in.
	* @param col  The column the cell is located in.
	* @param displayMes  Used to display game status message.
	* @return Nothing.
	*/
	private void gameContent(StackPane p, int row, int col, Text displayMes) {
	    p.setOnMouseClicked((MouseEvent e) -> {
		//When game is over clicking on cell will not place any x's.
		//Only empty cells can have an x placed.
		if(gameOver || !isCellEmpty(row, col)) {
			return;  //Stop method from continuing.
		}
		playerTurn(row, col);
		if(winCheck(board, humanPlayer)) {
		    gameOver = true;
		    displayMes.setText("You Win!!");
	        }
	        computerTurn();
	        if(winCheck(board, compPlayer)) {
		    gameOver = true;
	            displayMes.setText("You Lose!");
	        }

	        //All cells are filled and there is no winner.
	        if(getEmptyCells(board).size() == 0) { 
	            gameOver = true;
	            displayMes.setText("Game Tied!");
	        }
            });
	}
	/**
	* This method checks if cell is empty.
	* @param row  The row the cell is located in.
	* @param col  The column the cell is located in.
	* @return boolean Cell is either empty or not.
	*/
	private boolean isCellEmpty(int row, int col) {
	    return board[row][col].getText().equals("");
	}
	
	/**
	* This method checks if a winning combination 
	* is present.
	* @param bo Representation of current markings on board.
	* @param xo Defines which player is being checked for.
	* @return boolean A player has either won or not.
	*/
	private boolean winCheck(Text[][] bo, String xo) {
	    for(int row = 0; row < 3; row++) {
	        if(bo[row][0].getText().equals(xo) && 
		    areEqual(bo[row][0], bo[row][1]) && 
		    areEqual(bo[row][1], bo[row][2])) {
		    return true;  //Horizontal win found.
	    	}
            }
		
	    for(int col = 0; col < 3; col++) {
		if(bo[0][col].getText().equals(xo) && 
		    areEqual(bo[0][col], bo[1][col]) && 
		    areEqual(bo[1][col], bo[2][col])) {
	            return true;  //Vertical win found.
		}
	    }
	    if(bo[0][0].getText().equals(xo) && 
	        areEqual(bo[0][0], bo[1][1]) && 
	        areEqual(bo[1][1], bo[2][2])) {
		return true;  //Diagonal down-to-right win found.
	    }
	    else if(bo[0][2].getText().equals(xo) && 
	        areEqual(bo[0][2], bo[1][1]) && 
		areEqual(bo[1][1], bo[2][0])) {
	        return true;  ////Diagonal down-to-left win found.
	    }
		
	    return false;  //No winning combination found.
	}
	
	/**
	* This method checks if two entries are equal.
	* @param entry1  The first entry.
	* @param entry2  The second entry.
	* @return boolean Entries are either equal or not.
	*/
	private boolean areEqual(Text entry1, Text entry2) {
	    return entry1.getText().equals(entry2.getText());
	}
	
	/**
	* This method finds all empty cells and adds them to an ArrayList.
	* @param bo Representation of current markings on board.
	* @return ArrayList List of all empty cells.
	*/
	private ArrayList<int[]> getEmptyCells(Text[][] bo) {
	    ArrayList<int[]> emptyCells = new ArrayList<int[]>();
	    for(int row = 0; row < 3; row++) {
		for(int col = 0; col < 3; col++) {
		    if(bo[row][col].getText().equals("")) {
		        emptyCells.add(new int[] {row, col});
		    }
		}
	    }
	    return emptyCells;
	}
	
	/**
	* This method is the human players turn. Marking add to board 
	* field based on cell clicked on.
	* @param row  The row the cell is located in.
	* @param col  The column the cell is located in.
	*/
	private void playerTurn(int row, int col) {
	    board[row][col].setText(humanPlayer);
	}
	
	/**
	* This method is the computer players turn. Calls normalDiff method
	* if normal difficulty was chosen, optimalPlacement method in ImpossibleDifficulty
	* class if impossible difficulty was chosen.
	*/
	private void computerTurn() {
	    if(getEmptyCells(board).size() > 0) {
	        if(normalDifficulty) {
		    normalDiff();
		}
		else {
		    ImpossibleDifficulty id = new ImpossibleDifficulty();
		    id.optimalPlacement(board);
		}
	    }
	}
	
	/**
	* This method is used for the computer players turn if normal 
	* difficulty selected. A random cell from list of empty cells
	* is chosen and mark is placed on board.
	*/
	private void normalDiff() {
	    ArrayList<int[]> emptyCells = getEmptyCells(board);
	    Random rand = new Random();
	    int index = rand.nextInt(emptyCells.size());
	    int[] rowAndCol = emptyCells.get(index);
	    int r = rowAndCol[0];
	    int c = rowAndCol[1];
	    board[r][c].setText(compPlayer);
	}
	
	/**
	* The ImpossibleDifficulty class is used for the impossible difficulty.
	*/
	private class ImpossibleDifficulty {
		
		/**
		* This method finds the optimal placement for computer player using
		* the miniMax method and marks the board.
		* @param bo Representation of current markings on board.
		*/
		public void optimalPlacement(Text[][] bo) {
		    ArrayList<int[]> emptyCells = getEmptyCells(board);
		    int bestScore = Integer.MIN_VALUE;
		    int[] move = new int[2];

		    for(int[] eCell : emptyCells) {
			int r = eCell[0];
			int c = eCell[1];
			board[r][c].setText(compPlayer);
			int score = miniMax(board, false);
			board[r][c].setText("");

			if(score > bestScore) {
				bestScore = score;
				move = eCell;
			}
		    }
		    int row = move[0];
		    int col = move[1];
		    board[row][col].setText(compPlayer);
		}
		/**
		* This method examines all possible outcomes and returns a score for each one.
		* Situations that result in the maximizer winning (the computer player) 
		* have a score of 10, minimizer (the human player) winning have score of -10, and
		* a tie has a score of 0.
		* @param bo Representation of current markings on board.
		* @param isMaxPlayer The player that is the maximizer.
		* @return int This returns a score.
		*/
		public int miniMax(Text[][] bo, boolean isMaxPlayer) {
		    if(winCheck(board, humanPlayer)) {
		        return -10;
		    }
		    else if(winCheck(board, compPlayer)) {
			return 10;
		    }
		    else if(getEmptyCells(board).size() == 0) {
		        return 0;
		    }
		    if(isMaxPlayer) {
		        int bestScore = Integer.MIN_VALUE;
			ArrayList<int[]> remainingCells = getEmptyCells(board);
			for(int[] eCell : remainingCells) {
			    int r = eCell[0];
			    int c = eCell[1];
			    board[r][c].setText(compPlayer);
			    int score = miniMax(board, false);
			    board[r][c].setText("");
			    bestScore = Math.max(score, bestScore);
			}
			return bestScore;
		    }
		    else {
			int bestScore = Integer.MAX_VALUE;
			ArrayList<int[]> remainingCells = getEmptyCells(board);
			for(int[] eCell : remainingCells) {
			    int r = eCell[0];
			    int c = eCell[1];
			    board[r][c].setText(humanPlayer);
			    int score = miniMax(board, true);
			    board[r][c].setText("");
			    bestScore = Math.min(score, bestScore);
			}
			return bestScore;
		    }
	      }
	}
	
	/**
	* This method creates the main game scene that is added to the stage 
	* and all components that will appear in it after pre game 
	* options selection. 
	* @param stage GUI window
	*/
	private void startGame(Stage stage) {
		
	    GridPane gp  = new GridPane();
	    gp.setPrefSize(600, 600);
	    gp.setAlignment(Pos.CENTER);
		
	    Text displayMessage = new Text("In Game");
	    displayMessage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
		
	    for(int row = 0; row < 3; row++) {
	        for(int col = 0; col < 3; col++) {

	            StackPane pane  = new StackPane();
		    Text xOrO = new Text("");  //Text object for x or o placement.
		    xOrO.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 110));
		    board[row][col] = xOrO;
		    pane.getChildren().add(xOrO);
		    pane.setPrefSize(200, 200);
		    pane.setTranslateY(-15);  //Shift pane vertically to center it.
		    gameContent(pane, row, col, displayMessage);
		    gp.add(pane, col, row);
	        }
	    }
	    gp.setGridLinesVisible(true);  //Lines separating cells.
	    Button resetButton = new Button("Play Again/ Restart");
	    Text gameStatus = new Text("Game Status: ");
	    gameStatus.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));

	    HBox hbox = new HBox(gameStatus, displayMessage);
	    hbox.setAlignment(Pos.CENTER);
		
	    VBox vbox = new VBox(gp, resetButton, hbox);
	    vbox.setAlignment(Pos.CENTER);
	    Scene scene = new Scene(vbox);

	      // Add the Scene to the Stage.
	    stage.setTitle("TIC-TAC-TOE");
	    stage.setScene(scene);
	    stage.centerOnScreen();
	    stage.show();
	    
	    if(!playerStarts) {  //If computer player was selected to go first.
	    	computerTurn(); 
		}
	    
	    resetButton.setOnAction(e -> {  //Restarts the game.
    		restart(stage);
            });
		
	}
	
	/**
	* This method clears the board field in preparation for a game restart.
	*/
	private void clearBoard() {
	    for(int row = 0; row < 3; row++) {
	        for(int col = 0; col < 3; col++) {
	        	board[row][col].setText("");
	        }
	    }
	}
	
	/**
	* This method sets fields to default initializations, clears the board field,
	* and calls preGameOptions method.
	* @param stage GUI window.
	*/
	private void restart(Stage stage) {
	    gameOver = false;
	    normalDifficulty = true;
        playerStarts = true;
	    humanPlayer = "x";
    	compPlayer = "o";
	    clearBoard();
	    preGameOptions(stage);
    }
	
	/**
	* This method creates the pre game options scene that is 
	* added to the stage and all components that will appear in it. 
	* @param stage GUI window.
	*/
	private void preGameOptions(Stage stage) {
	    RadioButton normal = new RadioButton("Normal");
	    RadioButton impossible = new RadioButton("Impossible");
	    
	    RadioButton playerFirst = new RadioButton("Player");
	    RadioButton compFirst = new RadioButton("Computer");

	    ToggleGroup difficultyGroup = new ToggleGroup();
	    ToggleGroup goFirstGroup = new ToggleGroup();
	    
	    normal.setSelected(true);
	    playerFirst.setSelected(true);
	    
	    normal.setToggleGroup(difficultyGroup);
	    impossible.setToggleGroup(difficultyGroup);
	    
	    playerFirst.setToggleGroup(goFirstGroup);
	    compFirst.setToggleGroup(goFirstGroup);
	      
	    Button playGame = new Button("Play Game!");
	    
	    HBox difficultyHbox = new HBox(new Text("Choose Difficulty: "), normal, impossible);
	    HBox goFirstHbox = new HBox(new Text("Who Goes First?: "), playerFirst, compFirst);
	    
	    difficultyHbox.setAlignment(Pos.CENTER);
	    difficultyHbox.setPadding(new Insets(10));
	    difficultyHbox.setSpacing(5);
	    
	    goFirstHbox.setAlignment(Pos.CENTER);
	    goFirstHbox.setPadding(new Insets(10));
	    goFirstHbox.setSpacing(5);
	    
	    VBox box = new VBox(difficultyHbox, goFirstHbox, playGame);
	    box.setAlignment(Pos.CENTER);
	    
	    Scene scene = new Scene(box, 595, 150);
	    stage.setTitle("Pre-Game Options");
	    stage.setScene(scene);
	    stage.show();
	    
	    playGame.setOnAction((e)->
	    {
	    	if (impossible.isSelected()) {
	    		normalDifficulty = false;
            }
            if (compFirst.isSelected()) {
            	playerStarts = false;
            	humanPlayer = "o";
            	compPlayer = "x";  //Traditional rules are that player "x" goes first.
            }
	    	startGame(stage);
	    });
	}
	
	@Override
	public void start(Stage primaryStage)
	{
	    preGameOptions(primaryStage);
	}
	
	/**
	   * This is the main method that launches game.
	   * @param args 
	   */
	public static void main(String[] args)
	
	{
	    launch(args); // Launch the application.
	}
}