package br.com.i9algo.taxiadv.v2.components.UpdaterApp

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.core.content.FileProvider
import br.com.i9algo.taxiadv.BuildConfig
import br.com.i9algo.taxiadv.libs.utilcode.util.AppUtils
import br.com.i9algo.taxiadv.libs.utilcode.util.FileUtils
import br.com.i9algo.taxiadv.libs.utilcode.util.ShellUtils

import java.io.File

import br.com.i9algo.taxiadv.v2.helpers.Logger
import br.com.i9algo.taxiadv.v2.helpers.RemoteActions
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.ArrayList

class UpdateService : IntentService(UpdateService::class.java.name) {

    companion object {
        private val LOG_TAG = "UpdateService"
        val EXTRA_FILEPATH = "FILEPATH"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Logger.v(LOG_TAG, "UpdateApp ----> onStartCommand");

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        //Logger.v(LOG_TAG, "UpdateApp ----> Service Started!")

        val filePath = intent!!.getStringExtra(EXTRA_FILEPATH)

        Logger.v(LOG_TAG, "UpdateApp ----> filePath: $filePath")

        if (!TextUtils.isEmpty(filePath)) {
            val file = File(filePath)

            //ShellUtils.execCmd("cp " + file.path + " /data/local/tmp/Taxiadv.apk", false)
            //AppUtils.installAppSilent(file)

            installAppSilent(file)
        }
        this.stopSelf()
    }

    fun installAppSilent(file: File) {
        if (!FileUtils.isFileExists(file)) return

        val filePath = '"'.toString() + file.absolutePath + '"'.toString()

        val baseLib = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* "
        val cmds1 = ArrayList<String>()
        cmds1.add(baseLib + "mount -o remount,rw none /system")
        cmds1.add(baseLib + "cp " + filePath + " /system/app/" + file.name)
        cmds1.add(baseLib + "chmod 644 /system/app/" + file.name)
        cmds1.add(baseLib + "rm -f " + filePath)

        if (BuildConfig.DEBUG) {
            Logger.v(LOG_TAG, "filePath: $filePath")
            for (cmd in cmds1) {
                Logger.v(LOG_TAG, "command: $cmd")
            }
        }
        val commandResult = ShellUtils.execCmd(cmds1, true)
        if (commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success")) {
            Logger.v(LOG_TAG, "installAppSilent concluido!!!")
        } else {
            Logger.e(LOG_TAG, "installAppSilent successMsg: " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg)
        }

        // limpar memoria do equipamento para receber a novaversão
        RemoteActions.cleanAllData(false)

        val cmds2 = ArrayList<String>()
        cmds2.add(baseLib + "rm -f " + AppUtils.getAppPath())
        cmds2.add(baseLib + "mount -o remount,ro none /system")
        cmds2.add(baseLib + "reboot")
        ShellUtils.execCmd(cmds2, true)
    }

    /*@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun install(context: Context, packageName: String, apkPath: String) {
        Logger.v(LOG_TAG, "install packageName: $packageName")
        Logger.v(LOG_TAG, "install apkPath: $apkPath")

        // PackageManager provides an instance of PackageInstaller

        val packageInstaller = context.packageManager.packageInstaller

        // Prepare params for installing one APK file with MODE_FULL_INSTALL
        // We could use MODE_INHERIT_EXISTING to install multiple split APKs
        val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        params.setAppPackageName(packageName)

        // Get a PackageInstaller.Session for performing the actual update
        val sessionId = packageInstaller.createSession(params)
        val session = packageInstaller.openSession(sessionId)

        // Copy APK file bytes into OutputStream provided by install Session
        val out = session.openWrite(packageName, 0, -1)
        val fis = File(apkPath).inputStream()
        fis.copyTo(out)
        session.fsync(out)
        out.close()

        // The app gets killed after installation session commit
        session.commit(PendingIntent.getBroadcast(context, sessionId,
                Intent("android.intent.action.MAIN"), 0).intentSender)
    }*/
}