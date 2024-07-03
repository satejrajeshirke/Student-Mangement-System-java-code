import java.io.*;
import java.util.*;

public class StudentManagementSystem {
    private Map<String, StudentManagementSystem> students;
    private final String filePath;

    public StudentManagementSystem(String filePath) {
        this.filePath = filePath;
        this.students = new HashMap<>();
        loadStudents();
    }

    public void addStudent(String id, String name) {
        Student student = new Student(id, name);
        students.put(id, student);
        saveStudents();
    }

    public void deleteStudent(String id) {
        students.remove(id);
        saveStudents();
    }

    public void updateStudent(String id, String name) {
        Student student = students.get(id);
        if (student != null) {
            student = new Student(id, name);
            students.put(id, student);
            saveStudents();
        }
    }

    public void addGrade(String studentId, Course course, char grade) {
        Student student = students.get(studentId);
        if (student != null) {
            student.addGrade(new Grade(course, grade));
            saveStudents();
        }
    }

    public void displayStudent(String id) {
        Student student = students.get(id);
        if (student != null) {
            System.out.println(student);
            for (Grade grade : student.getGrades()) {
                System.out.println(grade);
            }
        } else {
            System.out.println("Student not found");
        }
    }

    public void displayAllStudents() {
        for (Student student : students.values()) {
            System.out.println(student);
        }
    }

    private void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                String name = data[1];
                Student student = new Student(id, name);
                for (int i = 2; i < data.length; i += 3) {
                    Course course = new Course(data[i], data[i+1], Integer.parseInt(data[i+2]));
                    char grade = data[i+3].charAt(0);
                    student.addGrade(new Grade(course, grade));
                }
                students.put(id, student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStudents() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Student student : students.values()) {
                bw.write(student.getId() + "," + student.getName());
                for (Grade grade : student.getGrades()) {
                    Course course = grade.getCourse();
                    bw.write("," + course.getCode() + "," + course.getTitle() + "," + course.getCredit() + "," + grade.getGrade());
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem("students.txt");

        Course math = new Course("MATH101", "Mathematics", 3);
        Course eng = new Course("ENG101", "English", 2);

        sms.addStudent("1", "John Doe");
        sms.addStudent("2", "Jane Smith");

        sms.addGrade("1", math, 'A');
        sms.addGrade("1", eng, 'B');

        sms.displayStudent("1");
        sms.displayAllStudents();
    }
}