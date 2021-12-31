package vn.edu.usth.attendancecheck.providers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import vn.edu.usth.attendancecheck.ui.CameraFragment;

@SuppressLint("StaticFieldLeak")
public class CameraProvider implements ImageAnalysis.Analyzer {
    private static final String TAG = "CameraProvider";
    private static CameraProvider instance;
    private final Object holder = new Object();
    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
    //
    private final CameraFragment fragment;
    private final ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private final PreviewView previewView;
    private final List<String> imagesPath = new ArrayList<>();
    private final List<String> imagesStatus = new ArrayList<>();
    private ImageCapture imageCapture;

    /**
     * @param previewView:
     * @param fragment:
     */
    private CameraProvider(PreviewView previewView, Fragment fragment) {
        this.previewView = previewView;
        this.fragment = (CameraFragment) fragment;

        cameraProviderFuture = ProcessCameraProvider.getInstance(fragment.requireContext());
        cameraProviderFuture.addListener(
                () -> {
                    try {
                        startCamera(CameraSelector.LENS_FACING_BACK);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                getExecutor()
        );
    }

    public static CameraProvider getInstance(@NonNull PreviewView previewView,
                                             @NonNull Fragment fragment) {
        if (instance == null) {
            instance = new CameraProvider(previewView, fragment);
        }
        return instance;
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        // image processing here for the current frame
        Log.d("TAG", "analyze: got the frame at: " + image.getImageInfo().getTimestamp());
        image.close();
    }

    public void capturePhoto() {
        capturePhoto(false);
    }

    public void capturePhoto(boolean attendance) {
        final long timestamp = System.currentTimeMillis();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        //
        final ImageCapture.OutputFileOptions options =
                new ImageCapture.OutputFileOptions.Builder(
                        fragment.requireContext().getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build();
        final ImageCapture.OnImageSavedCallback callback =
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        String path = Environment.getExternalStorageDirectory()
                                + "/Pictures/"
                                + timestamp
                                + ".jpg";

                        imagesPath.add(path);
                        imagesStatus.add("1");
                        if (attendance) fragment.checkAttendance();
                        fragment.getBCapture().setEnabled(true);
                        Log.e(TAG, "onImageSaved: Photo has been saved successfully at");
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e(TAG, "onError: ", exception);
                    }
                };
        /*
        Image is captured here
         */
        fragment.getBCapture().setEnabled(false);
        imageCapture.takePicture(
                options,
                getExecutor(),
                callback
        );

    }

    public void switchCamera() {
        try {
            startCamera(CameraSelector.LENS_FACING_BACK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param lenFacing:
     */
    public synchronized void startCamera(int lenFacing)
            throws ExecutionException, InterruptedException {
        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
        cameraProvider.unbindAll();
        //
        CameraSelector cameraSelector = new CameraSelector.Builder()
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
                fragment.getViewLifecycleOwner(),
                cameraSelector,
                preview,
                imageCapture
        );
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(fragment.requireContext());
    }

    public synchronized List<String> getImagesPath() {
        return imagesPath;
    }

    public synchronized List<String> getImagesStatus() {
        return imagesStatus;
    }
}
