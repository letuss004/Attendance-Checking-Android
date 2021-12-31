package vn.edu.usth.attendancecheck.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.edu.usth.attendancecheck.databinding.FragmentCameraBinding;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;
import vn.edu.usth.attendancecheck.providers.CameraProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment
        implements View.OnClickListener {
    private static final String TAG = "CameraFragment";
    private static final String QR_CONTENT = "content";

    private final RemoteDataSource remote = RemoteDataSource.getInstance();
    private CameraProvider camera;
    private FragmentCameraBinding binding;
    private String content;
    private Button bCapture;

    /**
     *
     */
    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance(String content) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(QR_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            content = getArguments().getString(QR_CONTENT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        bCapture = binding.bCapture;
        bCapture.setOnClickListener(this);
        camera = CameraProvider.getInstance(binding.previewView, this);
    }


    /**
     * Interface method
     *
     * @param v:
     */
    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        String text = (String) binding.bCapture.getText();
        switch (text) {
            case "5":
                bCapture.setText("4");
                camera.capturePhoto();
                break;
            case "4":
                bCapture.setText("3");
                camera.capturePhoto();
                break;
            case "3":
                bCapture.setText("Switch");
                camera.capturePhoto();
                break;
            case "Switch":
                bCapture.setText("2");
                camera.switchCamera();
                break;
            case "2":
                bCapture.setText("1");
                camera.capturePhoto();
                break;
            case "1":
                bCapture.setText("Done");
                camera.capturePhoto(true);
                break;
        }
    }


    /*
    ------------------------------------PRIVATE-----------------------------------------
     */

    public void checkAttendance() {
        Log.e(TAG, "checkAttendance: " + content);
        remote.checkAttendance(
                content,
                camera.getImagesPath(),
                camera.getImagesStatus()
        );
    }

    public Button getBCapture() {
        return bCapture;
    }
}