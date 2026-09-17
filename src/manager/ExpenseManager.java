package manager;

import exception.InvalidAmountException;
import exception.StudentNotFoundException;
import model.Expense;
import model.Student;
import util.FileHandler;
import util.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MODULE 2: Expense Management & Bill Splitting
 * Records shared hostel expenses (food, wifi, electricity, etc.),
 * splits each one equally among the chosen participants, and
 * updates each participant's running "amount owed".
 */
public class ExpenseManager {
    private static final String FILE_PATH = "data/expenses.csv";
    private final Map<String, Expense> expenses = new LinkedHashMap<>();
    private final StudentManager studentManager;
    private int nextId = 1;

    public ExpenseManager(StudentManager studentManager) {
        this.studentManager = studentManager;
        load();
    }

    /**
     * Adds a new shared expense and immediately splits it among participants.
     * @throws InvalidAmountException if amount is not a positive number
     * @throws StudentNotFoundException if payer or any participant ID is unknown
     */
    public Expense addExpense(String paidBy, String category, double amount,
                               String date, List<String> sharedWithIds)
            throws InvalidAmountException, StudentNotFoundException {

        if (amount <= 0) {
            throw new InvalidAmountException("Expense amount must be greater than zero.");
        }
        if (!studentManager.exists(paidBy)) {
            throw new StudentNotFoundException("Payer ID not found: " + paidBy);
        }
        for (String id : sharedWithIds) {
            if (!studentManager.exists(id)) {
                throw new StudentNotFoundException("Participant ID not found: " + id);
            }
        }

        String id = String.format("E%03d", nextId++);
        Expense expense = new Expense(id, paidBy, category, amount, date, sharedWithIds);
        expenses.put(id, expense);

        // Split the bill: everyone (including payer) owes their equal share,
        // then we credit back the payer for the full amount they fronted.
        double share = expense.perHeadShare();
        for (String participantId : sharedWithIds) {
            Student participant = studentManager.getStudent(participantId);
            participant.addOwedAmount(share);
        }
        Student payer = studentManager.getStudent(paidBy);
        payer.addOwedAmount(-amount); // payer already covered the full bill

        studentManager.save();
        save();
        Logger.info("Added expense " + id + " (" + category + ", Rs." + amount + ")");
        return expense;
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses.values());
    }

    /** Total spent by the whole hostel group so far (used by reports). */
    public double getTotalSpent() {
        double total = 0;
        for (Expense e : expenses.values()) total += e.getAmount();
        return total;
    }

    /** Total spent per category, e.g. {Food=1200.0, Wifi=500.0} - drives the analytics module. */
    public Map<String, Double> getSpendByCategory() {
        Map<String, Double> totals = new LinkedHashMap<>();
        for (Expense e : expenses.values()) {
            totals.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
        return totals;
    }

    public void save() {
        List<String> lines = new ArrayList<>();
        for (Expense e : expenses.values()) {
            lines.add(e.toCsvLine());
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    private void load() {
        List<String> lines = FileHandler.readLines(FILE_PATH);
        for (String line : lines) {
            try {
                Expense e = Expense.fromCsvLine(line);
                expenses.put(e.getExpenseId(), e);
                int num = Integer.parseInt(e.getExpenseId().substring(1));
                if (num >= nextId) nextId = num + 1;
            } catch (Exception ex) {
                Logger.warn("Skipped a corrupted expense record: " + line);
            }
        }
    }
}
