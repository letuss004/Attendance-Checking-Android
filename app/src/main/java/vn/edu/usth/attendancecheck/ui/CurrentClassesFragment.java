package vn.edu.usth.attendancecheck.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import vn.edu.usth.attendancecheck.adapters.CurrentClassesAdapter;
import vn.edu.usth.attendancecheck.databinding.FragmentCurrentClassesBinding;
import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.viewmodels.CurrentClassesViewModel;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentClassesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentClassesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FragmentCurrentClassesBinding binding;
    private final CurrentClassesViewModel viewModel = CurrentClassesViewModel.getInstance();
    private LiveData<List<CurrentClasses>> liveData;

    public CurrentClassesFragment() {
    }

    public static UserMenuFragment newInstance(String param1, String param2) {
        UserMenuFragment fragment = new UserMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCurrentClassesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiFetching();
        observeLiveData();
        setItemOnClick();
    }


    /*
     -------------------------------------Private-----------------------------------
     */

    /**
     * <p>
     * Call Api from server <b>into live data</b> by using remote data source
     * </p>
     */
    private void apiFetching() {
        liveData = viewModel.getCurrentClasses();
    }

    private void observeLiveData() {
        liveData.observe(
                getViewLifecycleOwner(),
                currentClasses -> setupRecyclerView()
        );
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.currentClassesRV;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        CurrentClassesAdapter adapter = new CurrentClassesAdapter(
                this,
                Objects.requireNonNull(liveData.getValue())
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setItemOnClick() {

    }
}