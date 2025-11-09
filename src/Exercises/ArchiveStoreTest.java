package Exercises;
//
//import java.util.Date;
//import java.util.Scanner;
//import java.util.*;
//
//abstract class Archive {
//    int id;
//    Date dateArchived;
//
//    public Archive(int id, Date date) {
//        this.id = id;
//        dateArchived = date;
//    }
//}
//
//class LockedArchive extends Archive {
//    Date dateToOpen;
//
//    public LockedArchive(int id, Date date, Date dateToOpen) {
//        super(id, date);
//        this.dateToOpen = dateToOpen;
//    }
//}
//
//class SpecialArchive extends Archive {
//    int maxOpen;
//
//    public SpecialArchive(int id, Date date, int maxOpen) {
//        super(id, date);
//        this.maxOpen = maxOpen;
//    }
//}
//
//class ArchiveStore {
//    ArrayList<Archive> archives;
//
//    public ArchiveStore() {
//        archives = new ArrayList<Archive>();
//    }
//
//    void archiveItem(Archive item, Date date) {
////        archives.add();
//    }
//}
//
//public class ArchiveStoreTest {
//    public static void main(String[] args) {
//        ArchiveStore store = new ArchiveStore();
//        Date date = new Date(113, 10, 7);
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();
//        int n = scanner.nextInt();
//        scanner.nextLine();
//        scanner.nextLine();
//        int i;
//        for (i = 0; i < n; ++i) {
//            int id = scanner.nextInt();
//            long days = scanner.nextLong();
//            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
//                    * 60 * 1000));
////            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
////            store.archiveItem(lockedArchive, date);
//        }
//        scanner.nextLine();
//        scanner.nextLine();
//        n = scanner.nextInt();
//        scanner.nextLine();
//        scanner.nextLine();
//        for (i = 0; i < n; ++i) {
//            int id = scanner.nextInt();
//            int maxOpen = scanner.nextInt();
//            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
//            store.archiveItem(specialArchive, date);
//        }
//        scanner.nextLine();
//        scanner.nextLine();
//        while (scanner.hasNext()) {
//            int open = scanner.nextInt();
////            try {
////                store.openItem(open, date);
////            } catch (NonExistingItemException e) {
////                System.out.println(e.getMessage());
////            }
////        }
////        System.out.println(store.getLog());
//    }
//}
//
//// вашиот код овде
//
//
//
