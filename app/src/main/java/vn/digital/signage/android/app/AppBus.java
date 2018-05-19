package vn.digital.signage.android.app;

public class AppBus {

    private static AppBus instance;

    private boolean mIsAutoOnOffSheduled = false;


    public static AppBus getInstance() {
        if (instance == null)
            instance = new AppBus();
        return instance;
    }

    public boolean isAutoOnOffScheduled() {
        return mIsAutoOnOffSheduled;
    }

    public void setAutoOnOffScheduled(boolean isSetted) {
        mIsAutoOnOffSheduled = isSetted;
    }
}
