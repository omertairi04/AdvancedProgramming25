package Lab4;

import java.util.*;
import java.util.function.Function;

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student{id='").append(id).append("', grades=[");

        for (int i = 0; i < grades.size(); i++) {
            sb.append(grades.get(i));
            if (i < grades.size() - 1) sb.append(", ");
        }

        sb.append("]}");
        return sb.toString();
    }

    int coursesPassed() {
        return this.grades.size();
    }

    int computeAvg() {
        int totalGrades = 0;
        for (int i = 0; i < grades.size(); i++) {
            totalGrades += grades.get(i);
        }
        return totalGrades / grades.size();
    }

    String getId() {
        return id;
    }
}

class Faculty {
    HashMap<String, Student> map;

    public Faculty() {
        map = new HashMap<>();
    }

    void addStudent(String id, List<Integer> grades) {
        if (map.containsKey(id)) {
            System.out.println("Student with ID " + id + " already exists!");
        } else {
            map.put(id, new Student(id, grades));
        }
    }

    void addGrade(String id, int grade) {
        map.get(id).grades.add(grade);
    }

    Set<Student> getStudentsSortedByAverageGrade() {

        TreeSet<Student> sorted = new TreeSet<>(
                Comparator.comparingInt(Student::computeAvg)
                        .thenComparing(Student::getId)
        );

        sorted.addAll(map.values());   // ← YOU FORGOT THIS

        return sorted;
    }

    Set<Student> getStudentsSortedByCoursesPassed() {

        TreeSet<Student> sorted = new TreeSet<>(
                Comparator.comparingInt(Student::coursesPassed)
                        .thenComparing(Student::computeAvg)
                        .reversed()
        );

        sorted.addAll(map.values());   // ← AND THIS

        return sorted;
    }
}

public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}
