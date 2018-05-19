package vn.digital.signage.android.data;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import se.simbio.encryption.Encryption;
import vn.digital.signage.android.BuildConfig;
import vn.digital.signage.android.utils.Utils;

/**
 * The type Encrypt helper.
 */
public class EncryptHelper {

    private Encryption encrypter;

    /**
     * Instantiates a new Encrypt helper.
     */
    public EncryptHelper() {
        if (encrypter == null)
            encrypter = getEncryptor();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static EncryptHelper getInstance() {
        if (ContextSingleton.getInstance() == null) {
            ContextSingleton.setInstance(new EncryptHelper());
        }
        return ContextSingleton.getInstance();
    }

    public String encrypt(String data) throws NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        if (BuildConfig.DEBUG) {
            return data; // no encrypt in debug mode
        } else {
            return encrypter.encrypt(data);
        }
    }

    public String decrypt(String data) throws NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        if (BuildConfig.DEBUG) {
            return data; // no decrypt in debug mode
        } else {
            return encrypter.decrypt(data);
        }
    }

    private Encryption getEncryptor() {
        String key = Utils.sha256("smg_digital_signage");
        String salt = Utils.sha256("vn.digital.signage.android");
        byte[] iv = new byte[16];
        return Encryption.getDefault(key, salt, iv);
    }


    static class ContextSingleton {
        private static EncryptHelper mInstance;

        private ContextSingleton() {
            // hide public Context Singleton
        }

        public static EncryptHelper getInstance() {
            return mInstance;
        }

        public static void setInstance(EncryptHelper instance) {
            mInstance = instance;
        }
    }
}
