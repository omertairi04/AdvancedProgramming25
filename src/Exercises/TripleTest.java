package Exercises;

import java.util.*;

class Triple<T extends Number & Comparable<T>> implements Comparator<T> {
    List<T> list;

    public Triple(T a, T b, T c) {
        list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
    }

    double max() {
        return Math.max(
                list.get(0).doubleValue(),
                Math.max(list.get(1).doubleValue(), list.get(2).doubleValue())
        );
    }

    double average() {
        return (list.get(0).doubleValue()
                + list.get(1).doubleValue()
                + list.get(2).doubleValue()) / 3.0;
    }

    void sort() {
        Collections.sort(list);
    }

    @Override
    public int compare(T t, T t1) {
        return t.compareTo(t1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(String.format("%.2f", list.get(i).doubleValue()));
            if (i < list.size() - 1) sb.append(" ");
        }
        return sb.toString();
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple



