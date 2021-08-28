package br.com.i9algo.taxiadv.v2.download;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.libs.utilcode.util.StringUtils;
import br.com.i9algo.taxiadv.v2.network.taxiadv.AdvService;

public class BatchDownloadFileReceiver extends BroadcastReceiver {
    @Inject
    AdvService service;

    @Inject
    DownloadHelperDAO dao;


    String servicestring = Context.DOWNLOAD_SERVICE;
    DownloadManager downloadManager;

    private final String LOG = "BatchDownload";

    @Inject
    public BatchDownloadFileReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ((CustomApplication) context.getApplicationContext()).getSchedulerComponent().inject(this);
        Log.e(LOG, "onReceive");
        if (downloadManager == null) {
            downloadManager = (DownloadManager) context.getSystemService(servicestring);
        }

        //check if the broadcast message is for our Enqueued download
        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        Bundle extras = intent.getExtras();
        ParcelFileDescriptor file;
        DownloadManager.Query q = new DownloadManager.Query();
        q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
        Cursor c = downloadManager.query(q);
        Log.e(LOG, "Cursor c = downloadManager.query(q);");
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                Log.e(LOG, "status = sucess");
                Uri fileURI2 = downloadManager.getUriForDownloadedFile(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                dao.updateTaskStatusSucess(Integer.parseInt(Long.toString(referenceId)), StringUtils.getFilePathFromUri(context, fileURI2), true);
            } else {
                Log.e(LOG, "status = fail");
                dao.updateTaskStatusSucess(Integer.parseInt(Long.toString(referenceId)), "error", false);
            }
        }
        c.close();
    }
}
