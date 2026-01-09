package Exercises;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/

class DeliveryDriver {
    String id;
    String name;
    Location location;
    int deliveries;
    float earned;

    public DeliveryDriver(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.deliveries = 0;
        this.earned = 0;
    }

    void completeDelivery(float distance) {
        this.earned += 90;
        this.earned += 10 + (distance / 10);
        this.deliveries++;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return "ID: " + this.id + " Name: " + this.name + " Total orders: " + deliveries
                + " Total amount earned: " + df.format(this.earned)
                + " Average amount earned: " + (deliveries > 0 ? df.format(this.earned / deliveries) : "0.00");
    }
}

class Restaurant {
    String id;
    String name;
    Location location;
    int orders;
    int earned;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    void completeDelivery(float price) {
        this.earned += price;
        orders++;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return "ID: " + this.id + " Name: " + this.name + " Total orders: " + orders
                + " Total amount earned: " + df.format(this.earned) +
                " Average amount earned: " + (orders > 0 ? df.format((this.earned / orders)) : "0.00");
    }
}

class User {
    String id;
    String name;
    HashMap<String, Location> addresses;
    float spent;
    int orders;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.addresses = new HashMap<>();
        this.spent = 0;
        this.orders = 0;
    }

    void addAddress(String name, Location location) {
        this.addresses.put(name, location);
    }

    void spend(float spent) {
        this.spent += spent;
        orders++;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return "ID: " + this.id + " Name: " + this.name + " Total orders: " + orders
                + " Total amount spent: " + df.format(this.spent) +
                " Average amount spent: " + (orders > 0 ? df.format((this.spent / orders)) : "0.00");
    }
}

class DeliveryApp {
    String name;
    HashMap<String, DeliveryDriver> drivers;
    HashMap<String, Restaurant> restaurants;
    HashMap<String, User> users;

    public DeliveryApp(String name) {
        this.name = name;
        this.drivers = new HashMap<>();
        this.restaurants = new HashMap<>();
        this.users = new HashMap<>();
    }

    void registerDeliveryPerson(String id, String name, Location currentLocation) {
        this.drivers.put(id, new DeliveryDriver(id, name, currentLocation));
    }

    void addRestaurant(String id, String name, Location location) {
        this.restaurants.put(id, new Restaurant(id, name, location));
    }

    void addUser(String id, String name) {
        this.users.put(id, new User(id, name));
    }

    void addAddress(String id, String addressName, Location location) {
        User user = this.users.get(id);
        user.addAddress(addressName, location);
    }

    void orderFood(String userId, String userAddressName,
                   String restaurantId, float cost) {

        Restaurant restaurant = restaurants.get(restaurantId);

        DeliveryDriver closestDriver =
                drivers.values().stream()
                        .min(Comparator.comparing(
                                driver -> driver.location.distance(restaurant.location)
                        ))
                        .orElseThrow(() -> new RuntimeException("No drivers available"));

        User user = users.get(userId);
        Location userLocation = user.addresses.get(userAddressName);

        float distance =
                closestDriver.location.distance(restaurant.location);

        closestDriver.location = userLocation;
        closestDriver.completeDelivery(distance);

        user.spend(cost);
        restaurant.completeDelivery(cost);
    }

    void printUsers() {
        List<User> toPrint = users.values().stream()
                .sorted(
                        Comparator.comparingDouble((User u) -> u.spent)
                                .thenComparing(u -> u.name).reversed())
                .collect(Collectors.toList());

        for (User user : toPrint) {
            System.out.println(user);
        }
    }

    void printRestaurants() {
        List<Restaurant> reses = restaurants.values().stream()
                .sorted(Comparator.comparingDouble((Restaurant r) -> r.earned)
                        .thenComparing(r -> r.name).reversed())
                .collect(Collectors.toList());

        for (Restaurant r : reses) {
            System.out.println(r);
        }
    }

    void printDeliveryPeople() {
        List<DeliveryDriver> dr = drivers.values().stream()
                .sorted(Comparator.comparingDouble((DeliveryDriver d) -> d.earned)
                        .thenComparing(d -> d.name).reversed())
                .collect(Collectors.toList());

        for (DeliveryDriver driver : dr) {
            System.out.println(driver);
        }
    }
}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
