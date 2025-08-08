package com.example.movie_browser_application;

import dao.MovieDAO;
import dao.WatchlistDAO;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.CastMember;
import model.Movie;

import java.util.List;


public class MovieDetailController {

    @FXML
    private ImageView coverImageView;

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Text summaryLabel;

    @FXML
    private Button backButton;

    @FXML
    private Label durationLabel;

    @FXML
    private Label ratingLabel;

    private Parent previousRoot;


    @FXML
    private VBox castVbox;

    private final MovieDAO movieDAO = new MovieDAO();



    private List<Movie> movieList;
    private int currentIndex;

    @FXML
    private Button nextMovieButton;

    @FXML
    private Button prevMovieButton;

    @FXML
    private Label nextMovieLabel;

    @FXML
    private Label prevMovieLabel;

    private final WatchlistDAO watchlistDAO = new WatchlistDAO();
    private Movie currentMovie;

    @FXML
    private Button watchlistButton;

    @FXML
    private void handleWatchlistButton() {
        if (watchlistDAO.isInWatchlist(currentMovie.getId())) {
            watchlistDAO.removeFromWatchlist(currentMovie.getId());
            watchlistButton.setText("Add to Watchlist");
        } else {
            watchlistDAO.addToWatchlist(currentMovie.getId());
            watchlistButton.setText("Remove from Watchlist");
        }

        Scene scene = backButton.getScene();
        HelloController controller = (HelloController) scene.getUserData();
        controller.refreshWatchlistMovies();
    }

    public void initialize() {
        backgroundImageView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                backgroundImageFit();
            }
        });
        castVbox.setVisible(false);
    }

    private void backgroundImageFit() {
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.fitWidthProperty().bind(backgroundImageView.getScene().widthProperty());
        backgroundImageView.fitHeightProperty().bind(backgroundImageView.getScene().heightProperty());
    }


    public void watchlistChecker() {
        if (watchlistDAO.isInWatchlist(currentMovie.getId())) {
            watchlistButton.setText("Remove from Watchlist");
        } else {
            watchlistButton.setText("Add to Watchlist");
        }
    }

    @FXML
    private void handleNextMovie() {
        if (movieList != null && currentIndex < movieList.size() - 1) {
            currentIndex++;
            updateNavigationLabels();
            fadeToMovie(movieList.get(currentIndex));
        }
    }

    @FXML
    private void handlePrevMovie() {
        if (movieList != null && currentIndex > 0) {
            currentIndex--;
            updateNavigationLabels();
            fadeToMovie(movieList.get(currentIndex));
        }
    }

    public void updateNavigationLabels() {
        if (currentIndex > 0) {
            prevMovieLabel.setText(movieList.get(currentIndex - 1).getTitle());
            prevMovieButton.setVisible(true);
            prevMovieButton.setDisable(false);
            prevMovieLabel.getStyleClass().add("genre-label-background");
        } else {
            prevMovieLabel.setText("");
            prevMovieButton.setVisible(false);
            prevMovieButton.setDisable(true);
        }

        if (currentIndex < movieList.size() - 1) {
            nextMovieLabel.setText(movieList.get(currentIndex + 1).getTitle());
            nextMovieButton.setVisible(true);
            nextMovieButton.setDisable(false);
            nextMovieLabel.getStyleClass().add("genre-label-background");
        } else {
            nextMovieLabel.setText("");
            nextMovieButton.setVisible(false);
            nextMovieButton.setDisable(true);
        }
    }

    private void fadeToMovie(Movie movie) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), coverImageView.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            setMovie(movie);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), coverImageView.getScene().getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }


    public void setMovieListAndIndex(List<Movie> movieList, int currentIndex) {
        this.movieList = movieList;
        this.currentIndex = currentIndex;
        setMovie(movieList.get(currentIndex)); // Load first movie
    }

    public void setMovie(Movie movie) {
        this.currentMovie = movie;
        titleLabel.setText(movie.getTitle());
        genreLabel.setText(movie.getGenre());
        summaryLabel.setText(movie.getPlot());
        durationLabel.setText(movie.getDuration());
        ratingLabel.setText(String.valueOf(movie.getImdbRating()));
        updateNavigationLabels();
        watchlistChecker();

        // Cover image
        if (movie.getDetailImage1() != null && !movie.getDetailImage1().isEmpty()) {
            try {
                coverImageView.setImage(
                        new Image(getClass().getResourceAsStream("/" + movie.getDetailImage1()))
                );
                coverImageView.setPreserveRatio(true);

                DropShadow imageShadow = new DropShadow();
                imageShadow.setRadius(12);
                imageShadow.setOffsetX(6);
                imageShadow.setOffsetY(10);
                imageShadow.setColor(Color.rgb(0, 0, 0, 1));

                coverImageView.setEffect(imageShadow);
                coverImageView.setClip(null);

                coverImageView.setEffect(imageShadow);
            } catch (Exception e) {
                System.out.println("Can't load: " + movie.getDetailImage1());
            }
        }

        // Background image
        if (movie.getDetailImage2() != null && !movie.getDetailImage2().isEmpty()) {
            try {
                backgroundImageView.setImage(
                        new Image(getClass().getResourceAsStream("/" + movie.getDetailImage2()))
                );
                backgroundImageView.setFitHeight(600);
                backgroundImageView.setFitWidth(800);
                backgroundImageView.setPreserveRatio(true);
            } catch (Exception e) {
                System.out.println("Can't load: " + movie.getDetailImage2());
            }
        }

        List<CastMember> castList = movieDAO.getCastByMovieId(movie.getId());
        castVbox.getChildren().clear();
        for (CastMember cast : castList) {
            castVbox.getChildren().add(createCastRow(cast));
        }
    }



    private HBox createCastRow(CastMember cast) {
        Label roleLabel = new Label(cast.getRole()+ " - ");
        roleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #FFB6C1;");

        Label nameLabel = new Label(cast.getName());
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #FFDAB9;");

        HBox row = new HBox(8, roleLabel, nameLabel); // 8px space between labels
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }


    public void setPreviousRoot(Parent root) {
        this.previousRoot = root;
    }

    @FXML
    private void handleBackButton() {
        if (previousRoot != null) {
            Scene scene = backButton.getScene();

            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), scene.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                scene.setRoot(previousRoot);
                previousRoot.setOpacity(0.0);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), previousRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);

                fadeIn.setOnFinished(ev -> {
                    // Ensure focus happens after layout is complete
                    Platform.runLater(() -> {
                        HelloController controller = (HelloController) scene.getUserData();
                        controller.focusOnTilePane();
                    });
                });

                fadeIn.play();
            });

            fadeOut.play();
        }
    }

    @FXML
    private void castInfoButtonActivator() {
        if (!castVbox.isVisible()) {
            // Show with fade & slide
            castVbox.setVisible(true);
            castVbox.setManaged(true);

            castVbox.setOpacity(0);
            castVbox.setTranslateY(-20); // Start slightly above

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), castVbox);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), castVbox);
            slideIn.setFromY(-20);
            slideIn.setToY(0);

            ParallelTransition showAnim = new ParallelTransition(fadeIn, slideIn);
            showAnim.play();

        } else {
            // Hide with fade & slide up
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), castVbox);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), castVbox);
            slideOut.setFromY(0);
            slideOut.setToY(-20);

            ParallelTransition hideAnim = new ParallelTransition(fadeOut, slideOut);
            hideAnim.setOnFinished(e -> {
                castVbox.setVisible(false);
                castVbox.setManaged(false);
            });
            hideAnim.play();
        }
    }



}
