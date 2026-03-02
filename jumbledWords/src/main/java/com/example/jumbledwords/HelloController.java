package com.example.jumbledwords;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class HelloController {
    @FXML
    private HBox hBox1;
    @FXML
    private HBox hBox2;
    @FXML
    private HBox hBox3;
    @FXML
    private Label ansLabel1;
    @FXML
    private Label ansLabel2;
    @FXML
    private Label ansLabel3;
    @FXML
    private Label judgeLabel1;
    @FXML
    private Label judgeLabel2;
    @FXML
    private Label judgeLabel3;
    @FXML
    private ImageView myImageView;
    @FXML
    private ImageView myImageView2;
    @FXML
    private ImageView myImageView3;

    private int clickCount1 = 0;
    private int clickCount2 = 0;
    private int clickCount3 = 0;
    private int correctCount = 0;
    private int completedCount = 0;

    private final String[] wordList = {"orange", "system", "hello", "world", "what", "guess", "java", "code", "logic"};
    private final Random random = new Random();

    public void initialize() {
        ansLabel1.setAlignment(Pos.CENTER);
        ansLabel2.setAlignment(Pos.CENTER);
        ansLabel3.setAlignment(Pos.CENTER);
        startGame();
    }

    private void startGame() {
        showWord(getRandomWord());
        showWord2(getRandomWord());
        showWord3(getRandomWord());
    }

    private String getRandomWord() {
        return wordList[random.nextInt(wordList.length)];
    }

    private void checkForImage(String word, int imageNumber) {

        Image image = new Image(getClass().getResource("/Orange.png").toExternalForm());
        if (imageNumber == 1)
            myImageView.setImage(image);
        else if (imageNumber == 2)
            myImageView2.setImage(image);
        else if (imageNumber == 3)
            myImageView3.setImage(image);
    }

    public void showWord(String word) {
        hBox1.getChildren().clear();
        ansLabel1.setText("");
        checkForImage(word, 1);
        String originalWord = word;
        List<Character> chars = new ArrayList<>();
        for (char ch : word.toCharArray()) {

            chars.add(ch);
        }

        Collections.shuffle(chars);

        for (char c : chars) {
            Button button = new Button(String.valueOf(c));
            button.setAlignment(Pos.CENTER);
            button.setPrefSize(50, 50);
            button.setOnAction(event -> {
                ansLabel1.setText(ansLabel1.getText() + c);
                button.setDisable(true);
                clickCount1++;

                if (clickCount1 == originalWord.length()) {
                    clickCount1 = 0;
                    handleWordCompletion(ansLabel1, originalWord, judgeLabel1);
                }
            });
            hBox1.getChildren().add(button);
        }

    }

    public void showWord2(String word) {
        hBox2.getChildren().clear();
        ansLabel2.setText("");
        checkForImage(word, 2);
        String originalWord = word;

        List<Character> chars = new ArrayList<>();
        for (char ch : word.toCharArray()) {

            chars.add(ch);
        }

        Collections.shuffle(chars);

        for (char c : chars) {
            Button button2 = new Button(String.valueOf(c));
            button2.setAlignment(Pos.CENTER);
            button2.setPrefSize(50, 50);

            button2.setOnAction(event -> {
                ansLabel2.setText(ansLabel2.getText() + c);
                button2.setDisable(true);
                clickCount2++;

                if (clickCount2 == originalWord.length()) {
                    clickCount2 = 0;
                    handleWordCompletion(ansLabel2, originalWord, judgeLabel2);
                }
            });
            hBox2.getChildren().add(button2);
        }

    }

    public void showWord3(String word) {
        hBox3.getChildren().clear();
        ansLabel3.setText("");
        checkForImage(word, 3);
        String originalWord = word;

        List<Character> chars = new ArrayList<>();
        for (char ch : word.toCharArray()) {

            chars.add(ch);
        }

        Collections.shuffle(chars);

        for (char c : chars) {
            Button button3 = new Button(String.valueOf(c));
            button3.setAlignment(Pos.CENTER);
            button3.setPrefSize(50, 50);
            button3.setOnAction(event -> {
                ansLabel3.setText(ansLabel3.getText() + c);
                button3.setDisable(true);
                clickCount3++;

                if (clickCount3 == originalWord.length()) {
                    clickCount3 = 0;
                    handleWordCompletion(ansLabel3, originalWord, judgeLabel3);
                }
            });
            hBox3.getChildren().add(button3);
        }

    }

    private void handleWordCompletion(Label answer, String originalWord, Label judgeLabel) {
        String ans = answer.getText();
        System.out.println("Full word entered: " + ans);

        if (ans.equals(originalWord)) {
            System.out.println("Correct answer entered: " + ans);
            judgeLabel.setStyle("-fx-text-fill: green");
            judgeLabel.setText("Bullseye!");
            correctCount++;
        } else {
            System.out.println("Wrong Answer Entered: " + ans);
            judgeLabel.setStyle("-fx-text-fill: red");
            judgeLabel.setText("CLose yet far!");
        }

        completedCount++;
        if (completedCount == 3) {
            System.out.println("GAME OVER: Correct: " + correctCount + " | Incorrect: " + (3 - correctCount));
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Round Completed!");
            alert.setContentText(
                    "Correct: " + correctCount + "\nIncorrect: " + (3 - correctCount)
            );

            alert.showAndWait();
            resetFX();
        }
    }

    private void resetFX() {
        clickCount1 = 0;
        clickCount2 = 0;
        clickCount3 = 0;
        correctCount = 0;
        completedCount = 0;
        judgeLabel1.setText("");
        judgeLabel2.setText("");
        judgeLabel3.setText("");
        startGame();
    }


}