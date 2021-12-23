package vn.edu.usth.attendancecheck.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRemoteData {
    private static final String TAG = "RemoteDataSource";
    private static LoginRemoteData instance;
//    private final MutableLiveData<List<NewsfeedPost>> liveData = new MutableLiveData<>();

    /*
    Retrofit set up things
     */
    private final String FLICKR_BASE_URL = "https://www.flickr.com/services/";
    private final Gson gson = new GsonBuilder()
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(FLICKR_BASE_URL)
            .build();
    // instance use for calling other api method
    private final LoginService service = retrofit.create(LoginService.class);
    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

    public static synchronized LoginRemoteData getInstance() {
        if (instance == null) {
            instance = new LoginRemoteData();
        }
        return instance;
    }

    public synchronized boolean login(String email, String password) {
        boolean result;
        Call call = service.login(email, password);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    return ;
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {

            }
        });
        return false;
    }
}
