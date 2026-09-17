package manager;

import exception.StudentNotFoundException;
import model.Student;
import util.FileHandler;
import util.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MODULE 1: Student Management
 * Handles adding students, looking them up, listing them,
 * and persisting the student list to disk.
 */
public class StudentManager {
    private static final String FILE_PATH = "data/students.csv";
    // HashMap-backed (LinkedHashMap keeps insertion order for nicer listing)
    // gives O(1) average lookup by ID -> Non-functional requirement: Performance.
    private final Map<String, Student> students = new LinkedHashMap<>();
    private int nextId = 1;

    public StudentManager() {
        load();
    }

    public Student addStudent(String name, String roomNumber) {
        String id = String.format("S%03d", nextId++);
        Student student = new Student(id, name, roomNumber);
        students.put(id, student);
        save();
        Logger.info("Added student " + id + " (" + name + ")");
        return student;
    }

    public Student getStudent(String id) throws StudentNotFoundException {
        Student s = students.get(id);
        if (s == null) {
            throw new StudentNotFoundException("No student found with ID: " + id);
        }
        return s;
    }

    public boolean exists(String id) {
        return students.containsKey(id);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public void save() {
        List<String> lines = new ArrayList<>();
        for (Student s : students.values()) {
            lines.add(s.toCsvLine());
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    private void load() {
        List<String> lines = FileHandler.readLines(FILE_PATH);
        for (String line : lines) {
            try {
                Student s = Student.fromCsvLine(line);
                students.put(s.getStudentId(), s);
                int num = Integer.parseInt(s.getStudentId().substring(1));
                if (num >= nextId) nextId = num + 1;
            } catch (Exception e) {
                Logger.warn("Skipped a corrupted student record: " + line);
            }
        }
    }
}
