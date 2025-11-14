package Exercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Subtitle {
    int id;
    String startTime;
    String endTime;
    String text;

    public Subtitle(int id, String startTime, String endTime, String text) {
        this.startTime = startTime;
        this.id = id;
        this.endTime = endTime;
        this.text = text;
    }

    @Override
    public String toString() {
        return id + "\n" + startTime + " --> " + endTime + "\n" + text + "\n";
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}

class Subtitles {
    List<Subtitle> subtitles;

    public Subtitles() {
        this.subtitles = new ArrayList<>();
    }

    int loadSubtitles(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue;

            int index = Integer.parseInt(line);
            String timeLine = reader.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                stringBuilder.append(line).append("\n");
            }
            String[] times = timeLine.split(" --> ");
            Subtitle subtitle = new Subtitle(index, times[0], times[1],
                    stringBuilder.toString().trim());

            subtitles.add(subtitle);
        }

        reader.close();
        return subtitles.size();
    }

    void print() {
        for (Subtitle subtitle : this.subtitles) {
            System.out.println(subtitle);
        }
    }

    void shift(int shiftMs) {
        for (Subtitle subtitle : this.subtitles) {
            String newStart = shiftTime(subtitle.getStartTime(), shiftMs);
            String newEnd = shiftTime(subtitle.getEndTime(), shiftMs);

            subtitle.setStartTime(newStart);
            subtitle.setEndTime(newEnd);
        }
    }

    private String shiftTime(String time, int shiftMs) {
        // time format: "HH:MM:SS,mmm"
        String[] parts = time.split("[:,]");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        int millis = Integer.parseInt(parts[3]);

        // convert to total milliseconds
        int totalMs = hours * 3600_000 + minutes * 60_000 + seconds * 1_000 + millis;
        totalMs += shiftMs;

        // prevent negative times
        if (totalMs < 0) totalMs = 0;

        // convert back to h:m:s,ms
        int newHours = totalMs / 3600_000;
        totalMs %= 3600_000;
        int newMinutes = totalMs / 60_000;
        totalMs %= 60_000;
        int newSeconds = totalMs / 1_000;
        int newMillis = totalMs % 1_000;

        // format back to the SRT style
        return String.format("%02d:%02d:%02d,%03d", newHours, newMinutes, newSeconds, newMillis);
    }


}

public class SubtitlesTest {
    public static void main(String[] args) throws IOException {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде