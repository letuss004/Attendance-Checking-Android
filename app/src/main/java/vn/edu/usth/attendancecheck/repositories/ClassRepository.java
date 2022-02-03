package vn.edu.usth.attendancecheck.repositories;

import java.util.concurrent.ExecutionException;

import vn.edu.usth.attendancecheck.network.RemoteDataSource;
import vn.edu.usth.attendancecheck.network.responses.ClassLessonsResponse;

public class ClassRepository {
    private final RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();
    private static ClassRepository instance;
    private ClassLessonsResponse response;

    public synchronized static ClassRepository getInstance() {
        if (instance == null)
            instance = new ClassRepository();
        return instance;
    }

    public ClassLessonsResponse getLessonsData(int courseID) {
        try {
            response = remoteDataSource.getLessonsAndStatuses(courseID);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

}
