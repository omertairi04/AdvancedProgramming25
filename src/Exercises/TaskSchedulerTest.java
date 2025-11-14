//package Exercises;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * I Partial exam 2016
// */
//
//class TaskRunner<T> ???{
//
//public void run(TaskScheduler????scheduler, T[] tasks) {
//    List<T> order = scheduler.schedule(tasks);
//    order.forEach(System.out::println);
//}
//}
//
//interface TaskScheduler<T> {
//    void schedule(List<T> tasks);
//}
//
//interface Task {
//    int numOfTasks();
//}
//
//class PriorityTask implements Task {
//    private final int priority;
//
//    public PriorityTask(int priority) {
//        this.priority = priority;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("PT -> %d", getOrder());
//    }
//
//    // Based on execution priority
//    @Override
//    public int numOfTasks() {
//        return 0;
//    }
//}
//
//class TimedTask implements Task {
//    private final int time;
//
//    public TimedTask(int time) {
//        this.time = time;
//    }
//
//
//    @Override
//    public String toString() {
//        return String.format("TT -> %d", getOrder());
//    }
//
//    @Override
//    public int numOfTasks() {
//        // Based on execution time
//        return 0;
//    }
//}
//
//class Schedulers<T> implements TaskScheduler<T> {
//    public TaskScheduler<T> getOrdered() {
//
//        Schedulers<T> scheduler = new Schedulers<>();
//        scheduler.schedule();
//
//    }
//
//    public TaskScheduler<T> getFiltered(int order) {
//
//        // vashiot kod ovde (lambda expression)
//
//    }
//
//    @Override
//    public void schedule(List<T> tasks) {
//        List<T> ordered = new ArrayList<>();
//        ordered = tasks;
//        for (int i = 0; i < ordered.size(); i++) {
//            for (int j = 0; j < ordered.size(); j++) {
//                if (ordered.get(i).equals(ordered.get(j))) {
//                    ordered.set(i, ordered.get(j));
//                    ordered.set(j, ordered.get(i));
//                }
//            }
//        }
//    }
//}
//
//public class TaskSchedulerTest {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        Task[] timeTasks = new Task[n];
//        for (int i = 0; i < n; ++i) {
//            int x = scanner.nextInt();
//            timeTasks[i] = new TimedTask(x);
//        }
//        n = scanner.nextInt();
//        Task[] priorityTasks = new Task[n];
//        for (int i = 0; i < n; ++i) {
//            int x = scanner.nextInt();
//            priorityTasks[i] = new PriorityTask(x);
//        }
//        Arrays.stream(priorityTasks).forEach(System.out::println);
//        TaskRunner<Task> runner = new TaskRunner<>();
//        System.out.println("=== Ordered tasks ===");
//        System.out.println("Timed tasks");
//        runner.run(Schedulers.getOrdered(), timeTasks);
//        System.out.println("Priority tasks");
//        runner.run(Schedulers.getOrdered(), priorityTasks);
//        int filter = scanner.nextInt();
//        System.out.printf("=== Filtered time tasks with order less then %d ===\n", filter);
//        runner.run(Schedulers.getFiltered(filter), timeTasks);
//        System.out.printf("=== Filtered priority tasks with order less then %d ===\n", filter);
//        runner.run(Schedulers.getFiltered(filter), priorityTasks);
//        scanner.close();
//    }
//}
//
//
