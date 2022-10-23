package sk.tuke.kpi.kp.linepuzzle.service;


import sk.tuke.kpi.kp.linepuzzle.entity.Score;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Transactional
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        entityManager.persist(score);
    }

    @Override
    public List getTopScores(String game) throws ScoreException {
        return entityManager.createNamedQuery("Score.selectToScores")
                .setParameter("game", game).setMaxResults(10).getResultList();

    }

    @Override
    public void reset() {
         entityManager.createNamedQuery("Score.resetScores").executeUpdate();
    }
}