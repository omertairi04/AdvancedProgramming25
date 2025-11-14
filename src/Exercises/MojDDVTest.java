package Exercises;

import javax.security.auth.Refreshable;
import java.io.InputStream;
import java.util.*;

class Item {
    int id;
    List<Integer> prices;
    List<String> types;

    public Item(int id) {
        this.id = id;
        prices = new ArrayList<>();
        types = new ArrayList<>();
    }

    void addPrice(int price) {
        prices.add(price);
    }

    void addType(String type) {
        types.add(type);
    }
}

class MojDDV {
    List<Item> items;

    public MojDDV() {
        this.items = new ArrayList<>();
    }

    void readRecords(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            String[] split = line.split(" ");

            int id = Integer.parseInt(split[0]);

            Item item = new Item(id);
            for (int i = 1; i < split.length - 1; i += 2) {
                int price = Integer.parseInt(split[i]);
                String type = split[i + 1];

                item.addPrice(price);
                item.addType(type);
            }
            items.add(item);
        }

        System.out.println(items.size());
    }


}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
//        mojDDV.printTaxReturns(System.out);

    }
}
