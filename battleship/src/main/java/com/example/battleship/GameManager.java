package com.example.battleship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

import java.awt.Point;

public class GameManager {

    private final int GRID_SIZE = 9;
    private final double CELL_SIZE = 30;

    private boolean[][] playerShips;
    private boolean[][] aiShips;
    private boolean[][] playerHits;
    private boolean[][] aiHits;

    private GridPane playerGrid;
    private GridPane aiGrid;

    private Random random = new Random();

    private BoardController controller;

    private int dummy_counter = 0;


    private boolean inTargetMode = false;
    private int targetRow, targetCol;
    private int targetAttempts = 0;
    private List<Point> targetCandidates = new ArrayList<>();

    private Map<String, Integer> playerShipHealth = new HashMap<>();
    private Map<String, Integer> aiShipHealth = new HashMap<>();
    private Map<String, String> playerShipPositions = new HashMap<>();
    private Map<String, String> aiShipPositions = new HashMap<>();



    private void initShipHealth() {
        playerShipHealth.put("Carrier", 5);
        playerShipHealth.put("Battleship", 4);
        playerShipHealth.put("Cruiser", 3);
        playerShipHealth.put("Submarine", 3);
        playerShipHealth.put("Destroyer", 2);

        aiShipHealth.putAll(playerShipHealth); // copy same setup for AI
    }

    public GameManager(GridPane playerGrid, GridPane aiGrid, boolean[][] playerShips, BoardController controller) {
        this.playerGrid = playerGrid;
        this.aiGrid = aiGrid;
        this.playerShips = playerShips;
        this.controller = controller;

        this.playerHits = new boolean[GRID_SIZE][GRID_SIZE];
        this.aiHits = new boolean[GRID_SIZE][GRID_SIZE];
        this.aiShips = new boolean[GRID_SIZE][GRID_SIZE];

        placeAIShipsRandomly();
        setupPlayerClicks();
        initShipHealth();
    }


    private void placeAIShipsRandomly() {
        placeShipRandomly(3); // battleship
        placeShipRandomly(2);
        placeShipRandomly(2);
        placeShipRandomly(1);
        placeShipRandomly(1);
        placeShipRandomly(1);
    }

    private void placeShipRandomly(int size) {
        boolean placed = false;
        while (!placed) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);
            boolean horizontal = random.nextBoolean();

            // Check bounds
            if (horizontal && col + size > GRID_SIZE) continue;
            if (!horizontal && row + size > GRID_SIZE) continue;

            // Check overlap
            boolean overlap = false;
            for (int r = 0; r < (horizontal ? 1 : size); r++) {
                for (int c = 0; c < (horizontal ? size : 1); c++) {
                    if (aiShips[row + r][col + c]) {
                        overlap = true;
                        break;
                    }
                }
                if (overlap) break;
            }
            if (overlap) continue;

            // Place ship
            for (int r = 0; r < (horizontal ? 1 : size); r++) {
                for (int c = 0; c < (horizontal ? size : 1); c++) {
                    aiShips[row + r][col + c] = true;
                }
            }
            placed = true;
        }
    }


    private void setupPlayerClicks() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle cell = (Rectangle) getNodeFromGridPane(aiGrid, row, col);
                final int r = row;
                final int c = col;
                cell.setOnMouseClicked(e -> {
                    if (!playerHits[r][c]) {
                        playerHits[r][c] = true;
                        if (aiShips[r][c]) {
                            cell.setFill(Color.RED); // hit
                        } else {
                            cell.setFill(Color.GRAY); // miss
                        }

                       // System.out.println(DifficultySetter.getDifficulty());
                        if(DifficultySetter.getDifficulty() == DifficultyEnum.EASY){
                            easyAiTurn();
                        }
                        else{
                            aiTurn();
                        }
                        checkWin();
                    }
                });
            }
        }
    }

    private void easyAiTurn() {
        System.out.println("easyAiTurn");
        int r, c;

        do {
            r = random.nextInt(GRID_SIZE);
            c = random.nextInt(GRID_SIZE);
        } while (aiHits[r][c]);


        aiHits[r][c] = true;

        Rectangle cell = (Rectangle) getNodeFromGridPane(playerGrid, r, c);


        if (playerShips[r][c]) {
            cell.setFill(Color.RED);
        } else {
            cell.setFill(Color.GRAY);
        }
    }




    private void aiTurn() {
        int r, c;
        System.out.println("HardaiTurn");
        if (inTargetMode && !targetCandidates.isEmpty()) {
            Point p = targetCandidates.removeFirst();
            r = p.x;
            c = p.y;
        } else {
            // random
            do {
                r = random.nextInt(GRID_SIZE);
                c = random.nextInt(GRID_SIZE);
            } while (aiHits[r][c]);
        }


        aiHits[r][c] = true;

        Rectangle cell = (Rectangle) getNodeFromGridPane(playerGrid, r, c);
        if (playerShips[r][c]) {
            // Hit
            cell.setFill(Color.RED);


            if (!inTargetMode) {
                inTargetMode = true;
                targetRow = r;
                targetCol = c;
                targetAttempts = 0;
                targetCandidates = generateTargetCandidates(r, c);
            } else {
                targetAttempts = 0;
            }

        } else {

            cell.setFill(Color.GRAY);

            if (inTargetMode) {
                targetAttempts++;
                if (targetAttempts >= 3 || targetCandidates.isEmpty()) {
                    inTargetMode = false;
                    targetCandidates.clear();
                }
            }
        }
    }

    // Getting nearby nearby coordinates
    private List<Point> generateTargetCandidates(int r, int c) {
        List<Point> candidates = new ArrayList<>();

        // Up
        if (r > 0 && !aiHits[r - 1][c]) candidates.add(new Point(r - 1, c));
        // Down
        if (r < GRID_SIZE - 1 && !aiHits[r + 1][c]) candidates.add(new Point(r + 1, c));
        // Left
        if (c > 0 && !aiHits[r][c - 1]) candidates.add(new Point(r, c - 1));
        // Right
        if (c < GRID_SIZE - 1 && !aiHits[r][c + 1]) candidates.add(new Point(r, c + 1));

        Collections.shuffle(candidates, random);

        return candidates;
    }

    /**
     * Check if either side has won
     */
    private void checkWin() {
        if (allShipsSunk(aiShips, playerHits)) {
            showGameOver("Player Wins!");
        } else if (allShipsSunk(playerShips, aiHits)) {
            showGameOver("AI Wins!");
        }
    }

    private void showGameOver(String message) {
        // Create a new Stage (window)
        Stage gameOverStage = new Stage();
        gameOverStage.initModality(Modality.APPLICATION_MODAL); // block interaction with main window
        gameOverStage.setTitle("Game Over");

        // Create content
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button resetBtn = new Button("Reset Game");
        resetBtn.setOnAction(e -> {
            controller.resetPosition(); // call reset
            gameOverStage.close();
            resetGame();// close the notification
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, resetBtn);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        Scene scene = new Scene(layout);
        gameOverStage.setScene(scene);
        gameOverStage.showAndWait(); // show window and wait until it’s closed
    }

    private boolean allShipsSunk(boolean[][] ships, boolean[][] hits) {

        /*dummy_counter++;
        System.out.println("dummy_counter = " + dummy_counter);
        if (dummy_counter > 6) {
            return true;
        } else {
            return false;
        }*/

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (ships[r][c] && !hits[r][c]) return false;
            }
        }
        return true;
    }


    private javafx.scene.Node getNodeFromGridPane(GridPane grid, int row, int col) {
        for (javafx.scene.Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    public void resetGame() {
        // Clear player hits
        for (int r = 0; r < GRID_SIZE; r++) {
            Arrays.fill(playerHits[r], false);
            Arrays.fill(aiHits[r], false);
            Arrays.fill(aiShips[r], false);
        }

        // Reset grid colors
        for (javafx.scene.Node node : playerGrid.getChildren()) {
            ((Rectangle) node).setFill(Color.LIGHTGRAY);
        }
        for (javafx.scene.Node node : aiGrid.getChildren()) {
            ((Rectangle) node).setFill(Color.LIGHTGRAY);
        }

        // Place AI ships again
        placeAIShipsRandomly();
    }


}
