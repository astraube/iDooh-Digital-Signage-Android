package br.com.i9algo.taxiadv.v2.components.UpdaterApp

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import br.com.i9algo.taxiadv.BuildConfig
import br.com.i9algo.taxiadv.v2.components.UpdaterApp.versioning.DefaultArtifactVersion
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import java.lang.ref.WeakReference

class UpdateAppCheck(context: Context, private val mUrlApkinfoUpdate: String) : Runnable {

    val LOG_TAG = javaClass.simpleName

    private val contextRef: WeakReference<Context>

    init {
        this.contextRef = WeakReference(context)

        this.init()
    }

    private fun init() {
        Thread(this).start()
    }

    //@Override
    override fun run() {
        if (UpdateAppCheck.isUpdateChecking)
            return

        val context = this.contextRef.get() as Context

        UpdateAppCheck.isUpdateChecking = true


        /*
         * Original
         *
        // Nao deu pra fazer o que eu queria assim
        new AppUpdater(context)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON(uri_apk_update)
                .setDisplay(Display.DIALOG)
                .showAppUpdated(true)
                .setButtonUpdateClickListener(new UpdateClickListener(context, UpdateFrom.JSON, updateUrl))
                .start();*/

        val appUpdaterUtils = AppUpdaterUtils(context)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON(this.mUrlApkinfoUpdate)
                .withListener(object : AppUpdaterUtils.UpdateListener {
                    override fun onSuccess(update: Update, isUpdateAvailable: Boolean?) {
                        /*
                        Log.d(LOG_TAG, "onSuccess: " + update.getLatestVersion());
                        Log.d(LOG_TAG, "onSuccess: " + update.getLatestVersionCode());
                        Log.d(LOG_TAG, "onSuccess: " + update.getUrlToDownload());
                        Log.d(LOG_TAG, "onSuccess: " + update.getReleaseNotes());
                        */


                        if (!newVersionAvailable(update.latestVersion, update.latestVersionCode)) {
                            Log.d(LOG_TAG, "Nenhuma nova versão disponível")
                            return
                        }

                        /**
                         * Exibe um dialog para o usuario aceitar atualizar o APP
                         *
                        val titleUpdate = context.resources.getString(com.github.javiersantos.appupdater.R.string.appupdater_update_available)
                        //String titleNoUpdate = context.getResources().getString(com.github.javiersantos.appupdater.R.string.appupdater_update_not_available);
                        val btnUpdate = context.resources.getString(com.github.javiersantos.appupdater.R.string.appupdater_btn_update)
                        val btnDismiss = context.resources.getString(com.github.javiersantos.appupdater.R.string.appupdater_btn_dismiss)
                        //String btnDisable = context.getResources().getString(com.github.javiersantos.appupdater.R.string.appupdater_btn_disable);

                        val updateClickListener = UpdateClickListener(context, UpdateFrom.JSON, update.urlToDownload)
                        //DialogInterface.OnClickListener disableClickListener = new DisableClickListener(context);

                        if (UpdateAppCheck.alertDialog != null && UpdateAppCheck.alertDialog!!.isShowing) {
                            UpdateAppCheck.alertDialog!!.dismiss()
                        }

                        try {
                            UpdateAppCheck.alertDialog = AlertDialog.Builder(context)
                                    .setTitle(titleUpdate)
                                    .setMessage("")
                                    .setNegativeButton(btnDismiss, null)
                                    .setPositiveButton(btnUpdate, updateClickListener)
                                    //.setNeutralButton(btnDisable, disableClickListener)
                                    .setCancelable(true)
                                    .create()
                            UpdateAppCheck.alertDialog!!.show()

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }*/

                        // Atualiza o app sozinho
                        try {
                            val updateApp = UpdateAppAsyncTask(context)
                            updateApp.execute(update.urlToDownload)

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                        UpdateAppCheck.isUpdateChecking = false
                    }

                    override fun onFailed(error: AppUpdaterError) {
                        Log.d(LOG_TAG, "AppUpdater Error Something went wrong")
                        UpdateAppCheck.isUpdateChecking = false
                    }
                })
        appUpdaterUtils.start()
    }

    /**
     * Se true, tem uma nova versao
     * Se false, nao tem nenhuma nova versao
     *
     * @param latest_version_name
     * @param latest_version_code
     * @return
     */
    private fun newVersionAvailable(latest_version_name: String, latest_version_code: Int?): Boolean {
        val verName = BuildConfig.VERSION_NAME
        val verCode = BuildConfig.VERSION_CODE

        val local_version = DefaultArtifactVersion(verName) // versao local
        val latest_version = DefaultArtifactVersion(latest_version_name) // Versao online

        /*
        Log.d(LOG_TAG, "latest_version_name: " + latest_version_name);
        Log.d(LOG_TAG, "latest_version_code: " + latest_version_code);

        DefaultArtifactVersion v1 = new DefaultArtifactVersion("4.8.0");
        //Log.d(LOG_TAG, "getMajorVersion: " + v1.getMajorVersion()); // return 4
        //Log.d(LOG_TAG, "getMinorVersion: " + v1.getMinorVersion()); // return 8

        DefaultArtifactVersion v2 = new DefaultArtifactVersion("4.6.0");
        DefaultArtifactVersion v3 = new DefaultArtifactVersion("4.7.1");
        Log.d(LOG_TAG, "compareTo v1: " + v1.compareTo(local_version));
        Log.d(LOG_TAG, "compareTo v2: " + v2.compareTo(local_version));
        Log.d(LOG_TAG, "compareTo v3: " + v3.compareTo(local_version));

        Log.d(LOG_TAG, "compareTo compareTo: " + latest_version.compareTo(local_version));
        Log.d(LOG_TAG, "compareTo compareTo: " + local_version.compareTo(latest_version));
        Log.d(LOG_TAG, "compareTo (latest_version_code > verCode): " + (latest_version_code > verCode));
        */

        if (latest_version.compareTo(local_version) > 0)
            return true

        if (latest_version_code != null) {
            return (latest_version_code > verCode)
        }
        return true
    }

    companion object {

        private var isUpdateChecking = false
        private var alertDialog: AlertDialog? = null
    }
}
