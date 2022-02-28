package vn.edu.usth.attendancecheck.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Lessons {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    @SerializedName("course_id")
    private int courseId;
    @Expose
    @SerializedName("teacher_id")
    private String teacherId;
    @Expose
    @SerializedName("create_at")
    private LocalDate createAt;

    public String getName() {
        return name;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getId() {
        return id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }
}
