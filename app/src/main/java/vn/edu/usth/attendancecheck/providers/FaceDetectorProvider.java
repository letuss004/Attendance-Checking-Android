package vn.edu.usth.attendancecheck.providers;


import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class FaceDetectorProvider {
    private final CameraProvider cameraProvider;

    public FaceDetectorProvider(CameraProvider cameraProvider) {
        this.cameraProvider = cameraProvider;
    }


    public boolean hasFace(InputImage image) {
        List<Face> faces = detectFaces(image);
        return faces.size() > 0;
    }


    private List<Face> detectFaces(InputImage image) {
        AtomicBoolean callbackFinished = new AtomicBoolean(false);
        // Or use the default options:
        synchronized (cameraProvider) {
            while (!callbackFinished.get()) {
                try {
                    cameraProvider.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        FaceDetector detector = FaceDetection.getClient();
        Task<List<Face>> result = detector.process(image)
                .addOnCompleteListener(
                        task -> {
                            callbackFinished.set(true);
                            synchronized (cameraProvider) {
                                cameraProvider.notifyAll();
                            }
                        }
                );
        return result.getResult();
    }


}
