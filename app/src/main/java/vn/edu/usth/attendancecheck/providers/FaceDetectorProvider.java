package vn.edu.usth.attendancecheck.providers;


import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import java.util.ArrayList;
import java.util.List;

public class FaceDetectorProvider {
    private final CameraProvider cameraProvider;
    private final List<Boolean> imagesStatus = new ArrayList<>();
    private final FaceDetector detector = FaceDetection.getClient();


    public FaceDetectorProvider(CameraProvider cameraProvider) {
        this.cameraProvider = cameraProvider;
    }

    public void process(InputImage inputImage) {
        detectFaces(inputImage);
    }

    public void processThenAttendance(InputImage inputImage) {
        detectFacesThenAttendance(inputImage);
    }

    private void detectFacesThenAttendance(InputImage image) {
        detector.process(image)
                .addOnCompleteListener(
                        task -> {
                            imagesStatus.add(task.getResult().size() > 0);
                            cameraProvider.getFragment().checkAttendance(imagesStatus);
                        }
                );
    }

    private void detectFaces(InputImage image) {
        detector.process(image)
                .addOnCompleteListener(task -> imagesStatus.add(task.getResult().size() > 0));
    }


}
