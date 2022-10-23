package sk.tuke.kpi.kp.linepuzzle.service;


import org.junit.Test;
import sk.tuke.kpi.kp.linepuzzle.entity.Score;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ScoreServiceTest {
    private final String GAME_NAME = "line puzzle";
    private Date date = new Date();

    @Test
    public void testReset() {
        ScoreService service = new ScoreServiceJDBC();
        service.reset();
        assertEquals(0, service.getTopScores(GAME_NAME).size());
    }

    @Test
    public void testAddScore() {
        ScoreService service = new ScoreServiceJDBC();
        service.reset();
        service.addScore(new Score(GAME_NAME, "Josko", 20, date));
        service.addScore(new Score(GAME_NAME, "Josko1", 10, date));
        service.addScore(new Score(GAME_NAME, "Josko2", 30, date));
        service.addScore(new Score(GAME_NAME, "Josko3", 20, date));
        service.addScore(new Score(GAME_NAME, "Josko4", 10, date));
        service.addScore(new Score(GAME_NAME, "Josko5", 30, date));

        List<Score> scores = service.getTopScores(GAME_NAME);
        assertEquals(6, scores.size());

        assertEquals(GAME_NAME, scores.get(0).getGame());
        assertEquals("Josko2", scores.get(0).getPlayer());
        assertEquals(30, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedon());

        assertEquals(GAME_NAME, scores.get(1).getGame());
        assertEquals("Josko5", scores.get(1).getPlayer());
        assertEquals(30, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedon());

        assertEquals(GAME_NAME, scores.get(2).getGame());
        assertEquals("Josko", scores.get(2).getPlayer());
        assertEquals(20, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedon());

        assertEquals(GAME_NAME, scores.get(3).getGame());
        assertEquals("Josko3", scores.get(3).getPlayer());
        assertEquals(20, scores.get(3).getPoints());
        assertEquals(date, scores.get(3).getPlayedon());

        assertEquals(GAME_NAME, scores.get(4).getGame());
        assertEquals("Josko1", scores.get(4).getPlayer());
        assertEquals(10, scores.get(4).getPoints());
        assertEquals(date, scores.get(4).getPlayedon());

        assertEquals(GAME_NAME, scores.get(5).getGame());
        assertEquals("Josko4", scores.get(5).getPlayer());
        assertEquals(10, scores.get(5).getPoints());
        assertEquals(date, scores.get(5).getPlayedon());

    }


}
