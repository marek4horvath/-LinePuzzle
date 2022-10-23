package sk.tuke.kpi.kp.linepuzzle.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.linepuzzle.entity.Rating;
import sk.tuke.kpi.kp.linepuzzle.service.RatingService;


@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {
    @Autowired
    private RatingService ratingService;


    @GetMapping("/{game}")
    public double getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }

   @GetMapping("/{game}/{player}")
    public int getCountRating(@PathVariable String game, @PathVariable String player) {
        return ratingService.getRating(game, player);
    }

    @PostMapping
    public void setRating(@RequestBody Rating rating) {
        ratingService.setRating(rating);
    }
}
