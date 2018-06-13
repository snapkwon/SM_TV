package vn.digital.signage.android.utils.asynctask;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import vn.digital.signage.android.Constants;
import vn.digital.signage.android.R;
import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.api.model.SourceInfo;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.utils.DebugLog;
import vn.digital.signage.android.utils.FileUtils;
import vn.digital.signage.android.utils.HashUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public final class MediaDownloadTask extends AsyncTask<Void, String, Boolean> {
    private final Logger log = Logger.getLogger(MediaDownloadTask.class);

    public static final int DOWNLOAD_BUFFER_SIZE = 4096;

    private List<LayoutInfo> lists;
    private String refUrl;
    private String prefFolderVideo;

    private Boolean mResult;
    private IProgressTracker mProgressTracker;

    /* UI Thread */
    public MediaDownloadTask(List<LayoutInfo> lists, SMRuntime runtime) {
        this.lists = lists;
        this.refUrl = runtime.getApiUrl();
        this.prefFolderVideo = runtime.getFolderVideoPath();
    }

    /* UI Thread */
    public void setProgressTracker(IProgressTracker progressTracker) {
        // Attach to progress tracker
        mProgressTracker = progressTracker;
        // Initialise progress tracker with current task state
        if (mProgressTracker != null) {
            if (mResult != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    /* UI Thread */
    @Override
    protected void onCancelled() {
        // Detach from progress tracker
        mProgressTracker = null;
    }

    /* UI Thread */
    @Override
    protected void onPostExecute(Boolean result) {
        // Update result
        mResult = result;
        // And send it to progress tracker
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        // Detach from progress tracker
        mProgressTracker = null;
    }

    /* Separate Thread */
    @Override
    protected Boolean doInBackground(Void... arg0) {

        for (LayoutInfo layout : lists) {
            if (layout.getType() == LayoutInfo.LayoutType.FRAME) {
                for (SourceInfo sourceInfo : layout.getObjSource()) {
                    if (sourceInfo.getType() == SourceInfo.SourceType.VIDEO_LIST) {

                        for (int i = 0; i < sourceInfo.getArrSources().size(); i++) {
                            String source = sourceInfo.getArrSources().get(i);
                            String hash = sourceInfo.getArrHashes().get(i);
                            downloadSourceData(getListOfSdCardFiles(), source, hash);
                        }
                    } else if (sourceInfo.getType() != SourceInfo.SourceType.URL) {
                        downloadSourceData(getListOfSdCardFiles(), sourceInfo.getSource(), sourceInfo.getHash());
                    }
                }
            } else {
                downloadSourceData(getListOfSdCardFiles(), layout.getAssets(), layout.getHash());
            }
        }

        // This return causes onPostExecute call on UI thread
        return true;
    }

    private void downloadSourceData(File[] files, String source, String hash) {

        String absolutePath = isExist(files, source);
        if (TextUtils.isEmpty(absolutePath) || !checkInvalidAndRemoveFile(absolutePath, hash)) {
            if (log.isDebugEnabled()) {
                DebugLog.d(String.format("Start download media: %s", source));
            }
            downloadMultiVideo(String.format(Config.OverallConfig.LINK_DOWNLOAD, refUrl, source));
        } else if (log.isDebugEnabled()) {
            DebugLog.d(String.format("exist download media: %s", source));
        }
    }

    public boolean checkInvalidAndRemoveFile(String url, String hash) {
        boolean result = true;

        //DebugLog.d("check hash :" + url + " - url MD5: " + hash);
        if (Constants.IS_HASH_CHECK_ENABLED) {

            if (!TextUtils.isEmpty(hash)
                    && !hash.equalsIgnoreCase(HashUtils.fileToMD5(url))) {

                DebugLog.d("check hash2 :" + url + " - url MD5: " + HashUtils.fileToMD5(url));
                FileUtils.deleteFileInPath(url);
                if (Config.hasLogLevel(LogLevel.DATA))
                    DebugLog.d("deleted file :" + url + " - url MD5: " + hash);
                result = false;
            }
        }
        return result;
    }

    private String isExist(File[] files, String source) {
        if (files != null) {
            for (File file : files) {
                if (file.getAbsolutePath().contains(source))
                    return file.getAbsolutePath();
            }
        }
        return null;
    }

    private File[] getListOfSdCardFiles() {
        final File file = new File(String.format(Config.OverallConfig.FOLDER_PATH, "media"));
        try {
//            DebugLog.d("media file in folder " + new Gson().toJson(file.listFiles()));
        } catch (Exception e) {
        }
        return file.listFiles();
    }

    private boolean downloadMultiVideo(String downloadUrl) {
        boolean result = false;
        final URL url;
        final URLConnection conn;
        final int fileSize;
        final int lastSlash;
        BufferedInputStream inStream = null;
        BufferedOutputStream outStream = null;
        FileOutputStream fileStream = null;

        File outFile = null;
        String fileName;

        try {
            url = new URL(downloadUrl);
            conn = url.openConnection();
            conn.setUseCaches(false);

            // get the filename
            lastSlash = url.toString().lastIndexOf('/');
            fileName = "file.bin";
            if (lastSlash >= 0) {
                fileName = url.toString().substring(lastSlash + 1);
            }
            if (fileName.equals("")) {
                fileName = "file.bin";
            }

            // start download
            inStream = new BufferedInputStream(conn.getInputStream());

            // create a File object for the parent directory
            File wallpaperDirectory = new File(String.format(Config.OverallConfig.FOLDER_PATH, prefFolderVideo));

            // have the object build the directory structure, if needed.
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdir();
            }

            // create a File object for the output file
            outFile = new File(wallpaperDirectory + "/" + fileName);

            fileStream = new FileOutputStream(outFile);
            outStream = new BufferedOutputStream(fileStream, DOWNLOAD_BUFFER_SIZE);
            byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
            int bytesRead = 0, totalRead = 0;

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = conn.getContentLength();


            while ((bytesRead = inStream.read(data, 0, data.length)) >= 0) {
                outStream.write(data, 0, bytesRead);
                // update progress bar
                totalRead += bytesRead;

                if (fileLength > 0) // only if total length is known
                    publishProgress(String.valueOf((int) (totalRead * 100 / fileLength)));
            }

            //outStream.close();
            //fileStream.close();
            //inStream.close();
            result = true;

        } catch (MalformedURLException e) {
            log.error(App.getInstance().getString(R.string.error_message_bad_url) + e.getMessage());
            if (outFile != null) {
                outFile.delete();
            }
        } catch (FileNotFoundException e) {
            log.error(App.getInstance().getString(R.string.error_message_file_not_found) + e.getMessage());
            if (outFile != null) {
                outFile.delete();
            }
        } catch (Exception e) {
            log.error(e);
            if (outFile != null) {
                outFile.delete();
            }
        } finally {
            // close in stream
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    log.error("Can't close in file stream");
                }
                inStream = null;
            }

            // close out stream
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    log.error("Can't close out file stream");
                }
                outStream = null;
            }

            // close file stream
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    log.error("Can't close file stream");
                }
                fileStream = null;
            }
        }
        return result;
    }
}