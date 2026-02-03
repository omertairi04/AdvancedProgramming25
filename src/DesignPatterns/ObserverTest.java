package DesignPatterns;

import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update();
}

interface Subject {
    void attach(Observer o);

    void detach(Observer o);

    void notifyObservers();
}

class ConcreteSubject implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private int state;

    public void attach(Observer o) {
        observers.add(o);
    }

    public void detach(Observer o) {
        observers.remove(o);
    }

    public void setState(int state) {
        this.state = state;
        notifyObservers();
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}

class ObserverA implements Observer {
    public void update() {
        // react to change
    }
}

class ObserverB implements Observer {
    public void update() {
        // react differently
    }
}

public class ObserverTest {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();

        Observer a = new ObserverA();
        Observer b = new ObserverB();

        subject.attach(a);
        subject.attach(b);

        subject.setState(10); // both observers notified

    }
}
