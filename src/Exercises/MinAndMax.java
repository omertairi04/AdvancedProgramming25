package Exercises;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    T min, max;
    int updateCounter;
    public MinMax() {
        min = null;
        max = null;
        updateCounter = 0;
    }

    void update(T element) {
        boolean updated = false;
        if ((min ==null ) || element.compareTo(min) < 0) {
            min = element;
            updated = true;
        }
        if ((max == null) || element.compareTo(max) > 0) {
            max = element;
            updated = true;
        }

        if (updated) {updateCounter++;}

    }

    @Override
    public String toString() {
        return min + " " + max + " " + updateCounter + "\n";
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
