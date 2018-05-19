package vn.digital.signage.android.utils.hash;

/**
 * The interface Hash file checker.
 */
public interface HashFileChecker {
    /**
     * Check invalid and remove file boolean.
     *
     * @param url the url
     * @return the boolean
     */
    boolean checkInvalidAndRemoveFile(String url);
}
