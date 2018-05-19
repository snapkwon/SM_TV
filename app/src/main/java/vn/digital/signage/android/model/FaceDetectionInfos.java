package vn.digital.signage.android.model;

import java.io.Serializable;
import java.util.List;

/**
 * The type Face detection infos.
 */
public class FaceDetectionInfos implements Serializable {

    private List<FaceDetectionInfo> faceDetectionInfos;

    /**
     * Gets face detection infos.
     *
     * @return the face detection infos
     */
    public List<FaceDetectionInfo> getFaceDetectionInfos() {
        return faceDetectionInfos;
    }

    /**
     * Sets face detection infos.
     *
     * @param faceDetectionInfos the face detection infos
     */
    public void setFaceDetectionInfos(List<FaceDetectionInfo> faceDetectionInfos) {
        this.faceDetectionInfos = faceDetectionInfos;
    }
}
