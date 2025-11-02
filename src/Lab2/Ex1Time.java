package Lab2;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

class LocalDateTest {
    public static void main(String[] args) {
        System.out.println(create());
        System.out.println(parse());
        System.out.println(with().getYear());
        System.out.println(withAdjuster());
        System.out.println(plus());
        System.out.println(minus());
        System.out.println(plusPeriod());
        System.out.println(isAfter());
        System.out.println(until());
    }

    static LocalDate create() {
        // Create a LocalDate of 2015-06-18
        return LocalDate.of(2015, 6, 18);
    }

    static LocalDate parse() {
        // Parse string to LocalDate
        return LocalDate.parse("2015-06-18");
    }

    static LocalDate with() {
        LocalDate ld = DateAndTimes.LD_20150618;
        // Set the year to 2015 (example of "with")
        return ld.withYear(2015);
    }

    static LocalDate withAdjuster() {
        LocalDate ld = DateAndTimes.LD_20150618;
        // Move to the first day of the next year
        return ld.with(TemporalAdjusters.firstDayOfNextYear());
    }

    static LocalDate plus() {
        LocalDate ld = DateAndTimes.LD_20150618;
        // Add 10 months
        return ld.plusMonths(10);
    }

    static LocalDate minus() {
        LocalDate ld = DateAndTimes.LD_20150618;
        // Subtract 10 days
        return ld.minusDays(10);
    }

    static LocalDate plusPeriod() {
        LocalDate ld = DateAndTimes.LD_20150618;
        // Add a period of 1 year, 2 months, and 3 days
        Period period = Period.of(1, 2, 3);
        return ld.plus(period);
    }

    static boolean isAfter() {
        LocalDate ld = DateAndTimes.LD_20150618;
        LocalDate ld2 = DateAndTimes.LD_20150807;
        // Check if ld2 is after ld
        return ld2.isAfter(ld);
    }

    static Period until() {
        LocalDate ld = DateAndTimes.LD_20150618;
        LocalDate ld2 = DateAndTimes.LD_20150807;
        // Calculate the period between ld and ld2
        return ld.until(ld2);
    }
}

class DateAndTimes {
    public static final LocalDate LD_20150618 = LocalDate.of(2015, 6, 18);
    public static final LocalDate LD_20150807 = LocalDate.of(2015, 8, 7);
}

public class Ex1Time { }
