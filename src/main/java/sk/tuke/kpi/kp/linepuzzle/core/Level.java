package sk.tuke.kpi.kp.linepuzzle.core;


public class Level {
    private final int sizeY;
    private final int sizeX;
    private  final double difficulty;
    private final String levelName;
    private final Graph gameGraph;
    private final Graph playerGraph;

    public Level(int sizeY, int sizeX,double difficulty, String levelName, Graph gameGraph, Graph playerGraph) {
        this.sizeY = sizeY;
        this.sizeX = sizeX;
        this.difficulty = difficulty;
        this.levelName = levelName;
        this.gameGraph = gameGraph;
        this.playerGraph = playerGraph;
    }


    public String getLevelName() {
        return levelName;
    }

    public Graph getGameGraph() {
        return gameGraph;
    }

    public Graph getPlayerGraph() {
        return playerGraph;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public double getDifficulty() {
        return difficulty;
    }
}
