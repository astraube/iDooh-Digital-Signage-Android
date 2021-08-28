package br.com.i9algo.taxiadv.v2.components.UpdaterApp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import br.com.i9algo.taxiadv.v2.views.LauncherActivity

class UpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val i = LauncherActivity.createIntent(context)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }
}
