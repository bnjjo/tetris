package tetris.game;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Attributes.ControlChar;
import org.jline.terminal.Attributes.InputFlag;
import org.jline.terminal.Attributes.LocalFlag;
import org.jline.utils.InfoCmp.Capability;

import tetris.util.InputHandler;

public class Game {
	private int debugTickCount = 0;

	private GameGrid grid;
	private Terminal terminal;

	private final Random rng;
	private final Attributes originalAttrs;

	Piece currentPiece;

	public Game() throws IOException {
		grid = new GameGrid();
		terminal = TerminalBuilder.builder().build();
		originalAttrs = terminal.getAttributes();
		rng = new Random();

		currentPiece = new Piece(rng.nextInt(Piece.Shape.values().length));
	}

	public void initGame() throws IOException {
		prepareTerminal();
		initGameLoop();
	}

	private void prepareTerminal() throws IOException {
		// raw mode attributes
		Attributes raw = new Attributes(originalAttrs);
		raw.setLocalFlag(LocalFlag.ICANON, false); // Disable canonical mode
		raw.setLocalFlag(LocalFlag.ECHO,   false); // Disable echo
		raw.setLocalFlag(LocalFlag.ISIG,   false); // Disable signals
		raw.setLocalFlag(LocalFlag.IEXTEN, false); // Disable extended functions
		raw.setInputFlag(InputFlag.IXON,   false); // Disable xon
		raw.setInputFlag(InputFlag.ICRNL,  false); // Disable cr/nl
		raw.setInputFlag(InputFlag.INLCR,  false); // Disable nl/cr
		raw.setControlChar(ControlChar.VTIME,  0);
		raw.setControlChar(ControlChar.VMIN,   1);

		// enter raw mode
		terminal.setAttributes(raw);

		// clear screen
		terminal.puts(Capability.cursor_invisible);
		clearScreen();
	}

	// poll for input and run game loop asynchronously
	private void initGameLoop() throws IOException {
		AtomicBoolean running = new AtomicBoolean(true);

		// spawns new thread for polling
		InputHandler inputH = new InputHandler(terminal, running);

		// 1 tick per second 
		double baseTickRate = 1_000_000_000;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		// cap at 60fps
		final long FPS = 1000 / 60; // 1,000ms/60

		try {
			draw();

			update();

			while (running.get()) {
				if (inputH.exited) running.set(false);

				if (inputH.leftPressed) {
					inputH.keyUp();
					currentPiece.moveLeft();

					if (!grid.isCollidingWithWall(currentPiece)) {
						grid.updateGrid(currentPiece);

						draw();
					} else {
						currentPiece.moveRight();
						grid.updateGrid(currentPiece);

						draw();
					}
				}

				if (inputH.rightPressed) {
					inputH.keyUp();
					currentPiece.moveRight();

					if (!grid.isCollidingWithWall(currentPiece)) {
						grid.updateGrid(currentPiece);

						draw();
					} else {
						currentPiece.moveLeft();
						grid.updateGrid(currentPiece);

						draw();
					}
				}

				if (currentPiece.landed) dropNewPiece();

				currentTime = System.nanoTime();
				// speeds up to 10 ticks per second/100ms pause after each tick
				double tickRate = (inputH.downPressed ? 100_000_000 : baseTickRate);
				delta += (currentTime - lastTime) / tickRate;
				lastTime = currentTime;

				if (delta >= 1) {
					draw();

					update();

					--delta;
				}

				try {
					TimeUnit.MILLISECONDS.sleep(FPS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
		} finally {
			running.set(false);
			inputH.killThread();
		}

		restoreTerminal();
	}

	public void draw() {
		clearScreen();

		terminal.writer().println("tick: " + debugTickCount);
		terminal.writer().flush();
		++debugTickCount;

		grid.drawGrid(terminal);
	}

	public void update() {
		grid.updateGrid(currentPiece);
		currentPiece.fall();

		if (!grid.isCollidingWithWall(currentPiece)) {
			grid.updateGrid(currentPiece);
		} else {
			currentPiece.landed = true;
			--currentPiece.currentRow;
			grid.updateGrid(currentPiece);
		}
	}
	
	public void dropNewPiece() {
		grid.saveGrid();
		currentPiece = new Piece(rng.nextInt(Piece.Shape.values().length));
	}

	private void clearScreen() {
		terminal.puts(Capability.clear_screen);
		terminal.flush();
	}

	private void restoreTerminal() throws IOException {
		// restore and flush
		terminal.puts(Capability.cursor_visible);
		terminal.setAttributes(originalAttrs);
		terminal.writer().flush();

		terminal.close();
	}
}
