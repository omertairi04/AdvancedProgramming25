package Lab1;

import java.util.Objects;
import java.util.Random;

class Bank {
    String name;
    Account[] accounts;

    public double totalTransfers = 0;
    public double totalProvisions = 0;
    Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = accounts;
    }

    public boolean makeTransaction(Transaction t) {

        Account fromAccount = null;
        Account toAccount = null;

        for (int i = 0; i < accounts.length; i++) {
            if (t.fromId == accounts[i].getId()) {
                fromAccount = accounts[i];
            }
            if (t.toId == accounts[i].getId()) {
                toAccount = accounts[i];
            }
        }


        if (fromAccount == null || toAccount == null) return false;

        if (t.amount > fromAccount.getBalance()) {
            return false;
        } else {
            fromAccount.setBalance(fromAccount.getBalance() - t.amount);
            toAccount.setBalance(toAccount.getBalance() + t.amount);
            totalTransfers += t.amount;
            if (t instanceof FlatPercentProvisionTransaction) {
                totalProvisions += ((FlatPercentProvisionTransaction) t).centsPerDollar;
            }
        }

        return true;
    }


    @Override
    public String toString() {
        return "Name: " + name + "\n";
    }
}

class Account {
    String name;
    long id;
    double balance;

    Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.id = generateRandom();
    }

    public long generateRandom() {
        Random rand = new Random();
        return rand.nextLong();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nBalance: " + balance + "\n";
    }
}

abstract class Transaction {
    long fromId;
    long toId;
    String description;
    double amount;

    Transaction(long fromId, long toId, String description, double amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

}

class FlatAmountProvisionTransaction extends Transaction {
    double flatProvision;

    FlatAmountProvisionTransaction(long fromId, long toId, double amount, double flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    public double getFlatAmount() {
        return flatProvision;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return Double.compare(flatProvision, that.flatProvision) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flatProvision);
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    int centsPerDollar;

    FlatPercentProvisionTransaction(long fromId, long toId, double amount, int centsPerDolar) {
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDollar = centsPerDolar;
    }

    public int getPercent() {
        return centsPerDollar;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return centsPerDollar == that.centsPerDollar;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(centsPerDollar);
    }
}

public class Ex1 {
    public static void main(String[] args) {

    }
}
