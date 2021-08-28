package br.com.i9algo.taxiadv.v2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by aStraube on 06/06/2016.
 */
public class DateUtils {

    public static final String BR_DATE_TIME_PATTERN = "dd/MM/yyyy - HH:mm";
    public static final String ISO8601_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String ALTERNATE_ISO8601_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String RFC822_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
    public static final String COMPRESSED_DATE_PATTERN = "yyyyMMdd'T'HHmmss'Z'";
    private static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
    private static final Map<String, ThreadLocal<SimpleDateFormat>> SDF_MAP = new HashMap();

    public DateUtils() {
    }

    public static String getCurrentDateAsString(){
        return new SimpleDateFormat("yyyy.MM.dd").format(new Date());
    }

    public static String getTimestamp(){
        return new SimpleDateFormat("HH.mm.ss").format(new Date());
    }

    private static ThreadLocal<SimpleDateFormat> getSimpleDateFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> sdf = (ThreadLocal)SDF_MAP.get(pattern);
        if (sdf == null) {
            Map var2 = SDF_MAP;
            synchronized(SDF_MAP) {
                sdf = (ThreadLocal)SDF_MAP.get(pattern);
                if (sdf == null) {
                    sdf = new ThreadLocal<SimpleDateFormat>() {
                        protected SimpleDateFormat initialValue() {
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
                            sdf.setTimeZone(DateUtils.GMT_TIMEZONE);
                            sdf.setLenient(false);
                            return sdf;
                        }
                    };
                    SDF_MAP.put(pattern, sdf);
                }
            }
        }

        return sdf;
    }

    public static Date parse(String pattern, String dateString) {
        try {
            return ((SimpleDateFormat)getSimpleDateFormat(pattern).get()).parse(dateString);
        } catch (ParseException var3) {
            throw new IllegalArgumentException(var3);
        }
    }

    public static String format(String pattern, Date date) {
        return ((SimpleDateFormat)getSimpleDateFormat(pattern).get()).format(date);
    }

    public static Date parseISO8601Date(String dateString) {
        try {
            return parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", dateString);
        } catch (IllegalArgumentException var2) {
            return parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateString);
        }
    }

    public static String formatISO8601Date(Date date) {
        return format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", date);
    }

    public static Date parseRFC822Date(String dateString) {
        return parse("EEE, dd MMM yyyy HH:mm:ss z", dateString);
    }

    public static String formatRFC822Date(Date date) {
        return format("EEE, dd MMM yyyy HH:mm:ss z", date);
    }

    public static Date parseCompressedISO8601Date(String dateString) {
        return parse("yyyyMMdd'T'HHmmss'Z'", dateString);
    }

    public static Date cloneDate(Date date) {
        return date == null ? null : new Date(date.getTime());
    }

    public static long numberOfDaysSinceEpoch(long milliSinceEpoch) {
        return TimeUnit.MILLISECONDS.toDays(milliSinceEpoch);
    }
}
