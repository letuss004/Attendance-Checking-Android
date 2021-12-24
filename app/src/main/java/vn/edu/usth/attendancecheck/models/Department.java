package vn.edu.usth.attendancecheck.models;

public class Department {
    public static String getUserType(int id) {
        if (id == 1) {
            return "Student";
        }
        if (id == 2) {
            return "Teacher";
        }
        if (id == 3) {
            return "Admin";
        }
        return null;
    }
}
