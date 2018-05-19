package vn.digital.signage.android.utils.network;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FakeX509TrustManager implements X509TrustManager {

    private static final X509Certificate[] _AcceptedIssuers = new
            X509Certificate[]{};
    private static TrustManager[] trustManagers;

    public static SSLSocketFactory getAllSslSocketFactory() {

        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new FakeX509TrustManager()};
        }

        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return context.getSocketFactory();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String
            authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String
            authType) throws CertificateException {
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }
}