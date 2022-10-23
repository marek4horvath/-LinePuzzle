package sk.tuke.kpi.kp.linepuzzle.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.linepuzzle.entity.Score;
import sk.tuke.kpi.kp.linepuzzle.service.ScoreException;
import sk.tuke.kpi.kp.linepuzzle.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {
    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) throws ScoreException {
        return scoreService.getTopScores(game);
    }

    @PostMapping
    public void addScore(@RequestBody Score score) throws ScoreException {
        scoreService.addScore(score);
    }


}
