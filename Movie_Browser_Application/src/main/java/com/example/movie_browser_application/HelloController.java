package com.example.movie_browser_application;

import dao.MovieDAO;
import dao.WatchlistDAO;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Movie;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

public class HelloController {

    private SceneType currentSceneType = SceneType.ALL_MOVIES;

    public void setCurrentSceneType(SceneType sceneType) {
        this.currentSceneType = sceneType;
    }

    public SceneType getCurrentSceneType() {
        return currentSceneType;
    }

    @FXML
    private TilePane movieTilePane;

    private final MovieDAO movieDAO = new MovieDAO();

    @FXML
    private HBox searchBox;

    @FXML
    private Button searchButton;

    @FXML
    private StackPane searchContainer;

    @FXML
    private TextField searchField;

    @FXML
    ScrollPane scrollPane;

    @FXML
    private Label noResultsLabel;

    private List<Movie> allMovies;

    private boolean searchVisible = false;
    private List<Movie> currentDisplayedMovies;

    @FXML
    private VBox sideMenu;

    @FXML
    private Button menuButton;

    @FXML
    private Button sortByNameButton;

    @FXML
    private Button sortByRatingButton;

    private boolean menuVisible = false;

    @FXML
    private VBox genresContainer;

    @FXML
    private Label titleLabel;

    @FXML
    Button clearWatchListButton;


    private boolean genresExpanded = false;
    private final WatchlistDAO watchlistDAO = new WatchlistDAO();


    public void initialize() {
        MovieDetailController movieDetailController = new MovieDetailController();
        currentSceneType = SceneType.ALL_MOVIES;
        scrollPane.setFocusTraversable(true);
        movieTilePane.setFocusTraversable(true);
        setupSearchBox();
        turnWatchListButtonOnOFF(0);
        noResultsLabel.setVisible(false);
        sideMenu.setTranslateX(200); // moves it off-screen to the right
        sideMenu.setOpacity(0);

        allMovies = movieDAO.getAllMovies();
        for (Movie movie : allMovies) {
            if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
                ImageCache.getImage(movie.getPosterUrl()); // Preload into memory
            }
        }
        currentDisplayedMovies = allMovies;
        displayMovies(allMovies);

        searchField.textProperty().addListener((obs, oldValue, newValue) -> filterMovies(newValue));
        sortByName();
        menuButton.setOnAction(e -> toggleMenu());
        sortByNameButton.setOnAction(e -> {
            fadeNode(movieTilePane, 1.0, 0.0, 200, () -> {
                currentDisplayedMovies = currentDisplayedMovies.stream()
                        .sorted((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()))
                        .toList();
                displayMovies(currentDisplayedMovies);
                fadeNode(movieTilePane, 0.0, 1.0, 200, null);
            });
            toggleMenu();
        });

        sortByRatingButton.setOnAction(e -> {
            turnWatchListButtonOnOFF(0);
            fadeNode(movieTilePane, 1.0, 0.0, 200, () -> {
                currentDisplayedMovies = currentDisplayedMovies.stream()
                        .sorted((m1, m2) -> Double.compare(m2.getImdbRating(), m1.getImdbRating()))
                        .toList();
                displayMovies(currentDisplayedMovies);
                fadeNode(movieTilePane, 0.0, 1.0, 200, null);
            });
            toggleMenu(0);
        });
        setupGenresSection();
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY() * 1.5; // multiplier for sensitivity
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight());
        });


    }

    public void showWatchlist() {
        currentSceneType = SceneType.WATCHLIST;
        titleLabel.setText("WATCHLIST");
        turnWatchListButtonOnOFF(1);
        List<Movie> watchlistMovies = watchlistDAO.getWatchlistMovies();

        currentDisplayedMovies = watchlistMovies;
        fadeNode(movieTilePane, 1.0, 0.0, 200, () -> {
            displayMovies(currentDisplayedMovies);
            fadeNode(movieTilePane, 0.0, 1.0, 200, null);
        });

        if (watchlistMovies.size() == 0) {
            noResultsLabel.setVisible(true);
            noResultsLabel.setText("No watchlist movies found");
        }

        toggleMenu(0);
    }

    @FXML
    private void clearWatchlist() {
        noResultsLabel.setVisible(true);
        noResultsLabel.setText("Watchlist Cleared. Press 'HOME' button to return to the main page");
        WatchlistDAO watchlistDAO = new WatchlistDAO();
        watchlistDAO.removeAllFromWatchlist();
        displayMovies(List.of());
    }

    private void setupGenresSection() {
        Button genresButton = new Button("Genres ▼");
        genresButton.setMaxWidth(Double.MAX_VALUE);
        genresButton.getStyleClass().add("menu-item");
        genresButton.setOnAction(e -> toggleGenresDropdown(genresButton));
        sideMenu.getChildren().add(genresButton);
        Button backButton = new Button("⬅");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> toggleMenu(0));
        backButton.getStyleClass().add("side-menu-back-button");
        sideMenu.getChildren().add(backButton);
    }

    private void toggleGenresDropdown(Button genresButton) {
        if (!genresExpanded) {
            // Expand
            List<String> genres = getDistinctGenres();
            for (String genre : genres) {
                Button genreBtn = new Button(genre);
                genreBtn.setMaxWidth(Double.MAX_VALUE);
                genreBtn.getStyleClass().add("genre-button");
                genreBtn.setOnAction(e -> filterByGenre(genre));
                genresContainer.getChildren().add(genreBtn);
            }
            genresButton.setText("Genres ▲");
            genresExpanded = true;
        } else {
            // Collapse
            genresContainer.getChildren().clear();
            genresButton.setText("Genres ▼");
            genresExpanded = false;
        }
    }


    private void filterByGenre(String genre) {
        noResultsLabel.setText("");
        List<Movie> filtered = allMovies.stream()
                .filter(m -> m.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .toList();
        titleLabel.setText(genre.toUpperCase());
        turnWatchListButtonOnOFF(0);
        // Use fadeNode to fade out, update movies, then fade in
        fadeNode(movieTilePane, 1.0, 0.0, 200, () -> {
            currentDisplayedMovies = filtered;
            displayMovies(filtered);
            fadeNode(movieTilePane, 0.0, 1.0, 200, null);
        });

        toggleMenu(0);
    }


    private List<String> getDistinctGenres() {
        return allMovies.stream()
                .flatMap(m -> List.of(m.getGenre().split(",")).stream()) // split by comma
                .map(String::trim) // remove spaces
                .filter(g -> !g.isEmpty())
                .distinct()
                .sorted(String::compareToIgnoreCase)
                .toList();
    }

    private void sortByName() {
        allMovies = movieDAO.getAllMovies();
        currentDisplayedMovies = allMovies;
        currentDisplayedMovies = currentDisplayedMovies.stream()
                .sorted((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()))
                .toList();
        displayMovies(currentDisplayedMovies);
    }

    private void toggleMenu() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), sideMenu);
        FadeTransition fade = new FadeTransition(Duration.millis(300), sideMenu);

        if (!menuVisible) {
            slide.setFromX(200);
            slide.setToX(0);
            fade.setFromValue(0);
            fade.setToValue(1);
            slide.play();
            fade.play();
            menuVisible = true;
        } else {
            slide.setFromX(0);
            slide.setToX(200);
            fade.setFromValue(1);
            fade.setToValue(0);
            slide.play();
            fade.play();
            menuVisible = false;
        }
    }

    private void toggleMenu(int state) {
        // state: 1 = open, 0 = close
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), sideMenu);
        FadeTransition fade = new FadeTransition(Duration.millis(300), sideMenu);

        if (state == 1 && !menuVisible) {
            slide.setFromX(200);
            slide.setToX(0);
            fade.setFromValue(0);
            fade.setToValue(1);
            slide.play();
            fade.play();
            menuVisible = true;

        } else if (state == 0 && menuVisible) {
            slide.setFromX(0);
            slide.setToX(200);
            fade.setFromValue(1);
            fade.setToValue(0);
            slide.play();
            fade.play();
            menuVisible = false;
        }
    }


    private void setupSearchBox() {

        searchButton.setOnMouseClicked(e -> {
            toggleMenu(0);
            showSearchBox();
        });


        searchField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused && searchField.getText().trim().isEmpty()) {
                hideSearchBox();
            }
        });
    }

    private void showSearchBox() {
        if (!searchVisible) {
            searchVisible = true;
            searchContainer.setOpacity(1.0);

            Timeline slideIn = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(searchContainer.maxWidthProperty(), 0),
                            new KeyValue(searchContainer.opacityProperty(), 0)
                    ),
                    new KeyFrame(Duration.millis(250),
                            new KeyValue(searchContainer.maxWidthProperty(), 150),
                            new KeyValue(searchContainer.opacityProperty(), 1)
                    )
            );
            slideIn.play();

            slideIn.setOnFinished(e -> searchField.requestFocus());
        }
    }

    private void hideSearchBox() {
        if (searchVisible) {
            searchVisible = false;

            Timeline slideOut = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(searchContainer.maxWidthProperty(), 150),
                            new KeyValue(searchContainer.opacityProperty(), 1)
                    ),
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(searchContainer.maxWidthProperty(), 0),
                            new KeyValue(searchContainer.opacityProperty(), 0)
                    )
            );
            slideOut.play();
        }
    }


    private void filterMovies(String query) {
        List<Movie> baseList = currentSceneType == SceneType.WATCHLIST
                ? watchlistDAO.getWatchlistMovies()  // always fresh watchlist
                : allMovies;

        List<Movie> filtered = baseList.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(query.toLowerCase()))
                .toList();


        if (currentDisplayedMovies != null && currentDisplayedMovies.equals(filtered)) {
            return;
        }

        currentDisplayedMovies = filtered; // Update

        if (filtered.isEmpty()) {
            noResultsLabel.setText("No movies found");
            noResultsLabel.setOpacity(0);
            noResultsLabel.setVisible(true);

            FadeTransition fadeInLabel = new FadeTransition(Duration.millis(250), noResultsLabel);
            fadeInLabel.setFromValue(0);
            fadeInLabel.setToValue(1);
            fadeInLabel.play();
        } else {
            noResultsLabel.setVisible(false);
        }

        // Fade out, update movies, fade in
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), movieTilePane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            displayMovies(filtered);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), movieTilePane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    private void displayMovies(List<Movie> movies) {
        movieTilePane.getChildren().clear();
        for (Movie movie : movies) {
            VBox movieCard = createMovieCard(movie);
            movieTilePane.getChildren().add(movieCard);
        }
    }


    private VBox createMovieCard(Movie movie) {


        ImageView coverImageView = new ImageView();
        coverImageView.setFitWidth(150);
        coverImageView.setFitHeight(220);
        coverImageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(150, 220);
        clip.setArcWidth(20);  // Horizontal corner radius
        clip.setArcHeight(20); // Vertical corner radius
        coverImageView.setClip(clip);

        DropShadow imageShadow = new DropShadow();
        imageShadow.setRadius(9);
        imageShadow.setOffsetX(0);
        imageShadow.setOffsetY(5);
        imageShadow.setColor(Color.rgb(0, 0, 0, 0.5));

        coverImageView.setEffect(imageShadow);
        coverImageView.setClip(null);


        String imagePath = movie.getPosterUrl();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image img = ImageCache.getImage(imagePath);
            if (img != null) {
                coverImageView.setImage(img);
            }
        }

        // Rating circle
        Label ratingLabel = new Label(String.format("%.1f", movie.getImdbRating()));
        ratingLabel.getStyleClass().add("rating-label");

        StackPane ratingBadge = new StackPane(ratingLabel);
        ratingBadge.setPrefSize(30, 30);
        ratingBadge.setMaxSize(30, 30);

        StackPane imageStack = new StackPane(coverImageView, ratingBadge);
        StackPane.setAlignment(ratingBadge, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(ratingBadge, new Insets(0, -5, -1, 0));
        // Title label
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(150);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);

        // Container for genres (wraps nicely)
        FlowPane genreBox = new FlowPane();
        genreBox.setHgap(3);  // horizontal space
        genreBox.setVgap(3);  // vertical space if wrapping
        genreBox.setAlignment(Pos.CENTER);
        genreBox.setPrefWrapLength(150); // match card width
        genreBox.setMaxHeight(Region.USE_PREF_SIZE); // prevents card from stretching

// Create separate labels for each genre
        String[] genres = movie.getGenre().split(",");
        for (String g : genres) {
            String trimmedGenre = g.trim();
            Label genreLabel = new Label(trimmedGenre);
            genreLabel.getStyleClass().add("genre-label-background");
            genreBox.getChildren().add(genreLabel);
        }


        //MovieCard
        VBox card = new VBox(10, imageStack, titleLabel, genreBox);
        card.setAlignment(Pos.TOP_CENTER);
        card.getStyleClass().add("movie-card-vbox");


        // Smooth scale on hover
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });

        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        card.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), card);
            st.setToX(0.95);
            st.setToY(0.95);
            st.play();
        });

        card.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        card.setOnMouseClicked(e -> openMovieDetail(movie));

        DropShadow darkShadow = new DropShadow();
        darkShadow.setRadius(12);                    // Spread of the shadow
        darkShadow.setOffsetX(10);                    // Horizontal offset
        darkShadow.setOffsetY(15);                    // Vertical offset
        darkShadow.setColor(Color.rgb(0, 0, 0, 0.8)); // Darker and more visible
        card.setEffect(darkShadow);

        card.setOnMouseClicked(e -> openMovieDetail(movie));
        return card;
    }

    private void openMovieDetail(Movie movie) {
        Scene scene = movieTilePane.getScene();
        Parent currentRoot = scene.getRoot(); // Save current root for fade-out
        toggleMenu(0);
        // Fade-out animation for the current screen
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/movie_browser_application/MovieDetail.fxml"));
                Parent detailRoot = loader.load();

                MovieDetailController controller = loader.getController();

                List<Movie> allMovies = currentDisplayedMovies;
                int currentIndex = -1;
                for (int i = 0; i < allMovies.size(); i++) {
                    if (allMovies.get(i).getId() == movie.getId()) {
                        currentIndex = i;
                        break;
                    }
                }

                controller.setMovieListAndIndex(allMovies, currentIndex);
                controller.setPreviousRoot(currentRoot);
                scene.setUserData(this);
                // Switch root to detail screen
                scene.setRoot(detailRoot);

                // Fade-in animation for the detail screen
                detailRoot.setOpacity(0.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), detailRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        fadeOut.play();
    }

    private void fadeNode(Node node, double from, double to, int durationMillis, Runnable onFinished) {
        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis), node);
        fade.setFromValue(from);
        fade.setToValue(to);
        fade.setOnFinished(e -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });
        fade.play();
    }

    public void focusOnTilePane() {
        scrollPane.requestFocus();
        movieTilePane.requestFocus();
    }

    private void turnWatchListButtonOnOFF(int sw) {
        if (sw == 0) {
            clearWatchListButton.setVisible(false);
            clearWatchListButton.setDisable(true);
        } else {
            clearWatchListButton.setVisible(true);
            clearWatchListButton.setDisable(false);
        }
    }

    @FXML
    private void homeButtonAction() {
        turnWatchListButtonOnOFF(0);
        noResultsLabel.setText("");
        noResultsLabel.setVisible(false);
        currentSceneType = SceneType.ALL_MOVIES;
        titleLabel.setText("ALL MOVIES");
        fadeNode(movieTilePane, 1.0, 0.0, 200, () -> {
            Platform.runLater(() -> {
                focusOnTilePane();
            });
            currentDisplayedMovies = allMovies;
            displayMovies(allMovies);
            fadeNode(movieTilePane, 0.0, 1.0, 200, null);
        });
        toggleMenu(0);
    }

    public void refreshWatchlistMovies() {
        if (currentSceneType == SceneType.WATCHLIST)
            showWatchlist();
    }


}
