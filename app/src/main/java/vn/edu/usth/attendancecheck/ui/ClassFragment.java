package vn.edu.usth.attendancecheck.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LiveData;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.adapters.ClassAdapter;
import vn.edu.usth.attendancecheck.adapters.CurrentClassesAdapter;
import vn.edu.usth.attendancecheck.databinding.FragmentClassBinding;
import vn.edu.usth.attendancecheck.network.responses.ClassLessonsResponse;
import vn.edu.usth.attendancecheck.viewmodels.ClassViewModel;
import vn.edu.usth.attendancecheck.viewmodels.CurrentClassesViewModel;

import java.util.Objects;

public class ClassFragment extends Fragment {

    public static final String ADAPTER_POSITION = "ADAPTER_POSITION";
    private int adapterPosition;
    private CurrentClassesViewModel currentClassesViewModel;
    private vn.edu.usth.attendancecheck.databinding.FragmentClassBinding binding;
    private final ClassViewModel viewModel = ClassViewModel.getInstance();
    private LiveData<ClassLessonsResponse> liveData;
    private RecyclerView recyclerView;
    private ClassAdapter adapter;

    public ClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassFragment.
     */
    public static ClassFragment newInstance(int position) {
        ClassFragment fragment = new ClassFragment();
        Bundle args = new Bundle();
        args.putInt(ADAPTER_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adapterPosition = getArguments().getInt(ADAPTER_POSITION);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClassBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiFetching();
        observeLiveData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    /*
     -------------------------------------Private-----------------------------------
     */

    private void apiFetching() {
        currentClassesViewModel = CurrentClassesViewModel.getInstance();
        liveData = viewModel.getData(
                currentClassesViewModel.getLiveData()
                        .getValue()
                        .get(adapterPosition)
                        .getId()
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeLiveData() {
        liveData.observe(
                getViewLifecycleOwner(),
                response -> {
                    if (recyclerView == null)
                        setupRecyclerView();
                    else {
                        adapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void setupRecyclerView() {
        recyclerView = binding.classRV;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new ClassAdapter(
                this,
                Objects.requireNonNull(liveData.getValue())
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}