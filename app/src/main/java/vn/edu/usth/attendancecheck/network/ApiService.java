package vn.edu.usth.attendancecheck.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.edu.usth.attendancecheck.models.AttendanceResponse;
import vn.edu.usth.attendancecheck.models.LoginResponse;
import vn.edu.usth.attendancecheck.models.LogoutResponse;

public interface ApiService {
    /**
     * todo: finish this
     */
    @POST()
    Call<AttendanceResponse> attendance(

    );

    @POST("api/auth/login")
    Call<LoginResponse> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("api/auth/logout")
    Call<LogoutResponse> logout(
            @Header("Authorization") String token
    );
}
