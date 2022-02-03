package vn.edu.usth.attendancecheck.repositories;

import androidx.lifecycle.LiveData;

import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.models.Lessons;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrentClassesRepository {
    private final RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();
    private static CurrentClassesRepository instance;
    private List<CurrentClasses> currentClasses;

    public synchronized static CurrentClassesRepository getInstance() {
        if (instance == null)
            instance = new CurrentClassesRepository();
        return instance;
    }

    public List<CurrentClasses> getCurrentClasses() {
        try {
            currentClasses = remoteDataSource.getCurrentClasses();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return currentClasses;
    }


}
