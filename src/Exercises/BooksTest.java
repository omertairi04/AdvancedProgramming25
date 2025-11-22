package Exercises;

import java.util.*;

class Book {
    String title;
    String category;
    float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    @Override
    public String toString() {
        return title + " (" + category + ") " + String.format("%.2f", price);
    }
}

class SortBooks implements Comparator<Book> {

    @Override
    public int compare(Book b1, Book b2) {
        int titleCompare = b1.title.compareTo(b2.title);
        if (titleCompare != 0) {
            return titleCompare;
        }

        return Double.compare(b1.price, b2.price);
    }
}

class SortBookByPrice implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        return Double.compare(b1.price, b2.price);
    }
}

class BookCollection {
    HashMap<String, List<Book>> books;

    public BookCollection() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book) {
        books.computeIfAbsent(book.category, b -> new ArrayList<>()).add(book);
    }

    public void printByCategory(String category) {
        List<Book> bookList = books.get(category);

        Collections.sort(bookList, new SortBooks());

        for (Book book : bookList) {
            System.out.println(book);
        }


    }

    public List<Book> getCheapestN(int n) {

        List<Book> allBooks = new ArrayList<>();

        for (List<Book> bookList : books.values()) {
            allBooks.addAll(bookList);  // add all books from this city/list
        }

        Collections.sort(allBooks, new SortBookByPrice());

        if (n >= books.size()) {

            return allBooks;
        }

        int i = 0;
        List<Book> toReturn = new ArrayList<>();

        while (i <= n) {
            toReturn.add(allBooks.get(i));
            i++;
        }

        Collections.sort(toReturn, new SortBookByPrice());

        return toReturn;
    }
}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде
