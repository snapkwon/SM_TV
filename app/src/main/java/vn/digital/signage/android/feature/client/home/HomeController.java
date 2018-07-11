package vn.digital.signage.android.feature.client.home;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
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
import vn.digital.signage.android.utils.DebugLog;
import vn.digital.signage.android.utils.enumeration.LogLevel;

public class HomeController extends BaseController {
    public static final String TAG = HomeController.class.getSimpleName();
    public static final String DEFAULT_VIDEO_PATH = "file:///android_asset/adv_default_video.mp4";
    public static boolean isUpdate = false;
    public static String LOCAL_VIDEO_PATH = "";
    private final Logger log = Logger.getLogger(HomeController.class);

    @Inject
    SMRuntime runtime;
    private boolean isLayoutCalling = false;

    @Inject
    public HomeController() {
        // default constructor
    }

    public String getVideoAdvLocal() {
        return LOCAL_VIDEO_PATH;
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

//                        json = "{\"layouts\":[{\"id\":\"1080\",\"group_id\":\"102\",\"name\":\"Frame02\",\"type\":\"5\",\"source\":\"[{\\\"id\\\":\\\"11\\\",\\\"layout_id\\\":\\\"1080\\\",\\\"name\\\":\\\"Frame01_1\\\",\\\"z-index\\\":\\\"1\\\",\\\"width\\\":\\\"320\\\",\\\"height\\\":\\\"720\\\",\\\"top\\\":\\\"0\\\",\\\"left\\\":\\\"960\\\",\\\"time\\\":\\\"180s\\\",\\\"source\\\":\\\"tygia.html\\\",\\\"hash\\\":\\\"a3ff833eeab9cc76fbce7c45ab0635f7\\\",\\\"type\\\":\\\"web\\\"},{\\\"id\\\":\\\"12\\\",\\\"layout_id\\\":\\\"1080\\\",\\\"name\\\":\\\"Frame01_2\\\",\\\"z-index\\\":\\\"2\\\",\\\"width\\\":\\\"960\\\",\\\"height\\\":\\\"180\\\",\\\"top\\\":\\\"540\\\",\\\"left\\\":\\\"0\\\",\\\"time\\\":\\\"180s\\\",\\\"source\\\":\\\"http:\\\\\\/\\\\\\/cms3.smg.com.vn:8200\\\\\\/exrate\\\\\\/laisuat.php\\\",\\\"hash\\\":\\\"\\\",\\\"type\\\":\\\"url\\\"},{\\\"id\\\":\\\"13\\\",\\\"layout_id\\\":\\\"1080\\\",\\\"name\\\":\\\"Frame01_3\\\",\\\"z-index\\\":\\\"3\\\",\\\"width\\\":\\\"960\\\",\\\"height\\\":\\\"540\\\",\\\"top\\\":\\\"0\\\",\\\"left\\\":\\\"0\\\",\\\"time\\\":\\\"180s\\\",\\\"source\\\":\\\"0f8531c69548e28249230a272b13f633.mp4,37c4dadeca529564464076c157e0c13d.mp4\\\",\\\"hash\\\":\\\"aa174901f65ead40171ff5d9e055df8b,27c8b74877153f5a9e756b2e719385a9\\\",\\\"type\\\":\\\"videolist\\\"}]\",\"assets\":\"12276974334f81e64aaff6681b34ea35.mp4\",\"hash\":\"5fef749ddfe72de0572b4118d1b3deda\",\"data\":\"{\\\"pause_jukebox\\\":\\\"0\\\",\\\"file_name\\\":\\\"Topcake Final.mp4\\\",\\\"loop_video\\\":\\\"1\\\",\\\"volume\\\":\\\"100\\\"}\",\"duration\":\"300\",\"play_from\":null,\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"1\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2018-06-04 17:31:49\"},{\"id\":\"9663\",\"group_id\":\"102\",\"name\":\"Video main circle\",\"type\":\"2\",\"source\":\"\n" +
//                                "<video id=\\\"video_layout\\\" class=\\\"fullscreen\\\" src=\\\"[asset_dir]bbeefcb25d2334a3de9df9a173729222.mp4\\\" autoplay=\\\"autoplay\\\" loop=\\\"loop\\\" onplay=\\\"this.volume=1.000000\\\" volume=\\\"1.000000\\\" \\/>\",\"assets\":\"bbeefcb25d2334a3de9df9a173729222.mp4\",\"hash\":\"f34096e61a08344c31d2d4168bcefa05\",\"data\":\"{\\\"pause_jukebox\\\":\\\"0\\\",\\\"file_name\\\":\\\"Sacombank_Don Xe Ruoc He.mp4\\\",\\\"loop_video\\\":\\\"1\\\",\\\"volume\\\":\\\"100\\\"}\",\"duration\":\"39\",\"play_from\":null,\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"4\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2018-06-14 18:52:31\"},{\"id\":\"9666\",\"group_id\":\"102\",\"name\":\"Hinh 1_TT\",\"type\":\"1\",\"source\":\"\n" +
//                                "<div class=\\\"fullscreen image-cover\\\" style=\\\"background-image:url('[asset_dir]3753d7b626b028b5378498f0507d9973.jpg')\\\"><\\/div>\",\"assets\":\"3753d7b626b028b5378498f0507d9973.jpg\",\"hash\":\"8f99933b23015042aeaa8d6438e5603a\",\"data\":null,\"duration\":\"7\",\"play_from\":null,\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"4\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2018-06-27 21:59:17\"},{\"id\":\"9667\",\"group_id\":\"102\",\"name\":\"Hinh 2_TT\",\"type\":\"1\",\"source\":\"\n" +
//                                "    <div class=\\\"fullscreen image-cover\\\" style=\\\"background-image:url('[asset_dir]00511dde233643cdba59acb9db0f1d99.jpg')\\\"><\\/div>\",\"assets\":\"00511dde233643cdba59acb9db0f1d99.jpg\",\"hash\":\"e715b2dabb251704bc157c614c70b351\",\"data\":null,\"duration\":\"7\",\"play_from\":null,\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"4\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2018-06-27 21:59:48\"},{\"id\":\"9668\",\"group_id\":\"102\",\"name\":\"Hinh 3_TT\",\"type\":\"1\",\"source\":\"\n" +
//                                "        <div class=\\\"fullscreen image-cover\\\" style=\\\"background-image:url('[asset_dir]19779c2b2bf342e0a5856c3d91ea218f.jpg')\\\"><\\/div>\",\"assets\":\"19779c2b2bf342e0a5856c3d91ea218f.jpg\",\"hash\":\"22ab7970377e62c4b9e7a261c74afd80\",\"data\":null,\"duration\":\"7\",\"play_from\":null,\"play_to\":null,\"dontplay_from\":null,\"dontplay_to\":null,\"play_at\":null,\"position\":\"5\",\"transition_id\":\"64\",\"transition_in_class\":\"none\",\"transition_out_class\":\"none\",\"enabled\":\"1\",\"last_update\":\"2018-06-27 22:00:07\"}]}";
//                        if (isUpdate)
//                        if (Config.hasLogLevel(LogLevel.API))
                            DebugLog.d(String.format("HomeController - response getCurrentPlaylis1122 - success: %s ", json));

                        final LayoutResponse result = new Gson().fromJson(json, LayoutResponse.class);
                        for (LayoutInfo info : result.getLayouts()) {
                            if (!TextUtils.isEmpty(info.getData())) {
                                final DataInfo dataInfo = new Gson().fromJson(info.getData(), DataInfo.class);
                                info.setObjData(dataInfo);
                            }
                            if (info.getType() == LayoutInfo.LayoutType.FRAME) {
                                ArrayList<SourceInfo> sourceInfos = new Gson().fromJson(info.getSource(), new TypeToken<ArrayList<SourceInfo>>() {
                                }.getType());
                                info.setObjSource(sourceInfos);
                                for (SourceInfo sourceInfo : sourceInfos) {
                                    if (sourceInfo.getType() == SourceInfo.SourceType.VIDEO_LIST) {
                                        String[] sources = sourceInfo.getSource().split(",");
                                        sourceInfo.setArrSources(Arrays.asList(sources));
                                        sources = sourceInfo.getHash().split(",");
                                        sourceInfo.setArrHashes(Arrays.asList(sources));
                                    }
                                }
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

