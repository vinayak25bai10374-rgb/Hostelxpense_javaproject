# HostelXpense — Smart Hostel Expense & Bill-Splitter

## Overview
HostelXpense is a console-based Java application that helps hostel roommates
manage shared expenses fairly. Instead of manually calculating who owes what
after every food order, wifi recharge, or electricity bill, students log the
expense once and the app automatically splits it equally among everyone
involved, tracks each person's running balance, and warns them when they're
close to (or over) their personal monthly budget.

This project was built as an original submission demonstrating core Java
concepts: object-oriented design, collections (HashMap/List), file I/O,
custom exceptions, and modular architecture — without using any external
frameworks or a database.

## Features
- **Student Management** — register hostel members with a room number; each
  gets a unique auto-generated ID (e.g. `S001`).
- **Shared Expense Logging & Bill Splitting** — record an expense, choose who
  paid and who shares it, and the app instantly computes each person's equal
  share and updates running balances.
- **Personal Budgets & Alerts** — set a monthly budget per student; the app
  flags students who are near or over their limit.
- **Analytics & Reports** — text-based bar chart of spending by category, a
  ranking of who currently owes the most, and an overall hostel summary.
- **Persistent Storage** — all data is saved to plain CSV files under `data/`
  so nothing is lost between runs.
- **Activity Logging** — every important action is timestamped and appended
  to `data/activity.log` for basic monitoring/auditing.
- **Robust Input Handling** — custom exceptions (`InvalidAmountException`,
  `StudentNotFoundException`) catch bad input without crashing the app.

## Technologies / Tools Used
- Java (core Java only — no external libraries)
- Java Collections Framework (`HashMap`/`LinkedHashMap`, `List`)
- File I/O (`BufferedReader`, `PrintWriter`) for CSV persistence
- Git for version control

## Project Structure
```
HostelXpense/
├── src/
│   ├── Main.java                  # Menu-driven entry point
│   ├── model/
│   │   ├── Student.java
│   │   └── Expense.java
│   ├── exception/
│   │   ├── InvalidAmountException.java
│   │   └── StudentNotFoundException.java
│   ├── manager/
│   │   ├── StudentManager.java    # Module 1: Student Management
│   │   ├── ExpenseManager.java    # Module 2: Expense & Bill Splitting
│   │   └── BudgetManager.java     # Module 3: Budget Tracking & Alerts
│   └── util/
│       ├── FileHandler.java       # CSV read/write
│       ├── Logger.java            # Activity logging
│       └── ReportGenerator.java   # Text-based analytics/charts
├── data/                          # Auto-created at runtime (CSV + log files)
├── README.md
└── statement.md
```

## Steps to Install & Run
1. Make sure you have JDK 11 or later installed:
   ```
   java -version
   javac -version
   ```
2. Clone or download this repository, then from the project root:
   ```
   mkdir -p bin
   javac -d bin $(find src -name "*.java")
   java -cp bin Main
   ```
3. On Windows (PowerShell), replace the compile line with:
   ```
   javac -d bin (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
   java -cp bin Main
   ```
4. The app creates a `data/` folder automatically on first run to store
   `students.csv`, `expenses.csv`, and `activity.log`.

## Instructions for Testing
Suggested manual test flow (also doubles as a demo):
1. Choose **1. Add Student** three times to create `S001`, `S002`, `S003`.
2. Choose **2. List Students** to confirm they were added.
3. Choose **3. Add Shared Expense** — e.g. paid by `S001`, category `Food`,
   amount `600`, shared with `S001,S002,S003`. Confirm each owes Rs. 200.
4. Choose **5. Set Monthly Budget** for `S002` with a small value (e.g. `150`)
   to trigger a warning.
5. Choose **6. Check Budget Status** with `all` to see alerts/warnings.
6. Choose **7. View Analytics / Reports** to see the category bar chart,
   owed-ranking, and summary.
7. Exit and re-run the app — confirm data persisted (students/expenses are
   still there), demonstrating reliability of file-based storage.
8. Try invalid inputs (negative amount, unknown student ID) to confirm the
   app shows a friendly error instead of crashing.
