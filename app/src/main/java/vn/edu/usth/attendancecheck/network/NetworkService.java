package vn.edu.usth.attendancecheck.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkService {

    @GET()
    void attendance();

}
