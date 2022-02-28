package vn.edu.usth.attendancecheck.repositories;

import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;
import vn.edu.usth.attendancecheck.network.responses.StudentCurrentClassesResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrentClassesRepository {
    private final RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();
    private static CurrentClassesRepository instance;
    private StudentCurrentClassesResponse response;

    public synchronized static CurrentClassesRepository getInstance() {
        if (instance == null)
            instance = new CurrentClassesRepository();
        return instance;
    }

    public List<CurrentClasses> getResponse() {
        try {
            response = remoteDataSource.getCurrentClasses();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.getCurrentClasses();
    }


}
