package dao;

import database.DatabaseConnector;
import model.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WatchlistDAO {

    public void addToWatchlist(int movieId) {
        String sql = "INSERT INTO watchlist (movie_id) VALUES (?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFromWatchlist(int movieId) {
        String sql = "DELETE FROM watchlist WHERE movie_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAllFromWatchlist() {
        String sql = "DELETE FROM watchlist";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isInWatchlist(int movieId) {
        String sql = "SELECT COUNT(*) FROM watchlist WHERE movie_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Movie> getWatchlistMovies() {
        List<Movie> watchlist = new ArrayList<>();
        String sql = """
            SELECT m.* FROM movies m
            JOIN watchlist w ON m.id = w.movie_id
        """;
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("cast"),
                        rs.getString("duration"),
                        rs.getDouble("imdb_rating"),
                        rs.getString("summary"),
                        rs.getString("coverImage"),
                        rs.getString("detailImage1"),
                        rs.getString("detailImage2")
                );
                watchlist.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchlist;
    }
}