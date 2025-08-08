package com.example.movie_browser_application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-browser-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Movie Browser");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
        Font.loadFont(getClass().getResourceAsStream("/fonts/chiffon-trial-semibold.ttf"), 14);
    }

    public static void main(String[] args) {
        launch();
    }
}