package vn.digital.signage.android.feature.client.home.face.logic;

import vn.digital.signage.android.api.model.LayoutInfo;

/**
 * The interface Face detection.
 */
public interface IFaceDetection {
    /**
     * On start face detection.
     */
    void onStartFaceDetection(LayoutInfo currentLayout);

    /**
     * On stop face detection.
     */
    void onStopFaceDetection(LayoutInfo currentLayout, long duration, int peopleCounter);
}
