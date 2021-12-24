package vn.edu.usth.attendancecheck.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource {
    private static final String TAG = "RemoteDataSource";
    private static RemoteDataSource instance;
//    private final MutableLiveData<List<NewsfeedPost>> liveData = new MutableLiveData<>();

    /*
    Retrofit set up things
     */
    public static final String FLICKR_BASE_URL = "http://127.0.0.1:8000/";
    private final Gson gson = new GsonBuilder()
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(FLICKR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    // instance use for calling other api method
    private final NetworkService service = retrofit.create(NetworkService.class);
    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

    public static synchronized RemoteDataSource getInstance() {
        if (instance == null) {
            instance = new RemoteDataSource();
        }
        return instance;
    }

    public synchronized boolean login(String email, String password) {
    }
}
