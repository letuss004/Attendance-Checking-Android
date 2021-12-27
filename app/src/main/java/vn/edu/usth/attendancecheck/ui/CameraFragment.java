//package vn.edu.usth.attendancecheck.ui;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageCapture;
//import androidx.camera.core.ImageCaptureException;
//import androidx.camera.core.ImageProxy;
//import androidx.camera.core.Preview;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.google.common.util.concurrent.ListenableFuture;
//
//import java.util.Objects;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
//
//import vn.edu.usth.attendancecheck.R;
//import vn.edu.usth.attendancecheck.databinding.FragmentCameraBinding;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link CameraFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class CameraFragment extends Fragment
//        implements ImageAnalysis.Analyzer, View.OnClickListener {
//    private View view;
//    private final Context context = requireContext();
//    private FragmentCameraBinding binding;
//
//    // camera variable
//    private final PreviewView pvvFront = binding.pvvFront;
//    private final PreviewView pvvBack = binding.pvvBack;
//    private final Button bCapture = binding.bCapture;
//    private final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);
//    private final ImageCapture imageCapture = new ImageCapture.Builder()
//            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//            .build();
//    // Image analysis use case
//    private final ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build();
//
//
//    /**
//     *
//     */
//    public CameraFragment() {
//        // Required empty public constructor
//    }
//
//    public static CameraFragment newInstance(String param1, String param2) {
//        CameraFragment fragment = new CameraFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container,
//                             Bundle savedInstanceState) {
//        binding = FragmentCameraBinding.inflate(inflater, container, false);
//        view = binding.getRoot();
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //
//        bCapture.setOnClickListener(this);
//        cameraProviderFuture.addListener(
//                () -> {
//                    try {
//                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                        startCamera(cameraProvider);
//                    } catch (ExecutionException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                },
//                getMainExecutor()
//        );
//    }
//
//
//    /**
//     * Interface method
//     *
//     * @param image:
//     */
//    @Override
//    public void analyze(@NonNull ImageProxy image) {
//        // image processing here for the current frame
//        Log.d("TAG", "analyze: got the frame at: " + image.getImageInfo().getTimestamp());
//        image.close();
//    }
//
//    /**
//     * Interface method
//     *
//     * @param v:
//     */
//    @Override
//    public void onClick(View v) {
//        capturePhoto();
//    }
//
//    /*
//    ------------------------------------PRIVATE-----------------------------------------
//     */
//    private Executor getMainExecutor() {
//        return ContextCompat.getMainExecutor(context);
//    }
//
//    /**
//     * @param cameraProvider:
//     */
//    private void startCamera(ProcessCameraProvider cameraProvider) {
//        cameraProvider.unbindAll();
//        imageAnalysis.setAnalyzer(getMainExecutor(), this);
//        // set camera
//        CameraSelector backCamera = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build();
//        Preview previewBackCamera = new Preview.Builder()
//                .build();
//        previewBackCamera.setSurfaceProvider(pvvFront.getSurfaceProvider());
//
//        CameraSelector frontCamera = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
//                .build();
//        Preview previewFrontCamera = new Preview.Builder()
//                .build();
//        previewBackCamera.setSurfaceProvider(pvvBack.getSurfaceProvider());
//
//
//        //bind to lifecycle:
//        cameraProvider.bindToLifecycle(
//                getViewLifecycleOwner(),
//                backCamera,
//                previewBackCamera,
//                imageCapture
//        );
//        cameraProvider.bindToLifecycle(
//                getViewLifecycleOwner(),
//                frontCamera,
//                previewFrontCamera,
//                imageCapture
//        );
//    }
//
//
//    /**
//     *
//     */
//    private void capturePhoto() {
//        long timestamp = System.currentTimeMillis();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//
//        imageCapture.takePicture(
//                new ImageCapture.OutputFileOptions.Builder(
//                        Objects.requireNonNull(getActivity()).getContentResolver(),
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        contentValues
//                ).build(),
//                getMainExecutor(),
//                new ImageCapture.OnImageSavedCallback() {
//                    @Override
//                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                        Toast.makeText(requireContext(), "Photo has been saved successfully.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(@NonNull ImageCaptureException exception) {
//                        Toast.makeText(requireContext(), "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//    }
//}