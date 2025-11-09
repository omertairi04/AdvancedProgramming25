 package Exercises;

import java.util.*;

class FileNameExistsException extends Exception {
    public FileNameExistsException(String message) {
        super(message);
    }
}

interface IFile {
    String getFileName();
    long getFileSize();
    String getFileInfo();
    void sortBySize();
    File findLargestFile();
}

class File implements IFile {
    String fileName;
    long fileSize;

    public File(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public long getFileSize() {
        return this.fileSize;
    }

    @Override
    public String getFileInfo() {
        // Right align to 10 chars
        return "\t\tFile name:\t" + fileName + "\tFile size:\t" + fileSize + "\n";
    }

    @Override
    public void sortBySize() {
        // nothing to sort
    }

    @Override
    public File findLargestFile() {
        return this;
    }
}


class Folder implements IFile {
    String folderName;
    List<IFile> fileList;
    long folderSize;

    public Folder(String fileName) {
        this.folderName = fileName;
        this.fileList = new ArrayList<>();
        this.folderSize = 0;
    }

    void addFile(IFile file) throws FileNameExistsException {

        for (IFile f : fileList) {
            if (f.getFileName().equals(file.getFileName()))
                throw new FileNameExistsException("There is already a file named " + file.getFileName() + " in the folder " + folderName);
        }
        folderSize += file.getFileSize();
        fileList.add(file);
    }

    @Override
    public String getFileName() {
        return this.folderName;
    }

    @Override
    public long getFileSize() {
        long sum = 0;
        for (IFile f : fileList)
            sum += f.getFileSize();
        return sum;
    }

    @Override
    public String getFileInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tFolder name:\t").append(folderName)
                .append("\tFolder size:\t").append(folderSize).append("\n");

        for (IFile f : fileList) {
            sb.append(f.getFileInfo());
        }

        return sb.toString();
    }

    @Override
    public void sortBySize() {
        fileList.sort(Comparator.comparingLong(IFile::getFileSize));

        for (IFile f : fileList) {
            f.sortBySize(); // recursive sorting
        }
    }

    @Override
    public File findLargestFile() {
        File largest = null;

        for (IFile f : fileList) {
            File curr = f.findLargestFile();
            if (curr != null) {
                if (largest == null || curr.getFileSize() > largest.getFileSize()) {
                    largest = curr;
                }
            }
        }

        return largest;
    }
}

class FileSystem {
    Folder root;

    public FileSystem() {
        root = new Folder("root");
    }

    void addFile(IFile file) {
        try {
            root.addFile(file);
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    long findLargestFile() {
        File f = root.findLargestFile();
        return (f == null) ? 0 : f.getFileSize();
    }

    void sortBySize() {
        root.sortBySize();
    }

    @Override
    public String toString() {
        return root.getFileInfo();
    }

}

public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        fileSystem.root = readFolder(sc);

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem);

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem);

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());
    }
}
