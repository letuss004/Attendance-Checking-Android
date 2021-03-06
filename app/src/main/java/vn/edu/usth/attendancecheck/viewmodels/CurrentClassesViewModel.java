package vn.edu.usth.attendancecheck.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.repositories.CurrentClassesRepository;

import java.util.List;

public class CurrentClassesViewModel extends ViewModel {
    private static CurrentClassesViewModel instance;
    private final CurrentClassesRepository repository = CurrentClassesRepository.getInstance();
    private final MutableLiveData<List<CurrentClasses>> liveData = new MutableLiveData<>();

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
            List<CurrentClasses> currentClasses = repository.getResponse();
            liveData.setValue(currentClasses);
        }
        return liveData;
    }

    public LiveData<List<CurrentClasses>> getLiveData() {
        return liveData;
    }
}
