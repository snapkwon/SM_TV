package vn.digital.signage.android.feature.client.home;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedString;
import vn.digital.signage.android.BuildConfig;
import vn.digital.signage.android.Constants;
import vn.digital.signage.android.api.model.DataInfo;
import vn.digital.signage.android.api.model.LayoutInfo;
import vn.digital.signage.android.api.model.SourceInfo;
import vn.digital.signage.android.api.request.AutoPlayRequest;
import vn.digital.signage.android.api.request.OnOffTimerRequest;
import vn.digital.signage.android.api.response.AutoPlayResponse;
import vn.digital.signage.android.api.response.AutoplayEntity;
import vn.digital.signage.android.api.response.LayoutResponse;
import vn.digital.signage.android.api.response.OnOffTimerResponse;
import vn.digital.signage.android.api.response.PostVersionResponse;
import vn.digital.signage.android.api.service.DigitalSignageApi;
import vn.digital.signage.android.app.Config;
import vn.digital.signage.android.app.SMRuntime;
import vn.digital.signage.android.feature.client.base.BaseController;
import vn.digital.signage.android.model.FaceDetectionInfos;
import vn.digital.signage.android.utils.DateUtils;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class HomeController extends BaseController {
    public static final String TAG = HomeController.class.getSimpleName();
    public static final String DEFAULT_VIDEO_PATH = "file:///android_asset/adv_default_video.mp4";
    private final Logger log = Logger.getLogger(HomeController.class);

    @Inject
    SMRuntime runtime;
    private boolean isLayoutCalling = false;

    @Inject
    public HomeController() {
        // default constructor
    }

    public String getVideoAdvLocal() {
        return DEFAULT_VIDEO_PATH;
    }

    public void getCurrentPlaylist(String name) {
        if (!isLayoutCalling) {
            isLayoutCalling = true;

            initRestAdapter(runtime.getApiUrl());

            final MultipartTypedOutput multipart = new MultipartTypedOutput();

            multipart.addPart(DigitalSignageApi.NAME, new TypedString(name));

            if (Config.hasLogLevel(LogLevel.API))
                log.debug(String.format("HomeController - call getCurrentPlaylist - name: %s ", name));
            api.doLayouts(multipart, name, new Callback<String>() {

                @Override
                public void success(String json, Response response) {
                    try {
                        isLayoutCalling = false;

//                        json = "{\"layouts\":[{\"id\":\"1079\",\"group_id\":\"90\",\"name\":\"Baskin_TraceabilityAnka\",\"type\":\"2\",\"source\":\"<video id=\\\"video_layout\\\" class=\\\"fullscreen\\\" src=\\\"[asset_dir]39a8d44eef01882ac68c50df5258df9c.mp4\\\" autoplay=\\\"autoplay\\\" loop=\\\"loop\\\" onplay=\\\"this.volume=1.000000\\\" volume=\\\"1.000000\\\" \\/>\",\"assets\":\"39a8d44eef01882ac68c50df5258df9c.mp4\",\"hash\":\"5c39a36b002181d3536055e8f4ca7137\",\"data\":\"{\\\"pause_jukebox\\\":\\\"0\\\",\\\"file_name\\\":\\\"Baskin_TraceabilityAnka.mp4\\\",\\\"loop_video\\\":\\\"1\\\",\\\"volume\\\":\\\"100\\\"}\",\"duration\":\"30\",\"play_from\":\"2017-09-20 15:10:00\",\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"1\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2018-05-23 15:00:38\"},{\"id\":\"4563\",\"group_id\":\"90\",\"name\":\"Ca si Dang Test\",\"type\":\"5\",\"source\":\"[\\r\\n    {\\r\\n      \\\"name\\\": 1111,    \\r\\n      \\\"z-index\\\": 1,\\r\\n      \\\"width\\\": 580,\\r\\n      \\\"height\\\": 380,\\r\\n      \\\"top\\\": 0,\\r\\n      \\\"left\\\": 0,\\r\\n      \\\"time\\\": \\\"30s\\\",\\r\\n      \\\"soure\\\": \\\"923bf5aab03c623167993b0b30022a9e.mp4\\\",\\r\\n      \\\"hash\\\": \\\"eff9aa8a2bf6209bdd000226eab42f34\\\",\\r\\n      \\\"type\\\": \\\"video\\\"\\r\\n    },\\r\\n    {\\r\\n      \\\"name\\\": 222,     \\r\\n      \\\"z-index\\\": 2,\\r\\n      \\\"width\\\": 700,\\r\\n      \\\"height\\\": 360,\\r\\n      \\\"top\\\": 0,\\r\\n      \\\"left\\\": 641,\\r\\n      \\\"time\\\": \\\"20s\\\",\\r\\n      \\\"soure\\\": \\\"bb57528bcb213093f037c47dba338cc7.mp4\\\",\\r\\n      \\\"hash\\\": \\\"2553768e2dcc1bd4e66636e430495891\\\",\\r\\n      \\\"type\\\": \\\"video\\\"\\r\\n    },\\r\\n    {\\r\\n      \\\"name\\\": 3333,     \\r\\n      \\\"z-index\\\": 3,\\r\\n      \\\"width\\\": 780,\\r\\n      \\\"height\\\": 340,\\r\\n      \\\"top\\\": 361,\\r\\n      \\\"left\\\": 0,\\r\\n      \\\"time\\\": \\\"30s\\\",\\r\\n      \\\"soure\\\": \\\"fb6621401d514f02e239d9f2c25291ad.jpg\\\",\\r\\n      \\\"hash\\\":\\\"none\\\",\\r\\n      \\\"type\\\": \\\"video\\\"\\r\\n    },\\r\\n    {\\r\\n      \\\"name\\\": 4444,     \\r\\n      \\\"z-index\\\": 4,\\r\\n      \\\"width\\\": 500,\\r\\n      \\\"height\\\": 360,\\r\\n      \\\"top\\\": 361,\\r\\n      \\\"left\\\": 641,\\r\\n      \\\"time\\\": \\\"30s\\\",\\r\\n      \\\"soure\\\": \\\"https:\\/\\/www.sacombank.com.vn\\/company\\/Pages\\/ty-gia.aspx\\\",\\r\\n      \\\"hash\\\":\\\"none\\\",\\r\\n      \\\"type\\\": \\\"url\\\"\\r\\n    }\\r\\n  ]\",\"assets\":\"923bf5aab03c623167993b0b30022a9e.mp4\",\"hash\":\"eff9aa8a2bf6209bdd000226eab42f34\",\"data\":\"{\\\"pause_jukebox\\\":\\\"0\\\",\\\"file_name\\\":\\\"Aerosmith - I Don't Want to Miss a Thing.mp4\\\",\\\"loop_video\\\":\\\"1\\\",\\\"volume\\\":\\\"100\\\"}\",\"duration\":\"30\",\"play_from\":null,\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"1\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2018-05-23 15:00:56\"},{\"id\":\"3759\",\"group_id\":\"90\",\"name\":\"Ford_DP_LED\",\"type\":\"2\",\"source\":\"<video id=\\\"video_layout\\\" class=\\\"fullscreen\\\" src=\\\"[asset_dir]bd42d0b7424c9e944ffb89c84da82c86.mp4\\\" autoplay=\\\"autoplay\\\" loop=\\\"loop\\\" onplay=\\\"this.volume=1.000000\\\" volume=\\\"1.000000\\\" \\/>\",\"assets\":\"bd42d0b7424c9e944ffb89c84da82c86.mp4\",\"hash\":\"87b2743fa7db426bd9f0e65374f9002d\",\"data\":\"{\\\"pause_jukebox\\\":\\\"0\\\",\\\"file_name\\\":\\\"Poster Ford.mp4\\\",\\\"loop_video\\\":\\\"1\\\",\\\"volume\\\":\\\"100\\\"}\",\"duration\":\"30\",\"play_from\":null,\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"2\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2017-09-21 14:35:21\"},{\"id\":\"10680\",\"group_id\":\"90\",\"name\":\"Image\",\"type\":\"1\",\"source\":\"<div class=\\\"fullscreen image-cover\\\" style=\\\"background-image:url('[asset_dir]fb6621401d514f02e239d9f2c25291ad.jpg')\\\"><\\/div>\",\"assets\":\"fb6621401d514f02e239d9f2c25291ad.jpg\",\"hash\":null,\"data\":null,\"duration\":\"30\",\"play_from\":null,\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"4\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2018-05-24 17:17:41\"}]}";

                        if (Config.hasLogLevel(LogLevel.API))
                            log.debug(String.format("HomeController - response getCurrentPlaylis1122 - success: %s ", json));

                        final LayoutResponse result = new Gson().fromJson(json, LayoutResponse.class);
                        for (LayoutInfo info : result.getLayouts()) {
                            if (!TextUtils.isEmpty(info.getData())) {
                                final DataInfo dataInfo = new Gson().fromJson(info.getData(), DataInfo.class);
                                info.setObjData(dataInfo);
                            }
                            if (info.getType() == LayoutInfo.LayoutType.FRAME) {
                              ArrayList<SourceInfo> sourceInfos = new Gson().fromJson(info.getSource(), new TypeToken<ArrayList<SourceInfo>>(){
                              }.getType());
                                info.setObjSource(sourceInfos);
                            }
                        }

                        result.setSuccess(true);
                        result.setError("Success");
                        eventBus.post(result);
                    } catch (Exception e) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.error(String.format("HomeController - response getCurrentPlaylist - error: - %s", e.getMessage()));
                    }
                    System.gc();
                    Runtime.getRuntime().gc();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    isLayoutCalling = false;
                    if (Config.hasLogLevel(LogLevel.API))
                        log.error(String.format("HomeController - response getCurrentPlaylist - error: %s ", retrofitError.getMessage()));

                    final LayoutResponse result = new LayoutResponse();
                    result.setSuccess(false);
                    result.setError("Error");
                    result.setMsg(retrofitError.getLocalizedMessage());
                    eventBus.post(result);
                }
            });
        }
    }

    public void doCheck(final String position,
                        final String name,
                        final String file,
                        final String time,
                        final String freeMemory,
                        final String totalMemory,
                        final String ipAddress) {
        initRestAdapter(runtime.getApiUrl());

        final MultipartTypedOutput multipart = new MultipartTypedOutput();
        multipart.addPart(DigitalSignageApi.SCREEN_POSITION, new TypedString(position));
        multipart.addPart(DigitalSignageApi.NAME, new TypedString(name));
        multipart.addPart(DigitalSignageApi.FILE, new TypedString(file));
        multipart.addPart(DigitalSignageApi.TIME, new TypedString(time));
        multipart.addPart(DigitalSignageApi.FREE_MEMORY, new TypedString(freeMemory));
        multipart.addPart(DigitalSignageApi.TOTAL_MEMORY, new TypedString(totalMemory));
        multipart.addPart(DigitalSignageApi.IP_ADDRESS, new TypedString(ipAddress));

        if (Config.hasLogLevel(LogLevel.API))
            log.debug(String.format("HomeController - call doCheck - name: %s ", name));
        api.doCheck(multipart, position, name, file, time, freeMemory, totalMemory, ipAddress, new Callback<String>() {

            @Override
            public void success(String json, Response response) {
                if (Config.hasLogLevel(LogLevel.API))
                    log.debug(String.format("HomeController - response doCheck - success: %s ", json));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (Config.hasLogLevel(LogLevel.API))
                    log.error(String.format("HomeController - response doCheck - error: %s ", retrofitError.getMessage()));
            }
        });
    }

    public void doHistory(final String id,
                          final String screenId,
                          final String position,
                          final String name,
                          final String timeOff,
                          final String atFileOff,
                          final String timeOn,
                          final String atFileOn) {

        initRestAdapter(runtime.getApiUrl());

        final MultipartTypedOutput multipart = new MultipartTypedOutput();

        multipart.addPart(DigitalSignageApi.ID, new TypedString(id));
        multipart.addPart(DigitalSignageApi.SCREEN_ID, new TypedString(screenId));
        multipart.addPart(DigitalSignageApi.SCREEN_POSITION, new TypedString(position));
        multipart.addPart(DigitalSignageApi.NAME, new TypedString(name));
        multipart.addPart(DigitalSignageApi.TIME_OFF, new TypedString(timeOff));
        multipart.addPart(DigitalSignageApi.AT_FILE_OFF, new TypedString(atFileOff));
        multipart.addPart(DigitalSignageApi.TIME_ON, new TypedString(timeOn));
        multipart.addPart(DigitalSignageApi.AT_FILE_ON, new TypedString(atFileOn));

        if (Config.hasLogLevel(LogLevel.API))
            log.debug(String.format("HomeController - call doHistory - name: %s ", name));
        api.doHistory(multipart,
                name,
                timeOff,
                atFileOff,
                timeOn,
                atFileOn,
                new Callback<String>() {
                    @Override
                    public void success(String json, Response response) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.debug(String.format("HomeController - response doHistory - success: %s ", json));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.error(String.format("HomeController - response doHistory - error: %s ", retrofitError.getMessage()));
                    }
                });
    }


    public void submitFaceDetectionInformation(final String screenId,
                                               final String atTime) {
        initRestAdapter(runtime.getApiUrl());

        FaceDetectionInfos faceDetectionInfos = runtime.getFaceDetectionInfos();

        final MultipartTypedOutput multipart = new MultipartTypedOutput();

        String payload = (new Gson()).toJson(faceDetectionInfos);

        multipart.addPart(DigitalSignageApi.SCREEN_ID, new TypedString(screenId));
        multipart.addPart(DigitalSignageApi.AT_TIME, new TypedString(atTime));
        multipart.addPart(DigitalSignageApi.PAYLOAD, new TypedString(payload));

        if (Config.hasLogLevel(LogLevel.API))
            log.debug(String.format("HomeController - submitFaceDetectionInformation - PAYLOAD: %s ", payload));

        api.submitFaceDetectionInformation(multipart,
                screenId,
                atTime,
                payload,
                new Callback<String>() {
                    @Override
                    public void success(String json, Response response) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.debug(String.format("HomeController - response submitFaceDetectionInformation - success: %s ", json));

                        runtime.setFaceDetectionInfos(null);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.error(String.format("HomeController - response submitFaceDetectionInformation - error: %s ", retrofitError.getMessage()));
                    }
                });
    }

    public void getAutoPlay(Long id) {
        initRestAdapter(runtime.getApiUrl());

        final AutoPlayRequest autoPlayRequest = new AutoPlayRequest();
        autoPlayRequest.setId(id);

        final MultipartTypedOutput multipart = new MultipartTypedOutput();

        multipart.addPart(DigitalSignageApi.ID, new TypedString(String.valueOf(id)));

        if (Config.hasLogLevel(LogLevel.API))
            log.debug(String.format("HomeController - call getAutoPlay - id: %s ", String.valueOf(id)));
        api.getAutoPlay(multipart,
                String.valueOf(id),
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.debug(String.format("HomeController - response getAutoPlay - success: %s ", s));
                        AutoPlayResponse autoPlayResponse = null;
                        try {
                            autoPlayResponse = new Gson().fromJson(s, AutoPlayResponse.class);
                        } catch (Exception e) {
                            if (Config.hasLogLevel(LogLevel.API))
                                log.error(String.format("HomeController - response getAutoPlay - error: %s ", e.getMessage()));
                        }

                        if (autoPlayResponse == null) {
                            autoPlayResponse = new AutoPlayResponse();
                            autoPlayResponse.setAutoplay(new AutoplayEntity());
                        }

                        AutoplayEntity autoplayEntity = autoPlayResponse.getAutoplay();

                        if (Constants.DEBUG_AUTO_PLAY_AT_FIXED_TIME) {
                            //TODO hard code
                            Date d = DateUtils.getCurrentDateWithSecondOffset(10);
                            autoplayEntity.setHour(d.getHours());
                            autoplayEntity.setMinute(d.getMinutes());
                            autoplayEntity.setSecond(d.getSeconds());
                            autoPlayResponse.setPlayAtIndex(2);
                        }
                        if (Config.hasLogLevel(LogLevel.API))
                            log.debug(TAG + String.format("auto_play_media - getAutoPlay - before compare hour - %s", (new Gson()).toJson(autoplayEntity)));
                        if (vn.digital.signage.android.utils.autoplay.AutoPlayHelper.isCurrentHourLargerThanCompareValues(autoplayEntity.getHour(),
                                autoplayEntity.getMinute())) {
                            autoplayEntity.setHour(autoplayEntity.getHour() + Constants.ONE_DAY_IN_HOUR);
                        }
                        if (Config.hasLogLevel(LogLevel.API))
                            log.debug(TAG + String.format("auto_play_media - getAutoPlay - after compare hour - %s", (new Gson()).toJson(autoplayEntity)));

                        autoPlayResponse.setSuccess(true);
                        autoPlayResponse.setError("Success");
                        eventBus.post(autoPlayResponse);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.error(String.format("HomeController - response getAutoPlay - error: %s ", retrofitError.getMessage()));
                    }
                });
    }

    public void getOnOffTimer(Long id) {
        initRestAdapter(runtime.getApiUrl());

        OnOffTimerRequest request = new OnOffTimerRequest();
        request.setId(id);

        if (Config.hasLogLevel(LogLevel.API))
            log.debug(String.format("HomeController - call getOnOffTimer - id: %s ", String.valueOf(id)));
        api.getOnOffTimer(request, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (Config.hasLogLevel(LogLevel.API))
                    log.debug(String.format("HomeController - response getOnOffTimer - success: %s ", s));
                OnOffTimerResponse r = new Gson().fromJson(s, OnOffTimerResponse.class);

                r.setSuccess(true);
                r.setError("Success");
                eventBus.post(r);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (Config.hasLogLevel(LogLevel.API))
                    log.error(String.format("HomeController - response getOnOffTimer - error: %s ", retrofitError.getMessage()));
            }
        });
    }

    public void postVersionCode(String id) {
        initRestAdapter(runtime.getApiUrl());

        final MultipartTypedOutput multipart = new MultipartTypedOutput();
        multipart.addPart(DigitalSignageApi.ID, new TypedString(id));
        multipart.addPart(DigitalSignageApi.APP_VERSION, new TypedString(BuildConfig.VERSION_NAME));

        if (Config.hasLogLevel(LogLevel.API))
            log.debug(String.format("HomeController - call postVersionCode - id: %s ", id));

        api.postVersionCode(multipart,
                id,
                BuildConfig.VERSION_NAME,
                new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.debug(String.format("HomeController - response postVersionCode - success: %s ", s));
                        PostVersionResponse r = new Gson().fromJson(s, PostVersionResponse.class);

                        r.setSuccess(true);
                        r.setError("Success");
                        eventBus.post(r);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        if (Config.hasLogLevel(LogLevel.API))
                            log.error(String.format("HomeController - response postVersionCode - error: %s ", retrofitError.getMessage()));
                    }
                });
    }
}

