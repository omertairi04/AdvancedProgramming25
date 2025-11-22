package Exercises;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class Folder {
    char name;
    int totalSize;

    public Folder(char name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Folder)) return false;
        Folder folder = (Folder) o;
        return name == folder.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}

class File implements Comparable<File> {
    String name;
    int size;
    LocalDateTime timeCreated;

    public File(String name, int size, LocalDateTime timeCreated) {
        this.name = name;
        this.size = size;
        this.timeCreated = timeCreated;
    }

    public int getYear() {
        return timeCreated.getYear();
    }

    public String monthAndDay() {
        return timeCreated.getMonth().toString() + "-" + timeCreated.getDayOfMonth();
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, timeCreated);
    }

    @Override
    public int compareTo(File o) {
        int cmp = timeCreated.compareTo(o.timeCreated);
        if (cmp != 0) return cmp;

        cmp = name.compareTo(o.name);
        if (cmp != 0) return cmp;

        return Integer.compare(size, o.size);
    }
}


class FileSystem {
    HashMap<Folder, List<File>> files;

    public FileSystem() {
        files = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        Folder folder1 = new Folder(folder);
        files.computeIfAbsent(folder1, c -> new ArrayList<>()).add(new File(name, size, createdAt));
        folder1.totalSize += size;
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        List<File> list = new ArrayList<>();
        List<File> toReturn = new ArrayList<>();

        for (Folder folder : files.keySet()) {
            list.addAll(files.get(folder));
        }

        for (File file : list) {
            if (file.name.startsWith(".") && file.size < size) {
                toReturn.add(file);
            }
        }

        return toReturn;

    }

    public int totalSizeOfFilesFromFolders(List<Character> folders) {
        int total = 0;
        for (Character c : folders) {
            for (Folder folder : files.keySet()) {
                if (folder.name == c) {
                    List<File> list = files.get(folder);
                    if (list != null) {
                        for (File file : list) {
                            total += file.size;
                        }
                    }
                }
            }
        }

        return total;
    }

    public Map<Integer, Set<File>> byYear() {
        Map<Integer, Set<File>> byYear = new HashMap<>();
        for (Folder folder : files.keySet()) {
            for (File file : files.get(folder)) {
                byYear.computeIfAbsent(file.getYear(), k -> new HashSet<>()).add(file);
            }
        }
        return byYear;
    }

    public Map<String, Long> sizeByMonthAndDay() {
        Map<String, Long> byMonthAndDay = new HashMap<>();

        for (Folder folder : files.keySet()) {
            for (File file : files.get(folder)) {
                String key = file.monthAndDay();
                byMonthAndDay.put(key, byMonthAndDay.getOrDefault(key, 0L) + file.size);
            }
        }

        return byMonthAndDay;
    }

}

public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here


