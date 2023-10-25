package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.Recording;
import com.github.pedromsilvapt.recollect.Models.RecordingLine;

import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utilities {
    /**
     * Regex expression that parses a properly formatted string representing a subtitle's time
     */
    public static Pattern subtitleTimeRegEx = Pattern.compile("(?<hh>\\d+):(?<mm>\\d+):(?<ss>\\d+),(?<ms>\\d+)");

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

    /**
     * Returns the String representation of a Duration object with the standard time format used in videos
     * and subtitles: hh:mm:ss
     *
     * @param duration
     * @return
     */
    public static String formatDuration(Duration duration) {
        var s = duration.getSeconds();
        var hours = s / 3600;
        var minutes = (s % 3600) / 60;
        var seconds = (s % 60);

        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Extremely naive and dirty solution to parse srt subtitles and return a list of lines
     * Assumes the file is a valid SRT format file
     *
     * @return
     */
    public static List<RecordingLine> parseSubtitleFile(Recording recording, Path path) throws IOException {
        var content = Files.readString(path);

        var contentLines = content.trim().split("\\r?\\n\\w*\\r?\\n");

        return Arrays.stream(contentLines)
                .map(contentLine -> contentLine.lines().toList())
                .filter(lineArray -> lineArray.size() >= 3)
                .map(lineArray -> {
                    // The first line should be the index
                    int index = Integer.parseInt(lineArray.get(0), 10);

                    // Timestamps should be in the second line, separated by "-->"
                    String[] timestamps = lineArray.get(1).trim().split("\\w*-->\\w*");

                    Duration startTime = parseSubtitleTime(timestamps[0]);
                    Duration endTime = parseSubtitleTime(timestamps[1]);

                    String text = lineArray.stream().skip(2).collect(Collectors.joining("\n"));

                    return new RecordingLine(recording, index, startTime, endTime, text);
                }).toList();
    }

    /**
     * Parses a string with the format hh:mm:ss,mmm and returns a Duration object representing it
     *
     * @param time
     * @return
     */
    public static Duration parseSubtitleTime(String time) {
        Matcher matcher = subtitleTimeRegEx.matcher(time);

        if (matcher.find()) {
            int hours = Integer.parseInt(matcher.group("hh"), 10);
            int minutes = Integer.parseInt(matcher.group("mm"), 10);
            int seconds = Integer.parseInt(matcher.group("ss"), 10);
            int milliseconds = Integer.parseInt(matcher.group("ms"), 10);

            return Duration.ofMillis((hours * 60L * 60L * 1000L) + (minutes * 60L * 1000) + (seconds * 1000L) + milliseconds);
        } else {
            return Duration.ZERO;
        }
    }

    /**
     * Changes the extension of the given path, returning an exactly equal path with just the extension changed
     * If the original path has no extension, then the new extension is appended to the end
     * The new extension should start with a dot
     *
     * @param path
     * @return
     */
    public static Path pathChangeExtension(Path path, String newExtension) {
        var fileName = path.getFileName().toString();
        var extensionIndex = fileName.lastIndexOf('.');

        if (extensionIndex >= 0) {
            return path.resolveSibling(fileName.substring(0, extensionIndex) + newExtension);
        } else {
            return path.resolveSibling(fileName + newExtension);
        }
    }
}
