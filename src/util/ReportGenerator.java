package util;

import model.Student;

import java.util.List;
import java.util.Map;

/**
 * MODULE 3 (supporting): Reporting & Visualization.
 * Produces simple text-based bar charts and summaries directly in the
 * console - no external charting library needed, so it stays within
 * core Java for a first-year level submission.
 */
public class ReportGenerator {

    /** Prints a horizontal bar chart of spend-by-category using '#' characters. */
    public static void printCategoryChart(Map<String, Double> spendByCategory) {
        if (spendByCategory.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        double max = spendByCategory.values().stream().mapToDouble(Double::doubleValue).max().orElse(1);
        System.out.println("\n--- Spend by Category ---");
        for (Map.Entry<String, Double> entry : spendByCategory.entrySet()) {
            int barLength = (int) ((entry.getValue() / max) * 40);
            String bar = "#".repeat(Math.max(barLength, 1));
            System.out.printf("%-12s | %-40s Rs.%.2f%n", entry.getKey(), bar, entry.getValue());
        }
    }

    /** Prints a ranked list of students by how much they currently owe. */
    public static void printOwedRanking(List<Student> students) {
        System.out.println("\n--- Amount Owed Ranking ---");
        students.stream()
                .sorted((a, b) -> Double.compare(b.getTotalOwed(), a.getTotalOwed()))
                .forEach(s -> System.out.printf("%-15s Rs.%.2f%n", s.getName(), s.getTotalOwed()));
    }

    public static void printSummary(double totalSpent, int studentCount, int expenseCount) {
        System.out.println("\n--- Hostel Summary ---");
        System.out.printf("Total students     : %d%n", studentCount);
        System.out.printf("Total expenses logged: %d%n", expenseCount);
        System.out.printf("Total amount spent : Rs.%.2f%n", totalSpent);
        if (studentCount > 0) {
            System.out.printf("Average per student: Rs.%.2f%n", totalSpent / studentCount);
        }
    }
}
