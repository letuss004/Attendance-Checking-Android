//package vn.edu.usth.attendancecheck;
//
//import android.content.ContentValues;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageCapture;
//import androidx.camera.core.ImageCaptureException;
//import androidx.camera.core.ImageProxy;
//import androidx.camera.core.Preview;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.core.content.ContextCompat;
//
//import com.google.common.util.concurrent.ListenableFuture;
//
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
//
//
//public class DualCameraActivity
//        extends AppCompatActivity implements ImageAnalysis.Analyzer {
//    private static final String TAG = "DualCameraActivity";
//    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
//    private PreviewView pvvBack;
//    private ImageCapture imageCapture;
//    private PreviewView pvvFont;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dual_camera);
//
//        pvvBack = findViewById(R.id.pvvBack);
//        pvvFont = findViewById(R.id.pvvFront);
//        Button bCapture = findViewById(R.id.bCapture);
//        bCapture.setOnClickListener(v -> capturePhoto());
//
//        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//        cameraProviderFuture.addListener(
//                this::startCameraX,
//                getExecutor()
//        );
//    }
//
//    private Executor getExecutor() {
//        return ContextCompat.getMainExecutor(this);
//    }
//
//    /**
//     *
//     * @param lenFacing:
//     */
//    private void startCameraX(int lenFacing) {
//        // cameraProvider
//        ProcessCameraProvider cameraProvider = null;
//        try {
//            cameraProvider = cameraProviderFuture.get();
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        assert cameraProvider != null;
//        cameraProvider.unbindAll();
//        //
//        CameraSelector selector = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build();
//        Preview preview = new Preview.Builder()
//                .build();
//        preview.setSurfaceProvider(pvvBack.getSurfaceProvider());
//
//        // Image capture use case
//        imageCapture = new ImageCapture.Builder()
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                .build();
//
//        // Image analysis use case
//        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .build();
//        imageAnalysis.setAnalyzer(getExecutor(), this);
//
//        //bind to lifecycle:
//        cameraProvider.bindToLifecycle(this, selector, preview, imageCapture);
//    }
//
//    @Override
//    public void analyze(@NonNull ImageProxy image) {
//        // image processing here for the current frame
//        Log.d("TAG", "analyze: got the frame at: " + image.getImageInfo().getTimestamp());
//        image.close();
//    }
//
//
//    private void capturePhoto() {
//        long timestamp = System.currentTimeMillis();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//
//
//        imageCapture.takePicture(
//                new ImageCapture.OutputFileOptions.Builder(
//                        getContentResolver(),
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        contentValues
//                ).build(),
//                getExecutor(),
//                new ImageCapture.OnImageSavedCallback() {
//                    @Override
//                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                        Toast.makeText(DualCameraActivity.this, "Photo has been saved successfully.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(@NonNull ImageCaptureException exception) {
//                        Toast.makeText(DualCameraActivity.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//    }
//}