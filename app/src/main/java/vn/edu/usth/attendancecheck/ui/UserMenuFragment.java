package vn.edu.usth.attendancecheck.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;
import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.databinding.FragmentUserMenuBinding;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserMenuFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FragmentUserMenuBinding binding;
    private final RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();

    public UserMenuFragment() {
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
        binding = FragmentUserMenuBinding.inflate(inflater, container, false);
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
        setItemOnClick();
    }

    /*
     -------------------------------------Private-----------------------------------
     */
    private void setItemOnClick() {
        binding.test.setOnClickListener(
                v -> {
                    try {
                        remoteDataSource.getCurrentClasses();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

}