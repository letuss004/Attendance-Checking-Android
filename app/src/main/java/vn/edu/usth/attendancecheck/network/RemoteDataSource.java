package vn.edu.usth.attendancecheck.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.attendancecheck.network.responses.LoginResponse;
import vn.edu.usth.attendancecheck.network.responses.LogoutResponse;
import vn.edu.usth.attendancecheck.models.User;

public class RemoteDataSource {
    private static final String TAG = "RemoteDataSource";
    private static RemoteDataSource instance;
    private static User user;
    private final MutableLiveData<User> liveData = new MutableLiveData<>();

    /*
    Retrofit set up things
    */
    public static final String BASE_URL = "http://192.168.0.103:8000/";
    //    public static final String BASE_URL = "http://127.0.0.1:8000/";
    private final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .addInterceptor(
                    chain -> {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest
                                .newBuilder()
                                .header("Authorization", "Bearer " + liveData.getValue().getToken());
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
            )
            .build();
    private final Gson gson = new GsonBuilder().setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    // instance use for calling other api method
    private final ApiService service = retrofit.create(ApiService.class);
    private final Retrofit retrofit2 = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build();
    // instance use for calling other api method
    private final ApiService service2 = retrofit2.create(ApiService.class);
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


}
