package vn.digital.signage.android.utils.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.apache.log4j.Logger;

import java.util.Locale;

import vn.digital.signage.android.BuildConfig;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class ExoPlayerImpl implements IPlayer {
    private final Logger log = Logger.getLogger(ExoPlayerImpl.class);
    com.google.android.exoplayer2.ExoPlayer.EventListener exoMediaPlayerListener;
    private SimpleExoPlayer mPlayer;
    private Context mContext;
    private SimpleExoPlayerView mExoVideoView;
    private VideoStateChanged mCallback;

    public ExoPlayerImpl(Context context, SimpleExoPlayerView exoVideoView, VideoStateChanged callback) {
        mContext = context;
        mExoVideoView = exoVideoView;
        mCallback = callback;
    }

    @Override
    public void playWithExoPlayer(String url, final String fileName, int urlIndex) {
        if (mPlayer == null)
            initPlayer();

        // set mPlayer listener
        if (exoMediaPlayerListener != null)
            mPlayer.removeListener(exoMediaPlayerListener);

        exoMediaPlayerListener = getExoPlayerListener(fileName);
        mPlayer.addListener(exoMediaPlayerListener);

        // Measures bandwidth during playback. Can be null if not required.
        //DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        String userAgent = Util.getUserAgent(App.getInstance().getApplicationContext(), "StarMedia");
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                App.getInstance().getApplicationContext(),
                userAgent,
                new DefaultBandwidthMeter());

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null);
        mPlayer.prepare(videoSource);

        // set looping media source
        // LoopingMediaSource lp = new LoopingMediaSource(videoSource);
        // Prepare the mPlayer with the source.
        // mPlayer.prepare(lp);

        mPlayer.seekTo(0);
        mPlayer.setPlayWhenReady(true);
    }

    @Override
    public void stopExoPlayer() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
            mPlayer.stop();
            mPlayer.seekTo(0);
            if (exoMediaPlayerListener != null)
                mPlayer.removeListener(exoMediaPlayerListener);
        }
    }

    private void initPlayer() {
        if (Config.hasLogLevel(LogLevel.UI))
            log.info("ExoPlayerImpl - initPlayer");
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the mPlayer
            /*final SimpleExoPlayer*/
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);

        // 4. Bind the mPlayer to the view.
        mExoVideoView.setPlayer(mPlayer);

        View surfaceView = mExoVideoView.getVideoSurfaceView();
        // resize view part 1
        AspectRatioFrameLayout l = (AspectRatioFrameLayout) surfaceView.getParent();
        //l.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        l.setAspectRatio(0);
        l.setIsResized(false);
        mExoVideoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);

        // resize view part 2
        /*ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
        int[] sizes = UiUtils.getScreenSize(mContext);
        layoutParams.width = sizes[0];
        layoutParams.height = sizes[1];
        surfaceView.setLayoutParams(layoutParams);

        // resize view part 3
        ViewGroup.LayoutParams lp = exoVideoView.getLayoutParams();
        lp.width = sizes[0];
        lp.height = sizes[1];
        exoVideoView.setLayoutParams(lp);*/

        // enable/disable player controller
        if (!BuildConfig.DEBUG)
            mExoVideoView.setUseController(false);
    }

    private ExoPlayer.EventListener getExoPlayerListener(final String fileName) {
        return new ExoPlayer.EventListener() {
            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (Config.hasLogLevel(LogLevel.UI))
                    log.info(String.format(Locale.ENGLISH, "ExoPlayerImpl - onLoadingChanged - isLoading %b", isLoading));

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (Config.hasLogLevel(LogLevel.UI))
                    log.info(String.format(Locale.ENGLISH, "ExoPlayerImpl - onPlayerStateChanged - playWhenReady %b - playbackState %d", playWhenReady, playbackState));

                switch (playbackState) {
                    case ExoPlayer.STATE_BUFFERING:
                        break;
                    case com.google.android.exoplayer2.ExoPlayer.STATE_ENDED:
                        mCallback.onStateChanged(fileName, -1);
                        //mPlayer.seekTo(0);
                        break;
                    case ExoPlayer.STATE_IDLE:
                    case ExoPlayer.STATE_READY:
                    default:
                        break;
                }
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                if (Config.hasLogLevel(LogLevel.UI))
                    log.info("ExoPlayerImpl - onTimelineChanged");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                if (Config.hasLogLevel(LogLevel.UI))
                    log.error("ExoPlayerImpl Error", error);
                mCallback.onError(fileName, error);
            }

            @Override
            public void onPositionDiscontinuity() {
                if (Config.hasLogLevel(LogLevel.UI))
                    log.info("ExoPlayerImpl - onPositionDiscontinuity");
            }
        };
    }
}
