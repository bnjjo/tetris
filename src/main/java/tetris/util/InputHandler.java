package tetris.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class InputHandler {
	public boolean upPressed, downPressed, leftPressed, rightPressed, exited;

	private ExecutorService executor;

	public InputHandler(Terminal terminal, AtomicBoolean running) {
		NonBlockingReader reader = terminal.reader();

		executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			try {
				while (running.get()) {
					int c = reader.read(100); // read every 100ms
					if (c > 0) {
						switch (c) {
							case 'w' -> upPressed    = true;
							case 's' -> downPressed  = true;
							case 'a' -> leftPressed  = true;
							case 'd' -> rightPressed = true;

							case 'q'      -> exited = true;
							case '\u0003' -> exited = true; // \u0003 - ctrl+c
							case '\u0004' -> exited = true; // \u0004 - ctrl+d
						}
					} else {
						keyUp();
					}
				}
			} catch (IOException e) {
				if (running.get()) {
					terminal.writer().println("Error reading input: " + e.getMessage());
					terminal.writer().flush();
				}
			}
		});
	}

	public void keyUp() {
		upPressed    = false;
		downPressed  = false;
		leftPressed  = false;
		rightPressed = false;
	}

	public void killThread() {
		executor.shutdownNow();
		try {
			executor.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
