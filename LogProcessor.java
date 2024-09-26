import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class LogProcessor {

    public static void main(String[] args) {
        Queue<String> logQueue = new LinkedList<>();
        Stack<String> errorStack = new Stack<>();
        int infoCount = 0, warnCount = 0, errorCount = 0, memoryWarnCount = 0;
        LinkedList<String> last100Errors = new LinkedList<>();
        final int MAX_ERRORS_TRACKED = 100;

        try (BufferedReader br = new BufferedReader(new FileReader("log-data.csv"))) {
            String line;
            // Reading each line from the log file
            while ((line = br.readLine()) != null) {
                logQueue.offer(line); // Enqueue each log entry
            }

            // Process the queue
            while (!logQueue.isEmpty()) {
                String logEntry = logQueue.poll(); // Dequeue
                if (logEntry.contains("INFO")) {
                    infoCount++;
                } else if (logEntry.contains("WARN")) {
                    warnCount++;
                    if (logEntry.contains("Memory")) {
                        memoryWarnCount++;
                    }
                } else if (logEntry.contains("ERROR")) {
                    errorCount++;
                    errorStack.push(logEntry); // Push onto the stack

                    // Track the last 100 errors
                    last100Errors.add(logEntry);
                    if (last100Errors.size() > MAX_ERRORS_TRACKED) {
                        last100Errors.removeFirst(); // Remove the oldest error if more than 100
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the log file.");
            e.printStackTrace();
        }

        // Output the results of the analysis
        System.out.println("Log Analysis Results:");
        System.out.println("INFO count: " + infoCount);
        System.out.println("WARN count: " + warnCount);
        System.out.println("ERROR count: " + errorCount);
        System.out.println("Memory Warnings: " + memoryWarnCount);
        System.out.println("Last 100 Errors:");
        for (String error : last100Errors) {
            System.out.println(error);
        }
    }
}
