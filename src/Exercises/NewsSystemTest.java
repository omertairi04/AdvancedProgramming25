package Exercises;

import java.time.LocalDateTime;
import java.util.*;

class Article {
    private final String category;
    private final String author;
    private final String content;
    private final LocalDateTime timestamp;

    public Article(String category, String author, String content, LocalDateTime timestamp) {
        this.category = category;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + author + " - " + category + "\n" + content;
    }
}

interface Observer {
    void update(Article article);
}

interface Subject {
    void subscribe(Observer o);

    void unsubscribe(Observer o);

    void notifyObservers(Article article);
}

class Category implements Subject {
    String name;
    Set<Observer> observers;

    public Category(String name) {
        this.name = name;
        this.observers = new HashSet<>();
    }

    @Override
    public void subscribe(Observer o) {
        observers.add(o);
    }

    @Override
    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Article article) {
        observers.forEach(o -> o.update(article));
    }
}

class Author implements Subject {
    String name;
    Set<Observer> observers;

    public Author(String name) {
        this.name = name;
        this.observers = new HashSet<>();
    }

    @Override
    public void subscribe(Observer o) {
        observers.add(o);
    }

    @Override
    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Article article) {
        observers.forEach(o -> o.update(article));
    }
}

class NUser implements Observer {
    String username;
    List<Article> recievedArticles;

    public NUser(String username) {
        this.username = username;
        this.recievedArticles = new ArrayList<>();
    }

    @Override
    public void update(Article article) {
        if (!recievedArticles.contains(article)) {
            recievedArticles.add(article);
        }
    }

    public void printNews() {
        System.out.println("News for user: " + username);
        recievedArticles.stream()
                .sorted(Comparator.comparing(Article::getTimestamp))
                .forEach(System.out::println);
    }
}

class NewsSystem {

    Map<String, Category> categories;
    Map<String, Author> authors;
    Map<String, NUser> users;

    public NewsSystem(List<String> categoryNames, List<String> authorNames) {
        categories = new HashMap<>();
        authors = new HashMap<>();
        users = new HashMap<>();

        categoryNames.forEach(c -> categories.put(c, new Category(c)));
        authorNames.forEach(a -> authors.put(a, new Author(a)));
    }

    public void addUser(String username) {
        users.putIfAbsent(username, new NUser(username));
    }

    public void subscribeUserToCategory(String username, String categoryName) {
        NUser user = users.get(username);
        Category category = categories.get(categoryName);
        category.subscribe(user);
    }

    public void unsubscribeUserFromCategory(String username, String categoryName) {
        NUser user = users.get(username);
        Category category = categories.get(categoryName);
        category.unsubscribe(user);
    }

    public void subscribeUserToAuthor(String username, String authorName) {
        NUser user = users.get(username);
        Author author = authors.get(authorName);
        author.subscribe(user);
    }

    public void unsubscribeUserFromAuthor(String username, String authorName) {
        NUser user = users.get(username);
        Author author = authors.get(authorName);
        author.unsubscribe(user);
    }

    public void publishArticle(Article article) {
        categories.get(article.getCategory()).notifyObservers(article);
        authors.get(article.getAuthor()).notifyObservers(article);
    }

    public void printNewsForUser(String username) {
        users.get(username).printNews();
    }
}


public class NewsSystemTest {

    public static void main(String[] args) {

        // Hardcoded categories and authors
        List<String> categories = List.of(
                "Technology", "Sports", "Politics", "Health", "Science",
                "Business", "Education", "Culture", "Travel", "Entertainment"
        );

        List<String> authors = List.of(
                "MartinFowler", "JohnDoe", "AliceSmith", "BobBrown", "JaneMiller"
        );

        NewsSystem system = new NewsSystem(categories, authors);

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String command = parts[0];

            switch (command) {

                case "ADD_USER":
                    system.addUser(parts[1]);
                    break;

                case "SUBSCRIBE_CATEGORY": {
                    String[] p = parts[1].split("\\s+");
                    system.subscribeUserToCategory(p[0], p[1]);
                    break;
                }

                case "UNSUBSCRIBE_CATEGORY": {
                    String[] p = parts[1].split("\\s+");
                    system.unsubscribeUserFromCategory(p[0], p[1]);
                    break;
                }

                case "SUBSCRIBE_AUTHOR": {
                    String[] p = parts[1].split("\\s+");
                    system.subscribeUserToAuthor(p[0], p[1]);
                    break;
                }

                case "UNSUBSCRIBE_AUTHOR": {
                    String[] p = parts[1].split("\\s+");
                    system.unsubscribeUserFromAuthor(p[0], p[1]);
                    break;
                }

                case "PUBLISH": {
                    // format:
                    // PUBLISH <category> <author> <timestamp> <content>
                    String[] p = parts[1].split("\\s+", 4);
                    Article article = new Article(
                            p[0],
                            p[1],
                            p[3],
                            LocalDateTime.parse(p[2])
                    );
                    system.publishArticle(article);
                    break;
                }

                case "PRINT":
                    system.printNewsForUser(parts[1]);
                    break;
            }
        }
    }
}
