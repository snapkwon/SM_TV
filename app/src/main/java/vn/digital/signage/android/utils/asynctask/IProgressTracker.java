package vn.digital.signage.android.utils.asynctask;

public interface IProgressTracker {

    // Updates progress message
    void onProgress(String totalReadInKB);

    // Notifies about task completeness
    void onComplete();
}