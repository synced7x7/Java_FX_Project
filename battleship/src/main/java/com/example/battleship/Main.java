package com.example.battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("StartingScreen.fxml"));
        Scene scene = new Scene(loader.load());
        DifficultySetter d = new DifficultySetter();
        d.setDifficulty(DifficultyEnum.EASY);
        StartingScreen controller = loader.getController();
        controller.setStage(stage);

        stage.setTitle("Battleship");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}