package vn.edu.usth.attendancecheck.models;

import com.google.gson.annotations.Expose;

public class LogoutResponse {
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
