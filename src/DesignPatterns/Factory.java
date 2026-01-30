package DesignPatterns;

interface Product {
    void use();
}

class ProductA implements Product {
    public void use() { }
}

class ProductB implements Product {
    public void use() { }
}

class ProductFactory {
    static Product create(String type) {
        if (type.equals("A")) return new ProductA();
        if (type.equals("B")) return new ProductB();
        throw new IllegalArgumentException();
    }
}

public class Factory {
    Product p = ProductFactory.create("A");
}
