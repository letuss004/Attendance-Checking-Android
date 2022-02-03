package vn.edu.usth.attendancecheck.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;
import vn.edu.usth.attendancecheck.repositories.CurrentClassesRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrentClassesViewModel extends ViewModel {
    private static CurrentClassesViewModel instance;
    private final MutableLiveData<List<CurrentClasses>> liveData = new MutableLiveData<>();
    private final CurrentClassesRepository repository = CurrentClassesRepository.getInstance();

    private CurrentClassesViewModel() {
        super();
    }

    public synchronized static CurrentClassesViewModel getInstance() {
        if (instance == null)
            instance = new CurrentClassesViewModel();
        return instance;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<List<CurrentClasses>> getCurrentClasses() {
        if (liveData.getValue() == null) {
            List<CurrentClasses> currentClasses = repository.getCurrentClasses();
            liveData.setValue(currentClasses);
        }
        return liveData;
    }

    public LiveData<List<CurrentClasses>> getLiveData() {
        return liveData;
    }
}
