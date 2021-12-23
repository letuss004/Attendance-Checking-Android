package vn.edu.usth.attendancecheck.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginService {
    /**
     * todo: finish this
     * @return
     */
    @GET()
    Call attendance(
            @Query("email") String email,
            @Query("password") String password
    );

    @GET()
    void login(
            @Query("email") String email,
            @Query("password") String password
    );
}
