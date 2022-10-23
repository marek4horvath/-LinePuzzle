package sk.tuke.kpi.kp.linepuzzle.service;

import org.junit.Test;
import sk.tuke.kpi.kp.linepuzzle.entity.Rating;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RatingServiceTest {
    private final String GAME_NAME = "line puzzle";
    private Date date = new Date();

    @Test
    public void testReset() {
        RatingService ratingService = new RatingServiceJDBC();
        ratingService.reset();
        assertEquals(0, ratingService.getAverageRating(GAME_NAME));
        assertEquals(0, ratingService.getRating(GAME_NAME, "TEST"));
    }

    @Test
    public void testAddRating() {
        RatingService ratingService = new RatingServiceJDBC();
        ratingService.reset();

        Rating rating = new Rating(GAME_NAME, "Karol", 4, date);
        Rating rating1 = new Rating(GAME_NAME, "Mato", 3, date);
        ratingService.setRating(rating);
        ratingService.setRating(rating1);

        int avg = ratingService.getAverageRating(GAME_NAME);
        int avg1 = ratingService.getAverageRating(GAME_NAME);
        assertEquals(3, avg);
        assertEquals(3, avg1);
        int rat = ratingService.getRating(GAME_NAME, "Karol");
        int rat1 = ratingService.getRating(GAME_NAME, "Mato");
        assertEquals(4, rat);
        assertEquals(3, rat1);

        assertEquals(GAME_NAME, rating.getGame());
        assertEquals("Karol", rating.getPlayer());
        assertEquals(4, rating.getRating());
        assertEquals(date, rating.getRatedon());

        assertEquals(GAME_NAME, rating1.getGame());
        assertEquals("Mato", rating1.getPlayer());
        assertEquals(3, rating1.getRating());
        assertEquals(date, rating1.getRatedon());
    }
}
