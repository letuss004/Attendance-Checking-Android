package vn.edu.usth.attendancecheck.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import vn.edu.usth.attendancecheck.databinding.FragmentCameraBinding;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment
        implements ImageAnalysis.Analyzer, View.OnClickListener {
    private static final String TAG = "CameraFragment";
    private View view;
    private Context context;
    private FragmentCameraBinding binding;
    private final RemoteDataSource remote = RemoteDataSource.getInstance();
    private static final String QR_CONTENT = "content";
    private String content;
    // camera variable
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraSelector cameraSelector;
    private ImageCapture imageCapture;
    private PreviewView previewView;
    private Button bCapture;
    private List<String> imagesPath = new ArrayList<>();
    private List<String> imagesStatus = new ArrayList<>();

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
        context = requireActivity().getApplicationContext();
        if (getArguments() != null) {
            content = getArguments().getString(QR_CONTENT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        previewView = binding.previewView;
        bCapture = binding.bCapture;
        bCapture.setOnClickListener(this);

        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(
                () -> startCameraX(CameraSelector.LENS_FACING_BACK),
                getExecutor()
        );
    }


    /**
     * Interface method
     *
     * @param image:
     */
    @Override
    public void analyze(@NonNull ImageProxy image) {
        // image processing here for the current frame
        Log.d("TAG", "analyze: got the frame at: " + image.getImageInfo().getTimestamp());
        image.close();
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
                binding.bCapture.setText("4");
                capturePhoto();
                break;
            case "4":
                binding.bCapture.setText("3");
                capturePhoto();
                break;
            case "3":
                binding.bCapture.setText("Switch");
                capturePhoto();
                break;
            case "Switch":
                binding.bCapture.setText("2");
                startCameraX(CameraSelector.LENS_FACING_FRONT);
                break;
            case "2":
                binding.bCapture.setText("1");
                capturePhoto();
                break;
            case "1":
                capturePhoto(true);
                binding.bCapture.setText("Done");
                break;
        }
    }


    /*
    ------------------------------------PRIVATE-----------------------------------------
     */
    private void checkAttendance() {
        Log.e(TAG, "checkAttendance: " + content);
        remote.checkAttendance(
                content,
                imagesPath,
                imagesStatus
        );
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(context);
    }

    private void capturePhoto() {
        capturePhoto(false);
    }

    private void capturePhoto(boolean wait) {
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        this.requireActivity().getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        String path = Environment.getExternalStorageDirectory()
                                + "/Pictures/"
                                + timestamp
                                + ".jpg";
                        imagesPath.add(path);
                        imagesStatus.add("1");
                        if (wait) checkAttendance();
                        Toast.makeText(context, "Photo has been saved successfully at " + path, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(context, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }


    /**
     * @param lenFacing:
     */
    private void startCameraX(int lenFacing) {
        // cameraProvider
        ProcessCameraProvider cameraProvider = null;
        try {
            cameraProvider = cameraProviderFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        assert cameraProvider != null;
        cameraProvider.unbindAll();
        //
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lenFacing)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // Image analysis use case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(getExecutor(), this);

        //bind to lifecycle:
        cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture
        );
    }

}