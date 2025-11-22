package Exercises;

import java.util.*;

class Participant {
    String code;
    String name;
    int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return code + " " + name + " " + age;
    }
}

class Audition {
    HashMap<String, HashSet<Participant>> participants;

    public Audition() {
        participants = new HashMap<>();
    }

    void addParticpant(String city, String code, String name, int age) {
        participants.computeIfAbsent(city, c -> new HashSet<>()).add(new Participant(code, name, age));
    }

    void listByCity(String city) {
        HashSet<Participant> participant = participants.get(city);
        List<Participant> list = new ArrayList<>(participant);

        list.sort(((p1, p2) -> {
            int compare = p1.name.compareTo(p2.name);
            if (compare != 0) return compare;
            return Integer.compare(p1.age, p2.age);
        }));
        for (Participant p : list) {
            System.out.println(p);
        }
    }

}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}