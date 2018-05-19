package vn.digital.signage.android.feature.client.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.apache.log4j.Logger;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import vn.digital.signage.android.R;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.UiUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;
import vn.digital.signage.android.utils.player.ExoPlayerImpl;
import vn.digital.signage.android.utils.player.IPlayer;
import vn.digital.signage.android.utils.player.VideoStateChanged;

public class TestVideoActivity extends FragmentActivity {
    private final Logger log = Logger.getLogger(TestVideoActivity.class);
    @InjectView(R.id.adv_video_player)
    SimpleExoPlayerView exoVideoView;

    private IPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiUtils.setFullScreenView(this);
        setContentView(R.layout.activity_test_video);
        ButterKnife.inject(this);

        UiUtils.setFullScreenWindow(this, getWindow().getDecorView());
        testVideoView();
    }

    private void testVideoView() {
        String url = "file:///android_asset/adv_default_video.mp4";
        //String url =  "/storage/emulated/0/Download/3.mp4";
        //url = "/storage/emulated/0/Android/data/vn.digital.signage.video/media/bedb23b7b238c4def51ef1074580560f.mp4";

        mPlayer = new ExoPlayerImpl(TestVideoActivity.this, exoVideoView, new VideoStateChanged() {
            @Override
            public void onError(String fileName, Exception error) {
                if (Config.hasLogLevel(LogLevel.UI))
                    log.error("VideoStateChanged - onError" + error.getMessage());
            }

            @Override
            public void onStateChanged(String fileName, int index) {
                if (Config.hasLogLevel(LogLevel.UI))
                    log.info(String.format(Locale.ENGLISH, "VideoStateChanged - onStateChanged - filename: %s - index: %d", fileName, index));
            }
        });

        mPlayer.playWithExoPlayer(url, "demo", 0);
    }

    @OnClick(R.id.test_video)
    public void onTestVideoButtonClicked(View v) {
        testVideoView();
    }
}
