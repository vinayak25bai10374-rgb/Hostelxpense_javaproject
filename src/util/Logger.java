package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "data/activity.log";
    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void log(String level, String message) {
        String line = "[" + LocalDateTime.now().format(FORMAT) + "] "
                + level + " - " + message;
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(line);
        } catch (IOException e) {
            // Logging should never crash the app; fall back to console only.
            System.out.println("(log write failed: " + e.getMessage() + ")");
        }
    }

    public static void info(String message)  { log("INFO", message); }
    public static void warn(String message)  { log("WARN", message); }
    public static void error(String message) { log("ERROR", message); }
}
