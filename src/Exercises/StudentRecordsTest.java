package Exercises;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class Student1 {
    String code;
    String nasoka;
    List<Integer> grades;
    float average;

    public Student1(String code, String nasoka, List<Integer> grades) {
        this.code = code;
        this.nasoka = nasoka;
        this.grades = grades;
        this.average = calculateAverage();
    }

    private float calculateAverage() {
        int sum = 0;
        for (int g : grades) {
            sum += g;
        }
        return (float) sum / grades.size();
    }


}


class StudentRecords {
    // nasoka
    Map<String, List<Student1>> students;

    public StudentRecords() {
        this.students = new TreeMap<>();
    }

    int readRecords(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        int count = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");

            String code = parts[0];
            String nasoka = parts[1];

            List<Integer> grades = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                grades.add(Integer.parseInt(parts[i]));
            }

            Student1 student = new Student1(code, nasoka, grades);

            students.computeIfAbsent(nasoka, k -> new ArrayList<>())
                    .add(student);

            count++;
        }

        return count;
    }

    void writeTable(OutputStream outputStream) {
        try {
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, List<Student1>> entry : students.entrySet()) {
                String nasoka = entry.getKey();
                List<Student1> list = entry.getValue();

                sb.append(nasoka).append("\n");

                list.stream()
                        .sorted(
                                Comparator
                                        .comparingDouble((Student1 s) -> s.average).reversed()
                                        .thenComparing(s -> s.code)
                        )
                        .forEach(s -> sb.append(
                                String.format("%s %.2f%n", s.code, s.average)
                        ));
            }

            outputStream.write(sb.toString().getBytes());
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    void writeDistribution(OutputStream outputStream) {

    }

}

public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here