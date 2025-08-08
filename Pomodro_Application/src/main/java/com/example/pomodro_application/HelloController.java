package com.example.pomodro_application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;

public class HelloController {

    @FXML
    private Label timerLabel;
    @FXML
    private TextField myTextField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label pomodoroTimeLabel;
    @FXML
    private Label shortTimeLabel;
    @FXML
    private Label longTimeLabel;
    @FXML
    private Label topic;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button modeSwitchButton;
    @FXML
    private boolean light;
    @FXML
    private Button startButton;

    enum Selector {
        POMO, SHORT, LONG
    }

    enum Mode {
        DARK, LIGHT
    }

    private int timeCounter = 5;
    private int pomodoroTimeSeconds = 5;
    private int shortTimeSeconds = 3;
    private int longTimeSeconds = 4;

    private boolean pomodoroSelection, shortBreakSelection, longBreakSelection;
    private int currentCycleTotalTime = 5;

    private Timeline timeline;
    private int counter;

    public void initialize() {
        timerLabel.setVisible(false);
        timerLabel.setText(formatTime(timeCounter));
        timerLabel.setAlignment(Pos.CENTER);
        selection(Selector.POMO);
        progressBar.setProgress(1.0);
        watchUpdater(pomodoroTimeSeconds, shortTimeSeconds, longTimeSeconds);
        styleTopicLabel();
        topic.setText("POMODORO");
        light = true;

        String targetColor = "#A0C4FF";
        animateBackgroundTransition(targetColor);
    }


    public void TextFieldAction() {
        if (pomodoroSelection) {
            pomodoroTimeSeconds = Integer.parseInt(myTextField.getText());
            System.out.println(pomodoroTimeSeconds);
        } else if (shortBreakSelection) {
            shortTimeSeconds = Integer.parseInt(myTextField.getText());
            System.out.println(pomodoroTimeSeconds);
        } else if (longBreakSelection) {
            longTimeSeconds = Integer.parseInt(myTextField.getText());
            System.out.println(pomodoroTimeSeconds);
        }
        watchUpdater(pomodoroTimeSeconds, shortTimeSeconds, longTimeSeconds);
        myTextField.clear();
    }

    @FXML
    public void startTimer() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return;
        }
        topic.setText("Pomodoro");
        timerLabel.setVisible(true);
        styleTimerLabel();
        timeCounter = pomodoroTimeSeconds;
        textFieldSetReset(false);
        currentCycleTotalTime = timeCounter;
        progressBar.setProgress(1.0);
        timerLabel.setText(formatTime(timeCounter));
        System.out.println("Pomodoro Time: " + pomodoroTimeSeconds + "Short Time: " + shortTimeSeconds + "Mid Time: " + longTimeSeconds);
        startButton.setVisible(false);
        startCountdown();
    }

    public void onClickPomodoroButton() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return;
        }
        selection(Selector.POMO);
        System.out.println("PromoButton Selected");
    }

    public void onClickShortBreakButton() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return;
        }
        selection(Selector.SHORT);
        System.out.println("ShortBreakButton Selected");
    }

    public void onClickLongBreakButton() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return;
        }
        selection(Selector.LONG);
        System.out.println("LongBreakButton Selected");
    }

    private void startCountdown() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timerLabel.setText(formatTime(timeCounter));
            progressBar.setProgress((double) timeCounter / currentCycleTotalTime);
            timeCounter--;


            if (timeCounter < 0) {
                counter++;

                if (counter == 6) {
                    timeline.stop();
                    counter = 0;
                    timeCounter = pomodoroTimeSeconds;
                    currentCycleTotalTime = timeCounter;
                    progressBar.setProgress(1.0);
                    textFieldSetReset(true);
                    selection(Selector.POMO);
                    topic.setText("Pomodoro");
                    timerLabel.setVisible(false);
                    startButton.setVisible(true);

                    SoundPlayer player = new SoundPlayer();
                    player.playSound(2);
                } else if (counter == 5) {
                    timeCounter = longTimeSeconds;
                    currentCycleTotalTime = timeCounter;
                    progressBar.setProgress(1.0);
                    topic.setText("Long Break");
                    selection(Selector.LONG);

                    SoundPlayer player = new SoundPlayer();
                    player.playSound(1);
                } else if (counter % 2 != 0) {
                    timeCounter = shortTimeSeconds;
                    currentCycleTotalTime = timeCounter;
                    progressBar.setProgress(1.0);
                    topic.setText("Short Break");
                    selection(Selector.SHORT);

                    SoundPlayer player = new SoundPlayer();
                    player.playSound(1);
                } else if (counter % 2 == 0) {
                    timeCounter = pomodoroTimeSeconds;
                    currentCycleTotalTime = timeCounter;
                    progressBar.setProgress(1.0);
                    topic.setText("Pomodoro");
                    selection(Selector.POMO);

                    SoundPlayer player = new SoundPlayer();
                    player.playSound(1);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void selection(Selector selected) {
        resetSelection();
        String colorTo;
        String textColor;

        if (light) {
            textColor = "#222222";
            switch (selected) {
                case POMO:
                    pomodoroSelection = true;
                    topic.setText("POMODORO");
                    colorTo = "#A0C4FF";
                    break;
                case SHORT:
                    shortBreakSelection = true;
                    topic.setText("SHORT BREAK");
                    colorTo = "#B8F2E6";
                    break;
                case LONG:
                    longBreakSelection = true;
                    topic.setText("LONG BREAK");
                    colorTo = "#FF9A8B";
                    break;
                default:
                    topic.setText("Pomodoro");
                    colorTo = "#FFFFFF";
                    break;
            }
        } else {
            textColor = "#f0f0f0";
            switch (selected) {
                case POMO:
                    pomodoroSelection = true;
                    topic.setText("POMODORO");
                    colorTo = "#232b2b";
                    break;
                case SHORT:
                    shortBreakSelection = true;
                    topic.setText("SHORT BREAK");
                    colorTo = "#064E3B";
                    break;
                case LONG:
                    longBreakSelection = true;
                    topic.setText("LONG BREAK");
                    colorTo = "#1E3A8A";
                    break;
                default:
                    topic.setText("Pomodoro");
                    colorTo = "#1e1e1e";
                    break;
            }
        }

        animateBackgroundTransition(colorTo);
        setTextColor(textColor);
    }


    private void animateBackgroundTransition(String toColorHex) {

        String currentStyle = rootPane.getStyle();
        String fromColorHex = "#FFFFFF";

        if (currentStyle != null && currentStyle.contains("-fx-background-color:")) {
            fromColorHex = currentStyle.replace("-fx-background-color:", "").replace(";", "").trim();
            if (fromColorHex.isEmpty()) {
                fromColorHex = "#FFFFFF";
            }
        }

        Color fromColor = Color.web(fromColorHex);
        Color toColor = Color.web(toColorHex);

        ObjectProperty<Color> color = new SimpleObjectProperty<>(fromColor);
        color.addListener((obs, oldColor, newColor) -> {
            String newStyle = String.format("-fx-background-color: #%02X%02X%02X;",
                    (int) (newColor.getRed() * 255),
                    (int) (newColor.getGreen() * 255),
                    (int) (newColor.getBlue() * 255));
            rootPane.setStyle(newStyle);
        });

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(color, fromColor)),
                new KeyFrame(Duration.millis(500), new KeyValue(color, toColor))
        );
        timeline.play();
    }


    private void resetSelection() {
        pomodoroSelection = longBreakSelection = shortBreakSelection = false;
    }

    private void textFieldSetReset(boolean T) {
        myTextField.clear();
        if (T) {
            myTextField.setVisible(true);
            myTextField.setDisable(false);
        } else {
            myTextField.setVisible(false);
            myTextField.setDisable(true);
        }
    }

    private void watchUpdater(int PTS, int STS, int LTS) {
        pomodoroTimeLabel.setText("Pomodoro Time: " + PTS);
        shortTimeLabel.setText("Short Time: " + STS);
        longTimeLabel.setText("Long Time: " + LTS);
    }

    private void styleTopicLabel() {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Bebas.ttf"), 28);

        // Style for topic label (main heading)
        topic.setStyle("-fx-font-size: 36px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.8), 6, 0, 2, 2);");
        topic.setFont(customFont);
        topic.setAlignment(Pos.CENTER);
        topic.setWrapText(true);

        // Common style for time labels
        String timeLabelStyle = "-fx-background-color: linear-gradient(to right, #4D96FF, #6BCB77);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 6 12 6 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 4, 0.3, 2, 2);" +
                "-fx-border-color: white;" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 2;" +
                "-fx-border-style: solid;";

        styleSingleTimeLabel(pomodoroTimeLabel, customFont, timeLabelStyle);
        styleSingleTimeLabel(shortTimeLabel, customFont, timeLabelStyle);
        styleSingleTimeLabel(longTimeLabel, customFont, timeLabelStyle);
    }

    private void styleSingleTimeLabel(Label label, Font font, String style) {
        label.setFont(font);
        label.setStyle(style);
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
    }

    public void OnClickModeButton() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return;
        }

        if (modeSwitchButton.getText().equals("DARK")) {
            modeSwitch(Mode.DARK);
            modeSwitchButton.setText("LIGHT");
        } else if (modeSwitchButton.getText().equals("LIGHT")) {
            modeSwitch(Mode.LIGHT);
            modeSwitchButton.setText("DARK");
        }
    }

    private void modeSwitch(Mode mode) {
        String targetColor;
        String textColor;

        switch (mode) {
            case DARK:
                targetColor = "#1e1e1e"; // dark charcoal
                textColor = "#f0f0f0";   // off-white text
                light = false;
                break;
            case LIGHT:
            default:
                targetColor = "#A0C4FF"; // bright white
                textColor = "#222222";   // dark text
                light = true;
                break;
        }
        styleTimerLabel();
        animateBackgroundTransition(targetColor);
        setTextColor(textColor);
    }

    private void setTextColor(String colorHex) {

        String baseTopicStyle = String.format(
                "-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: %s; " +
                        "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.8), 6, 0, 2, 2);", colorHex);
        topic.setStyle(baseTopicStyle);

        String baseTimeLabelStyle = String.format(
                "-fx-background-color: linear-gradient(to right, #4D96FF, #6BCB77); " +
                        "-fx-text-fill: %s; -fx-font-size: 18px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 12; -fx-padding: 6 12 6 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 4, 0.3, 2, 2); " +
                        "-fx-border-color: white; -fx-border-radius: 12; -fx-border-width: 2; -fx-border-style: solid;",
                colorHex);

        pomodoroTimeLabel.setStyle(baseTimeLabelStyle);
        shortTimeLabel.setStyle(baseTimeLabelStyle);
        longTimeLabel.setStyle(baseTimeLabelStyle);
    }

    private void styleTimerLabel() {
        String bgColor;
        String textColor;

        if (light) {
            bgColor = "rgba(255, 255, 255, 0.8)";
            textColor = "#222222";
        } else {
            bgColor = "rgba(30, 30, 30, 0.8)";
            textColor = "#f0f0f0";
        }

        String style = String.format(
                "-fx-font-size: 64px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: %s; " +
                        "-fx-background-color: %s; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 20 40 20 40; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);",
                textColor, bgColor);

        timerLabel.setStyle(style);
        timerLabel.setAlignment(Pos.CENTER);
    }


}
