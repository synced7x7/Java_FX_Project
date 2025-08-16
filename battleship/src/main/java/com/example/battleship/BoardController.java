package com.example.battleship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BoardController {

    @FXML
    private AnchorPane rootPane;


    @FXML
    private GridPane gridPane;

    @FXML
    private Rectangle battleship;

    @FXML
    private Rectangle destroyer1;

    @FXML
    private Rectangle destroyer2;

    @FXML
    private Rectangle submarine1;

    @FXML
    private Rectangle submarine2;

    @FXML
    private Rectangle submarine3;

    @FXML
    private Button rotateButton;

    @FXML
    private Button startButton;

    @FXML
    private Button resetButton;

    @FXML
    private GridPane gridPane2;

    @FXML
    private ImageView battleshipImage;

    @FXML
    private ImageView destroyerImage;

    @FXML
    private ImageView submarineImage;

    @FXML
    private Button backButton;

    private Map<Rectangle, Point> shipPositions = new HashMap<>();

    private Rectangle selectedShip = null;
    private Rectangle previousShip = null;
    private boolean[][] occupied = new boolean[9][9];

    private Map<Rectangle, Boolean> isHorizontal = new HashMap<>();
    private Point origin;

    private final double CELL_SIZE = 30; // each cell is 30x30 px

    public void initialize() {
        createGridCells();
        setupShipSelection();
        setHorizontal();
        rotateGridInit();
        startGame();
        gridPane2.setDisable(true);
        shipImageSR(true);
    }

    private void shipImageSR(boolean x) {
        if(!x) {
            battleshipImage.setVisible(false);
            destroyerImage.setVisible(false);
            submarineImage.setVisible(false);
        }
        else
        {
            battleshipImage.setVisible(true);
            destroyerImage.setVisible(true);
            submarineImage.setVisible(true);
        }
    }

    private void rotateGridInit() {
        rotateButton.setOnAction(e -> rotateSelectedShip());
    }

    private void startGame() {
        startButton.setOnAction(e -> {
            if (shipPositions.size() < 6) {
                System.out.println("Place all ships before starting the game!");
                return;
            }
            //Game Start Logic starts from here
            gridPane2.setDisable(false);
            shipImageSR(false);
            GameManager game = new GameManager(gridPane, gridPane2, occupied, this);
            setResetShipVisibility(0);
        });
    }

    public void setResetShipVisibility(int a) {
        if(a== 0) {
            battleship.setVisible(false);
            destroyer1.setVisible(false);
            destroyer2.setVisible(false);
            submarine1.setVisible(false);
            submarine2.setVisible(false);
            submarine3.setVisible(false);
            resetButton.setVisible(false);
            rotateButton.setVisible(false);
            startButton.setVisible(false);
        }
        else {
            battleship.setVisible(true);
            destroyer1.setVisible(true);
            destroyer2.setVisible(true);
            submarine1.setVisible(true);
            submarine2.setVisible(true);
            submarine3.setVisible(true);
            resetButton.setVisible(true);
            rotateButton.setVisible(true);
            startButton.setVisible(true);
        }

    }

    private void setHorizontal() {
        isHorizontal.put(battleship, true);
        isHorizontal.put(destroyer1, true);
        isHorizontal.put(destroyer2, true);
        isHorizontal.put(submarine1, true);
        isHorizontal.put(submarine2, true);
        isHorizontal.put(submarine3, true);
    }

    private void createGridCells() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setStroke(Color.BLACK);
                cell.setFill(Color.LIGHTGRAY);

                final int r = row;
                final int c = col;
                cell.setOnMouseClicked(e -> placeSelectedShip(r, c, selectedShip));

                gridPane.add(cell, col, row);

            }
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setStroke(Color.BLACK);
                cell.setFill(Color.LIGHTGRAY);

                final int r = row;
                final int c = col;
                cell.setOnMouseClicked(e -> placeSelectedShip(r, c, selectedShip));

                gridPane2.add(cell, col, row);

            }
        }


    }

    //Set up clicking on ships to select them
    private void setupShipSelection() {
        battleship.setOnMouseClicked(e -> selectShip(battleship));
        destroyer1.setOnMouseClicked(e -> selectShip(destroyer1));
        destroyer2.setOnMouseClicked(e -> selectShip(destroyer2));
        submarine1.setOnMouseClicked(e -> selectShip(submarine1));
        submarine2.setOnMouseClicked(e -> selectShip(submarine2));
        submarine3.setOnMouseClicked(e -> selectShip(submarine3));
    }

    /** Select a ship and highlight it */
    private void selectShip(Rectangle ship) {
        if(selectedShip != null) {
            selectedShip.setStroke(Color.BLACK);
            selectedShip.setStrokeWidth(1);
        }
        selectedShip = ship;
        selectedShip.setStroke(Color.RED);
        selectedShip.setStrokeWidth(3);
        //previousShip = selectedShip;
    }

    private void placeSelectedShip(int row, int col, Rectangle ship) {
        if (ship != null) {
            //ships own size
            int shipRows = (int) (ship.getHeight() / CELL_SIZE);
            int shipCols = (int) (ship.getWidth() / CELL_SIZE);

            // Constraint 1: Ensure ship stays inside grid
            if (row + shipRows > 9 || col + shipCols > 9) {
                return;
            }

            // Constraint 2: Clear previous cells
            Point prev = shipPositions.get(ship);
            if (prev != null) {
                int prevRow = prev.x;
                int prevCol = prev.y;
                for (int r = prevRow; r < prevRow + shipRows; r++) {
                    for (int c = prevCol; c < prevCol + shipCols; c++) {
                        occupied[r][c] = false;
                    }
                }
            }

            // Constraint 3: Check all new cells for overlap
            for (int r = row; r < row + shipRows; r++) {
                for (int c = col; c < col + shipCols; c++) {
                    if (occupied[r][c]) {
                        return;
                    }
                }
            }

            // Place ship
            double gridX = gridPane.getLayoutX();
            double gridY = gridPane.getLayoutY();
            ship.setLayoutX(gridX + col * CELL_SIZE);
            ship.setLayoutY(gridY + row * CELL_SIZE);

            // Mark cells as occupied
            for (int r = row; r < row + shipRows; r++) {
                for (int c = col; c < col + shipCols; c++) {
                    occupied[r][c] = true;
                }
            }

            // Save ship’s new position
            shipPositions.put(ship, new Point(row, col));

            // Reset stroke
            ship.setStroke(Color.BLACK);
            ship.setStrokeWidth(1);
            selectedShip = null;
        }
    }


    private void rotateSelectedShip() {
        if (selectedShip == null) return;

        int shipRows = (int) (selectedShip.getHeight() / CELL_SIZE);
        int shipCols = (int) (selectedShip.getWidth() / CELL_SIZE);

        // Swap width and height to rotate
        double newWidth = selectedShip.getHeight();
        double newHeight = selectedShip.getWidth();

        // Determine top-left grid cell of ship
        Point pos = shipPositions.get(selectedShip);
        int row = pos != null ? pos.x : 0;
        int col = pos != null ? pos.y : 0;

        // Bound checker
        if (row + (int)(newHeight / CELL_SIZE) > 9 || col + (int)(newWidth / CELL_SIZE) > 9) {
            return;
        }

        // Temporarily free old occupied cells
        if (pos != null) {
            for (int r = row; r < row + shipRows; r++) {
                for (int c = col; c < col + shipCols; c++) {
                    occupied[r][c] = false;
                }
            }
        }

        // Check for collision in rotated position
        boolean canRotate = true;
        for (int r = row; r < row + (int)(newHeight / CELL_SIZE); r++) {
            for (int c = col; c < col + (int)(newWidth / CELL_SIZE); c++) {
                if (occupied[r][c]) {
                    canRotate = false;
                    break;
                }
            }
            if (!canRotate) break;
        }

        if (!canRotate) {
            // Restore old cells
            if (pos != null) {
                for (int r = row; r < row + shipRows; r++) {
                    for (int c = col; c < col + shipCols; c++) {
                        occupied[r][c] = true;
                    }
                }
            }
            return;
        }


        selectedShip.setWidth(newWidth);
        selectedShip.setHeight(newHeight);

        // Mark new cells as occupied
        for (int r = row; r < row + (int)(newHeight / CELL_SIZE); r++) {
            for (int c = col; c < col + (int)(newWidth / CELL_SIZE); c++) {
                occupied[r][c] = true;
            }
        }
    }

    public void resetPosition() {
        if(selectedShip!=null) {
            selectedShip.setStroke(Color.BLACK);
            selectedShip.setStrokeWidth(1);
        }
        shipPositions.clear();
        selectedShip = null;
        previousShip = null;

        // Reset occupied cells
        for (boolean[] booleans : occupied) {
            Arrays.fill(booleans, false);
        }

        // Reset ship orientations to horizontal
        setHorizontal();

        // Reset position AND size (width & height) to original horizontal
        battleship.setLayoutX(27);
        battleship.setLayoutY(28);
        battleship.setWidth(90);   // example: 3 cells horizontally
        battleship.setHeight(30);

        destroyer1.setLayoutX(27);
        destroyer1.setLayoutY(78);
        destroyer1.setWidth(60);   // 2 cells horizontally
        destroyer1.setHeight(30);

        destroyer2.setLayoutX(101);
        destroyer2.setLayoutY(78);
        destroyer2.setWidth(60);
        destroyer2.setHeight(30);

        submarine1.setLayoutX(27);
        submarine1.setLayoutY(123);
        submarine1.setWidth(30);   // 1 cell
        submarine1.setHeight(30);

        submarine2.setLayoutX(72);
        submarine2.setLayoutY(123);
        submarine2.setWidth(30);
        submarine2.setHeight(30);

        submarine3.setLayoutX(116);
        submarine3.setLayoutY(123);
        submarine3.setWidth(30);
        submarine3.setHeight(30);

        setResetShipVisibility(1);
        gridPane2.setDisable(true);
        shipImageSR(true);
    }

    @FXML
    private void backHandler() {
        try {

            Stage stage = (Stage) backButton.getScene().getWindow();


            FXMLLoader loader = new FXMLLoader(Main.class.getResource("StartingScreen.fxml"));
            Scene scene = new Scene(loader.load());

            StartingScreen controller = loader.getController();
            controller.setStage(stage);


            stage.setScene(scene);
            stage.show();
            resetPosition();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
