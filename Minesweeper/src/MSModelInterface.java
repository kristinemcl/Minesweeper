
public interface MSModelInterface {
	
	// distribute mines across the grid after the first move
	public void setGridMines(int firstMoveR, int firstMoveC);
	
	public boolean isRevealed(int row, int col);
	
	public boolean isFlagged(int row, int col);
	
	public void setFlag(int row, int col);
	
	public boolean minePresent(int row, int col);
	
	public void revealNeighbors(int row, int col);
	
	public int getNumMinesLeft();
	
	public boolean gameOver();
	
	public boolean gameWon();

}
