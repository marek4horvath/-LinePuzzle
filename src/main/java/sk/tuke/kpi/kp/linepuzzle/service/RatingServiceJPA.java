package sk.tuke.kpi.kp.linepuzzle.service;

import sk.tuke.kpi.kp.linepuzzle.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        double avg;
        int convert;
        Object e = entityManager.createNamedQuery("Rating.selectToAVGRating").setParameter("game", game).getSingleResult();
        if (e != null) {
            avg = (double) e;
        } else {
            avg = 0;
        }
        convert = (int) avg;
        return convert;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            return ((Rating) entityManager.createNamedQuery("Rating.selectToRating").setParameter("game", game).setParameter("player", player).getSingleResult()).getRating();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetScores").executeUpdate();
    }
}
