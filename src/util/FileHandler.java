package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all raw file reading/writing for the app.
 * Kept generic (plain CSV text files) so it works with core Java only
 * (Non-functional requirement: Reliability - data survives program restarts).
 */
public class FileHandler {

    /** Reads every line of a file, returning an empty list if it doesn't exist yet. */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return lines; // first run - no data yet, not an error
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            Logger.error("Failed to read " + filePath + ": " + e.getMessage());
        }
        return lines;
    }

    /** Overwrites the file with the given lines (used to save the full in-memory state). */
    public static void writeLines(String filePath, List<String> lines) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
                for (String line : lines) {
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            Logger.error("Failed to write " + filePath + ": " + e.getMessage());
            System.out.println("Warning: could not save data to " + filePath);
        }
    }
}
