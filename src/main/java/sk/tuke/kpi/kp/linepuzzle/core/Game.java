package sk.tuke.kpi.kp.linepuzzle.core;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.kpi.kp.linepuzzle.entity.Comment;
import sk.tuke.kpi.kp.linepuzzle.entity.Rating;
import sk.tuke.kpi.kp.linepuzzle.entity.Score;
import sk.tuke.kpi.kp.linepuzzle.service.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public abstract class Game {
    protected static final String GAME_NAME = "Line puzzle";
    private Board board;
    private Player player;
    private GameState state;
    private Long startedTime;
    private Long endedTime;
    //private ScoreService scoreService = new ScoreServiceJDBC();
   // private CommentService commentService = new CommentServiceJDBC();
   // private RatingService ratingService = new RatingServiceJDBC();
    @Autowired
    private ScoreService scoreService ;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService ;

    private final LinkedList<Level> levels;
    protected Level currentLevel;

    protected Game(LinkedList<Level> levels) {
        this.levels = levels;
    }

    protected void setPlaying() {
        state = GameState.PLAYING;
    }

    public void play() {
        currentLevel = levels.pollFirst();
        if (this.player != null) {
            this.player = new Player(this.player.getName(), new Board(currentLevel.getSizeX(), currentLevel.getSizeY(), currentLevel.getPlayerGraph()));
        } else {
            this.player = createNewPlayer(new Board(currentLevel.getSizeX(), currentLevel.getSizeY(), currentLevel.getPlayerGraph()));
        }
        this.board = new Board(currentLevel.getSizeX(), currentLevel.getSizeY(), currentLevel.getGameGraph());

        startedTime = System.currentTimeMillis();
        state = GameState.PLAYING;
        do {
            if (exitGame() == GameState.TOGIVEUP) {
                System.exit(0);
            }
            render();
            if (isWin() && !isProblem()) {
                endedTime = System.currentTimeMillis();
                state = GameState.DONE;
            }

        } while (state == GameState.PLAYING);
        if (isWin()) {
            Long time = endedTime - startedTime;
            long timeSecund = TimeUnit.MILLISECONDS.toSeconds(time);
            double a = timeSecund - currentLevel.getDifficulty();
            long diff = (long) a;
            int points = getPoints(diff);
            writeScore(points);
            renderDone(diff, points);
        }

        if (!levels.isEmpty() && state == GameState.PLAYING) {
            play();
        } else if (levels.isEmpty() && state == GameState.PLAYING) {
            renderDone(null, null);
        }
    }

    private boolean isWin() {
        final Graph playerGraph = player.getBoard().getGraph();
        final Graph gameGraph = board.getGraph();
        return gameGraph.isEquals(playerGraph);
    }

    public void writeComment(String comment) {
        commentService.addComment(new Comment(this.player.getName(), GAME_NAME, comment, new Date()));
    }

    public void writeScore(int points) {
        scoreService.addScore(new Score(GAME_NAME, this.player.getName(), points, new Date()));
    }

    public void writeRating(int rating) {
        ratingService.setRating(new Rating(GAME_NAME, this.player.getName(), rating, new Date()));
    }


    private int getPoints(Long timeSecund) {
        if (timeSecund <= 10) {
            return 30;
        } else if (timeSecund <= 20 && timeSecund >= 10) {
            return 20;
        } else if (timeSecund <= 30 && timeSecund >= 20) {
            return 10;
        } else {
            return 0;
        }
    }

    protected abstract Player createNewPlayer(Board board);

    public abstract void render();

    public abstract void renderDone(Long time, Integer points);

    public abstract GameState exitGame();

    public abstract boolean isProblem();

    public abstract void printTopScores();

    public abstract void printComment();


    protected Board getBoard() {
        return board;
    }

    protected Player getPlayer() {
        return player;
    }
}
