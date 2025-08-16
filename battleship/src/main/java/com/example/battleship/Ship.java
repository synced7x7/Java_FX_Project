package com.example.battleship;

import java.awt.Point;
import java.util.List;

public class Ship {
    private int length;
    private int health;
    private List<Point> positions; // cells occupied

    public Ship(int length, List<Point> positions) {
        this.length = length;
        this.health = length; // each cell = 1 health
        this.positions = positions;
    }

    public boolean hit(Point shot) {
        if (positions.contains(shot)) {
            health--;
            return true;
        }
        return false;
    }

    public boolean isSunk() {
        return health <= 0;
    }
}

