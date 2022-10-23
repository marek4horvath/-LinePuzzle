package sk.tuke.kpi.kp.linepuzzle.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQuery(name = "Rating.selectToAVGRating", query = "select avg(r.rating) from Rating r where r.game=:game")
@NamedQuery(name = "Rating.selectToRating", query = "select r from Rating r where  r.player=:player and r.game=:game")
@NamedQuery(name = "Rating.resetScores", query = "DELETE FROM Score")
public class Rating {
    @Id
    @GeneratedValue
    private int ident;
    private String game;
    private String player;
    private int rating;
    private Date ratedon;

    public Rating() {
    }

    public Rating(String game, String player, int rating, Date ratedon) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedon = ratedon;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedon() {
        return ratedon;
    }

    public void setRatedon(Date ratedon) {
        this.ratedon = ratedon;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ident=" + ident +
                ", game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", rating=" + rating +
                ", ratedon=" + ratedon +
                '}';
    }
}
