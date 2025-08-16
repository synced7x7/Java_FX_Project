package com.example.battleship;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartingScreen {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void startGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    // Difficulty setters...
    public void easyDifficulty() {
        DifficultySetter difficultySetter = new DifficultySetter();
        difficultySetter.setDifficulty(DifficultyEnum.EASY);
    }

    public void hardDifficulty() {
        DifficultySetter difficultySetter = new DifficultySetter();
        difficultySetter.setDifficulty(DifficultyEnum.HARD);
    }
}
