package Exercises;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    String category;
    String name;
    String id;
    double price;
    LocalDateTime createdAt;
    int qtySold;

    public Product(String category, String name, String id, LocalDateTime createdAt, double price) {
        this.category = category;
        this.name = name;
        this.id = id;
        this.price = price;
        this.createdAt = createdAt;
        qtySold = 0;
    }

    public void increaseQtySold(int num) {
        qtySold += num;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getQtySold() {
        return qtySold;
    }

    @Override
    public String toString() {
        return String.format(
                "Product{id='%s', name='%s', createdAt=%s, price=%.2f, quantitySold=%d}",
                name,
                id,
                createdAt,
                price,
                qtySold
        );
    }

}


class OnlineShop {
    // id-> product
    Map<String, Product> products;

    OnlineShop() {
        products = new TreeMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        products.computeIfAbsent(id, f -> new Product(category, id, name, createdAt, price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {

        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException("");
        }

        product.increaseQtySold(quantity);
        return product.price * quantity;
        //return 0.0;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
//        List<List<Product>> result = new ArrayList<>();
//        result.add(new ArrayList<>());
//        return result;

        List<Product> result = new ArrayList<>();
        if (category != null) {
            result = this.products
                    .values()
                    .stream()
                    .filter(p -> Objects.equals(p.category, category))
                    .collect(Collectors.toList());
        } else {
            result =
                    new ArrayList<>(this.products
                            .values());
        }

        if (comparatorType == COMPARATOR_TYPE.OLDEST_FIRST) {
            result.sort(
                    Comparator.comparing(Product::getCreatedAt)
            );
        } else if (comparatorType == COMPARATOR_TYPE.NEWEST_FIRST) {
            result.sort(
                    Comparator.comparing(Product::getCreatedAt).reversed()
            );
        } else if (comparatorType == COMPARATOR_TYPE.LOWEST_PRICE_FIRST) {
            result.sort(
                    Comparator.comparing(Product::getPrice)
            );
        } else if (comparatorType == COMPARATOR_TYPE.HIGHEST_PRICE_FIRST) {
            result.sort(
                    Comparator.comparing(Product::getPrice).reversed()
            );
        } else if (comparatorType == COMPARATOR_TYPE.MOST_SOLD_FIRST) {
            result.sort(
                    Comparator.comparing(Product::getQtySold).reversed()
            );
        } else if (comparatorType == COMPARATOR_TYPE.LEAST_SOLD_FIRST) {
            result.sort(
                    Comparator.comparing(Product::getQtySold)
            );
        }

        List<List<Product>> pages = new ArrayList<>();

        for (int i = 0; i < result.size(); i += pageSize) {
            int end = Math.min(i + pageSize, result.size());
            pages.add(new ArrayList<>(result.subList(i, end)));
        }

        return pages;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


