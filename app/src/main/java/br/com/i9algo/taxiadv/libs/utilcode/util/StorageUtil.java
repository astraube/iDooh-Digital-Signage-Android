package br.com.i9algo.taxiadv.libs.utilcode.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by aStraube on 02/05/2016.
 */
public class StorageUtil {


    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * @return Numero em Bytes que corresponde ao Espaço DISPONÍVEL na memoria INTERNA
     */
    public static long getInternalAvailableSpace() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize);
    }

    /**
     * @return Numero em Bytes que corresponde ao Espaço TOTAL na memoria INTERNA
     */
    public static long getInternalTotalSpace() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return (totalBlocks * blockSize);
    }

    /**
     * @return Numero em Bytes que corresponde ao Espaço DISPONÍVEL na memoria EXTERNA
     */
    public static long getExternalAvailableSpace() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return (availableBlocks * blockSize);
        } else {
            return 0;
        }
    }

    /**
     * @return Numero em Bytes que corresponde ao Espaço TOTAL na memoria EXTERNA
     */
    public static long getExternalTotalSpace() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return (totalBlocks * blockSize);

        } else {
            return 0;
        }
    }

    public static boolean hasLessThanHalfAvailableInternalDiskSpace() {
        if (getInternalDiskSpaceUsagePercentage() < 50) {
            return true;
        }
        return false;
    }

    public static boolean hasLessThanHalfAvailableExternalDiskSpace() {
        if (externalMemoryAvailable()) {
            if (StorageUtil.getExternalDiskSpaceUsagePercentage() < 50) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static double getExternalDiskSpaceUsagePercentage(){
        long externalTotalSpace = StorageUtil.getExternalTotalSpace();
        long externalAvailableSpace = StorageUtil.getExternalAvailableSpace();
        return (double) (externalAvailableSpace * 100) / externalTotalSpace;
    }

    public static double getInternalDiskSpaceUsagePercentage(){
        long internalTotalSpace = StorageUtil.getInternalTotalSpace();
        long internalAvailableSpace = StorageUtil.getInternalAvailableSpace();
        return (double) (internalAvailableSpace * 100) / internalTotalSpace;
    }
}
