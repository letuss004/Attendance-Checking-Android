package vn.edu.usth.attendancecheck.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;


import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.databinding.FragmentCodeScannerBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CodeScannerFragment extends Fragment {
    private static final String TAG = "CameraFragment";
    private View view;
    private FragmentCodeScannerBinding binding;
    private CodeScanner mCodeScanner;


    public CodeScannerFragment() {
        // Required empty public constructor
    }


    public static CodeScannerFragment newInstance(String param1, String param2) {
        CodeScannerFragment fragment = new CodeScannerFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCodeScannerBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        final CodeScannerView scannerView = binding.scannerView;
        assert activity != null;
        //
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(
                (result) -> activity.runOnUiThread(
                        () -> {
                            Toast.makeText(getContext(), result.getText(), Toast.LENGTH_SHORT).show();
                            switchFragment(new CameraFragment());
                        }
                )
        );
        scannerView.setOnClickListener(
                v -> mCodeScanner.startPreview()
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    /*

     */
    public void switchFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }
}