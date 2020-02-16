import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class View extends GridPane {
	private final Image BLANK = new Image("file:minesweeper_images/blank.gif");
	
	private int numRows = 10;
	private int numCols = 10;
	private double tileSize;
	
	public View() {
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				ImageView iv = new ImageView();
				iv.setImage(BLANK);
				GridPane.setConstraints(iv, r, c);
				this.getChildren().add(iv);
			}
		}
	}
	
	public View(int rows, int cols) {
		numRows = rows;
		numCols = cols;
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				ImageView iv = new ImageView();
				iv.setImage(BLANK);
				GridPane.setConstraints(iv, r, c);
				this.getChildren().add(iv);
			}
		}
	}
	
	public void changeImage(int row, int col, Image newImg) {
		this.add(new ImageView(newImg), col, row);
	}
	
	public double xPosForCol(int col) {
		tileSize = this.getWidth() / numCols;
		return col * tileSize;
	}
	
	public double yPosForRow(int row) {
		tileSize = this.getWidth() / numRows;
		return row * tileSize;
	}
	
	public int colForXPos(double x) {
		tileSize = this.getWidth() / numCols;
		return (int)(x / tileSize);
	}
	
	public int rowForYPos(double y) {
		tileSize = this.getWidth() / numRows;
		return (int)(y / tileSize);
	}
}
