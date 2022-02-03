package vn.edu.usth.attendancecheck.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import vn.edu.usth.attendancecheck.models.HistoryClasses;
import vn.edu.usth.attendancecheck.repositories.HistoryClassesRepository;

import java.util.List;

public class HistoryClassesViewModel
        extends ViewModel {
    private static HistoryClassesViewModel instance;
    private final HistoryClassesRepository repository = HistoryClassesRepository.getInstance();
    private final MutableLiveData<List<HistoryClasses>> liveData = new MutableLiveData<>();

    private HistoryClassesViewModel() {
        super();
    }

    public synchronized static HistoryClassesViewModel getInstance() {
        if (instance == null)
            instance = new HistoryClassesViewModel();
        return instance;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<List<HistoryClasses>> getHistoryClasses() {
        if (liveData.getValue() == null) {
            List<HistoryClasses> value = repository.getHistoryClasses();
            liveData.setValue(value);
        }
        return liveData;
    }

    public LiveData<List<HistoryClasses>> getLiveData() {
        return liveData;
    }
}
