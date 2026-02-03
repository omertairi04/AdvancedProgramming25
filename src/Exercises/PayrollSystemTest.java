package Exercises;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

interface Employee {
}

class HourlyUser implements Employee {
    String id;
    String type; // H - HOURLY , F - FREELANCER
    int level;
    double hourlyRate;

    public HourlyUser(String id, String type, int level, double hourlyRate) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "HourlyUser{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", level=" + level +
                ", hourlyRate=" + hourlyRate +
                '}';
    }
}

class FreelanceUser implements Employee {
    String id;
    String type; // H - HOURLY , F - FREELANCER
    int level;
    List<Integer> ticketPoints;

    public FreelanceUser(String id, String type, int level, List<Integer> ticketPoints) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.ticketPoints = ticketPoints;
    }

    @Override
    public String toString() {
        return "FreelanceUser{" +
                "level=" + level +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}


class PayrollSystem {
    Map<String, Double> hourlyRate;
    Map<String, Double> ticketRate;

    // level
    Map<Integer, List<Employee>> users;

    public PayrollSystem(Map<String, Double> hourlyRate, Map<String, Double> ticketRate) {
        this.hourlyRate = hourlyRate;
        this.ticketRate = ticketRate;
        this.users = new TreeMap<>();
    }

    void readEmployees(InputStream is) {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            if (parts[0] == "F") {
                String id = parts[1];
                int level = Integer.parseInt(parts[2].replaceAll("[\\D]", ""));
                List<Integer> ticketPoints = new ArrayList<>();
                for (int i = 3; i < parts.length; i++) {
                    ticketPoints.add(Integer.parseInt(parts[i]));
                }
                FreelanceUser f = new FreelanceUser(id, "F", level, ticketPoints);
                this.users.computeIfAbsent(level, k -> new ArrayList<>()).add(f);
            } else {
                String id = parts[1];
                int level = Integer.parseInt(parts[2].replaceAll("[\\D]", ""));
                double hourlyRate = Double.parseDouble(parts[3]);
                HourlyUser h = new HourlyUser(id, "H", level, hourlyRate);
                this.users.computeIfAbsent(level, k -> new ArrayList<>()).add(h);
            }
        }
    }

//    public Map<String, Collection<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels) {
//        StringBuilder builder = new StringBuilder();
//
//        for (Map.Entry<String, Double> entry : hourlyRate.entrySet()) {
//            System.out.println("KEY: " +  entry.getKey());
//        }
//
//
//    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {

        for (Map.Entry<Integer, List<Employee>> entry : users.entrySet()) {
            System.out.println("KEY: " + entry.getKey());
            for (Employee e : entry.getValue()) {
                System.out.println(e.toString());
            }
        }

        return new HashMap<>();
    }


}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
        });


    }
}