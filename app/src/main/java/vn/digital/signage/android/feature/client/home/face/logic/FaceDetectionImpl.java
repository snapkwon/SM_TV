package vn.digital.signage.android.feature.client.home.face.logic;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.model.FaceDetectionInfo;
import vn.digital.signage.android.model.FaceDetectionInfos;

public class FaceDetectionImpl implements IFaceDetection {
    private final Logger log = Logger.getLogger(FaceDetectionImpl.class);

    private SMRuntime runtime;

    public FaceDetectionImpl(SMRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public void onStartFaceDetection(LayoutInfo currentLayout) {
        // default
    }

    @Override
    public void onStopFaceDetection(LayoutInfo currentLayout, long durationInSeconds, int peopleCounter) {
        if (currentLayout != null) {

            FaceDetectionInfos faceDetectionInfos = runtime.getFaceDetectionInfos();

            if (faceDetectionInfos == null) {
                faceDetectionInfos = new FaceDetectionInfos();
            }

            if (faceDetectionInfos.getFaceDetectionInfos() == null) {
                faceDetectionInfos.setFaceDetectionInfos(new ArrayList<FaceDetectionInfo>());
            }

            FaceDetectionInfo currentInfo = null;
            for (FaceDetectionInfo info : faceDetectionInfos.getFaceDetectionInfos()) {
                if (info.getLayoutId() != null && info.getLayoutId().equals(currentLayout.getId())) {
                    currentInfo = info;
                    break;
                }
            }

            if (currentInfo == null) {
                currentInfo = new FaceDetectionInfo();
                currentInfo.setLayoutId(currentLayout.getId());
                currentInfo.setDuration(durationInSeconds);
                currentInfo.setNumPeople(peopleCounter);
                faceDetectionInfos.getFaceDetectionInfos().add(currentInfo);
            } else {
                currentInfo.setDuration(currentInfo.getDuration() + durationInSeconds);
                currentInfo.setNumPeople(currentInfo.getNumPeople() + peopleCounter);
            }

            runtime.setFaceDetectionInfos(faceDetectionInfos);
        }
    }
}
