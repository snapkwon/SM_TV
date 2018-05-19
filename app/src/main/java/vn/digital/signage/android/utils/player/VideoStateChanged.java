package vn.digital.signage.android.utils.player;

public interface VideoStateChanged {

    void onError(String fileName, Exception error);

    void onStateChanged(String fileName, int index);
}
