package model;

public class Student {
    private String studentId;      
    private String name;
    private String roomNumber;
    private double monthlyBudget;  
    private double totalOwed;     
    public Student(String studentId, String name, String roomNumber) {
        this.studentId = studentId;
        this.name = name;
        this.roomNumber = roomNumber;
        this.monthlyBudget = 0.0;
        this.totalOwed = 0.0;
    }

    public Student(String studentId, String name, String roomNumber,
                    double monthlyBudget, double totalOwed) {
        this.studentId = studentId;
        this.name = name;
        this.roomNumber = roomNumber;
        this.monthlyBudget = monthlyBudget;
        this.totalOwed = totalOwed;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getRoomNumber() { return roomNumber; }
    public double getMonthlyBudget() { return monthlyBudget; }
    public double getTotalOwed() { return totalOwed; }

    public void setMonthlyBudget(double monthlyBudget) { this.monthlyBudget = monthlyBudget; }

    public void addOwedAmount(double amount) { this.totalOwed += amount; }

    public String toCsvLine() {
        return studentId + "," + name + "," + roomNumber + ","
                + monthlyBudget + "," + totalOwed;
    }

    public static Student fromCsvLine(String line) {
        String[] parts = line.split(",");
        return new Student(parts[0], parts[1], parts[2],
                Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
    }

    @Override
    public String toString() {
        return String.format("%-6s %-15s Room:%-6s Budget:Rs.%-8.2f Owed:Rs.%-8.2f",
                studentId, name, roomNumber, monthlyBudget, totalOwed);
    }
}
