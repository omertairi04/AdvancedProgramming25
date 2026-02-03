package DesignPatterns;

interface Strategy {
    void execute();
}

class StrategyA implements Strategy {
    public void execute() {
        // algorithm A
    }
}
class StrategyB implements Strategy {
    public void execute() {
        // algorithm B
    }
}

class Context {
    private Strategy strategy;

    void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    void perform() {
        strategy.execute();
    }
}

public class StrategyTest {
    public static void main(String[] args) {
        Context c = new Context();

        c.setStrategy(new StrategyA());
        c.perform();

        c.setStrategy(new StrategyB());
        c.perform();

    }
}
