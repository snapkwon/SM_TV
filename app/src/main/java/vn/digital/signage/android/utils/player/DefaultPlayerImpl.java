package vn.digital.signage.android.utils.player;

public class DefaultPlayerImpl implements IPlayer {

    private void playOldPlayer(String url, final String fileName, int urlIndex) {
        /*videoView.setVisibility(View.VISIBLE);
        Log.d(TAG, "auto_play_media - playVideoMedia - stop playback");
        log.debug("stop play back");
        videoView.stopPlayback();
        videoView.setOnCompletionListener(listener);
        videoView.setLayoutParams(Utils.UIConversion.getFullScreenView(mContext.getActivity(), videoView));
        log.debug(String.format("start play back - %s", fileName));
        videoView.setOnPreparedListener(new MediaPlayerOnPreparedListener()); // start video view in this listener
        videoView.setOnErrorListener(new MediaPlayerOnErrorListener(fileName));
        Uri uri = Uri.parse(url);
        log.debug(String.format("playing file uri - %s", Uri.parse(url).getPath()));
        log.debug(String.format("playing file url - %s", url));
        Log.d(TAG, String.format("auto_play_media - playing file url - index: %d - url: %s", urlIndex, url));
        videoView.setVideoURI(uri);

        videoView.setTag(fileName);
        videoView.requestFocus();*/
    }

    @Override
    public void playWithExoPlayer(String url, String fileName, int urlIndex) {

    }

    /*public void getOnOffTimer() {
        if (runtime.getRegisterInfo() != null) {
            homeController.getOnOffTimer(runtime.getRegisterInfo().getId());
        }
    }*/

    /*private void playDefaultAdvertiseMedia(final String url) {
        final String fileName = url.substring(url.lastIndexOf('/') + 1);

        runtime.setMediaFileNameOn(fileName);
        videoView.setVideoURI(Uri.parse(url));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                runtime.setMediaFileNameOff(fileName);
            }
        });

        videoView.setLayoutParams(Utils.UIConversion.getFullScreenView(mContext.getActivity(), videoView));
        videoView.requestFocus();
        videoView.bringToFront();
    }*/

    /*private class MediaPlayerOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mp.isPlaying())
                mp.reset();

            Log.d(TAG, String.format("auto_play_media - on Video started"));
            //videoView.start();
        }
    }

    private class MediaPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        String fileNameCompletion;

        MediaPlayerOnCompletionListener(String fileNameCompletion) {
            this.fileNameCompletion = fileNameCompletion;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            runtime.setMediaFileNameOff(fileNameCompletion);

            playSelectedMediaFile(getMediaFileList());
        }
    }

    private class MediaPlayerOnErrorListener implements MediaPlayer.OnErrorListener {

        private String mFileName;

        MediaPlayerOnErrorListener(String fileName) {
            this.mFileName = fileName;
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            try {
                deleteFileInPath(mFileName);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.error("Error start video by list", e);
                    Log.e(TAG, String.format("auto_play_media - MediaPlayerOnErrorListener - %s",
                            e.getMessage()));

                }
            } finally {
                playDefaultAdvertiseMedia(getMediaFileList());
            }
            return true;
        }
    }*/
}
