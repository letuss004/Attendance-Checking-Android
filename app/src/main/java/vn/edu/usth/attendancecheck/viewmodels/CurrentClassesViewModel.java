package vn.edu.usth.attendancecheck.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrentClassesViewModel extends ViewModel {
    private final RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();
    private static CurrentClassesViewModel instance;
    private final MutableLiveData<List<CurrentClasses>> liveData = new MutableLiveData<>();

    private CurrentClassesViewModel() {
        super();
    }

    public synchronized static CurrentClassesViewModel getInstance() {
        if (instance == null) {
            instance = new CurrentClassesViewModel();
        }
        return instance;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<List<CurrentClasses>> getCurrentClasses() {
        try {
            if (remoteDataSource.getCurrentClasses() != null) {
                liveData.setValue(remoteDataSource.getCurrentClasses());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return liveData;
    }
}
