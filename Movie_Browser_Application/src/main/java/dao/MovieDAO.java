package dao;

import database.DatabaseConnector;
import model.CastMember;
import model.Movie;

import java.sql.*;
import java.util.*;

public class MovieDAO {

    public List<CastMember> getCastByMovieId(int movieId) {
        List<CastMember> castList = new ArrayList<>();
        String sql = "SELECT * FROM movie_cast WHERE movie_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CastMember cast = new CastMember(
                        rs.getInt("id"),
                        rs.getInt("movie_id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getString("image")
                );
                castList.add(cast);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return castList;
    }

    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
                movieList.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return movieList;
    }
}
