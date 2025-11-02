package Lab1;

import java.util.Arrays;
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

        double provision;

        if (t instanceof FlatAmountProvisionTransaction) {
            FlatAmountProvisionTransaction fa = (FlatAmountProvisionTransaction) t;
            provision = fa.getFlatAmount();
        } else if (t instanceof FlatPercentProvisionTransaction) {
            FlatPercentProvisionTransaction fp = (FlatPercentProvisionTransaction) t;
            provision = t.amount * fp.getPercent() / 100.0;
        } else return false;

        double totalDebit = t.amount + provision;
        if (fromAccount.getBalance() < totalDebit) return false;

        fromAccount.setBalance(fromAccount.balance - totalDebit);
        toAccount.setBalance(toAccount.balance + t.amount);
        totalTransfers += t.amount;
        totalProvisions += provision;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;

        if (!name.equals(bank.name)) return false;
        if (accounts.length != bank.accounts.length) return false;
        for (int i = 0; i < accounts.length; i++) {
            if (!accounts[i].equals(bank.accounts[i])) return false;
        }

        return Double.compare(totalTransfers, bank.totalTransfers) == 0 &&
                Double.compare(totalProvisions, bank.totalProvisions) == 0;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(accounts);
        result = 31 * result + Double.hashCode(totalTransfers);
        result = 31 * result + Double.hashCode(totalProvisions);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        for (Account a : accounts) {
            sb.append(a.toString());
        }
        return sb.toString();
    }
}

class Account {
    String name;
    long id;
    double balance;
    private static long ID_COUNTER = 1;

    Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.id = ID_COUNTER++;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, balance);
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

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
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
        return fromId == that.fromId && toId == that.toId;
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
        return fromId == that.fromId && toId == that.toId;
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
