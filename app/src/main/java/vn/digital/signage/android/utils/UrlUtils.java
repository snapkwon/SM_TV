package vn.digital.signage.android.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import vn.digital.signage.android.app.Config;

/**
 * The type Url utils.
 */
public class UrlUtils {

    /**
     * Download file from server.
     *
     * @param fileName  the file name
     * @param urlString the url string
     * @throws MalformedURLException the malformed url exception
     * @throws IOException           the io exception
     */
    public static void downloadFileFromServer(String fileName, String urlString) throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            final File folder = new File(Config.OverallConfig.FOLDER_DEFAULT + Config.OverallConfig.FOLDER_PACKAGE_VIDEO);
            if (!folder.exists()) {
                folder.mkdir();
            }
            final File f = new File(folder.getAbsoluteFile(), fileName);
            f.setWritable(true, false);


            URL url = new URL(urlString);

            in = new BufferedInputStream(url.openStream());
            fout = new FileOutputStream(f);

            byte buffer[] = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) > 0) {
                fout.write(buffer, 0, length);
            }

        } finally {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
    }
}
