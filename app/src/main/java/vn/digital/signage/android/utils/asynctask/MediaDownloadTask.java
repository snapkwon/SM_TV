package vn.digital.signage.android.utils.asynctask;

import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;

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

import vn.digital.signage.android.R;
import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.api.model.SourceInfo;
import vn.digital.signage.android.app.App;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.DebugLog;

public final class MediaDownloadTask extends AsyncTask<Void, String, Boolean> {
    private final Logger log = Logger.getLogger(MediaDownloadTask.class);

    public static final int DOWNLOAD_BUFFER_SIZE = 4096;

    private List<LayoutInfo> lists;
    private String refUrl;
    private String prefFolderVideo;

    private Boolean mResult;
    private IProgressTracker mProgressTracker;

    /* UI Thread */
    public MediaDownloadTask(List<LayoutInfo> lists, String refUrl, String prefFolderVideo) {
        this.lists = lists;
        this.refUrl = refUrl;
        this.prefFolderVideo = prefFolderVideo;
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
        // Check if task is cancelled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            if (isCancelled()) {
                // This return causes onPostExecute call on UI thread
                return false;
            }
        }

        File[] files = getListOfSdCardFiles();

        for (LayoutInfo layout : lists) {
            if (layout.getType() == LayoutInfo.LayoutType.FRAME) {
                for (SourceInfo sourceInfo : layout.getObjSource()) {
                    if (sourceInfo.getType() != SourceInfo.SourceType.URL) {

                        if (log.isDebugEnabled()) {
                            log.debug(String.format("Start download media: %s", sourceInfo.getSource()));
                        }
                        if (!isExist(files, sourceInfo.getSource()))
                            downloadMultiVideo(String.format(Config.OverallConfig.LINK_DOWNLOAD, refUrl, sourceInfo.getSource()));
                    }
                }
            } else {

                if (log.isDebugEnabled()) {
                    log.debug(String.format("Start download media: %s", layout.getAssets()));
                }
                if (!isExist(files, layout.getAssets()))
                    downloadMultiVideo(String.format(Config.OverallConfig.LINK_DOWNLOAD, refUrl, layout.getAssets()));
            }
        }

        // This return causes onPostExecute call on UI thread
        return true;
    }

    private boolean isExist(File[] files, String source) {
        for (File file : files) {
            if (file.getAbsolutePath().contains(source))
                return true;
        }
        return false;
    }

    private File[] getListOfSdCardFiles() {
        final File file = new File(String.format(Config.OverallConfig.FOLDER_PATH, "media"));
        try {
            DebugLog.d("media file in folder " + new Gson().toJson(file.listFiles()));
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