package Lab1;

import java.util.ArrayList;
import java.util.List;

// ------------------- Exceptions -------------------
class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(String message) {
        super(message);
    }
}

// ------------------- Interfaces & Enums -------------------
interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();
    int getCurrentYPosition();
}

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

// ------------------- MovingPoint -------------------
class MovingPoint implements Movable {
    int x;
    int y;
    int xSpeed;
    int ySpeed;

    public MovingPoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() {
        y += ySpeed;
    }

    @Override
    public void moveDown() {
        y -= ySpeed;
    }

    @Override
    public void moveLeft() {
        x -= xSpeed;
    }

    @Override
    public void moveRight() {
        x += xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return "MovingPoint(" + x + ", " + y + ")";
    }
}

// ------------------- MovingCircle -------------------
class MovingCircle implements Movable {
    int radius;
    MovingPoint center;

    public MovingCircle(int radius, MovingPoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() {
        center.moveUp();
    }

    @Override
    public void moveDown() {
        center.moveDown();
    }

    @Override
    public void moveLeft() {
        center.moveLeft();
    }

    @Override
    public void moveRight() {
        center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return "MovingCircle(center=" + center + ", radius=" + radius + ")";
    }
}

// ------------------- MovablesCollection -------------------
class MovablesCollection {
    List<Movable> movables;
    static int maxX;
    static int maxY;

    public MovablesCollection(int maxX, int maxY) {
        MovablesCollection.maxX = maxX;
        MovablesCollection.maxY = maxY;
        this.movables = new ArrayList<>();
    }

    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        // Check if inside boundaries
        if ((m.getCurrentXPosition() >= 0 && m.getCurrentXPosition() <= maxX)
                && (m.getCurrentYPosition() >= 0 && m.getCurrentYPosition() <= maxY)) {
            movables.add(m);
        } else {
            throw new MovableObjectNotFittableException("Object does not fit inside the collection area!");
        }
    }

    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        for (Movable m : movables) {
            boolean isPoint = (m instanceof MovingPoint);
            boolean isCircle = (m instanceof MovingCircle);

            if ((type == TYPE.POINT && isPoint) || (type == TYPE.CIRCLE && isCircle)) {
                switch (direction) {
                    case UP -> m.moveUp();
                    case DOWN -> m.moveDown();
                    case LEFT -> m.moveLeft();
                    case RIGHT -> m.moveRight();
                }

                // Optional boundary check
                if (m.getCurrentXPosition() < 0 || m.getCurrentXPosition() > maxX
                        || m.getCurrentYPosition() < 0 || m.getCurrentYPosition() > maxY) {
                    throw new ObjectCanNotBeMovedException("Object moved out of bounds!");
                }
            }
        }
    }

    public void setxMax(int maxX) {
        MovablesCollection.maxX = maxX;
    }

    public void setyMax(int maxY) {
        MovablesCollection.maxY = maxY;
    }

    @Override
    public String toString() {
        return "MovablesCollection{\n" +
                "movables=" + movables +
                "\n}";
    }
}

// ------------------- Main Class -------------------
public class Ex2 {
    public static void main(String[] args) {
        try {
            MovablesCollection collection = new MovablesCollection(100, 100);

            MovingPoint p1 = new MovingPoint(10, 10, 5, 5);
            MovingCircle c1 = new MovingCircle(10, new MovingPoint(50, 50, 5, 5));

            collection.addMovableObject(p1);
            collection.addMovableObject(c1);

            System.out.println("Before move:");
            System.out.println(collection);

            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.UP);
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.RIGHT);

            System.out.println("\nAfter move:");
            System.out.println(collection);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
