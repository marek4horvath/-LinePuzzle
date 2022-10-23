package sk.tuke.kpi.kp.linepuzzle.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import java.util.Date;

@Entity
@NamedQuery(name = "Score.selectToScores", query = "select s from Score s where s.game=:game order by s.points desc")
@NamedQuery(name = "Score.resetScores",query = "DELETE FROM Score")

public class Score{
    @Id
    @GeneratedValue
    private int ident;

    private String game;

    private String player;

    private int points;

    private Date playedon;

    public Score() {
    }

    public Score(String game, String player, int points, Date playedon) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedon = playedon;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedon() {
        return playedon;
    }

    public void setPlayedAt(Date playedon) {
        this.playedon = playedon;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "Score{" +
                "ident=" + ident +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", points=" + points +
                ", playedon=" + playedon +
                '}';
    }

}
