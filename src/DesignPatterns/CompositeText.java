package DesignPatterns;

import java.util.ArrayList;
import java.util.List;

interface Component {
    void operation();
}

class LeafA implements Component {
    @Override
    public void operation() {
        System.out.println("LeafA");
    }
}

class LeafB implements Component {
    @Override
    public void operation() {
        System.out.println("LeafB");
    }
}

class Composite implements Component {
    List<Component> children = new ArrayList<>();

    void add(Component c) {
        children.add(c);
    }

    public void operation() {
        for (Component c : children) {
            c.operation();
        }
    }
}

public class CompositeText {
    public static void main(String[] args) {
        Component a = new LeafA();
        Component b = new LeafB();

        Composite group = new Composite();
        group.add(a);
        group.add(b);

        group.operation(); // uniform treatment
    }
}

