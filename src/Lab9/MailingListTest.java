package Lab9;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

interface IUser extends Comparable<IUser> {
    void notify(String mailingListName, String text);

    String getName();
}

interface MailingList {
    void subscribe(IUser user);

    void unsubscribe(IUser user);

    void publish(String text);
}

class MailingListUser implements IUser {
    String name;
    String email;

    public MailingListUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public void notify(String mailingListName, String text){
        System.out.println("[USER] " + name + " received email from " + mailingListName +
                ": " + text);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(IUser iUser) {
        return Comparator.comparing(IUser::getName).compare(this, iUser);
    }
}

class FilteredMailingListUser implements IUser {
    String name;
    String email;
    String keyword;

    public FilteredMailingListUser(String name, String email, String keyword) {
        this.name = name;
        this.email = email;
        this.keyword = keyword;
    }

    @Override
    public void notify(String mailingListName, String text) {
        if (text.contains(keyword)) {
            System.out.println("[FILTERED USER] " + name +
                    " received filtered email from " + mailingListName +
                    ": " + text);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(IUser iUser) {
        return Comparator.comparing(IUser::getName).compare(this, iUser);
    }
}

class AdminUser implements IUser {
    String name;
    String email;

    public AdminUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public void notify(String mailingListName, String text) {
        System.out.println("[ADMIN LOG] MailingList=" + mailingListName +
                " | Message=" + text);

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(IUser iUser) {
        return Comparator.comparing(IUser::getName).compare(this, iUser);
    }
}

class SimpleMailingList implements MailingList {
    String listName;
    Set<IUser> users;

    public SimpleMailingList(String listName) {
        this.listName = listName;
        users = new TreeSet<IUser>();
    }


    @Override
    public void subscribe(IUser user) {
        this.users.add(user);
    }

    @Override
    public void unsubscribe(IUser user) {
        this.users.remove(user);
    }

    @Override
    public void publish(String text) {
        for (IUser user : this.users) {
            user.notify(this.listName, text);
        }
    }
}

public class MailingListTest {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        Map<String, MailingList> mailingLists = new HashMap<>();
        Map<String, IUser> usersByEmail = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            String[] parts = line.split(" ");

            String command = parts[0];

            switch (command) {

                case "CREATE_LIST": {
                    String listName = parts[1];
                    mailingLists.put(listName, new SimpleMailingList(listName));
                    break;
                }

                case "ADD_USER": {
                    String listName = parts[1];
                    String type = parts[2];
                    String name = parts[3];
                    String email = parts[4];

                    IUser user;
                    if (type.equals("NORMAL")) {
                        user = new MailingListUser(name, email);
                    } else if (type.equals("FILTERED")) {
                        String keyword = parts[5];
                        user = new FilteredMailingListUser(name, email, keyword);
                    } else { // ADMIN
                        user = new AdminUser(name, email);
                    }

                    usersByEmail.put(email, user);
                    mailingLists.get(listName).subscribe(user);
                    break;
                }

                case "REMOVE_USER": {
                    String listName = parts[1];
                    String email = parts[2];

                    IUser user = usersByEmail.get(email);
                    mailingLists.get(listName).unsubscribe(user);
                    break;
                }

                case "PUBLISH": {
                    String listName = parts[1];
                    String text = line.substring(
                            line.indexOf(listName) + listName.length() + 1
                    );
                    mailingLists.get(listName).publish(text);
                    break;
                }
            }
        }
    }
}

