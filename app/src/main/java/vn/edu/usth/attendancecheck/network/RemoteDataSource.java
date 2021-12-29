package vn.edu.usth.attendancecheck.network;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.attendancecheck.models.User;
import vn.edu.usth.attendancecheck.network.responses.LoginResponse;
import vn.edu.usth.attendancecheck.network.responses.LogoutResponse;

public class RemoteDataSource {
    private static final String TAG = "RemoteDataSource";
    private static RemoteDataSource instance;
    private static User user;
    private final MutableLiveData<User> liveData = new MutableLiveData<>();
    private final File filePath = Environment.getExternalStoragePublicDirectory("Pictures/");


    /*
    Retrofit set up things
    */
    public static final String BASE_URL = "http://192.168.0.103:8000/";
    private final Gson gson = new GsonBuilder().setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
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
        Future<User> future = pool.submit(
                () -> {
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
                }
        );
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
        Future<String> future = pool.submit(
                () -> {
                    Log.e(TAG, "logout token: " + user.getToken());
                    Call<LogoutResponse> call = service.logout("Bearer " + user.getToken());
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
                                @NonNull List<String> imagesStatus) {
        MultipartBody.Part b1 = createPart(imagesPath.get(0), "b1");
        MultipartBody.Part b2 = createPart(imagesPath.get(1), "b2");
        MultipartBody.Part b3 = createPart(imagesPath.get(2), "b3");
        MultipartBody.Part f1 = createPart(imagesPath.get(3), "f1");
        MultipartBody.Part f2 = createPart(imagesPath.get(4), "f2");
        RequestBody b1s = RequestBody.create(MediaType.parse("text/plain"), imagesStatus.get(0));
        RequestBody b2s = RequestBody.create(MediaType.parse("text/plain"), imagesStatus.get(1));
        RequestBody b3s = RequestBody.create(MediaType.parse("text/plain"), imagesStatus.get(2));
        RequestBody f1s = RequestBody.create(MediaType.parse("text/plain"), imagesStatus.get(3));
        RequestBody f2s = RequestBody.create(MediaType.parse("text/plain"), imagesStatus.get(4));

        final Call<ResponseBody> call = service.attendance(
                "Bearer " + user.getToken(),
                content.split("/")[content.split("/").length - 1],
                content.split("/")[content.split("/").length - 2],
                user.getId(),
                b1,
                b2,
                b3,
                f1,
                f2
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                Log.d(TAG, "onResponse() returned: " + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    private MultipartBody.Part createPart(String path, String name) {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("image/*"),
                file
        );
        return MultipartBody.Part.createFormData(
                name,
                "name",
                requestBody
        );
    }
}