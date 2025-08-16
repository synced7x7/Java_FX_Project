package com.example.weatherapplication;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class WorldClockExample {
    public static void main(String[] args) {
        // Example: New York
        ZoneId nyZone = ZoneId.of("America/New_York");
        ZonedDateTime nyTime = ZonedDateTime.now(nyZone);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a");
        System.out.println("New York: " + nyTime.format(fmt));

        // Example: Tokyo
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        ZonedDateTime tokyoTime = ZonedDateTime.now(tokyoZone);
        System.out.println("Tokyo: " + tokyoTime.format(fmt));
    }
}
