package vn.edu.usth.attendancecheck.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.models.Lessons;
import vn.edu.usth.attendancecheck.network.responses.ClassLessonsResponse;
import vn.edu.usth.attendancecheck.repositories.ClassRepository;

import java.util.List;

public class ClassViewModel extends ViewModel {
    private static ClassViewModel instance;
    private ClassLessonsResponse response;
    private final ClassRepository repository = ClassRepository.getInstance();
    private final MutableLiveData<List<Lessons>> lessons = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> statuses = new MutableLiveData<>();

    private ClassViewModel() {
        super();
    }

    public synchronized static ClassViewModel getInstance() {
        if (instance == null)
            instance = new ClassViewModel();
        return instance;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<List<Lessons>> getLessons(int courseID) {
        if (response == null) {
            response = requestData(courseID);
            if (lessons.getValue() == null)
                lessons.setValue(response.getLessons());
        }
        return lessons;
    }

    public LiveData<List<Integer>> getStatuses(int courseID) {
        if (response == null) {
            response = requestData(courseID);
            if (statuses.getValue() == null)
                statuses.setValue(response.getStatuses());
        }
        return statuses;
    }

    public LiveData<ClassLessonsResponse> getData(int courseId) {
        if (response == null)
            response = requestData(courseId);
        return new MutableLiveData<>(response);
    }

    private ClassLessonsResponse requestData(int courseId) {
        return repository.getLessonsData(courseId);
    }
}
