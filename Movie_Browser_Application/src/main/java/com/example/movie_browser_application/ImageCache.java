package com.example.movie_browser_application;
import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
    private static final Map<String, Image> cache = new HashMap<>();

    public static Image getImage(String path) {
        return cache.computeIfAbsent(path, p -> {
            try {
                return new Image(ImageCache.class.getResourceAsStream("/" + p));
            } catch (Exception e) {
                System.err.println("Could not load image: " + p);
                return null;
            }
        });
    }
}