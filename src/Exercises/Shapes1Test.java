package Exercises;

import javax.security.auth.Refreshable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Canvas {
    String id;
    List<Integer> numbers;
    public int sqrNum;
    private int perimeter;

    public Canvas(String id, List<Integer> numbers, int sqrNum) {
        this.id = id;
        this.numbers = numbers;
        this.sqrNum = sqrNum;
        perimeter = 0;
    }

    void addNumber(int number) {
        numbers.add(number);
    }

    void increaseSqrNum() {
        sqrNum++;
    }

    @Override
    public String toString() {
        return "Canvas{" +
                "id='" + id + '\'' +
                ", numbers=" + numbers +
                ", sqrNum=" + sqrNum +
                '}';
    }

    void calculatePerimeter() {
        perimeter = 0;
        for (int n : numbers) {
            perimeter += 4 * n;
        }
    }

    void setPerimeter(int perimeter) {
        this.perimeter = perimeter;
    }

    int getPerimeter() {
        return perimeter;
    }

    String getId() {
        return id;
    }



}

class ShapesApplication {
    List<Canvas> canvases;

    public ShapesApplication() {
        canvases = new ArrayList<>();
    }

    int readCanvases(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(" ");
            var id = parts[0];
            List<Integer> numbers = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                numbers.add(Integer.parseInt(parts[i]));
            }

            Canvas canvas = new Canvas(id, numbers, numbers.size());
            canvas.calculatePerimeter();
            canvases.add(canvas);

        }

        int totalSquares = 0;
        for (Canvas canvas : canvases) {
            totalSquares += canvas.sqrNum;
        }
        return totalSquares;
    }

    void printLargestCanvasTo(OutputStream outputStream) {
        Canvas largest = canvases.get(0);

        for (Canvas c : canvases) {
            if (c.getPerimeter() > largest.getPerimeter()) {
                largest = c;
            }
        }

        String result = largest.id + " " + largest.sqrNum + " " + largest.getPerimeter() + "\n";
        try {
            outputStream.write(result.getBytes());
            outputStream.flush();
        } catch( Exception e) {
            e.printStackTrace();
        }
    }

}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
