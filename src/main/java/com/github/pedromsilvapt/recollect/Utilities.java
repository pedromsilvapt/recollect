package com.github.pedromsilvapt.recollect;

import java.time.Duration;

public class Utilities {
    /**
     * Simple solution to produce human friendly text representations of Durations.
     * <p>
     * Written by lucasls (https://stackoverflow.com/users/1731824/lucasls), taken from
     * https://stackoverflow.com/a/40487511
     *
     * @param duration The duration we want to convert to string
     * @return The string representation of the duration
     */
    public static String humanizeDuration(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    public static String formatDuration(Duration duration) {
        var s = duration.getSeconds();
        var hours = s / 3600;
        var minutes = (s % 3600) / 60;
        var seconds = (s % 60);
        
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
