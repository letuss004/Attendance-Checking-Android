package vn.edu.usth.attendancecheck.network;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.edu.usth.attendancecheck.network.responses.AttendanceResponse;
import vn.edu.usth.attendancecheck.network.responses.LoginResponse;
import vn.edu.usth.attendancecheck.network.responses.LogoutResponse;

public interface ApiService {

    @POST("api/auth/login")
    Call<LoginResponse> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("api/auth/logout")
    Call<LogoutResponse> logout(
            @Header("Authorization") String token
    );

    @POST()
    Call<AttendanceResponse> attendance(
            @Header("Authorization") String token
    );

    @POST()
    Call studentsCourse(
            @Header("Authorization") String token
    );
}
