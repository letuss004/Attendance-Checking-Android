package vn.edu.usth.attendancecheck.ui;

import android.content.ContentValues;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.databinding.FragmentCameraBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment
        implements ImageAnalysis.Analyzer, View.OnClickListener {
    private View view;
    private Context context;
    private FragmentCameraBinding binding;

    // camera variable
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private Button bCapture;
    private List<Uri> imagesURI = new ArrayList<>();

    /**
     *
     */
    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity().getApplicationContext();
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
    @Override
    public void onClick(View v) {
        if (bCapture.getText().equals("Switch")) {
            startCameraX(CameraSelector.LENS_FACING_FRONT);
            return;
        }
        capturePhoto();
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(context);
    }



    /*
    ------------------------------------PRIVATE-----------------------------------------
     */

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
        CameraSelector selector = new CameraSelector.Builder()
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
                selector,
                preview,
                imageCapture
        );
    }


    private void capturePhoto() {
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
                        Toast.makeText(context, "Photo has been saved successfully at " + outputFileResults.getSavedUri(), Toast.LENGTH_SHORT).show();
                        imagesURI.add(outputFileResults.getSavedUri());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(context, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

}