package Exercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Team {
    String name;
    int points;
    int playedMatches;
    int wins;
    int losses;
    int draws;
    int scoredGoals;
    int concededGoals;

    Team(String name) {
        this.name = name;
        this.points = 0;
        this.playedMatches = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.scoredGoals = 0;
        this.concededGoals = 0;
    }

    public int calculatePoints() {
        points = wins * 3 + draws;
        return wins * 3 + draws;
    }

    public int calculateGoalDifference() {
        return scoredGoals - concededGoals;
    }

    @Override
    public String
    toString() {
        String space = " ";
        return name + space + playedMatches + space +
                wins + space + draws + space + losses + space + calculatePoints() + "\n";
    }
}

class Game {
    Team homeTeam;
    Team awayTeam;
    int homeGoals;
    int awayGoals;

    public Game(Team homeTeam, Team awayTeam, int homeGoals, int awayGoals) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        analyzeMatch();
    }

    void analyzeMatch() {
        if (homeGoals > awayGoals) {
            homeTeam.wins++;
            awayTeam.losses++;
        } else if (awayGoals > homeGoals) {
            awayTeam.wins++;
            homeTeam.losses++;
        } else {
            homeTeam.draws++;
            awayTeam.draws++;
        }
        homeTeam.scoredGoals += homeGoals;
        awayTeam.scoredGoals += awayGoals;

        homeTeam.concededGoals += awayGoals;
        awayTeam.concededGoals += homeGoals;

        homeTeam.playedMatches++;
        awayTeam.playedMatches++;
    }

}

class FootballTable {
    List<Game> games;
    HashMap<String, Team> teams;

    public FootballTable() {
        this.games = new ArrayList<>();
        this.teams = new HashMap<>();
    }

    void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Team home = null;
        Team away = null;
        if (teams.containsKey(homeTeam)) {
            home = teams.get(homeTeam);
        } else {
            home = new Team(homeTeam);
        }
        if (teams.containsKey(awayTeam)) {
            away = teams.get(awayTeam);
        } else {
            away = new Team(awayTeam);
        }
        games.add(new Game(home, away, homeGoals, awayGoals));
        if (!teams.containsKey(homeTeam)) {
            teams.put(homeTeam, home);
        }
        if (!teams.containsKey(awayTeam)) {
            teams.put(awayTeam, away);
        }
    }

    public void printTable() {

//        List<Team> sorted = teams.values().stream()
//                .sorted(Comparator.comparingInt((Team t) -> t.points).reversed()
//                        .thenComparingInt((Team t) -> t.goals).reversed()
//                        .thenComparing((Team t) -> t.name).reversed())
//                .collect(Collectors.toList());

        List<Team> sorted = teams.values().stream()
                .sorted(Comparator.comparingInt((Team t) -> t.points).reversed()
                        .thenComparingInt((Team t) -> t.calculateGoalDifference()).reversed()
                        .thenComparing((Team t) -> t.name))
                .collect(Collectors.toList());

        for (Team t : sorted) {
            System.out.println(t);
        }
    }

}

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

