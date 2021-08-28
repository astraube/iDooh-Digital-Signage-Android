package br.com.i9algo.taxiadv.v2.components.UpdaterApp

import android.content.Context
import android.content.DialogInterface
import com.github.javiersantos.appupdater.enums.UpdateFrom
import java.net.URL

class UpdateClickListener(val context: Context, val updateFrom: UpdateFrom, val apkUrl: URL) : DialogInterface.OnClickListener {

    private val LOG_TAG = javaClass.simpleName

    override fun onClick(dialog: DialogInterface, which: Int) {
        //UtilsLibrary.goToUpdate(this.context, this.updateFrom, this.apk);

        //Log.d(LOG_TAG, "onClick: " + this.apk);

        val updateApp = UpdateAppAsyncTask(context)
        updateApp.execute(this.apkUrl)
    }
}
