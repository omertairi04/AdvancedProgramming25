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
        return String.format("Student{id='%s', grades=%s}", id, grades);
    }

    int coursesPassed() {
        return grades.size();
    }

    double computeAvg() {
        return grades.stream().mapToInt(i -> i).average().orElse(0);
    }

    String getId() {
        return id;
    }
}

class Faculty {
    private final Map<String, Student> map = new HashMap<>();

    void addStudent(String id, List<Integer> grades) {
        if (map.containsKey(id)) {
            throw new RuntimeException("Student with ID " + id + " already exists");
        }
        map.put(id, new Student(id, grades));
    }

    void addGrade(String id, int grade) {
        map.get(id).grades.add(grade);
    }

    Set<Student> getStudentsSortedByAverageGrade() {

        return new TreeSet<>(
                Comparator.comparingDouble(Student::computeAvg).reversed()
                        .thenComparing(Student::coursesPassed, Comparator.reverseOrder())
                        .thenComparing(Student::getId, Comparator.reverseOrder())
        ) {{
            addAll(map.values());
        }};
    }

    Set<Student> getStudentsSortedByCoursesPassed() {

        return new TreeSet<>(
                Comparator.comparingInt(Student::coursesPassed).reversed()
                        .thenComparing(Student::computeAvg, Comparator.reverseOrder())
                        .thenComparing(Student::getId, Comparator.reverseOrder())
        ) {{
            addAll(map.values());
        }};
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
