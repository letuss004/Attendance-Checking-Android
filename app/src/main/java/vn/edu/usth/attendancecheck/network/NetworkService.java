package vn.edu.usth.attendancecheck.network;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.edu.usth.attendancecheck.models.AttendanceResponse;
import vn.edu.usth.attendancecheck.models.LoginResponse;
import vn.edu.usth.attendancecheck.models.LogoutResponse;

public interface NetworkService {
    /**
     * todo: finish this
     *
     */
    @POST()
    Call<AttendanceResponse> attendance(
    );

    @POST()
    Call<LoginResponse> login(
            @Query("email") String email,
            @Query("password") String password
    );


    Call<LogoutResponse> logout();
}
