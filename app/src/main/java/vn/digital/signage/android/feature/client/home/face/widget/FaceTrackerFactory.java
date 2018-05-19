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

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.home.face.widget.camera.GraphicOverlay;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new face.  The
 * multi-processor uses this factory to create face trackers as needed -- one for each individual.
 */
public class FaceTrackerFactory implements MultiProcessor.Factory<Face> {
    public static final String TAG = FaceTrackerFactory.class.getSimpleName();

    private GraphicOverlay mGraphicOverlay;

    SMRuntime runtime;

    public FaceTrackerFactory(GraphicOverlay graphicOverlay, SMRuntime runtime) {
        Log.i(TAG, "FaceTrackerFactory - constructor");
        mGraphicOverlay = graphicOverlay;
        this.runtime = runtime;
    }

    @Override
    public Tracker<Face> create(Face face) {
        Log.i(TAG, "FaceTrackerFactory - create face detection");
        FaceGraphic graphic = new FaceGraphic(mGraphicOverlay);
        return new GraphicTracker<>(mGraphicOverlay, graphic, runtime);
    }
}