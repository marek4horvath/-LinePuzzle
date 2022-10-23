package sk.tuke.kpi.kp.linepuzzle.service;

import sk.tuke.kpi.kp.linepuzzle.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "Marek123";
    public static final String SELECT_RATING = "SELECT rating FROM rating WHERE game = ? AND player = ?;";
    public static final String SELECT_AVG = "SELECT AVG(rating) FROM rating WHERE game = ?;";
    public static final String INSERT = "INSERT INTO rating (game, player, rating, ratedon) VALUES (?, ?, ?, ?);";
    public static final String DELETE = "DELETE FROM rating";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(INSERT)) {
                ps.setString(1, rating.getGame());
                ps.setString(2, rating.getPlayer());
                ps.setInt(3, rating.getRating());
                ps.setDate(4, new Date(rating.getRatedon().getTime()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Problem insert raiting", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        int avg = 1;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_AVG)) {
                ps.setString(1, game);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        avg = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem average select raiting", e);
        }
        return avg;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        int ratings = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_RATING);
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    ratings = set.getInt(1);
                }
            }
            return ratings;
        } catch (SQLException e) {
            throw new RatingException("Problem select raiting", e);
        }

    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting raiting", e);
        }
    }
}
