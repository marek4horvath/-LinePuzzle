package sk.tuke.kpi.kp.linepuzzle.service;

import org.springframework.stereotype.Component;
import sk.tuke.kpi.kp.linepuzzle.entity.Score;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

@Component

public class ScoreServiceJDBC implements ScoreService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "Marek123";
    public static final String SELECT = "SELECT game, player, points, playedon FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";
    public static final String DELETE = "DELETE FROM score";
    public static final String INSERT = "INSERT INTO score (game, player, points, playedon) VALUES (?, ?, ?, ?)";


    @Override
    public void addScore(Score score) throws ScoreException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, score.getGame());
            statement.setString(2, score.getPlayer());
            statement.setInt(3, score.getPoints());
            statement.setTimestamp(4, new Timestamp(score.getPlayedon().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting score", e);
        }

    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT);

        ) {
            statement.setString(1, game);
            try (ResultSet set = statement.executeQuery()) {
                List<Score> scores = new ArrayList<>();
                while (set.next()) {
                    scores.add(new Score(set.getString(1), set.getString(2), set.getInt(3), set.getTimestamp(4)));
                }
                return scores;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    @Override
    public void reset() throws ScoreException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting score", e);
        }

    }
}
