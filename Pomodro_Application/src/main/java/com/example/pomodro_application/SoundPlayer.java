package com.example.pomodro_application;

import javafx.scene.media.AudioClip;

public class SoundPlayer {

    public void playSound(int counter) {
        try {
            if (counter == 1) {
                AudioClip sound = new AudioClip(getClass().getResource("/sounds/ping.mp3").toExternalForm());
                sound.play();
            }
            else if (counter == 2) {
                AudioClip sound = new AudioClip(getClass().getResource("/sounds/finish.mp3").toExternalForm());
                sound.play();
            }
        } catch (Exception e) {
            System.out.println("Failed to play sound: " + e.getMessage());
        }
    }
}

