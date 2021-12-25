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

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.attendancecheck.models.LoginResponse;
import vn.edu.usth.attendancecheck.models.LogoutResponse;
import vn.edu.usth.attendancecheck.models.User;

public class RemoteDataSource {
    private static final String TAG = "RemoteDataSource";
    private static RemoteDataSource instance;
    private final MutableLiveData<User> liveData = new MutableLiveData<>();

    /*
    Retrofit set up things
    */
    public static final String BASE_URL = "http://192.168.0.103:8000/";
    //    public static final String BASE_URL = "http://127.0.0.1:8000/";
    private final Gson gson = new GsonBuilder()
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    // instance use for calling other api method
    private final ApiService service = retrofit.create(ApiService.class);
    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

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
        User user = null;
        try {
            user = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        // if there is exist a user return true
        liveData.setValue(user);
        return user != null;
    }

    public LiveData<User> getUser() {
        return liveData;
    }

    public synchronized boolean logout() {
        User user = liveData.getValue();
        Future<String> future = pool.submit(() -> {
            assert user != null;
            Log.e(TAG, "logout: " + user.getToken().split("\\|")[1]);
            Call<LogoutResponse> call = service.logout(user.getToken());
            Response<LogoutResponse> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert response != null;
            assert response.body() != null;
            Log.e(TAG, "logout: " + response.body());
            return response.body().getMessage();
        });
        try {
            return future.get().equals("success");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
