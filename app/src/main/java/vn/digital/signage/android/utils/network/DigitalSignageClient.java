package vn.digital.signage.android.utils.network;

import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import vn.digital.signage.android.app.Config;

public class DigitalSignageClient implements Client {
    private Client mWrappedClient;

    public DigitalSignageClient() {

        final OkHttpClient client = new OkHttpClient();
        //2 minutes connect timeout
        client.setConnectTimeout(Config.NetworkApiConfig.DEFAULT_CONNECTION_TIME_OUT, TimeUnit.MINUTES);
        //2 minutes socket timeout
        client.setReadTimeout(Config.NetworkApiConfig.DEFAULT_CONNECTION_TIME_OUT, TimeUnit.MINUTES);
        client.setWriteTimeout(Config.NetworkApiConfig.DEFAULT_CONNECTION_TIME_OUT, TimeUnit.MINUTES);
        // Ssl Socket
        client.setSslSocketFactory(FakeX509TrustManager.getAllSslSocketFactory());
        // The Verifier Hostname
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        // set max idle connections to zero.
        client.setConnectionPool(new ConnectionPool(Config.NetworkApiConfig.MAX_CONNECTION_POOL,
                Config.NetworkApiConfig.DEFAULT_KEEP_ALIVE_DURATION));

        File cacheDir = new File(System.getProperty("java.io.tmpdir"), "okhttp-cache");
        Cache cache = null;
        try {
            cache = new Cache(cacheDir, 10L * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.setCache(cache);

        mWrappedClient = new OkClient(client);
    }

    public DigitalSignageClient(Client client) {
        mWrappedClient = client;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Log.e("Retrofit Request Body", request.toString());

        return mWrappedClient.execute(request);
    }
}
