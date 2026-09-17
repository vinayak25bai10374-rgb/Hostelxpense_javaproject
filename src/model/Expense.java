package model;

import java.util.List;

public class Expense {
    private String expenseId;
    private String paidByStudentId;
    private String category;       
    private double amount;
    private String date;           
    private List<String> sharedWithIds; 

    public Expense(String expenseId, String paidByStudentId, String category,
                    double amount, String date, List<String> sharedWithIds) {
        this.expenseId = expenseId;
        this.paidByStudentId = paidByStudentId;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.sharedWithIds = sharedWithIds;
    }

    public String getExpenseId() { return expenseId; }
    public String getPaidByStudentId() { return paidByStudentId; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public List<String> getSharedWithIds() { return sharedWithIds; }

    /** Each participant's equal share of this expense. */
    public double perHeadShare() {
        return sharedWithIds.isEmpty() ? 0 : amount / sharedWithIds.size();
    }

    /** Converts this expense to one CSV line ("|" separates shared IDs). */
    public String toCsvLine() {
        return expenseId + "," + paidByStudentId + "," + category + ","
                + amount + "," + date + "," + String.join("|", sharedWithIds);
    }

    public static Expense fromCsvLine(String line) {
        String[] parts = line.split(",", -1);
        List<String> shared = List.of(parts[5].split("\\|"));
        return new Expense(parts[0], parts[1], parts[2],
                Double.parseDouble(parts[3]), parts[4], shared);
    }

    @Override
    public String toString() {
        return String.format("%-6s Paid by:%-6s %-12s Rs.%-8.2f Date:%-11s SplitAmong:%d",
                expenseId, paidByStudentId, category, amount, date, sharedWithIds.size());
    }
}
