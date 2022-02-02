package vn.edu.usth.attendancecheck.network.responses;

import com.google.gson.annotations.Expose;

import com.google.gson.annotations.SerializedName;

import vn.edu.usth.attendancecheck.models.CurrentClasses;

import java.util.List;

public class StudentCurrentClassesResponse {
    @Expose
    @SerializedName("courses")
    private List<CurrentClasses> currentClasses;

    public List<CurrentClasses> getCurrentClasses() {
        return currentClasses;
    }
}
