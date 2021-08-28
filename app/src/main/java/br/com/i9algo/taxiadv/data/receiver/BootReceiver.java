package br.com.i9algo.taxiadv.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.libs.utilcode.util.AppUtils;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.views.LauncherActivity;

public class BootReceiver extends BroadcastReceiver {


	private final String LOG_TAG = getClass().getSimpleName();
	
    @Override
    public void onReceive(Context context, Intent intent) {
		Logger.v(LOG_TAG, "-----> ligou device -" + intent.getAction());

		if (intent.getAction().equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
			Logger.v(LOG_TAG, "-----> ligou device em modo seguro -" + intent.getAction());
		}

		if (!BuildConfig.DEBUG && !AppUtils.isAppForeground()) {
			context.startActivity( LauncherActivity.createIntent(context) );
		}
    }
}