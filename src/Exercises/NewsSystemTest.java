package Exercises;

import java.time.LocalDateTime;
import java.util.*;

/* ===================== ARTICLE ===================== */

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

/* ===================== USER ===================== */

class NUser {
    String username;
    Set<String> subscribedCategories;
    Set<String> subscribedAuthors;

    public NUser(String username) {
        this.username = username;
        this.subscribedCategories = new HashSet<>();
        this.subscribedAuthors = new HashSet<>();
    }

    void addCategory(String category) {
        subscribedCategories.add(category);
    }

    void removeCategory(String category) {
        subscribedCategories.remove(category);
    }

    void addAuthor(String author) {
        subscribedAuthors.add(author);
    }

    void removeAuthor(String author) {
        subscribedAuthors.remove(author);
    }
}

/* ===================== NEWS SYSTEM ===================== */

class NewsSystem {
    List<String> categoryNames;
    List<String> authorNames;

    Map<String, NUser> users;
    Map<String, List<Article>> articlesByCategory;

    public NewsSystem(List<String> categoryNames, List<String> authorNames) {
        this.categoryNames = categoryNames;
        this.authorNames = authorNames;
        this.users = new HashMap<>();
        this.articlesByCategory = new HashMap<>();
    }

    public void addUser(String username) {
        users.putIfAbsent(username, new NUser(username));
    }

    public void subscribeUserToCategory(String username, String categoryName) {
        users.get(username).addCategory(categoryName);
    }

    public void unsubscribeUserFromCategory(String username, String categoryName) {
        users.get(username).removeCategory(categoryName);
    }

    public void subscribeUserToAuthor(String username, String authorName) {
        users.get(username).addAuthor(authorName);
    }

    public void unsubscribeUserFromAuthor(String username, String authorName) {
        users.get(username).removeAuthor(authorName);
    }

    public void publishArticle(Article article) {
        articlesByCategory
                .computeIfAbsent(article.getCategory(), k -> new ArrayList<>())
                .add(article);
    }

    public void printNewsForUser(String username) {
        NUser user = users.get(username);
        if (user == null) return;

        Set<Article> received = new HashSet<>();

        // category subscriptions
        for (String category : user.subscribedCategories) {
            List<Article> list = articlesByCategory.get(category);
            if (list != null) {
                received.addAll(list);
            }
        }

        // author subscriptions
        for (List<Article> list : articlesByCategory.values()) {
            for (Article article : list) {
                if (user.subscribedAuthors.contains(article.getAuthor())) {
                    received.add(article);
                }
            }
        }

        List<Article> result = new ArrayList<>(received);
        result.sort(Comparator.comparing(Article::getTimestamp));

        System.out.println("News for user: " + username);
        for (Article article : result) {
            System.out.println(article);
        }
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




