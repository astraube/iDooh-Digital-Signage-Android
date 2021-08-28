package br.com.i9algo.taxiadv.v2.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import br.com.i9algo.taxiadv.libs.utilcode.util.DeviceUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.PhoneUtils;
import br.com.i9algo.taxiadv.v2.helpers.Logger;

/**
 * Identifying App Installations
 * @author andre
 * @version 1.0
 */
public class Installation {

    // Auxilia a identificar se eh a primeira vez que o aplicativo esta sendo executado
    // Caso seja a primeira vez, o estado da variavel permanece em true durante toda a execucao da APP
	private static boolean _firstAccess = false;

    private static String sID = null;

    private static final String INSTALLATION = "INSTALLATION";

    /**
     * Identificar se eh o primeiro acesso da APP no DEVICE
     * @param context
     * @return
     */
    public synchronized static boolean isFirstAccess(Context context) {
    	File installation = new File(context.getFilesDir(), INSTALLATION);
    	try {
            boolean first = !installation.exists();

            if (first)
                _firstAccess = true;

        	return (_firstAccess);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Identificar se o codigo de instalacao ja foi criado
     * @param context
     * @return
     */
    public synchronized static boolean isInstalled(Context context) {
    	File installation = new File(context.getFilesDir(), INSTALLATION);
    	try {
            boolean first = !installation.exists();

        	return (first);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public synchronized static String id(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
            	//Log.v("Application", "First Access ---> " + !installation.exists());
            	//Log.v("Application", "First Access ---> " + installation.getPath());
            	
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Logger.d("Installation", "installation ID @#=> " + sID);
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = getUniquePsuedoID();
        out.write(id.getBytes());
        out.close();
    }

    /**
     * Return pseudo unique ID
     * @return ID
     */
    public static String getUniquePsuedoID() {
        String m_szDevIDShort = DeviceUtils.getPseudoUniqueID();

        // Thanks to @Roman SL!
        // https://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        @SuppressLint("MissingPermission")
        String serial = PhoneUtils.getSerial();
        try {
            //serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
        }

        // Thanks @Joe!
        // https://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}