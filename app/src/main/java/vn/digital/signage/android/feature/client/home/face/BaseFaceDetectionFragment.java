package vn.digital.signage.android.feature.client.home.face;

import android.support.v4.app.Fragment;

public interface BaseFaceDetectionFragment {

    String getTagName();

    String getTagFromClassName();

    Fragment getInstance();

    void setupFragmentComponent();
}