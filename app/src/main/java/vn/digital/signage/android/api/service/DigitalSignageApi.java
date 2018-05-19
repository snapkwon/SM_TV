package vn.digital.signage.android.api.service;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.mime.MultipartTypedOutput;
import vn.digital.signage.android.api.request.OnOffTimerRequest;

public interface DigitalSignageApi {

    String ID = "id";
    String IP_ADDRESS = "ip_address";
    String NAME = "name";
    String SECRET = "secret";
    String TOTAL_MEMORY = "total_memory";
    String FREE_MEMORY = "free_memory";
    String PAYLOAD = "payload";
    String AT_TIME = "attime";

    String FILE = "file";
    String TIME = "time";
    String SCREEN_ID = "screen_id";
    String SCREEN_POSITION = "position";
    String TIME_OFF = "timeoff";
    String TIME_ON = "timeon";
    String AT_FILE_ON = "atfileon";
    String AT_FILE_OFF = "atfileoff";
    String APP_VERSION = "versioncode";

    @Headers({"User-Agent: Retrofit-DigitalSignage-App",
            "Connection: Close"})
    @POST("/sync/register")
    void doRegister(@Body MultipartTypedOutput multipart,
                    @Header(NAME) String name,
                    @Header(SECRET) String secret,
                    @Header(TOTAL_MEMORY) String total_memory,
                    @Header(FREE_MEMORY) String free_memory, Callback<String> cb);

    @Headers({"User-Agent: Retrofit-DigitalSignage-App",
            "Connection: Close"})
    @POST("/sync/getlayouts")
    void doLayouts(@Body MultipartTypedOutput multipart,
                   @Header(NAME) String name, Callback<String> cb);

    @Headers({"User-Agent: Retrofit-DigitalSignage-App",
            "Connection: Close"})
    @POST("/sync/check")
    void doCheck(@Body MultipartTypedOutput multipart,
                 @Header(SCREEN_POSITION) String position,
                 @Header(NAME) String name,
                 @Header(FILE) String file,
                 @Header(TIME) String time,
                 @Header(FREE_MEMORY) String freeMemory,
                 @Header(TOTAL_MEMORY) String totalMemory,
                 @Header(IP_ADDRESS) String ipAddress,
                 Callback<String> cb);

    @Headers({"User-Agent: Retrofit-DigitalSignage-App",
            "Connection: Close"})
    @POST("/sync/history")
    void doHistory(@Body MultipartTypedOutput multipart,
                   //@Header(ID) String id,
                   //@Header(SCREEN_ID) String screenId,
                   //@Header(SCREEN_POSITION) String position,
                   @Header(NAME) String name,
                   @Header(TIME_OFF) String timeOff,
                   @Header(AT_FILE_OFF) String atFileOff,
                   @Header(TIME_ON) String timeOn,
                   @Header(AT_FILE_ON) String atFileOn,
                   Callback<String> cb);

    @Headers({"User-Agent: Retrofit-DigitalSignage-App",
            "Connection: Close"})
    @POST("/sync/check_auto_play")
    void getAutoPlay(@Body MultipartTypedOutput multipart,
                     @Header(ID) String id,
                     Callback<String> cb);

    @Headers({"User-Agent: Retrofit-DigitalSignage-App",
            "Connection: Close"})
    @POST("/sync/check_on_off")
    void getOnOffTimer(@Body OnOffTimerRequest request, Callback<String> cb);

    @Headers({"User-Agent: Retrofit-DigitalSignage-App",
            "Connection: Close"})
    @POST("/sync/post_versioncode")
    void postVersionCode(@Body MultipartTypedOutput multipart,
                         @Header(NAME) String name,
                         @Header(APP_VERSION) String appVersion,
                         Callback<String> cb);

    @Headers({"User-Agent: Retrofit-DigitalSignage-App",
            "Connection: Close"})
    @POST("/sync/getface")
    void submitFaceDetectionInformation(@Body MultipartTypedOutput multipart,
                                        @Header(SCREEN_ID) String screenId,
                                        @Header(AT_TIME) String atTime,
                                        @Header(PAYLOAD) String peopleCount,
                                        Callback<String> cb);
}
