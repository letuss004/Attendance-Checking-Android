package vn.edu.usth.attendancecheck.network.responses;

import com.google.gson.annotations.Expose;

import vn.edu.usth.attendancecheck.models.Lessons;

import java.util.List;

public class ClassLessonsResponse {
    @Expose
    private List<Lessons> lessons;
    @Expose
    private List<Integer> status;

    public List<Lessons> getLessons() {
        return lessons;
    }

    public List<Integer> getStatuses() {
        return status;
    }
}
