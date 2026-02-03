package DesignPatterns;

interface DComponent {
    void operation();
}

class ConcreteComponent implements DComponent {
    @Override
    public void operation() {
        System.out.println("ConcreteComponent operation");
    }
}

abstract class Decorator implements DComponent {
    protected DComponent component;

    public Decorator(DComponent component) {
        this.component = component;
    }
}

class DecoratorA extends Decorator {
    DecoratorA(DComponent c) {
        super(c);
    }

    public void operation() {
        component.operation();
        // extra behavior A
    }
}

class DecoratorB extends Decorator {
    DecoratorB(DComponent c) {
        super(c);
    }

    public void operation() {
        component.operation();
        // extra behavior A
    }
}



public class DecoratorTest {
    public static void main(String[] args) {
        DComponent obj =
                new DecoratorB(
                        new DecoratorA(
                                new ConcreteComponent()
                        )
                );

        obj.operation();

    }
}
