package com.example.battleship;

public class DifficultySetter {
    public static DifficultyEnum difficulty;

    static public DifficultyEnum getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyEnum difficulty) {
        System.out.println("Difficulty set to " + difficulty);
        this.difficulty = difficulty;
    }
}
