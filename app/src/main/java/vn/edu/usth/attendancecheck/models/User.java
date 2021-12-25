package vn.edu.usth.attendancecheck.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;
import java.time.LocalTime;

public class User {
    @Expose
    private String id;
    @Expose
    private String email;
    @Expose
    private String username;
    @Expose
    private int user_type_id;
    @Expose
    private String name;
    @Expose
    private int department_id;
    @Expose
    private LocalDate email_verified_at;
    @Expose
    private LocalDate created_at;
    @Expose
    private LocalDate updated_at;
    @Expose
    private String token;

    public User(String id, String email,
                String username, int user_type_id, String name, int department_id,
                LocalDate email_verified_at, LocalDate created_at,
                LocalDate updated_at, String token) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.user_type_id = user_type_id;
        this.name = name;
        this.department_id = department_id;
        this.email_verified_at = email_verified_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.token = token;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", user_type_id=" + user_type_id +
                ", name='" + name + '\'' +
                ", department_id=" + department_id +
                ", email_verified_at=" + email_verified_at +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", token='" + token + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(int user_type_id) {
        this.user_type_id = user_type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public LocalDate getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(LocalDate email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
