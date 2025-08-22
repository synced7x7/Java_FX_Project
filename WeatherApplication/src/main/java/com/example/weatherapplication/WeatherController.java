package com.example.weatherapplication;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;


public class WeatherController {

    @FXML
    private Label tempLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label conditionLabel;
    @FXML
    private AnchorPane searchDropPane;
    @FXML
    private TextField dropdownCityInput;
    @FXML
    private Label locationLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label feelLabel;
    @FXML
    private Label prLabel;
    @FXML
    private Label aqiLabel;
    @FXML
    private VBox future3DaysVBox;
    @FXML
    private HBox last7DaysHBox;
    @FXML
    private ImageView weatherIcon;
    @FXML
    private ImageView background;
    @FXML
    private AnchorPane root;

    private JSONObject prevData;


    private Timeline clock;

    private final String lightCss = Objects.requireNonNull(getClass().getResource("WeatherApplicationStyle.css")).toExternalForm();
    private final String darkCss = Objects.requireNonNull(getClass().getResource("WeatherApplicationStyle_Dark.css")).toExternalForm();

    @FXML
    private void onDropdownSearch() {
        String city = dropdownCityInput.getText().trim();
        performSearch(city);
    }

    public void initialize() {
        javafx.application.Platform.runLater(() -> {
            Scene scene = tempLabel.getScene();
            if (scene != null) {
                scene.getStylesheets().add(lightCss);
            }
        });
    }


    private void performSearch(String city) {
        if (city.isEmpty()) {
            javafx.application.Platform.runLater(() -> showError("City cannot be empty!"));
            return;
        }

        new Thread(() -> {
            try {
                String newCityName = city.trim().toLowerCase();

                if (prevData != null) {
                    String prevCityName = prevData.getString("name").trim().toLowerCase();
                    if (newCityName.equals(prevCityName)) {
                        System.out.println("SameCity");
                        return; // skip fetching
                    }
                }

                javafx.application.Platform.runLater(() -> showError("Fetching Data.."));

                JSONObject data = WeatherService.getCurrentWeather(city);
                JSONObject location = data.getJSONObject("location");
                prevData = location;
                JSONObject current = data.getJSONObject("current");
                String iconUrl = "https:" + current.getJSONObject("condition").getString("icon");
                Image image = new Image(iconUrl, true); // true = load in background
                weatherIcon.setImage(image);
                weatherIcon.setPreserveRatio(true);

                double tempC = current.getDouble("temp_c");
                String temp = Math.round(tempC) + "°";
                String humidity = current.getInt("humidity") + "%";
                String condition = current.getJSONObject("condition").getString("text");
                String tzId = location.getString("tz_id");
                String cityName = location.getString("name");
                double feelsLikeC = current.getDouble("feelslike_c");
                double precipitation = current.getDouble("precip_mm");
                int aqi = current.getJSONObject("air_quality").getInt("us-epa-index");

                //Future 3-day forecast
                JSONArray next3Days = WeatherService.getNext3DaysForecast(city);

                //Last 7-day history
                JSONArray last7Days = WeatherService.getLast7DaysHistory(city);

                // update UI on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    // Current weather
                    tempLabel.setText(temp);
                    humidityLabel.setText(humidity);
                    conditionLabel.setText(condition);
                    locationLabel.setText("➤ " + cityName);
                    feelLabel.setText(Math.round(feelsLikeC) + "°");
                    prLabel.setText(precipitation + "ₘₘ");
                    aqiLabel.setText(String.valueOf(aqi));

                    if (clock != null) clock.stop();

                    DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
                    DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM yyyy");

                    clock = new Timeline(new KeyFrame(Duration.ZERO, _ -> {
                        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(tzId));
                        timeLabel.setText(now.format(dateFmt) + ", " + now.format(timeFmt));
                        applyDayNightMode(now);
                    }), new KeyFrame(Duration.seconds(1)));
                    clock.setCycleCount(Timeline.INDEFINITE);
                    clock.play();


                    // Future 3-day forecast
                    future3DaysVBox.getChildren().clear();
                    for (int i = 0; i < next3Days.length(); i++) {
                        JSONObject day = next3Days.getJSONObject(i);

                        LocalDate date = LocalDate.parse(day.getString("date"));
                        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

                        Label dayLabel = new Label(dayOfWeek + " | Max: " + Math.round(day.getDouble("maxtemp_c")) + "°"
                                + " | Min: " + Math.round(day.getDouble("mintemp_c")) + "°"
                                + " | " + day.getString("condition"));
                        dayLabel.getStyleClass().add("future-day-card");
                        future3DaysVBox.getChildren().add(dayLabel);
                    }

                    // Last 7-day history
                    last7DaysHBox.getChildren().clear();
                    for (int i = 0; i < last7Days.length(); i++) {
                        JSONObject day = last7Days.getJSONObject(i);

                        LocalDate date = LocalDate.parse(day.getString("date"));
                        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

                        Label dayLabel = new Label(dayOfWeek + "\nMax: " + Math.round(day.getDouble("maxtemp_c")) + "°\nMin: "
                                + Math.round(day.getDouble("mintemp_c")) + "°\n" + day.getString("condition"));
                        dayLabel.getStyleClass().add("history-day-card"); // CSS class
                        last7DaysHBox.getChildren().add(dayLabel);
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> showError("City not found!"));
            }
        }).start();
    }

    private void showError(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        StackPane toast = new StackPane(errorLabel);
        toast.setOpacity(0);
        toast.setMaxWidth(300);
        toast.setStyle(
                "-fx-background-color: rgba(220,20,60,1);" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 12px 20px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.3, 0, 4);"
        );


        toast.setEffect(new BoxBlur(10, 10, 3));


        root.getChildren().add(toast);
        StackPane.setAlignment(toast, Pos.TOP_CENTER);
        StackPane.setMargin(toast, new Insets(80, 0, 0, 0)); // push ~80px down


        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), toast);
        slideIn.setFromY(70);
        slideIn.setToY(120);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition showAnim = new ParallelTransition(slideIn, fadeIn);
        showAnim.play();

        // Auto hide after 3s
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), toast);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> root.getChildren().remove(toast));
            fadeOut.play();
        });
        delay.play();
    }


    // Toggle the dropdown
    @FXML
    private void onSearchFieldClick() {
        double h = ((Region) searchDropPane).getPrefHeight();

        if (!searchDropPane.isVisible()) {
            searchDropPane.setVisible(true);
            searchDropPane.setManaged(true);
            searchDropPane.setOpacity(0);
            searchDropPane.setTranslateY(-h);

            Timeline show = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(searchDropPane.translateYProperty(), -h, Interpolator.EASE_OUT),
                            new KeyValue(searchDropPane.opacityProperty(), 0)
                    ),
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(searchDropPane.translateYProperty(), 0, Interpolator.EASE_OUT),
                            new KeyValue(searchDropPane.opacityProperty(), 1)
                    )
            );
            show.setOnFinished(e -> dropdownCityInput.requestFocus());
            show.play();
        } else {
            Timeline hide = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(searchDropPane.translateYProperty(), 0),
                            new KeyValue(searchDropPane.opacityProperty(), 1)
                    ),
                    new KeyFrame(Duration.millis(180),
                            new KeyValue(searchDropPane.translateYProperty(), -h, Interpolator.EASE_IN),
                            new KeyValue(searchDropPane.opacityProperty(), 0)
                    )
            );
            hide.setOnFinished(e -> {
                searchDropPane.setVisible(false);
                searchDropPane.setManaged(false);
            });
            hide.play();
        }
    }


    private void applyDayNightMode(ZonedDateTime cityTime) {
        int hour = cityTime.getHour();
        Scene scene = tempLabel.getScene();
        if (scene == null) return;

        boolean isDay = (hour >= 6 && hour < 18);


        String cssToApply = isDay ? lightCss : darkCss;
        String imagePath = isDay ? "/Image/after_noon.jpg" : "/Image/night.jpg";
        System.out.println(cssToApply);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(cssToApply);

        scene.getRoot().applyCss();
        scene.getRoot().layout();

        try (var is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                background.setImage(new Image(is));
            } else {
                System.err.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
