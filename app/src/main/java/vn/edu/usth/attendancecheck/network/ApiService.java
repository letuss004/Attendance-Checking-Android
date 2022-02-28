package vn.edu.usth.attendancecheck.network;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import vn.edu.usth.attendancecheck.network.responses.ClassLessonsResponse;
import vn.edu.usth.attendancecheck.network.responses.HistoryResponse;
import vn.edu.usth.attendancecheck.network.responses.StudentCurrentClassesResponse;
import vn.edu.usth.attendancecheck.network.responses.LoginResponse;
import vn.edu.usth.attendancecheck.network.responses.LogoutResponse;

import java.util.List;
import java.util.Map;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("auth/logout")
    Call<LogoutResponse> logout(
            @Header("Authorization") String token
    );


    @POST("attendance")
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
            @Part MultipartBody.Part f2,
            @Query("scores") Map<String, Boolean> scoresList
    );

    @POST("attendance")
    @Multipart
    Call<ResponseBody> attendance(
            @Header("Authorization") String token,
            @Query("lesson_id") String lessonID,
            @Query("qr_id") String qrID,
            @Query("student_id") String studentID,
            @Part List<MultipartBody.Part> partList
    );

    @POST("test")
    Call<ResponseBody> test(
            @Header("Authorization") String token,
            @Body List<String> strings
    );

    @POST("test")
    Call<ResponseBody> test(
            @Header("Authorization") String token,
            @Body Map<String, Integer> map
    );

    @POST("student/current/classes")
    Call<StudentCurrentClassesResponse> getStudentCurrentClasses(
            @Header("Authorization") String token
    );


    @POST("course/lessons")
    Call<ClassLessonsResponse> getLessonsAndStatuses(
            @Header("Authorization") String token,
            @Query("student_id") String studentId,
            @Query("course_id") int courseId
    );

    @POST("student/history/classes")
    Call<HistoryResponse> getStudentHistoryClasses(
            @Header("Authorization") String token
    );
}
