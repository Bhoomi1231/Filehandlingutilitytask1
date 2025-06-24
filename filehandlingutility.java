import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors; // For more modern file reading

/**
 * A simple utility class for common file handling operations.
 */
public class FileUtility {

    /**
     * Checks if a file exists at the given path.
     *
     * @param filePath The absolute or relative path to the file.
     * @return true if the file exists and is a regular file, false otherwise.
     */
    public static boolean doesFileExist(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    /**
     * Checks if a directory exists at the given path.
     *
     * @param directoryPath The absolute or relative path to the directory.
     * @return true if the directory exists and is a directory, false otherwise.
     */
    public static boolean doesDirectoryExist(String directoryPath) {
        Path path = Paths.get(directoryPath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    /**
     * Creates a directory at the specified path if it does not already exist.
     *
     * @param directoryPath The path of the directory to create.
     * @return true if the directory was created or already exists, false on failure.
     */
    public static boolean createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            return directory.mkdirs(); // mkdirs() creates parent directories if needed
        }
        return true; // Directory already exists
    }

    /**
     * Reads the entire content of a text file into a single String.
     *
     * @param filePath The path to the text file.
     * @return The content of the file as a String.
     * @throws IOException If an I/O error occurs during reading.
     */
    public static String readFileContent(String filePath) throws IOException {
        // Modern approach using java.nio.file.Files
        Path path = Paths.get(filePath);
        return Files.readString(path);

        // Older approach using BufferedReader:
        /*
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator()); // Use platform-specific line separator
            }
        }
        return content.toString();
        */
    }

    /**
     * Writes the given content to a text file. If the file does not exist, it will be created.
     * If it exists, its content will be overwritten.
     *
     * @param filePath The path to the file to write to.
     * @param content The String content to write to the file.
     * @throws IOException If an I/O error occurs during writing.
     */
    public static void writeToFile(String filePath, String content) throws IOException {
        // Modern approach using java.nio.file.Files
        Path path = Paths.get(filePath);
        Files.writeString(path, content);

        // Older approach using BufferedWriter:
        /*
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
        */
    }

    /**
     * Appends the given content to an existing text file. If the file does not exist, it will be created.
     *
     * @param filePath The path to the file to append to.
     * @param content The String content to append to the file.
     * @throws IOException If an I/O error occurs during appending.
     */
    public static void appendToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) { // 'true' for append mode
            writer.write(content);
            writer.newLine(); // Add a new line after appending content
        }
    }

    /**
     * Deletes a file at the specified path.
     *
     * @param filePath The path to the file to delete.
     * @return true if the file was successfully deleted, false otherwise.
     * @throws IOException If an I/O error occurs (e.g., permissions).
     */
    public static boolean deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.deleteIfExists(path);
    }


    public static void main(String[] args) {
        String testDir = "myTestDirectory";
        String testFilePath = testDir + File.separator + "myTextFile.txt";
        String anotherTestFilePath = testDir + File.separator + "anotherFile.log";

        System.out.println("--- File Handling Utility Test ---");

        // 1. Create a directory
        System.out.println("Creating directory: " + testDir);
        if (FileUtility.createDirectory(testDir)) {
            System.out.println("Directory created or already exists.");
        } else {
            System.out.println("Failed to create directory.");
        }
        System.out.println("Does directory exist? " + FileUtility.doesDirectoryExist(testDir));

        // 2. Write to a file
        String contentToWrite = "Hello, this is a test line.\nAnother line here.";
        System.out.println("\nWriting to file: " + testFilePath);
        try {
            FileUtility.writeToFile(testFilePath, contentToWrite);
            System.out.println("Content successfully written to " + testFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        System.out.println("Does file exist? " + FileUtility.doesFileExist(testFilePath));

        // 3. Read from the file
        System.out.println("\nReading from file: " + testFilePath);
        try {
            String readContent = FileUtility.readFileContent(testFilePath);
            System.out.println("Content read:\n" + readContent);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        // 4. Append to the file
        String contentToAppend = "This line was appended.\nFinal line.";
        System.out.println("\nAppending to file: " + testFilePath);
        try {
            FileUtility.appendToFile(testFilePath, contentToAppend);
            System.out.println("Content successfully appended to " + testFilePath);
            String readContentAfterAppend = FileUtility.readFileContent(testFilePath);
            System.out.println("Content after append:\n" + readContentAfterAppend);
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
        }

        // 5. Write another file
        String logContent = "2025-06-22 19:15:00 - INFO: Application started.\n" +
                            "2025-06-22 19:15:05 - DEBUG: Processing user request.";
        System.out.println("\nWriting another file: " + anotherTestFilePath);
        try {
            FileUtility.writeToFile(anotherTestFilePath, logContent);
            System.out.println("Content successfully written to " + anotherTestFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        // 6. Delete a file
        System.out.println("\nDeleting file: " + anotherTestFilePath);
        try {
            if (FileUtility.deleteFile(anotherTestFilePath)) {
                System.out.println(anotherTestFilePath + " deleted successfully.");
            } else {
                System.out.println(anotherTestFilePath + " could not be deleted or did not exist.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
        System.out.println("Does " + anotherTestFilePath + " exist after deletion? " + FileUtility.doesFileExist(anotherTestFilePath));

        // Clean up: delete the test directory and the remaining file
        System.out.println("\nCleaning up...");
        try {
            if (FileUtility.doesFileExist(testFilePath)) {
                FileUtility.deleteFile(testFilePath);
                System.out.println(testFilePath + " deleted.");
            }
            if (FileUtility.doesDirectoryExist(testDir)) {
                // To delete a non-empty directory, you might need to use Files.walk and delete
                // or ensure all files within it are deleted first. For simplicity here,
                // we assume testDir will be empty after deleting testFilePath.
                if (new File(testDir).delete()) { // File.delete() works only for empty directories
                    System.out.println(testDir + " directory deleted.");
                } else {
                    System.out.println("Could not delete " + testDir + " (it might not be empty).");
                }
            }
        } catch (IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
        System.out.println("Cleanup complete.");
    }
}