package Exercises;

import javax.swing.*;
import java.io.*;
import java.util.*;

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super("Unsupported format " + message);
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException() {
        super("Invalid format");
    }
}


class Time implements Comparable<Time> {
    TimeFormat format;
    int hour;
    int minute;

    public Time(TimeFormat format, int hour, int minute) {
        this.format = format;
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public int compareTo(Time time) {
        if (!Objects.equals(this.hour, time.hour)) {
            return this.hour - time.hour;
        }
        return this.minute - time.minute;
    }
}


class TimeTable {
    List<Time> times;
    String inputException;

    public TimeTable() {
        this.inputException = "";
        this.times = new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.contains(":") || line.contains(".")) {
                    String[] split = line.split(":");
                    if (split.length != 2) {
                        split = line.split("\\.");
                    }
                    if (Integer.parseInt(split[0]) > 23 || Integer.parseInt(split[1]) > 59) {
                        throw new InvalidTimeException();
                    } else {
                        times.add(new Time(TimeFormat.FORMAT_24, Integer.parseInt(split[0]), Integer.parseInt(split[1])));
                        sortAscending();
                    }
                } else {
                    inputException = line;
                    throw new UnsupportedFormatException(getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeTimes(OutputStream outputStream, TimeFormat timeFormat) {
        for (Time time : times) {
            if (timeFormat == TimeFormat.FORMAT_24) {
                System.out.println(time.hour + ":" + time.minute);
            } else {
                int hour = time.hour;
                String ampm = "AM";
                if (hour == 0) hour = 12;
                else if (hour == 12) ampm = "PM";
                else if (hour > 12) {
                    hour -= 12;
                    ampm = "PM";
                }
                System.out.println(hour + ":" + time.minute + " " + ampm);
            }
        }
    }

    String getMessage() {
        return inputException;
    }

    void sortAscending() {
        Collections.sort(times);
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}