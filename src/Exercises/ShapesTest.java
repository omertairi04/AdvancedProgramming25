package Exercises;

import java.util.*;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor); // scale up/down
}

interface Stackable {
    float weight(); // returns weight
}

class Form implements Scalable, Stackable {
    String id;
    Color color;
    float radius;
    float width;
    float height;

    // Circle constructor
    public Form(String id, Color color, float radius) {
        this.id = id;
        this.color = color;
        this.radius = radius;
        this.width = 0;
        this.height = 0;
    }

    // Rectangle constructor
    public Form(String id, Color color, float width, float height) {
        this.id = id;
        this.color = color;
        this.width = width;
        this.height = height;
        this.radius = 0;
    }

    @Override
    public void scale(float scaleFactor) {
        // TODO:
    }

    @Override
    public float weight() {
        if (radius > 0) {
            return (float) (Math.PI * (radius * radius));
        } else if (height > 0 && width > 0) {
            return height * width;
        }
        return 0;
    }


}

class Canvas2 {
    List<Form> formList;

    Canvas2() {
        this.formList = new ArrayList<>();
    }

    void add(String id, Color color, float radius) {
        Form form = new Form(id, color, radius);
        formList.add(form);
        // sort
        sortDescending();
    }

    void add(String id, Color color, float width, float height) {
        Form form = new Form(id, color, width, height);
        formList.add(form);
        // sort
        sortDescending();
    }

    void scale(String id, float scaleFactor) {
        // find form
        for (Form form : formList) {
            if (form.id.equals(id)) {
                Form toScale = form;
                if (toScale.radius > 0) {
                    toScale.radius = toScale.radius * scaleFactor;
                } else if (toScale.width > 0 && toScale.height > 0) {
                    toScale.width = toScale.width * scaleFactor;
                    toScale.height = toScale.height * scaleFactor;
                }
            }
        }

        // sort
        sortDescending();
    }

    void sortDescending() {
        for (int i = 0; i < formList.size() - 1; i++) {
            for (int j = i + 1; j < formList.size(); j++) {
                if (formList.get(i).weight() < formList.get(j).weight()) {
                    Form temp = formList.get(i);
                    formList.set(i, formList.get(j));
                    formList.set(j, temp);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Form form : formList) {
            sb.append(String.format("%s: %-4s %-7s %12.2f\n",
                    (form.radius > 0 ? "C" : "R"),
                    form.id,
                    form.color,
                    form.weight()));
        }

        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas2 canvas = new Canvas2();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

