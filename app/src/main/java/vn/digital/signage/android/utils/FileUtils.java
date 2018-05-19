package vn.digital.signage.android.utils;

import android.content.Context;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.utils.enumeration.LogLevel;

/**
 * The type File util.
 */
public class FileUtils {
    private static final Logger log = Logger.getLogger(FileUtils.class);

    /**
     * Delete directory boolean.
     *
     * @param fileLink the file link
     * @return the boolean
     */
    public static boolean deleteDirectory(String fileLink) {
        final File file = new File(fileLink);
        if (!file.exists()) {
            return false;
        }
        try {
            return file.delete();
        } catch (Exception e) {
            if (Config.hasLogLevel(LogLevel.DATA))
                log.error("FileUtils - deleteDirectory" + e.getMessage());
        }
        return false;
    }

    /**
     * Delete file in path.
     *
     * @param src       the src
     * @param videoPath the video path
     */
    public static void deleteFileInPath(String src, String videoPath) {
        String fileLink = String.format(Config.OverallConfig.FOLDER_PATH, videoPath);

        fileLink = String.format("%s/%s", fileLink, src);
        deleteDirectory(fileLink);
    }

    /**
     * Delete file in path.
     *
     * @param src the src
     */
    public static void deleteFileInPath(String src) {
        deleteDirectory(src);
    }

    /**
     * Write utf 8 file to external storage.
     *
     * @param context  the context
     * @param fileName the file name
     * @param append   the append
     * @param data     the data
     * @throws FileNotFoundException the file not found exception
     */
    public static void writeUtf8FileToExternalStorage(Context context,
                                                      String fileName,
                                                      boolean append,
                                                      String data) throws FileNotFoundException {
        log.debug(String.format("FileUtils - writeUtf8FileToExternalStorage - filename: %s", fileName));

        FileOutputStream out = null;
        try {
            File outFile = new File(fileName);
            out = new FileOutputStream(outFile, append);
            byte[] contents = data.getBytes();
            out.write(contents);
            out.flush();
            out.close();
            log.debug(String.format("FileUtils - writeUtf8FileToExternalStorage - filename: %s -  write success - data: %s", fileName, data));
        } catch (Exception e) {
            log.error(String.format("FileUtils - writeUtf8FileToExternalStorage - filename: %s - error: %s", fileName, e.getMessage()));
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                log.error(String.format("FileUtils - writeUtf8FileToExternalStorage - filename: %s - error: %s", fileName, e.getMessage()));
            }
        }
    }


    /**
     * Read file from external storage string.
     *
     * @param context  the context
     * @param fileName the file name
     * @return the string
     * @throws FileNotFoundException the file not found exception
     */
    public static String readFileFromExternalStorage(Context context,
                                                     String fileName) throws FileNotFoundException {
        String result = null;

        log.debug(String.format("FileUtils - readFileFromExternalStorage - filename: %s", fileName));

        File file = new File(fileName); // hidden
        if (file.exists()) {
            // Read text from file
            StringBuilder text = new StringBuilder();
            BufferedReader br = null;

            try {
                br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }

                result = text.toString();

                br.close();
            } catch (IOException e) {
                log.error(String.format("FileUtils - readFileFromExternalStorage - filename: %s - error: %s", fileName, e.getMessage()));
                //You'll need to add proper error handling here
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(String.format("FileUtils - readFileFromExternalStorage - filename: %s - error: %s", fileName, e.getMessage()));
                }
            }
        } else {
            log.error(String.format("FileUtils - readFileFromExternalStorage - filename: %s - error: %s", fileName, "File does not exist"));

        }

        return result;
    }
}
