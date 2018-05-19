/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.digital.signage.android.feature.client.home.face.widget;

import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.home.face.logic.FaceDetectionImpl;
import vn.digital.signage.android.feature.client.home.face.logic.IFaceDetection;
import vn.digital.signage.android.feature.client.home.face.widget.camera.GraphicOverlay;

/**
 * Generic tracker which is used for tracking either a face or a barcode (and can really be used for
 * any type of item).  This is used to receive newly detected items, add a graphical representation
 * to an overlay, update the graphics as the item changes, and remove the graphics when the item
 * goes away.
 */
class GraphicTracker<T> extends Tracker<T> {
    static final String TAG = "GraphicTracker";

    private GraphicOverlay mOverlay;
    private TrackedGraphic<T> mGraphic;
    private List<TrackerInfo> trackerInfos;

    SMRuntime runtime;
    IFaceDetection faceDetection;

    GraphicTracker(GraphicOverlay overlay, TrackedGraphic<T> graphic, SMRuntime runtime) {
        Log.i(TAG, "GraphicTracker - constructor");

        mOverlay = overlay;
        mGraphic = graphic;

        this.runtime = runtime;
        faceDetection = new FaceDetectionImpl(runtime);

        trackerInfos = new ArrayList<>();
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    @Override
    public void onNewItem(int id, T item) {
        mGraphic.setId(id);

        Log.i(TAG, "GraphicTracker - onNewItem: " + id);
        TrackerInfo info = new TrackerInfo();
        info.setId(id);
        info.setStartTime(System.currentTimeMillis());
        trackerInfos.add(info);
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */
    @Override
    public void onUpdate(Detector.Detections<T> detectionResults, T item) {
        mOverlay.add(mGraphic);
        mGraphic.updateItem(item);

        Log.i(TAG, "GraphicTracker - onUpdate: " + (new Gson()).toJson(detectionResults));
    }

    /**
     * Hide the graphic when the corresponding face was not detected.  This can happen for
     * intermediate frames temporarily, for example if the face was momentarily blocked from
     * view.
     */
    @Override
    public void onMissing(Detector.Detections<T> detectionResults) {
        mOverlay.remove(mGraphic);

        Log.i(TAG, "GraphicTracker - onMissing: " + (new Gson()).toJson(detectionResults));
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
        mOverlay.remove(mGraphic);
        Log.i(TAG, "GraphicTracker - onDone");

        for (TrackerInfo info : trackerInfos) {
            if (info.getId() == mGraphic.getId()) {
                info.setEndTime(System.currentTimeMillis());

                Log.i(TAG, String.format("GraphicTracker - onDone - people count %d in %d seconds", 1, info.getViewInSeconds()));
                if (runtime != null)
                    faceDetection.onStopFaceDetection(runtime.getCurrentLayout(), info.getViewInSeconds(), 1);

                trackerInfos.remove(info);
            }
            break;
        }
    }
}
