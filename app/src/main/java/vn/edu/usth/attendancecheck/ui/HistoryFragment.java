package vn.edu.usth.attendancecheck.ui;

import android.annotation.SuppressLint;
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

import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.adapters.CurrentClassesAdapter;
import vn.edu.usth.attendancecheck.adapters.HistoryAdapter;
import vn.edu.usth.attendancecheck.databinding.FragmentCurrentClassesBinding;
import vn.edu.usth.attendancecheck.databinding.FragmentHistoryBinding;
import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.models.HistoryClasses;
import vn.edu.usth.attendancecheck.viewmodels.CurrentClassesViewModel;
import vn.edu.usth.attendancecheck.viewmodels.HistoryClassesViewModel;

import java.util.List;
import java.util.Objects;


public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private final HistoryClassesViewModel viewModel = HistoryClassesViewModel.getInstance();
    private LiveData<List<HistoryClasses>> liveData;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
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
    }

    /**
     * @param adapterPosition:
     */
    public void switchFragment(int adapterPosition) {
        getParentFragmentManager().beginTransaction().replace(
                R.id.nav_host_fragment,
                ClassFragment.newInstance(adapterPosition)
        ).commit();
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
        liveData = viewModel.getHistoryClasses();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeLiveData() {
        liveData.observe(
                getViewLifecycleOwner(),
                historyClasses -> {
                    if (recyclerView == null)
                        setupRecyclerView();
                    else {
                        adapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void setupRecyclerView() {
        recyclerView = binding.historyFragmentRV;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new HistoryAdapter(
                this,
                Objects.requireNonNull(liveData.getValue())
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

}