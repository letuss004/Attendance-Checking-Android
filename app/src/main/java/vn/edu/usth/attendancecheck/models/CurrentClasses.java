package vn.edu.usth.attendancecheck.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentClasses {
    @Expose
    private int id;
    private int courseListId;
    private String adminId;
    private String teacherId;
    @Expose
    @SerializedName("course_name")
    private String course;
    @Expose
    @SerializedName("teacher_name")
    private String teacher;
    private String admin;

    public int getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getAdmin() {
        return admin;
    }
}
