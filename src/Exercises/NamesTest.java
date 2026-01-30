package Exercises;

import java.util.*;
import java.util.stream.Collectors;

class Name implements Comparable<Name>{
    String name;
    int length;
    int uniqueLetters;

    public Name(String name, int length) {
        this.name = name;
        this.length = length;
        this.uniqueLetters = calculateUniqueLetters(name);
    }

    private int calculateUniqueLetters(String name) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < name.length(); i++) {
            String c = String.valueOf(name.charAt(i)).toUpperCase(Locale.ROOT);
            if (!set.contains(c)) {
                set.add(c);
            }
        }
        return set.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name other = (Name) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String print(int occurrences) {
        return name + " (" +occurrences +") " + uniqueLetters;
    }

    @Override
    public int compareTo(Name name) {
        int compare = this.name.compareTo(name.name);
        if (compare != 0) {
            return compare;
        }
        return Integer.compare(this.length, name.length);
    }
}

class Names {
    // Name -> Occurrence
    HashMap<Name,Integer> map;

    public Names() {
        this.map = new HashMap<>();
    }

    public void addName(String name) {
        Name key = new Name(name, name.length());
        map.put(key, map.getOrDefault(key, 0) + 1);
    }


    public void printN(int n) {

//        List<Name> names = map.values().stream()
//                .filter(k -> k.length >= n)
//                .sorted().collect(Collectors.toList());

        List<Name> names = map.entrySet().stream()
                .filter(e-> e.getValue() >= n)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        for (Name name : names) {
            System.out.println(name.print(map.get(name)));
        }


    }

    public String findName(int len, int x) {
        List<Name> filtered = map.keySet().stream()
                .filter(n -> n.length < len)   // remove length >= len
                .sorted()                      // lexicographical order
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return null; // or "" depending on your spec
        }

        int index = x % filtered.size(); // wrap around
        return filtered.get(index).name;
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde