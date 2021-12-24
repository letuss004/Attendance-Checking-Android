package vn.edu.usth.attendancecheck.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class User {
    private String id;
    private String email;
    private String username;
    private UserType user_type_id;
    private String name;
    private Department department_id;
    private LocalDate email_verified_at;
    private LocalDate created_at;
    private LocalDate updated_at;
    private String token;
}
