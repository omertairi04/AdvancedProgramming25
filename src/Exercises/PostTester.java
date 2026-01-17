package Exercises;

import java.util.*;
import java.util.stream.Collectors;

class Comment {
    String author;
    String commentId;
    String content;
    String replyToId;
    int likes;

    Comment(String author, String commentId, String content, String replyToId) {
        this.author = author;
        this.commentId = commentId;
        this.content = content;
        this.replyToId = replyToId;
        this.likes = 0;
    }

    public String toString(String tab) {
        return "Comment: " + content +
                "\n" + tab + "Written by: " + author + "\n" + tab + "Likes: " + likes + "\n";
    }
}

class Post {
    String username;
    String postContent;
    HashMap<String, Comment> comments;

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        this.comments = new HashMap<>();
    }

    void addComment(String username, String commentId, String content, String replyToId) {
        Comment c = new Comment(username, commentId, content, replyToId);
        comments.put(commentId, c);
    }

    void likeComment(String commentId) {
        Comment comment = comments.get(commentId);
        if (comment != null) {
            comment.likes++;
        }
    }

    @Override
    public String toString() {
        List<Comment> commentList = comments.values().stream()
                .sorted(Comparator.comparingInt((Comment c) -> c.likes).reversed())
                .collect(Collectors.toList());

        String tab = "        ";
        StringBuilder sb = new StringBuilder();
        sb.append("Post: " + postContent + "\nWritten by: " + username + "\nComments: \n");
        for (Comment comment : commentList) {
            if (comment.replyToId != null) {
                tab += "    ";
            }
            sb.append(tab).append(comment.toString(tab));
        }

        return sb.toString();
    }
}

public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}
