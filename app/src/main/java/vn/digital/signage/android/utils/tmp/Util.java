package vn.digital.signage.android.utils.tmp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import vn.digital.signage.android.app.App;

public class Util {
    private static final int MEGA_BYTES = 10241024;

    public static String nameLeftValueRightJustify(String param1, String param2, int cpl) {
        if (param1 == null)
            param1 = "";
        if (param2 == null)
            param2 = "";
        int len = param1.length();
        return param1.trim() + rightJustify(param2, (cpl - len));
    }

    public static String rightJustify(String item, int digits) {
        StringBuffer buf = null;
        if (digits < 0) {
            buf = new StringBuffer();
        } else {
            buf = new StringBuffer(digits);
        }
        for (int i = 0; i < digits - item.length(); i++) {
            buf.append(" ");
        }
        buf.append(item);
        return buf.toString();
    }

    public static class Security {


        public static long getTimeSpanFromLastTime(long lastTime) {
            long currentTime = System.currentTimeMillis();
            long differenceInSecond = currentTime - lastTime;
            return TimeUnit.MILLISECONDS.toSeconds(differenceInSecond);
        }
    }

    public static class Device {

        public static String getDeviceId() {
            TelephonyManager tm = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            if (TextUtils.isEmpty(deviceId) || deviceId.equals("000000000000000")) {
                deviceId = Settings.Secure.getString(App.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return deviceId;
        }

        public static String getDeviceType() {
            return Build.MODEL;
        }

        public static Locale getDeviceLanguage() {
            return Locale.getDefault();
        }

        public static void playSound(Context context, int notificationType) {
            Uri notification = RingtoneManager.getDefaultUri(notificationType);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();
        }


    }

    public static class FileUtil {

        public static String readAssetFile(Context context, String file_name) {
            AssetManager assetManager = context.getAssets();
            ByteArrayOutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                inputStream = assetManager.open(file_name);
                outputStream = new ByteArrayOutputStream();
                byte buf[] = new byte[1024];
                int len;
                try {
                    while ((len = inputStream.read(buf)) != -1) {
                        outputStream.write(buf, 0, len);
                    }
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                }
            } catch (IOException e) {
            }
            return outputStream.toString();
        }
    }

    public static class BitmapUtil {

        public static Bitmap savedBitmapFromByte(byte[] data, int w, int h) {
            final YuvImage image = new YuvImage(data, ImageFormat.NV21, w, h, null);
            ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
            if (!image.compressToJpeg(new Rect(0, 0, w, h), 100, os)) {
                return null;
            }
            byte[] tmp = os.toByteArray();
            return BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        }
    }

    public static class AnimationUtil {

        public static void expand(final View v) {
            v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();

            v.getLayoutParams().height = 0;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1
                            ? ViewGroup.LayoutParams.WRAP_CONTENT
                            : (int) (targetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }

        public static void collapse(final View v) {
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1) {
                        v.setVisibility(View.GONE);
                    } else {
                        v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }
    }
}
