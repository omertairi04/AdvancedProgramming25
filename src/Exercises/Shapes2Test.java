package Exercises;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class Canvas1 {
    String id;
    List<Integer> squareSizes;
    List<Integer> circleSizes;
    double minArea;
    double maxArea;
    double averageArea;
    double totalSquareArea;
    double totalCircleArea;

    public Canvas1(String id, List<Integer> sizes, List<Integer> circles) {
        this.id = id;
        this.squareSizes = sizes;
        this.circleSizes = circles;
    }

    void calculateAreas() {
        List<Double> allAreas = new ArrayList<>();

        // Squares
        for (int size : squareSizes) {
            double area = size * size;
            totalSquareArea += area;
            allAreas.add(area);
        }

        // Circles
        for (int radius : circleSizes) {
            double area = Math.PI * radius * radius;
            totalCircleArea += area;
            allAreas.add(area);
        }

        if (allAreas.isEmpty()) {
            minArea = maxArea = averageArea = 0;
            return;
        }

        // Calculate min, max, average
        minArea = Collections.min(allAreas);
        maxArea = Collections.max(allAreas);
        double sum = 0;
        for (double area : allAreas) sum += area;
        averageArea = sum / allAreas.size();
    }

    double getTotalArea() {
        return totalSquareArea + totalCircleArea;
    }
}

class InvalidCanvasException extends Exception {
    public InvalidCanvasException(String message) {
        super(message);
    }
}

class ShapesApplication1 {
    private double maxArea;
    List<Canvas1> canvases;

    public ShapesApplication1(double maxArea) {
        this.maxArea = maxArea;
        canvases = new ArrayList<>();
    }

    double calculateSquareArea(double size) {
        return size * size;
    }

    double calculateCircleArea(double radius) {
        return Math.PI * radius * radius;
    }

    void readCanvases(InputStream inputStream) throws InvalidCanvasException {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] split = line.split("\\s+");
            String id = split[0];
            List<Integer> sizes = new ArrayList<>();
            List<Integer> circles = new ArrayList<>();
            boolean validCanvas = true;

            try {
                for (int i = 1; i < split.length - 1; i++) {
                    String type = split[i];
                    int value = Integer.parseInt(split[i + 1]);

                    if (type.equals("S")) {
                        double area = calculateSquareArea(value);
                        if (area > maxArea)
                            throw new InvalidCanvasException("Canvas " + id + " has a shape with area larger than " + maxArea + "0");
                        sizes.add(value);
                    } else if (type.equals("C")) {
                        double area = calculateCircleArea(value);
                        if (area > maxArea)
                            throw new InvalidCanvasException("Canvas " + id + " has a shape with area larger than " + maxArea+ "0");
                        circles.add(value);
                    }

                    i++; // skip the value we just processed
                }
            } catch (InvalidCanvasException e) {
                System.out.println(e.getMessage());
                validCanvas = false;
            }

            if (validCanvas) {

                Canvas1 canvas = new Canvas1(id, sizes, circles);
                canvas.calculateAreas();
                canvases.add(canvas);
            }
        }
    }

    void printCanvases(OutputStream os) {
        canvases.sort((a, b) -> Double.compare(b.getTotalArea(), a.getTotalArea()));

        PrintWriter writer = new PrintWriter(os);
        for (Canvas1 canvas : canvases) {
            writer.println(canvas.id + " "
                    + (canvas.squareSizes.size() + canvas.circleSizes.size()) + " "
                    + canvas.circleSizes.size() + " "
                    + canvas.squareSizes.size() + " "
                    + String.format("%.2f", canvas.minArea) + " "
                    + String.format("%.2f", canvas.maxArea) + " "
                    + String.format("%.2f", canvas.averageArea));
        }
        writer.flush();
    }
}

public class Shapes2Test {

    public static void main(String[] args) throws InvalidCanvasException {
        ShapesApplication1 shapesApplication = new ShapesApplication1(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);
    }
}
