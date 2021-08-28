package br.com.i9algo.taxiadv.v2.logging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.utils.DateUtils;

public class LogFileWriteHelper {

    private static boolean isLogEnable = false;

    @SuppressLint("SimpleDateFormat")
    private static String getFileName() {
        return "taxi-adv-log-" + DateUtils.getCurrentDateAsString() + ".txt";
    }

    private static void createLogFile(Context context) {
        File path = context.getFilesDir();
        File file = new File(path, getFileName());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String textToBeLogged, Context context) {
        if (!isLogEnable)
            return;

        if (BuildConfig.DEBUG) {
            File path = context.getFilesDir();
            File f = new File(path + "/" + getFileName());
            PrintWriter output = null;
            String message = "\r\n" + "[" + DateUtils.getTimestamp() + "] - " + textToBeLogged;
            Logger.v("adv", message);
            if (!f.exists()) {
                createLogFile(context);
            }
            try {
                output = new PrintWriter(new FileWriter(path + "/" + getFileName(), true));
                output.write(message);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    output.flush();
                    output.close();
                }

            }
        }


    }

}
