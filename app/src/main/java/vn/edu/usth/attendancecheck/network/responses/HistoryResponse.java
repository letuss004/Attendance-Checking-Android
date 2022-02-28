package vn.edu.usth.attendancecheck.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.usth.attendancecheck.models.HistoryClasses;

import java.util.List;

public class HistoryResponse {
    @Expose
    @SerializedName("courses")
    private List<HistoryClasses> historyClasses;

    public List<HistoryClasses> getClasses() {
        return historyClasses;
    }
}
