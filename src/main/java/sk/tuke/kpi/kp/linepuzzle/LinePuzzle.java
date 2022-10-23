package sk.tuke.kpi.kp.linepuzzle;

import sk.tuke.kpi.kp.linepuzzle.consoleui.ConsoleGame;
import sk.tuke.kpi.kp.linepuzzle.core.*;


public class LinePuzzle {
    public static void main(String[] args) {
        ConsoleGame consoleGame = new ConsoleGame(new LibraryLevels());
        consoleGame.play();
    }
}
