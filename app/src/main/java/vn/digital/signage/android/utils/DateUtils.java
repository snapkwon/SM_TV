package vn.digital.signage.android.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import vn.digital.signage.android.api.response.AutoPlayResponse;
import vn.digital.signage.android.app.SMRuntime;

/**
 * The type Date utils.
 */
public class DateUtils {

    /**
     * The constant millisInDay.
     */
    public static final long millisInDay = 86400000;
    /**
     * The constant FORMAT_LONG_DATE_WITH_AM_PM.
     */
    public static final String FORMAT_LONG_DATE_WITH_AM_PM = "dd MMMM yyyy hh:mm aa";
    /**
     * The constant FORMAT_LONG_DATE_WITH_NO_AM_PM.
     */
    public static final String FORMAT_LONG_DATE_WITH_NO_AM_PM = "dd MMMM yyyy HH:mm";
    /**
     * The constant FULL_DATE_FORMAT.
     */
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    /**
     * The constant DATE_FORMAT.
     */
    public static final String DATE_FORMAT = "dd-MM-yyyy";

    private static final SimpleDateFormat mFormat8chars =
            new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat mFormatIso8601Day =
            new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat mFormatLongDay =
            new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

    private static final SimpleDateFormat mFormatLongDayNotTime =
            new SimpleDateFormat("dd MMMM yyyy");

    private static final SimpleDateFormat mFormatLongDayWithAM_PM =
            new SimpleDateFormat("dd MMMM yyyy hh:mm aa");

    private static final SimpleDateFormat mFormatShortDay =
            new SimpleDateFormat("dd MMM yyyy");

    private static final SimpleDateFormat mFormatIso8601 =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private static final SimpleDateFormat mSimple =
            new SimpleDateFormat("M/dd/yyyy");

    // http://www.w3.org/Protocols/rfc822/Overview.html#z28
    // Using Locale.US to fix ROL-725 and ROL-628
    private static final SimpleDateFormat mFormatRfc822 =
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
    // some static date formats
    private static SimpleDateFormat[] mDateFormats = loadDateFormats();

    private static SimpleDateFormat[] loadDateFormats() {
        SimpleDateFormat[] temp = {
                //new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS a"),
                new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"), // standard Date.toString() results
                new SimpleDateFormat("M/d/yy hh:mm:ss"),
                new SimpleDateFormat("M/d/yyyy hh:mm:ss"),
                new SimpleDateFormat("M/d/yy hh:mm a"),
                new SimpleDateFormat("M/d/yyyy hh:mm a"),
                new SimpleDateFormat("M/d/yy HH:mm"),
                new SimpleDateFormat("M/d/yyyy HH:mm"),
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"),
                new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"), // standard Timestamp.toString() results
                new SimpleDateFormat("M-d-yy HH:mm"),
                new SimpleDateFormat("M-d-yyyy HH:mm"),
                new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS"),
                new SimpleDateFormat("M/d/yy"),
                new SimpleDateFormat("M/d/yyyy"),
                new SimpleDateFormat("M-d-yy"),
                new SimpleDateFormat("M-d-yyyy"),
                new SimpleDateFormat("MMMM d, yyyyy"),
                new SimpleDateFormat("MMM d, yyyyy"),
        };

        return temp;
    }

    private static SimpleDateFormat[] getFormats() {
        return mDateFormats;
    }


    /**
     * Parse from formats date.
     *
     * @param aValue the a value
     * @return the date
     */
    public static Date parseFromFormats(String aValue) {
        if (TextUtils.isEmpty(aValue)) return null;

        // get DateUtils's formats
        SimpleDateFormat formats[] = DateUtils.getFormats();
        if (formats == null) return null;

        // iterate over the array and parse
        Date myDate = null;
        for (int i = 0; i < formats.length; i++) {
            try {
                myDate = DateUtils.parse(aValue, formats[i]);
                //if (myDate instanceof Date)
                return myDate;
            } catch (Exception e) {
                // do nothing because we want to try the next
                // format if current one fails
            }
        }
        // haven't returned so couldn't parse
        return null;
    }

    /**
     * Parse timestamp from formats java . sql . timestamp.
     *
     * @param aValue the a value
     * @return the java . sql . timestamp
     */
    public static java.sql.Timestamp parseTimestampFromFormats(String aValue) {
        if (TextUtils.isEmpty(aValue)) return null;

        // call the regular Date formatter
        Date myDate = DateUtils.parseFromFormats(aValue);
        if (myDate != null) return new java.sql.Timestamp(myDate.getTime());
        return null;
    }

    /**
     * Now java . sql . timestamp.
     *
     * @return the java . sql . timestamp
     */
    public static java.sql.Timestamp now() {
        return new java.sql.Timestamp(new Date().getTime());
    }

    /**
     * Format string.
     *
     * @param aDate   the a date
     * @param aFormat the a format
     * @return the string
     */
    public static String format(Date aDate, SimpleDateFormat aFormat) {
        if (aDate == null || aFormat == null) {
            return "";
        }
        synchronized (aFormat) {
            return aFormat.format(aDate);
        }
    }

    /**
     * From date string.
     *
     * @return the string
     */
    public static String fromDate() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * From date string.
     *
     * @param timeInMillis the time in millis
     * @return the string
     */
    public static String fromDate(long timeInMillis) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timeInMillis);
    }

    /**
     * Time milis to string string.
     *
     * @param milis  the milis
     * @param format the format
     * @return the string
     */
    public static String timeMilisToString(long milis, String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milis);
        return sd.format(calendar.getTime());
    }

    /**
     * Parse date.
     *
     * @param aValue  the a value
     * @param aFormat the a format
     * @return the date
     * @throws ParseException the parse exception
     */
    public static Date parse(String aValue, SimpleDateFormat aFormat) throws ParseException {
        if (TextUtils.isEmpty(aValue) || aFormat == null) {
            return null;
        }

        return aFormat.parse(aValue);
    }


    /**
     * Parse string.
     *
     * @param aValue the a value
     * @return the string
     */
    public static String parse(String aValue) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        if (TextUtils.isEmpty(aValue)) {
            return null;
        }
        Date date = null;
        try {
            date = parse(aValue, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    /**
     * Friendly date format simple date format.
     *
     * @param minimalFormat the minimal format
     * @return the simple date format
     */
    public static SimpleDateFormat friendlyDateFormat(boolean minimalFormat) {
        if (minimalFormat) {
            return new SimpleDateFormat("d.M.yy");
        }

        return new SimpleDateFormat("dd.MM.yyyy");
    }

    private final static long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;

    private static long getDateToLong(Date date) {
        return Date.UTC(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
    }

    /**
     * Gets signed diff in days.
     *
     * @param beginDate the begin date
     * @param endDate   the end date
     * @return the signed diff in days
     */
    public static int getSignedDiffInDays(Date beginDate, Date endDate) {
        long beginMS = getDateToLong(beginDate);
        long endMS = getDateToLong(endDate);
        long diff = (endMS - beginMS) / (MILLISECS_PER_DAY);
        return (int) diff;
    }

    /**
     * Gets remaining day.
     *
     * @param dayStr the day str
     * @return the remaining day
     */
    public static long getRemainingDay(String dayStr) {
        long days = 0;
        try {
            Date startDate = DateUtils.now();
            Date endDate = DateUtils.parse(dayStr, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            days = Math.abs(DateUtils.getSignedDiffInDays(startDate, endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * Current formatted date string.
     *
     * @return the string
     */
    public static String currentFormattedDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    /**
     * Gets current date with second offset.
     *
     * @param second the second
     * @return the current date with second offset
     */
    public static Date getCurrentDateWithSecondOffset(int second) {
        long time = new GregorianCalendar().getTimeInMillis() + second * 1000;
        Date d = new Date(time);
        return d;
    }

    /**
     * Schedule auto play time long.
     *
     * @param runtime the runtime
     * @return the long
     */
    public static long scheduleAutoPlayTime(SMRuntime runtime) {
        AutoPlayResponse response = runtime.getAutoPlaySchedule();
        int hour = response.getAutoplay().getHour();
        int minute = response.getAutoplay().getMinute();
        int second = response.getAutoplay().getSecond();

        Long time;

        Calendar timeOff9 = Calendar.getInstance();
        if (timeOff9.get(Calendar.HOUR_OF_DAY) > hour)
            hour = hour + 24;
        timeOff9.set(Calendar.HOUR_OF_DAY, hour);
        timeOff9.set(Calendar.MINUTE, minute);
        timeOff9.set(Calendar.SECOND, second);

        time = timeOff9.getTimeInMillis();

        // set active current hour
        response.getAutoplay().setHour(hour);
        response.getAutoplay().setMinute(minute);
        response.getAutoplay().setSecond(second);
        runtime.setAutoPlaySchedule(response);

        return time;
    }
}
