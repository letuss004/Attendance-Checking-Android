package vn.edu.usth.attendancecheck.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
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
    Call studentsCourse(
            @Header("Authorization") String token
    );


    @POST("api/attendance/")
    @Multipart
    Call<ResponseBody> attendance(
            @Header("Authorization") String token,
            @Query("lesson_id") String lessonID,
            @Query("qr_id") String qrID,
            @Query("student_id") String studentID,
            @Part MultipartBody.Part b1,
            @Part MultipartBody.Part b2,
            @Part MultipartBody.Part b3,
            @Part MultipartBody.Part f1,
            @Part MultipartBody.Part f2
    );

    @GET("api/test")
    Call<ResponseBody> test(
            @Header("Authorization") String token,
            @Query("value") String value);
}
