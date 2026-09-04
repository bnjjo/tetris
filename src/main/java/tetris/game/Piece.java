package tetris.game;

public class Piece {
	byte rotation;
	byte maxRotation;
	byte currentRow, currentCol;
	byte[][] gridShape;

	boolean landed;

	public static final byte GRID_SIZE = 4;

	Shape shape;

	enum Shape {
		BOX,
		BAR,
		T,
		L,
		J,
		S,
		Z,
	}

	public Piece(int shapeIdx) {
		rotation = 0;
		currentRow = 0;
		currentCol = 5;
		landed = false;
		shape = switch (shapeIdx) {
			case 0  -> Shape.BOX;
			case 1  -> Shape.BAR;
			case 2  -> Shape.T;
			case 3  -> Shape.L;
			case 4  -> Shape.J;
			case 5  -> Shape.S;
			case 6  -> Shape.Z;
			default -> Shape.T;
		};
		maxRotation = switch (shape) {
			case BAR, S, Z -> 1;
			default -> 3;
		};
		
		switch (shape) {
			case BOX -> gridShape = new byte[][] {
				{0, 2, 2, 0},
				{0, 2, 2, 0},
				{0, 0, 0, 0},
				{0, 0, 0, 0}
			};
			case BAR -> gridShape = new byte[][] {
				{0, 0, 3, 0},
				{0, 0, 3, 0},
				{0, 0, 3, 0},
				{0, 0, 3, 0},
			};
			case T -> gridShape = new byte[][] {
				{0, 0, 4, 0},
				{0, 4, 4, 4},
				{0, 0, 0, 0},
				{0, 0, 0, 0}
			};
			case L -> gridShape = new byte[][] {
				{0, 0, 5, 0},
				{0, 0, 5, 0},
				{0, 0, 5, 5},
				{0, 0, 0, 0}
			};
			case J -> gridShape = new byte[][] {
				{0, 0, 6, 0},
				{0, 0, 6, 0},
				{0, 6, 6, 0},
				{0, 0, 0, 0}
			};
			case S -> gridShape = new byte[][] {
				{0, 7, 0, 0},
				{0, 7, 7, 0},
				{0, 0, 7, 0},
				{0, 0, 0, 0}
			};
			case Z -> gridShape = new byte[][] {
				{0, 0, 0, 8},
				{0, 0, 8, 8},
				{0, 0, 8, 0},
				{0, 0, 0, 0}
			};
		}
	}

	public void rotate(boolean reverse) {
		if (shape == Shape.BOX) return;

		if (!reverse) ++rotation;
		else --rotation;

		if (rotation > maxRotation) rotation = 0;

		if (shape == Shape.BAR) {
			switch (rotation) {
				case 0 -> gridShape = new byte[][] {
					{0, 0, 3, 0},
					{0, 0, 3, 0},
					{0, 0, 3, 0},
					{0, 0, 3, 0}
				};
				case 1 -> gridShape = new byte[][] {
					{0, 0, 0, 0},
					{0, 0, 0, 0},
					{3, 3, 3, 3},
					{0, 0, 0, 0}
				};
			}
		} else if (shape == Shape.T) {
			switch (rotation) {
				case 0 -> gridShape = new byte[][] {
					{0, 0, 4, 0},
					{0, 4, 4, 4},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				};
				case 1 -> gridShape = new byte[][] {
					{0, 0, 4, 0},
					{0, 0, 4, 4},
					{0, 0, 4, 0},
					{0, 0, 0, 0}
				};
				case 2 -> gridShape = new byte[][] {
					{0, 0, 0, 0},
					{0, 4, 4, 4},
					{0, 0, 4, 0},
					{0, 0, 0, 0}
				};
				case 3 -> gridShape = new byte[][] {
					{0, 0, 4, 0},
					{0, 4, 4, 0},
					{0, 0, 4, 0},
					{0, 0, 0, 0}
				};
			}
		} else if (shape == Shape.L) {
			switch (rotation) {
				case 0 -> gridShape = new byte[][] {
					{0, 0, 5, 0},
					{0, 0, 5, 0},
					{0, 0, 5, 5},
					{0, 0, 0, 0}
				};
				case 1 -> gridShape = new byte[][] {
					{0, 0, 0, 0},
					{0, 5, 5, 5},
					{0, 5, 0, 0},
					{0, 0, 0, 0}
				};
				case 2 -> gridShape = new byte[][] {
					{0, 5, 5, 0},
					{0, 0, 5, 0},
					{0, 0, 5, 0},
					{0, 0, 0, 0}
				};
				case 3 -> gridShape = new byte[][] {
					{0, 0, 0, 5},
					{0, 5, 5, 5},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				};
			}
		} else if (shape == Shape.J) {
			switch (rotation) {
				case 0 -> gridShape = new byte[][] {
					{0, 0, 6, 0},
					{0, 0, 6, 0},
					{0, 6, 6, 0},
					{0, 0, 0, 0}
				};
				case 1 -> gridShape = new byte[][] {
					{0, 6, 0, 0},
					{0, 6, 6, 6},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				};
				case 2 -> gridShape = new byte[][] {
					{0, 0, 6, 6},
					{0, 0, 6, 0},
					{0, 0, 6, 0},
					{0, 0, 0, 0}
				};
				case 3 -> gridShape = new byte[][] {
					{0, 0, 0, 0},
					{0, 6, 6, 6},
					{0, 0, 0, 6},
					{0, 0, 0, 0}
				};
			}
		} else if (shape == Shape.S) {
			switch (rotation) {
				case 0 -> gridShape = new byte[][] {
					{0, 7, 0, 0},
					{0, 7, 7, 0},
					{0, 0, 7, 0},
					{0, 0, 0, 0}
				};
				case 1 -> gridShape = new byte[][] {
					{0, 0, 0, 0},
					{0, 0, 7, 7},
					{0, 7, 7, 0},
					{0, 0, 0, 0}
				};
			}
		} else if (shape == Shape.Z) {
			switch (rotation) {
				case 0 -> gridShape = new byte[][] {
					{0, 0, 0, 8},
					{0, 0, 8, 8},
					{0, 0, 8, 0},
					{0, 0, 0, 0}
				};
				case 1 -> gridShape = new byte[][] {
					{0, 0, 0, 0},
					{0, 8, 8, 0},
					{0, 0, 8, 8},
					{0, 0, 0, 0}
				};
			}
		}
	}

	public void fall() {
		if (landed) return;
		else ++currentRow;
	}
	
	public void moveLeft() {
		--currentCol;
	}

	public void moveRight() {
		++currentCol;
	}
}
