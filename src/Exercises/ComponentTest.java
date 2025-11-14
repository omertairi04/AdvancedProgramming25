package Exercises;

import java.util.*;

class InvalidPositionException extends Exception {
    public InvalidPositionException(int id) {
        super("Invalid position: " + id + ", already taken");
    }
}

class Component {
    String color;
    int weight;
    List<Component> components;

    public Component(String color, int weight) {
        this.weight = weight;
        this.color = color;
        components = new ArrayList<>();
    }

    void addComponent(Component component) {
        components.add(component);
        for (int i = 0; i < components.size() - 1; i++) {
            if (components.get(i).weight > component.weight) {
                Component comp = components.get(i);
                components.set(i, component);
                if (i++ == components.size())
                    components.set(i++, comp);
                else
                    components.add(comp);
            }

            if (components.get(i).color.charAt(0) == component.color.charAt(0)) {
                for (int j = 0; j < Math.min(components.get(i).color.length(), component.color.length()); j++) {
                    if (components.get(j).color.charAt(j) > component.color.charAt(j)) {
                        Component temp = components.get(i);
                        components.set(i, component);
                        if (i++ == components.size()) {
                            components.set(i++, temp);
                        } else
                            components.add(temp);
                    }
                }
            } else if (components.get(i).color.charAt(0) > component.color.charAt(0)) {
                Component temp = components.get(i);
                components.set(i, component);
                if (i++ == components.size()) {
                    components.set(i++, temp);
                } else
                    components.add(temp);
            }
        }
    }

    @Override
    public String toString() {
        return "Component{" +
                "weight=" + weight +
                ", color='" + color + '\'' +
                '}';
    }
}

class Window {
    String name;
    List<Component> components;

    public Window(String name) {
        this.name = name;
        this.components = new ArrayList<>();
    }

    void addComponent(int position, Component component) throws InvalidPositionException {
        if (position < 0 || position > components.size()) {
            throw new InvalidPositionException(position);
        }
        components.add(position, component);
    }

    void changeColor(int weight, String color) {
        for (Component component : components) {
            if (component.weight < weight) {
                component.color = color;
            }
        }
    }

    void switchComponents(int pos1, int post2) {
        if (pos1 >= 0 && pos1 < components.size() && post2 >= 0 && post2 < components.size()) {
            Component temp = components.get(pos1);
            components.set(pos1, components.get(post2));
            components.set(post2, temp);
        }
    }

    @Override
    public String toString() {
        return "Window name: " + name + "\n";
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.switchComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде
