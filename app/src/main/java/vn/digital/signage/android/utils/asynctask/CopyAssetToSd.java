package vn.digital.signage.android.utils.asynctask;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.feature.client.home.HomeController;
import vn.digital.signage.android.utils.DebugLog;

public class CopyAssetToSd extends AsyncTask<Void, Void, String> {

    private Context mContext;

    public CopyAssetToSd(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(Void... voids) {

        String url = HomeController.DEFAULT_VIDEO_PATH;
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        File[] files = getListOfSdCardFiles();
        if (files == null) {
            return copyAssets(mContext);
        }
        for (File file : files) {
            if (file.getAbsolutePath().contains(fileName))
                return file.getAbsolutePath();
        }

        return copyAssets(mContext);
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        HomeController.LOCAL_VIDEO_PATH = aVoid;
    }

    private String copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File file = new File(String.format(Config.OverallConfig.FOLDER_PATH, "assets"));
                if (!file.exists())
                    file.mkdir();

                File outFile = new File(file, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                return outFile.getAbsolutePath();
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
        return null;
    }

    private File[] getListOfSdCardFiles() {
        final File file = new File(String.format(Config.OverallConfig.FOLDER_PATH, "assets"));
        try {
            DebugLog.d("media file in folder " + new Gson().toJson(file.listFiles()));
        } catch (Exception e) {
        }
        return file.listFiles();
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[MediaDownloadTask.DOWNLOAD_BUFFER_SIZE];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
