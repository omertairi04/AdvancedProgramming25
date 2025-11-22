//package Exercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

class Item1 {
    float discountPrice;
    float price;

    public Item1(float discountPrice, float price) {
        this.discountPrice = discountPrice;
        this.price = price;
    }
}

class Store {
    String name;
    List<Item1> items;
    float averageDiscountPrice;
    float totalDiscount;

    public Store(String name) {
        this.name = name;
        items = new ArrayList<>();
        this.averageDiscountPrice = 0f;
        this.totalDiscount = 0f;
    }

    public void addItem(String item) {
        String[] split = item.split(":");
        Item1 i = new Item1(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
        totalDiscount += i.discountPrice;
        items.add(i);
    }

    public void calculateAverage() {
        float price = 0f;
        for (Item1 i : items) {
            price += i.discountPrice;
        }
        this.averageDiscountPrice = price / items.size();
    }

    @Override
    public String toString() {
        return name + "\nAverage Discount: " + averageDiscountPrice + "%\nTotal Discount: " + totalDiscount;
    }
}

class getAverageStores implements Comparator<Store> {
    @Override
    public int compare(Store o1, Store o2) {
        int priceCompare = Float.compare(o1.averageDiscountPrice, o2.averageDiscountPrice);
        if (priceCompare != 0) {
            return priceCompare;
        }

        return o1.name.compareTo(o2.name);
    }
}

class getTotalStores implements Comparator<Store> {
    @Override
    public int compare(Store o1, Store o2) {
        int priceCompare = Float.compare(o1.totalDiscount, o2.totalDiscount);
        if (priceCompare != 0) {
            return priceCompare;
        }

        return o1.name.compareTo(o2.name);
    }
}

class Discounts {
    List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("  ");
            String name = split[0].split(" ")[0];
            Store store = new Store(name);
            store.addItem(split[0].split(" ")[1]);
            for (int i = 1; i < split.length; i++) {
                store.addItem(split[i]);
            }
            store.calculateAverage();
            stores.add(store);
        }
        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        Collections.sort(stores, new getAverageStores());
        List<Store> toReturn = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            toReturn.add(stores.get(i));
        }
        return toReturn;
    }

    public List<Store> byTotalDiscount() {
        Collections.sort(stores, new getTotalStores());
        Collections.sort(stores, Collections.reverseOrder());

        List<Store> toReturn = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            toReturn.add(stores.get(i));
        }

        return toReturn;
    }

}

public class DiscountsTest {
    public static void main(String[] args) throws IOException {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde
