package com.example.scrambleword;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HelloController {
    @FXML
    private HBox letterBox;
    @FXML
    private Label answer;
    @FXML
    private Label judgeLabel;

    private int clickCount = 0;
    private String originalWord;

    private final String[] wordList = {"apple", "banana", "hello", "world", "scramble", "guess", "java", "code", "logic"};
    private final Random random = new Random();

    public void initialize() {
        letterBox.setAlignment(Pos.CENTER);
        judgeLabel.setAlignment(Pos.CENTER);
        answer.setAlignment(Pos.CENTER);
        showWord(getRandomWord());
    }

    private String getRandomWord() {
        return wordList[random.nextInt(wordList.length)];
    }

    private void showWord(String word) {
        letterBox.getChildren().clear();
        answer.setText("");
        judgeLabel.setText("");
        clickCount = 0;
        originalWord = word;

        List<Character> chars = new ArrayList<>();
        for (char ch : word.toCharArray()) {
            chars.add(ch);
        }

        Collections.shuffle(chars);

        for (char c : chars) {
            Button button = new Button(String.valueOf(c));
            button.setAlignment(Pos.CENTER);
            button.setPrefSize(50, 50);
            button.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-font-size: 20px; -fx-background-color: beige;");
            button.setOnAction(event -> {
                answer.setText(answer.getText() + c);
                button.setDisable(true);
                clickCount++;

                if (clickCount == originalWord.length()) {
                    handleWordCompletion();
                }
            });
            letterBox.getChildren().add(button);
        }
    }

    private void handleWordCompletion() {
        String ans = answer.getText();
        System.out.println("Full word entered: " + ans);

        if (ans.equals(originalWord)) {
            judgeLabel.setStyle("-fx-text-fill: green");
            judgeLabel.setText("Wow! You are right");
        } else {
            judgeLabel.setStyle("-fx-text-fill: red");
            judgeLabel.setText("Better luck next time");
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            judgeLabel.setText("");
            showWord(getRandomWord());
        });
        pause.play();
    }
}
