package tetris.game;

public class Piece {
	byte rotation;
	byte currentRow, currentCol;
	byte[][] gridShape;

	boolean landed;

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
		
		switch (shape) {
			case BOX -> gridShape = new byte[][] {
				{0, 1, 1, 0},
				{0, 1, 1, 0},
				{0, 0, 0, 0},
				{0, 0, 0, 0}
			};
			case BAR -> gridShape = new byte[][] {
				{0, 0, 1, 0},
				{0, 0, 1, 0},
				{0, 0, 1, 0},
				{0, 0, 1, 0},
			};
			case T -> gridShape = new byte[][] {
				{0, 1, 1, 1},
				{0, 0, 1, 0},
				{0, 0, 0, 0},
				{0, 0, 0, 0}
			};
			case L -> gridShape = new byte[][] {
				{0, 0, 1, 0},
				{0, 0, 1, 0},
				{0, 0, 1, 1},
				{0, 0, 0, 0}
			};
			case J -> gridShape = new byte[][] {
				{0, 0, 1, 0},
				{0, 0, 1, 0},
				{0, 1, 1, 0},
				{0, 0, 0, 0}
			};
			case S -> gridShape = new byte[][] {
				{0, 0, 1, 1},
				{0, 1, 1, 0},
				{0, 0, 0, 0},
				{0, 0, 0, 0}
			};
			case Z -> gridShape = new byte[][] {
				{0, 1, 1, 0},
				{0, 0, 1, 1},
				{0, 0, 0, 0},
				{0, 0, 0, 0}
			};
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
