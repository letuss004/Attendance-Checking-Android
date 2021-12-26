package vn.edu.usth.attendancecheck.network.responses;

import com.google.gson.annotations.Expose;

import vn.edu.usth.attendancecheck.models.User;

public class LoginResponse {
    @Expose
    private User user;
    @Expose
    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
