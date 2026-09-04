package tetris.game;

import org.jline.terminal.Terminal;

public class GameGrid {
	public boolean gameOver = false;

	private int[][] baseGrid = new int[][] {
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	};
	private int[][] currentGrid;

	private static final byte START_ROW = 1;
	private static final byte START_COL = 3;
	private static final byte ROWS = 20;
	private static final byte COLS = 10;
	private static final byte TRUE_ROWS = 23;
	private static final byte TRUE_COLS = 15;

	public GameGrid() {
		currentGrid = new int[TRUE_ROWS][TRUE_COLS];
		resetGrid();
	}

	public void drawGrid(Terminal terminal) {
		for (int i = START_ROW; i < ROWS + START_ROW; ++i) {
			for (int j = START_COL; j < COLS + START_COL; ++j) {
				if (currentGrid[i][j] == 1) terminal.writer().printf("███", currentGrid[i][j]);
				else terminal.writer().printf("░░░", currentGrid[i][j]);
			}
			terminal.writer().println();
		}
		terminal.writer().flush();
	}

	public void updateGrid(Piece p) {
		resetGrid();

		for (int i = p.currentRow; i < p.currentRow + 4; ++i) {
			for (int j = p.currentCol; j < p.currentCol + 4; ++j) {
				currentGrid[i][j] = p.gridShape[i - p.currentRow][j - p.currentCol] | currentGrid[i][j];
			}
		}
	}

	public boolean isCollidingWithWall(Piece p) {
		resetGrid();

		for (int i = p.currentRow; i < p.currentRow + 4; ++i) {
			for (int j = p.currentCol; j < p.currentCol + 4; ++j) {
				if (p.gridShape[i - p.currentRow][j - p.currentCol] + currentGrid[i][j] > 1) {
					return true;
				}
			}
		}

		return false;
	}

	public void checkForBarClears() {
		int towerHeight = 0;

		for (int i = ROWS;; --i) {
			if (i < 0) {
				gameOver = true;
				return;
			}

			int sum = 0;
			boolean clearedBarPresent = true;

			for (int j = START_COL; j < COLS + START_COL; ++j) {
				sum += currentGrid[i][j];
				if (currentGrid[i][j] < 1) clearedBarPresent = false;
			}

			if (sum == 0) { towerHeight = ROWS - i; break; }
			if (clearedBarPresent) {
				for (int k = i; k > START_ROW; --k) {
					for (int l = START_COL; l < COLS + START_COL; ++l) {
						currentGrid[k][l] = currentGrid[k - 1][l];
						currentGrid[k - 1][l] = 0;
					}
				}

				i = ROWS + 1;
			}
		}

		saveGrid();

		System.out.println(towerHeight);
	}

	private void resetGrid() {
		for (int i = 0; i < TRUE_ROWS; ++i) {
			for (int j = 0; j < TRUE_COLS; ++j) {
				currentGrid[i][j] = baseGrid[i][j];
			}
		}
	}

	public void saveGrid() {
		for (int i = 0; i < TRUE_ROWS; ++i) {
			for (int j = 0; j < TRUE_COLS; ++j) {
				baseGrid[i][j] = currentGrid[i][j];
			}
		}
	}
}

