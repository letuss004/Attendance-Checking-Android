package vn.edu.usth.attendancecheck.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.models.HistoryClasses;
import vn.edu.usth.attendancecheck.network.responses.ClassLessonsResponse;
import vn.edu.usth.attendancecheck.network.responses.HistoryResponse;
import vn.edu.usth.attendancecheck.network.responses.StudentCurrentClassesResponse;
import vn.edu.usth.attendancecheck.models.User;
import vn.edu.usth.attendancecheck.network.responses.LoginResponse;
import vn.edu.usth.attendancecheck.network.responses.LogoutResponse;

public class RemoteDataSource {
    private static final String TAG = "RemoteDataSource";
    private static RemoteDataSource instance;
    private static User user;
    private final MutableLiveData<User> liveData = new MutableLiveData<>();
    /*
    Retrofit set up things
    */
//    public static final String BASE_URL = "http://192.168.0.101:8000/api/";
//    public static final String BASE_URL = "http://127.0.0.1:8000/api/";
    public static final String BASE_URL = "http://192.168.1.11:8000/api/";

    private final Gson gson = new GsonBuilder().setLenient().excludeFieldsWithoutExposeAnnotation().create();
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    // instance use for calling other api method
    private final ApiService service = retrofit.create(ApiService.class);
    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

    /**
     * @return instance:
     */
    public static synchronized RemoteDataSource getInstance() {
        if (instance == null) {
            instance = new RemoteDataSource();
        }
        return instance;
    }

    public synchronized boolean login(@NonNull String email, @NonNull String password) {
        Future<User> future = pool.submit(() -> {
            Call<LoginResponse> call = service.login(email, password);
            Response<LoginResponse> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert response != null;
            if (response.code() == 200) {
                assert response.body() != null;
                return response.body().getUser();
            } else {
                return null;
            }
        });
        try {
            user = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        liveData.setValue(user);
        return user != null;
    }

    public LiveData<User> getUser() {
        return liveData;
    }

    public synchronized boolean logout() {
        assert user != null;
        Call<LogoutResponse> call = service.logout("Bearer " + user.getToken());
        Future<String> future = pool.submit(
                () -> {
                    Log.e(TAG, "logout token: " + user.getToken());
                    Response<LogoutResponse> response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert response != null;
                    assert response.body() != null;
                    Log.e(TAG, "logout: " + response.body().getMessage());
                    return response.body().getMessage();
                }
        );
        try {
            return future.get().equals("success");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void checkAttendance(@NonNull String content,
                                @NonNull List<String> imagesPath,
                                @NonNull List<Boolean> imagesStatus) {
        final MultipartBody.Part b1 = createPart(imagesPath.get(0), "b1");
        final MultipartBody.Part b2 = createPart(imagesPath.get(1), "b2");
        final MultipartBody.Part b3 = createPart(imagesPath.get(2), "b3");
        final MultipartBody.Part f1 = createPart(imagesPath.get(3), "f1");
        final MultipartBody.Part f2 = createPart(imagesPath.get(4), "f2");

        Map<String, Boolean> scoresList = new HashMap<>();
        scoresList.put("b1s", imagesStatus.get(0));
        scoresList.put("b2s", imagesStatus.get(1));
        scoresList.put("b3s", imagesStatus.get(2));
        scoresList.put("f1s", imagesStatus.get(3));
        scoresList.put("f2s", imagesStatus.get(4));

        final Call<ResponseBody> call = service.attendance(
                "Bearer " + user.getToken(),
                content.split("/")[content.split("/").length - 2],
                content.split("/")[content.split("/").length - 1],
                user.getId(),
                b1,
                b2,
                b3,
                f1,
                f2,
                scoresList
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d(TAG, "onResponse() returned: " + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public List<CurrentClasses> getCurrentClasses()
            throws ExecutionException, InterruptedException {
        List<CurrentClasses> currentClasses = null;
        Call<StudentCurrentClassesResponse> call = service.getStudentCurrentClasses(
                "Bearer " + user.getToken()
        );
        Future<StudentCurrentClassesResponse> future = pool.submit(
                () -> {
                    Response<StudentCurrentClassesResponse> response;
                    try {
                        response = call.execute();
                        return response.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        );
        if (future.get() != null) {
            currentClasses = future.get().getCurrentClasses();
        }
        return currentClasses;
    }

    public ClassLessonsResponse getLessonsAndStatuses(int courseId)
            throws ExecutionException, InterruptedException {
        Call<ClassLessonsResponse> call = service.getLessonsAndStatuses(
                "Bearer " + user.getToken(),
                user.getId(),
                courseId
        );
        Future<ClassLessonsResponse> future = pool.submit(
                () -> {
                    Response<ClassLessonsResponse> response;
                    try {
                        response = call.execute();
                        return response.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        );
        return future.get();
    }

    public HistoryResponse getHistoryClasses()
            throws ExecutionException, InterruptedException {
        Call<HistoryResponse> call = service.getStudentHistoryClasses(
                "Bearer " + user.getToken()
        );
        Future<HistoryResponse> future = pool.submit(
                () -> {
                    Response<HistoryResponse> response;
                    try {
                        response = call.execute();
                        return response.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        );
        return future.get();
    }
    /*
    -------------------------------------------private----------------------------------------
     */

    private static MultipartBody.Part createPart(String path, String name) {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(name, "name", requestBody);
    }

    public void test() {
        Map<String, Integer> strings = new HashMap<>();
        strings.put("One", 1);
        strings.put("two", 2);
        strings.put("three", 3);
        final Call<ResponseBody> call = service.test(
                "Bearer " + user.getToken(),
                strings
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}