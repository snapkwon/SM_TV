package vn.digital.signage.android.utils;

import android.text.TextUtils;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Validation utils.
 */
public class ValidationUtils {

    private static String URL_PATTERN = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
    private static String EMAIL_ADDRESS_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
    private static String PIN_PATTERN = "^(\\d)\\1+$";
    private static String USER_FULL_NAME = "^[0-9a-zA-Z' ]{1,40}";
    private static int PIN_MAX_LENGTH = 6;
    private static String VIETNAMESE_PATTERN = "[a-z0-9A-Z'[:space:]ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂ ưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]+";

    /**
     * Is null or empty boolean.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isNullOrEmpty(String s) {
        if (s != null && !s.equals("") && !s.equalsIgnoreCase("null")) {

            return false;
        }
        return true;
    }

    /**
     * Is valid email boolean.
     *
     * @param inputEmail the input email
     * @return the boolean
     */
    public static boolean isValidEmail(String inputEmail) {
        Pattern patternObj = Pattern.compile(EMAIL_ADDRESS_PATTERN);
        Matcher matcherObj = patternObj.matcher(inputEmail);
        if (matcherObj.matches()) {
            // Valid email
            return true;
        } else {
            // not a valid email
            return false;
        }
    }

    /**
     * Is valid url boolean.
     *
     * @param url the url
     * @return the boolean
     */
    public static boolean isValidUrl(String url) {
        if (!isNullOrEmpty(url)) {
            boolean isOk = url.startsWith("http://") || url.startsWith("https://");
            if (!isOk) {
                url = "http://" + url;
            }
        }
        Pattern urlPattern = Pattern.compile(URL_PATTERN,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = urlPattern.matcher(url);
        if (matcher.find())
            return true;
        else
            return false;
    }

    /**
     * Is exists url boolean.
     *
     * @param url the url
     * @return the boolean
     */
    public static boolean isExistsUrl(String url) {
        try {
            HttpURLConnection con =
                    (HttpURLConnection) new URL(url).openConnection();
            con.setReadTimeout(3000);
            con.setConnectTimeout(3500);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Is valid date boolean.
     *
     * @param value       the value
     * @param datePattern the date pattern
     * @param strict      the strict
     * @return the boolean
     */
    public static boolean isValidDate(String value, String datePattern,
                                      boolean strict) {
        if (value == null || datePattern == null || datePattern.length() <= 0) {
            return false;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        formatter.setLenient(false);
        try {
            formatter.parse(value);
        } catch (ParseException e) {
            return false;
        }
        if (strict && (datePattern.length() != value.length())) {
            return false;
        }
        return true;
    }

    /**
     * Is valid date boolean.
     *
     * @param value  the value
     * @param locale the locale
     * @return the boolean
     */
    public static boolean isValidDate(String value, Locale locale) {
        if (value == null) {
            return false;
        }
        DateFormat formatter = null;
        if (locale != null) {
            formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        } else {
            formatter = DateFormat.getDateInstance(DateFormat.SHORT,
                    Locale.getDefault());
        }
        formatter.setLenient(false);
        try {
            formatter.parse(value);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Is valid card expiry date boolean.
     *
     * @param cardDate the card date
     * @return the boolean
     */
    public static boolean isValidCardExpiryDate(String cardDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        simpleDateFormat.setLenient(false);
        Date expiry = null;
        try {
            expiry = simpleDateFormat.parse(cardDate);
        } catch (ParseException e) {
            expiry = new Date();
            Log.i("isValidCardExpiryDate", e.getMessage());
        }
        return !expiry.before(new Date());

    }

    /**
     * Is valid phone number boolean.
     *
     * @param target the target
     * @return the boolean
     */
    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    /**
     * Is double numeric boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isDoubleNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Is integer numeric boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isIntegerNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Is account valid boolean.
     *
     * @param text the text
     * @return the boolean
     */
    public static boolean isAccountValid(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }

        return true;
    }

    /**
     * Is valid length pin boolean.
     *
     * @param pin the pin
     * @return the boolean
     */
    public static boolean isValidLengthPIN(String pin) {
        if (TextUtils.isEmpty(pin) || pin.length() < PIN_MAX_LENGTH) {
            return false;
        }
        return true;
    }

}
