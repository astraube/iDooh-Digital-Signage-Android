package br.com.i9algo.taxiadv.v2.helpers;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class Db {
    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;
    public static final String ID = "id";

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat(DateFormatHelper.DATE_FORMAT);

    private Db() {
        throw new AssertionError("No instances.");
    }

    public static String getString(Cursor cursor, String columnName, String fallback) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            String string = cursor.getString(columnIndex);
            return string != null ? string : fallback;
        } else {
            return fallback;
        }
    }

    public static boolean getBoolean(Cursor cursor, String columnName, boolean fallback) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            return getInt(cursor, columnName, BOOLEAN_FALSE) == BOOLEAN_TRUE;
        } else {
            return fallback;
        }
    }

    public static long getLong(Cursor cursor, String columnName, long fallback) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            return cursor.getLong(columnIndex);
        } else {
            return fallback;
        }
    }

    public static int getInt(Cursor cursor, String columnName, int fallback) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            return cursor.getInt(columnIndex);
        } else {
            return fallback;
        }
    }

    public static float getFloat(Cursor cursor, String columnName, int fallback) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            return cursor.getFloat(columnIndex);
        } else {
            return fallback;
        }
    }

    public static Date getDate(Cursor cursor, String columnName, Date fallback) {
        long date = getLong(cursor, columnName, 0);
        if (date != 0) {
            return DateFormatHelper.convertTimeZone(date, TimeZone.getTimeZone("GMT"), TimeZone.getDefault());
        }

        return fallback;
    }
}