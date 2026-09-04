build:
	mvn compile

run: build
	mvn exec:java -Dexec.mainClass="tetris.main.App"
