package br.com.i9algo.taxiadv.v2.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static java.text.DateFormat.SHORT;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class DateFormatHelper {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_AND_TIME_FORMAT = "yyyy-MM-dd hh:mm";
    public static final String TIME_FORMAT = "hhmm";
    private static final Locale locale = new Locale(System.getProperty("user.language"));

    public static String getTodaysDateAsString(){
        SimpleDateFormat format = new SimpleDateFormat(DateFormatHelper.DATE_FORMAT);
        return format.format(new Date());
    }

    public static Date convertTimeZone(long timestamp, TimeZone fromTimeZone, TimeZone toTimeZone) {
        Date date = new Date(timestamp);
        long fromTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, fromTimeZone);
        long toTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, toTimeZone);
        return new Date(timestamp + (toTimeZoneOffset - fromTimeZoneOffset));
    }

    private static long getTimeZoneUTCAndDSTOffset(Date date, TimeZone timeZone) {
        long timeZoneDSTOffset = 0;
        if (timeZone.inDaylightTime(date)) {
            timeZoneDSTOffset = timeZone.getDSTSavings();
        }

        return timeZone.getRawOffset() + timeZoneDSTOffset;
    }

    public static String getDateAsString(Date date) {
        String formattedDate = null;
        java.text.DateFormat dateFormat;
        Calendar adCalendar = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();
        adCalendar.setTime(date);
        nowCalendar.setTime(new Date());
        if (isSameDay(adCalendar, nowCalendar)) {
            dateFormat = getTimeInstance(SHORT, locale);
            formattedDate = "Today, " + dateFormat.format(date);
        } else {
            dateFormat = getDateInstance(SHORT, locale);
            formattedDate = dateFormat.format(date);
            dateFormat = getTimeInstance(SHORT, locale);
            formattedDate = formattedDate + ", " + dateFormat.format(date);
        }
        return formattedDate;
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static Date getPlaylistDate(String dateStart, int timeStart) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_AND_TIME_FORMAT);
        String formattedTime = parsePlaylistTime(timeStart);
        Date playlistDate = new Date();
        try {
            playlistDate = simpleDateFormat.parse(dateStart + " " + formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return playlistDate;
    }

    private static String parsePlaylistTime(int timeStart) {
        String time = Integer.toString(timeStart);
        int hour = 0;
        int minutes = 0;
        if (time.length()== 4){
            hour = time.charAt(0) + time.charAt(1);
            minutes = time.charAt(2) + time.charAt(3);
        }else{
            hour = 0 + time.charAt(0);
            minutes = time.charAt(1) + time.charAt(2);
        }

        time = Integer.toString(hour) + ":" + Integer.toString(minutes);

        return time;
    }

    public static int getCurrentHourAsInt() {
        SimpleDateFormat format = new SimpleDateFormat(DateFormatHelper.TIME_FORMAT);
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);
        String horaminutos;
        if (minutos < 10){
            horaminutos = Integer.toString(hora) + 0 + Integer.toString(minutos);
        }else{
            horaminutos = Integer.toString(hora) + Integer.toString(minutos);
        }

        return Integer.parseInt(horaminutos);
    }

    public static int getMilitaryTime(String timeStart) {
        if (timeStart.isEmpty()){
            timeStart = "0";
        }
        return Integer.parseInt(timeStart.replace(":", ""));
    }

    public static int addTenMinutesMilitaryTime(String s) {
        int minutes;
        int hour;
        String minutesString;
        String hourString;
        if (s.trim().length() < 4){
            minutes = Integer.parseInt(s.trim().substring(1));
            hour = Integer.parseInt(s.trim().substring(0, 0));
            minutes = minutes + 10;
            if (minutes >= 60){
                hour = hour + 1;
                minutes = minutes - 60;
                if (minutes > 10){
                    minutesString = Integer.toString(0) + Integer.toString(minutes);
                }else{
                    minutesString = Integer.toString(minutes);
                }
            }
        }else{
            minutes = Integer.parseInt(s.trim().substring(2));
            hour = Integer.parseInt(s.trim().substring(0, 2));
            minutes = minutes + 10;
            if (minutes >= 60){
                hour = hour + 1;
                minutes = minutes - 60;
                if (minutes > 10){
                    minutesString = Integer.toString(0) + Integer.toString(minutes);
                }else{
                    minutesString = Integer.toString(minutes);
                }
            }
        }
         String ret = Integer.toString(hour) + Integer.toString(minutes);
        return Integer.parseInt(ret);
    }
}
