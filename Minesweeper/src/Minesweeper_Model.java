/* Kristine McLaughlin, Period 6, 4/17/19
 * Because my original submission was very broken, I fixed a lot of things in this lab, the biggest being
 * getting the recursive revealing cells method to work and adding the 
 * print out of bottom layer of the board. Seeing the bottom layer of the 
 * board definitely helped me in fixing my code because I could see if my
 * methods were working and it also helped me in fixing my reveal neighbors
 * method. However, I mostly fixed my reveal neighbors method by playing
 * the actual minesweeper game to fully understand how the logic worked
 * and what the base case was. I also added an extra feature of putting a
 * question mark option on the cells because I knew I had to do that in
 * the GUI version.
 */

import java.util.Random;

public class Minesweeper_Model implements MSModelInterface {
	private Tile[][] tiles;
	private int numMinesLeft;
	private int totalMines;
	
	public void setGame(int boardRows, int boardCols, int numMines) {
		totalMines = numMines;
		tiles = new Tile[boardRows][boardCols];
		for(int r = 0; r < boardRows; r++) {
			for(int c = 0; c < boardCols; c++) {
				tiles[r][c] = new Tile(false, r, c, boardRows, boardCols);
			}
		}
		numMinesLeft = numMines;
	}
	
	public boolean hasQuestion(int row, int col) {
		return tiles[row][col].getIsQuestion();
	}
	
	public int getNumNeighborMines(int row, int col) {
		return tiles[row][col].getNumNeighborMines();
	}
	
	// set the mines to be anywhere except for the first move cell
	@Override
	public void setGridMines(int firstMoveR, int firstMoveC) {
		for(int i = 0; i < numMinesLeft; i++) {
			int row, col;
			do {
				Random rand = new Random();
				row = rand.nextInt(tiles.length);
				col = rand.nextInt(tiles[0].length);
			} while(row == firstMoveR && col == firstMoveC || tiles[row][col].getHasMine());
			tiles[row][col].setHasMine(true);
		}
	}

	@Override
	public boolean isRevealed(int row, int col) {
		return tiles[row][col].getIsRevealed();
	}

	@Override
	public boolean isFlagged(int row, int col) {
		return tiles[row][col].getIsFlagged();
	}

	@Override
	public boolean minePresent(int row, int col) {
		return tiles[row][col].getHasMine();
	}

	// recursive method to reveal neighbors when the cell at (row, col) is clicked
	@Override
	public void revealNeighbors(int row, int col) {
		if(!tiles[row][col].getIsRevealed()) tiles[row][col].setIsRevealed(true);
		int neighborMines = tiles[row][col].getNumNeighborMines();
		// base case
		if(neighborMines >= 1) {
			// do nothing
		} else {
			// starting top middle of surrounding squares and going clockwise
			if(isInBounds(row-1, col) && !tiles[row-1][col].getIsRevealed()) {
				tiles[row-1][col].setIsRevealed(true);
				revealNeighbors(row-1, col);
			}
			if(isInBounds(row-1, col+1) && !tiles[row-1][col+1].getIsRevealed()) {
				tiles[row-1][col+1].setIsRevealed(true);
				revealNeighbors(row-1, col+1);
			}
			if(isInBounds(row, col+1) && !tiles[row][col+1].getIsRevealed()) {
				tiles[row][col+1].setIsRevealed(true);
				revealNeighbors(row, col+1);
			}
			if(isInBounds(row+1, col+1) && !tiles[row+1][col+1].getIsRevealed()) {
				tiles[row+1][col+1].setIsRevealed(true);
				revealNeighbors(row+1, col+1);
			}
			if(isInBounds(row+1, col) && !tiles[row+1][col].getIsRevealed()) {
				tiles[row+1][col].setIsRevealed(true);
				revealNeighbors(row+1, col);
			}
			if(isInBounds(row+1, col-1) && !tiles[row+1][col-1].getIsRevealed()) {
				tiles[row+1][col-1].setIsRevealed(true);
				revealNeighbors(row+1, col-1);
			}
			if(isInBounds(row, col-1) && !tiles[row][col-1].getIsRevealed()) {
				tiles[row][col-1].setIsRevealed(true);
				revealNeighbors(row, col-1);
			}
			if(isInBounds(row-1, col-1) && !tiles[row-1][col-1].getIsRevealed()) {
				tiles[row-1][col-1].setIsRevealed(true);
				revealNeighbors(row-1, col-1);
			}
		}
	}
	
	private boolean isInBounds(int r, int c) {
		return (r >= 0 && r < getNumRows() && c >= 0 && c < getNumCols());
	}

	@Override
	public int getNumMinesLeft() {
		return numMinesLeft;
	}
	
	public int getTotalMines() {
		return totalMines;
	}

	@Override
	public boolean gameOver() {
		for(int row = 0; row < tiles.length; row++) {
			for(int col = 0; col < tiles[0].length; col++) {
				if(tiles[row][col].getHasMine() && tiles[row][col].getIsRevealed()) return true;
			}
		}
		return false;

	}
	
	public int getNumRows() {
		return tiles.length;
	}
	
	public int getNumCols() {
		return tiles[0].length;
	}

	@Override
	public boolean gameWon() {
		for(int row = 0; row < tiles.length; row++) {
			for(int col = 0; col < tiles[0].length; col++) {
				// if any spaces without mines are still hidden, you don't win
				if(!tiles[row][col].getHasMine() && !tiles[row][col].getIsRevealed()) return false;
			}
		}
		return true;
	}

	@Override
	public void setFlag(int row, int col) {
		// cycles through blank, flag, question
		if(tiles[row][col].getIsQuestion()) {
			tiles[row][col].setIsQuestion(false);
		} else if(!tiles[row][col].getIsFlagged()) {
			tiles[row][col].setIsFlagged(true);
			numMinesLeft--;
		} else if(tiles[row][col].getIsFlagged()) {
			tiles[row][col].setIsFlagged(false);
			tiles[row][col].setIsQuestion(true);
		} 
	}

	public void setRevealed(int row, int col, boolean revealed) {
		tiles[row][col].setIsRevealed(revealed);
	}
	
	public void setQuestion(int row, int col) {
		if(tiles[row][col].getIsQuestion()) {
			tiles[row][col].setIsQuestion(false);
		} else {
			tiles[row][col].setIsQuestion(true);
		}
	}


	class Tile {
		private boolean isRevealed;
		private boolean isFlagged;
		private boolean hasMine;
		private boolean isQuestion;
		private int row;
		private int col;
		private int boardRows;
		private int boardCols;
		
		public Tile() {
			isRevealed = true;
			isFlagged = false;
			hasMine = false;
			isQuestion = false;
			row = 0;
			col = 0;
		}
		
		public Tile(boolean isRevealed, int row, int col, int boardRows, int boardCols) {
			setIsRevealed(isRevealed);
			hasMine = false;
			this.row = row;
			this.col = col;
			this.boardRows = boardRows;
			this.boardCols = boardCols;
		}
		
		public void setIsRevealed(boolean revealed) {
			isRevealed = revealed;
			if(revealed) isFlagged = false;
		}
		
		public boolean getIsRevealed() {
			return isRevealed;
		}
		
		public void setIsFlagged(boolean flagged) {
			isFlagged = flagged;
		}
		
		public boolean getIsFlagged() {
			return isFlagged;
		}
		
		public void setHasMine(boolean mine) {
			hasMine = mine;
		}
		
		public boolean getHasMine() {
			return hasMine;
		}
		
		public void setIsQuestion(boolean question) {
			isQuestion = question;
		}
		
		public boolean getIsQuestion() {
			return isQuestion;
		}
		
		private int getNumNeighborMines() {
			int total = 0;
			
			// start at middle top
			if(isInBounds(row-1, col)) {
				if(tiles[row-1][col].getHasMine()) total++;
			}
			if(isInBounds(row-1, col+1)) {
				if(tiles[row-1][col+1].getHasMine()) total++;
			}
			if(isInBounds(row, col+1)) {
				if(tiles[row][col+1].getHasMine()) total++;
			}
			if(isInBounds(row+1, col+1)) {
				if(tiles[row+1][col+1].getHasMine()) total++;
			}
			if(isInBounds(row+1, col)) {
				if(tiles[row+1][col].getHasMine()) total++;
			}
			if(isInBounds(row+1, col-1)) {
				if(tiles[row+1][col-1].getHasMine()) total++;
			}
			if(isInBounds(row, col-1)) {
				if(tiles[row][col-1].getHasMine()) total++;
			}
			if(isInBounds(row-1, col-1)) {
				if(tiles[row-1][col-1].getHasMine()) total++;
			}
			
			return total;
		}
		
		private boolean isInBounds(int row, int col) {
			if(row >= 0 && row < boardRows && col >= 0 && col < boardCols) {
				return true;
			} else return false;
		}
		
	}

}
