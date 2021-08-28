package br.com.i9algo.taxiadv.data.task;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import br.com.i9algo.taxiadv.v2.network.taxiadv.IdoohMediaDeviceController;

public class LocationUpdateSendAsyncTask extends AsyncTask<Location, Void, Void> {

    private Context mContext;
    public LocationUpdateSendAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(final Location... params) {
        IdoohMediaDeviceController.sendGeopoint(params[0]);
        return null;
    }
}
