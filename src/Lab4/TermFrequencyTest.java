package Lab4;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.*;
import java.util.stream.Collectors;

class TermFrequency {
    private Map<String, Integer> freqMap = new TreeMap<>();
    private Set<String> stopSet = new HashSet<>();
    private int totalWords = 0;

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        Collections.addAll(stopSet, stopWords);

        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNext()) {
            String word = scanner.next()
                    .toLowerCase()
                    .replaceAll("[,.]", ""); // ignore punctuation

            if (word.isEmpty()) continue;
            if (stopSet.contains(word)) continue;

            totalWords++;
            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        }
    }

    int countTotal() {
        return totalWords;
    }

    int countDistinct() {
        return freqMap.size();
    }

    List<String> mostOften(int k) {
        return freqMap.entrySet()
                .stream()
                .sorted((a, b) -> {
                    if (!a.getValue().equals(b.getValue()))
                        return b.getValue() - a.getValue(); // sort by frequency desc
                    return a.getKey().compareTo(b.getKey()); // alphabetical if tie
                })
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde

