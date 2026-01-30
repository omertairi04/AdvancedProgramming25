package Exercises;

import java.util.*;
import java.util.stream.Collectors;

class Movie {
    String title;
    double rating;
    double ratingCoef;

    Movie(String title, double rating, double ratingCoef) {
        this.title = title;
        this.rating = rating;
        this.ratingCoef = ratingCoef;
    }


    public double getRating() {
        return rating;
    }

    public double getRatingCoef() {
        return ratingCoef;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", rating=" + rating +
                ", ratingCoef=" + ratingCoef +
                '}';
    }
}

class MoviesList {
    // rating ->
    Map<Double, List<Movie>> movies;

    public MoviesList() {
        movies = new HashMap<>();
    }

    public void addMovie(String title, int[] ratings) {
        int sum = 0;
        int max = ratings[0];
        for (int i = 0; i < ratings.length; i++) {
            if (max > ratings[i]) {
                max = ratings[i];
            }
            sum += ratings[i];
        }

        double avg = sum / ratings.length;
        double coef = (avg * ratings.length) / max;

        Movie movie = new Movie(title, avg, coef);
        movies.computeIfAbsent(movie.rating, k -> new ArrayList<>()).add(movie);
    }

    public List<Movie> top10ByAvgRating() {
        List<Movie> top10 = movies.values().stream().flatMap(List::stream)
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed()
                        .thenComparing((Movie m) -> m.title))
                .limit(10)
                .collect(Collectors.toList());

        return top10;
    }

    public List<Movie> top10ByRatingCoef() {
        List<Movie> top10 = movies.values().stream().flatMap(List::stream)
                .sorted(Comparator.comparingDouble(Movie::getRatingCoef).reversed())
                .limit(10)
                .collect(Collectors.toList());

        return top10;
    }


}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde