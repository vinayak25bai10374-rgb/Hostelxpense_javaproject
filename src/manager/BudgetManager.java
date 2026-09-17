package manager;

import exception.InvalidAmountException;
import exception.StudentNotFoundException;
import model.Student;
import util.Logger;

/**
 * MODULE 3: Budget Tracking & Alerts
 * Lets each student set a personal monthly budget and checks
 * their current "owed" total against it, raising a simple alert
 * when they are close to or over budget.
 */
public class BudgetManager {
    private final StudentManager studentManager;

    // How close to the budget (as a fraction) counts as a "warning" zone.
    private static final double WARNING_THRESHOLD = 0.8;

    public BudgetManager(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    public void setBudget(String studentId, double amount)
            throws StudentNotFoundException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Budget must be greater than zero.");
        }
        Student student = studentManager.getStudent(studentId);
        student.setMonthlyBudget(amount);
        studentManager.save();
        Logger.info("Set budget Rs." + amount + " for " + studentId);
    }

    /** Returns a short human-readable status string for a student's budget usage. */
    public String checkBudgetStatus(String studentId) throws StudentNotFoundException {
        Student student = studentManager.getStudent(studentId);
        double budget = student.getMonthlyBudget();
        double owed = student.getTotalOwed();

        if (budget <= 0) {
            return student.getName() + ": no budget set yet.";
        }
        double usedFraction = owed / budget;

        if (owed < 0) {
            return student.getName() + " is currently owed money back (credit): Rs."
                    + String.format("%.2f", -owed);
        } else if (usedFraction >= 1.0) {
            return "ALERT: " + student.getName() + " has EXCEEDED their budget! Owed Rs."
                    + String.format("%.2f", owed) + " of Rs." + String.format("%.2f", budget);
        } else if (usedFraction >= WARNING_THRESHOLD) {
            return "WARNING: " + student.getName() + " is close to their budget limit ("
                    + String.format("%.0f", usedFraction * 100) + "% used).";
        } else {
            return student.getName() + " is within budget ("
                    + String.format("%.0f", usedFraction * 100) + "% used).";
        }
    }
}
