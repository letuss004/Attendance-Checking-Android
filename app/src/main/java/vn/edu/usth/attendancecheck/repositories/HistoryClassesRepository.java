package vn.edu.usth.attendancecheck.repositories;

import vn.edu.usth.attendancecheck.models.HistoryClasses;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;
import vn.edu.usth.attendancecheck.network.responses.HistoryResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class HistoryClassesRepository {

    private final RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();
    private static HistoryClassesRepository instance;
    private HistoryResponse response;

    public synchronized static HistoryClassesRepository getInstance() {
        if (instance == null)
            instance = new HistoryClassesRepository();
        return instance;
    }


    public List<HistoryClasses> getHistoryClasses() {
        try {
            response = remoteDataSource.getHistoryClasses();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.getClasses();
    }
}
