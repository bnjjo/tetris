# tetris
<p align="center">
  <img width="342" height="524" alt="tetris in the terminal" src="https://github.com/user-attachments/assets/94fe5349-5389-4689-99ee-fa89665b7fde" />
</p>

## overview
tetris, but in the terminal.
made using the jline tui library.

## controls
w - rotate<br>
a, d - move piece left/right<br>
s - fast drop<br>
q/ctrl+c/ctrl+d - quit

## building from source
### requirements
* JDK that supports Java 25
* Maven
* (optionally) GNU Make
### steps
```bash
git clone git@github.com:bnjjo/tetris.git
cd tetris
make run
```
or if you don't have make...
```bash
git clone git@github.com:bnjjo/tetris.git
cd tetris
mvn compile && mvn exec:java -Dexec.mainClass="tetris.main.App"
```

## license
MIT
