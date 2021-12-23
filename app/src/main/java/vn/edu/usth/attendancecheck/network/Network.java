package vn.edu.usth.attendancecheck.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static final String BASE_URL = "http://127.0.0.1:8000/";
    /*

     */
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    // instance use for calling other api method

    private static final NetworkService service = retrofit.create(NetworkService.class);
}
