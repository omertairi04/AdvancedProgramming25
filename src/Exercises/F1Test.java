package Exercises;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Driver {
    String name;
    List<String> laps;
    String bestLap;

    public Driver(String name, String lap1, String lap2, String lap3) {
        this.name = name;
        laps = new ArrayList<>();
        laps.add(lap1);
        laps.add(lap2);
        laps.add(lap3);
        bestLap = findBestTime();
    }

    String findBestTime() {
        bestLap = laps.get(0);

        for (int i = 1; i < laps.size(); i++) {
            String[] currLap = bestLap.split(":");
            String[] nextLap = laps.get(i).split(":");

            int currMin = Integer.parseInt(currLap[0]);
            int currSec = Integer.parseInt(currLap[1]);
            int currMs = Integer.parseInt(currLap[2]);

            int nextMin = Integer.parseInt(nextLap[0]);
            int nextSec = Integer.parseInt(nextLap[1]);
            int nextMs = Integer.parseInt(nextLap[2]);

            if (nextMin < currMin
                    || (nextMin == currMin && nextSec < currSec)
                    || (nextMin == currMin && nextSec == currSec && nextMs < currMs)) {
                bestLap = laps.get(i);
            }
        }

        return bestLap;
    }

    @Override
    public String toString() {
        return name + "          " + bestLap;
    }
}

class F1Race {
    List<Driver> drivers;

    public F1Race() {
        this.drivers = new ArrayList<>();
    }

    void readResults(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] data = line.split(" ");
            Driver driver = new Driver(data[0], data[1], data[2], data[3]);
            drivers.add(driver);
        }

        reader.close();
    }

    void printSorted(OutputStream outputStream) throws IOException {
        for (int i = 0; i < drivers.size() - 1; i++) {
            for (int j = i + 1; j < drivers.size(); j++) {
                String[] driver1 = drivers.get(i).bestLap.split(":");
                String[] driver2 = drivers.get(j).bestLap.split(":");

                int c1 = Integer.parseInt(driver1[0]);
                int s1 = Integer.parseInt(driver1[1]);
                int m1 = Integer.parseInt(driver1[2]);

                int c2 = Integer.parseInt(driver2[0]);
                int s2 = Integer.parseInt(driver2[1]);
                int m2 = Integer.parseInt(driver2[2]);

                boolean shouldSwap = false;

                if (c2 < c1) {
                    shouldSwap = true;
                } else if (c2 == c1) {
                    if (s2 < s1) {
                        shouldSwap = true;
                    } else if (s2 == s1 && m2 < m1) {
                        shouldSwap = true;
                    }
                }

                if (shouldSwap) {
                    Driver temp = drivers.get(i);
                    drivers.set(i, drivers.get(j));
                    drivers.set(j, temp);
                }
            }
        }

        for (Driver driver : drivers) {
            System.out.println(driver);
        }
    }
}

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
