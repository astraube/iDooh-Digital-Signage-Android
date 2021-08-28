package br.com.i9algo.taxiadv.v2.helpers;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.view.View;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import br.com.i9algo.taxiadv.domain.constants.FirebaseVars;
import br.com.i9algo.taxiadv.domain.models.Device;
import br.com.i9algo.taxiadv.libs.utilcode.util.AppUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.CleanUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.ScreenUtils;
import br.com.i9algo.taxiadv.libs.utilcode.util.ShellUtils;
import br.com.i9algo.taxiadv.v2.CustomApplication;

public class RemoteActions {

    public static void cleanAllData(boolean isAppRestart) {
        CleanUtils.cleanExternalCache();
        CleanUtils.cleanInternalCache();
        CleanUtils.cleanInternalDbs();
        CleanUtils.cleanInternalFiles();
        CleanUtils.cleanInternalSp();
        if (isAppRestart)
            appRestart();
    }

    public static void appRestart() {
        AppUtils.relaunchApp();
    }

    public static void saveActivityPrintScreen() {
        /*String filename = SystemClock.elapsedRealtime() + ".jpg";
        Bitmap bmp = ScreenUtils.screenShot(CustomApplication.getInstance().getCurrentActivity());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference printRef = storage.getReference().child(FirebaseVars.GS_CHILD_DEVICE_PRINT + filename);

        UploadTask uploadTask = printRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Logger.e("IdoohMessagingService", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Logger.e("IdoohMessagingService", "taskSnapshot.getUploadSessionUri()--> " + taskSnapshot.getUploadSessionUri());
            }
        });*/
    }

    public static void saveDevicePrintScreen() {
        String filename = SystemClock.elapsedRealtime() + ".png";
        String filpath = "/mnt/sdcard/" + filename;
        ShellUtils.CommandResult result = ShellUtils.execCmd("screencap " + filpath, true);
        if (result.result == 0) {
            String successMsg = result.successMsg;
            //Logger.e("IdoohMessagingService", "successMsg--> " + successMsg);

            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Uri file = Uri.fromFile(new File(filpath));
            StorageReference printRef = storage.getReference().child(FirebaseVars.GS_CHILD_DEVICE_PRINT + filename);
            try {
                InputStream stream = new FileInputStream(new File(filpath));

                UploadTask uploadTask = printRef.putStream(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Logger.e("IdoohMessagingService", exception);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Remover arquivo local
                        ShellUtils.execCmd("rm " + filpath, true);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return printRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    //Logger.e("IdoohMessagingService", "downloadUri--> " + downloadUri);
                                } else {

                                }
                            }
                        });
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static void deviceReboot() {
        AppUtils.relaunchApp();
    }
}
