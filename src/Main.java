import exception.InvalidAmountException;
import exception.StudentNotFoundException;
import manager.BudgetManager;
import manager.ExpenseManager;
import manager.StudentManager;
import model.Expense;
import model.Student;
import util.Logger;
import util.ReportGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * HostelXpense - Smart Hostel Expense & Bill-Splitter
 * ----------------------------------------------------
 * A console-based Java application for hostel students to log shared
 * expenses (food orders, wifi, electricity, cleaning, etc.), split bills
 * automatically among roommates, track personal budgets, and view
 * simple spend analytics - all backed by plain CSV file storage so no
 * database setup is required.
 *
 * Entry point: wires together the three functional modules and
 * presents a simple text menu (Usability requirement).
 */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final StudentManager studentManager = new StudentManager();
    private static final ExpenseManager expenseManager = new ExpenseManager(studentManager);
    private static final BudgetManager budgetManager = new BudgetManager(studentManager);

    public static void main(String[] args) {
        Logger.info("Application started.");
        System.out.println("=====================================");
        System.out.println(" Welcome to HostelXpense ");
        System.out.println("=====================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> listStudents();
                    case "3" -> addExpense();
                    case "4" -> listExpenses();
                    case "5" -> setBudget();
                    case "6" -> checkBudgetStatus();
                    case "7" -> showAnalytics();
                    case "0" -> {
                        running = false;
                        System.out.println("Goodbye! Data has been saved.");
                        Logger.info("Application exited normally.");
                    }
                    default -> System.out.println("Invalid choice, please try again.");
                }
            } catch (InvalidAmountException | StudentNotFoundException e) {
                // Centralised error handling for expected, recoverable errors.
                System.out.println("Error: " + e.getMessage());
                Logger.warn("Handled error: " + e.getMessage());
            } catch (Exception e) {
                // Catch-all so a bad input never crashes the whole program.
                System.out.println("Unexpected error: " + e.getMessage());
                Logger.error("Unexpected error: " + e);
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n----- MAIN MENU -----");
        System.out.println("1. Add Student");
        System.out.println("2. List Students");
        System.out.println("3. Add Shared Expense");
        System.out.println("4. List Expenses");
        System.out.println("5. Set Monthly Budget");
        System.out.println("6. Check Budget Status");
        System.out.println("7. View Analytics / Reports");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    // ---------- Module 1: Student Management ----------

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter room number: ");
        String room = sc.nextLine().trim();
        if (name.isEmpty() || room.isEmpty()) {
            System.out.println("Name and room number cannot be empty.");
            return;
        }
        Student s = studentManager.addStudent(name, room);
        System.out.println("Added: " + s);
    }

    private static void listStudents() {
        List<Student> students = studentManager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students added yet.");
            return;
        }
        System.out.println("\n--- Students ---");
        for (Student s : students) System.out.println(s);
    }

    // ---------- Module 2: Expense Management & Bill Splitting ----------

    private static void addExpense() throws InvalidAmountException, StudentNotFoundException {
        if (studentManager.getAllStudents().isEmpty()) {
            System.out.println("Add at least one student first.");
            return;
        }
        System.out.print("Paid by (student ID, e.g. S001): ");
        String paidBy = sc.nextLine().trim();
        System.out.print("Category (Food/Wifi/Electricity/Cleaning/Other): ");
        String category = sc.nextLine().trim();
        System.out.print("Amount (Rs.): ");
        double amount = parseAmount(sc.nextLine().trim());
        System.out.print("Date (dd-MM-yyyy): ");
        String date = sc.nextLine().trim();
        System.out.print("Share with student IDs (comma separated, e.g. S001,S002,S003): ");
        String idsLine = sc.nextLine().trim();

        List<String> sharedWith = new ArrayList<>();
        for (String id : idsLine.split(",")) {
            if (!id.trim().isEmpty()) sharedWith.add(id.trim());
        }
        if (sharedWith.isEmpty()) {
            System.out.println("At least one participant is required to split the bill.");
            return;
        }

        Expense e = expenseManager.addExpense(paidBy, category, amount, date, sharedWith);
        System.out.printf("Added expense %s. Each of the %d participants owes Rs.%.2f%n",
                e.getExpenseId(), sharedWith.size(), e.perHeadShare());
    }

    private static double parseAmount(String input) throws InvalidAmountException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            throw new InvalidAmountException("'" + input + "' is not a valid number.");
        }
    }

    private static void listExpenses() {
        List<Expense> expenses = expenseManager.getAllExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println("\n--- Expenses ---");
        for (Expense e : expenses) System.out.println(e);
    }

    // ---------- Module 3: Budget Tracking & Analytics ----------

    private static void setBudget() throws StudentNotFoundException, InvalidAmountException {
        System.out.print("Student ID: ");
        String id = sc.nextLine().trim();
        System.out.print("Monthly budget (Rs.): ");
        double amount = parseAmount(sc.nextLine().trim());
        budgetManager.setBudget(id, amount);
        System.out.println("Budget set successfully.");
    }

    private static void checkBudgetStatus() throws StudentNotFoundException {
        System.out.print("Student ID (or 'all'): ");
        String id = sc.nextLine().trim();
        if (id.equalsIgnoreCase("all")) {
            for (Student s : studentManager.getAllStudents()) {
                System.out.println(budgetManager.checkBudgetStatus(s.getStudentId()));
            }
        } else {
            System.out.println(budgetManager.checkBudgetStatus(id));
        }
    }

    private static void showAnalytics() {
        ReportGenerator.printSummary(
                expenseManager.getTotalSpent(),
                studentManager.getAllStudents().size(),
                expenseManager.getAllExpenses().size());
        ReportGenerator.printCategoryChart(expenseManager.getSpendByCategory());
        ReportGenerator.printOwedRanking(studentManager.getAllStudents());
    }
}
