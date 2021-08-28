package br.com.i9algo.taxiadv.v2.components.UpdaterApp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.content.pm.PackageInstaller
import android.app.PendingIntent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import br.com.i9algo.taxiadv.BuildConfig
import br.com.i9algo.taxiadv.libs.utilcode.util.AppUtils
import br.com.i9algo.taxiadv.libs.utilcode.util.FileUtils
import br.com.i9algo.taxiadv.libs.utilcode.util.ShellUtils
import br.com.i9algo.taxiadv.v2.helpers.Logger
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.channels.FileChannel
import java.util.ArrayList


class UpdateAppAsyncTask(context: Context) : AsyncTask<URL, Void, Boolean>() {

    val LOG_TAG = javaClass.simpleName

    private val contextRef: WeakReference<Context>


    init {
        this.contextRef = WeakReference(context)
        //Logger.v(LOG_TAG, "new UpdateAppAsyncTask()");
    }

    override fun onPreExecute() {
        super.onPreExecute()
        //Context context = (Context)this.contextRef.get();

        Logger.v(LOG_TAG, "onPreExecute")
    }

    override fun doInBackground(vararg args: URL): Boolean? {
        var flag = false

        Logger.v(LOG_TAG, "doInBackground")

        val context = this.contextRef.get() as Context

        try {
            val url = args[0]
            val c = url.openConnection() as HttpURLConnection
            c.requestMethod = "GET"
            c.doOutput = true
            c.connect()

            val fileName = FileUtils.getFileName(url.toString())
            val localPath = "/mnt/sdcard/Download/"
            val file = File(localPath)
            file.mkdirs()
            val outputFile = File(file, fileName!!)
            if (outputFile.exists()) {
                outputFile.delete()
            }
            Logger.v(LOG_TAG, "doInBackground - fileName: $fileName")
            Logger.v(LOG_TAG, "doInBackground - URL.getHost(): " + url.host)
            Logger.v(LOG_TAG, "doInBackground - URL.getPath(): " + url.path)
            Logger.v(LOG_TAG, "doInBackground - URL.toString(): " + url.toString())
            Logger.v(LOG_TAG, "doInBackground - URL.getFile(): " + url.file)
            Logger.v(LOG_TAG, "doInBackground - URL.getAuthority(): " + url.authority)

            //downloadFile(c.inputStream, outputFile)

            //var fileOut = FileOutputStream(outputFile)
            var fileOut: OutputStream = FileOutputStream(outputFile)
            val inputStream = c.inputStream
            val buffer = ByteArray(1024)
            var length: Int?

            while (true) {
                length = inputStream.read(buffer)
                if (length <= 0)
                    break
                fileOut.write(buffer, 0, length)
            }

            fileOut.flush()
            fileOut.close()
            inputStream.close()

            openNewVersion(context, localPath, fileName)

            flag = true

        } catch (e: MalformedURLException) {
            Logger.e(LOG_TAG, "Update Error: " + e.message)
            flag = false
        } catch (e: IOException){
            e.printStackTrace()
        } catch (e: Exception) {
            Logger.e(LOG_TAG, "Update error! " + e.message)

            this.cancel(true)
        }
        return flag
    }

    private fun openNewVersion(context: Context, localPath: String, fileName: String) {
        //val directory = context.getExternalFilesDir(null)
        //val file = File(directory, fileName)
        val file = File(localPath + fileName)

        //Logger.v(LOG_TAG, "localPath: " + localPath)
        //Logger.v(LOG_TAG, "fileName: " + fileName)
        Logger.v(LOG_TAG, "FileUtils.getFileName: " + FileUtils.getFileName(fileName))
        //Logger.v(LOG_TAG, "InstallApp fileName: " + fileName)
        Logger.v(LOG_TAG, "file.path: " + file.path)
        Logger.v(LOG_TAG, "file.name: " + file.name)

        if (FileUtils.isApkFile(file.name)) {
            Logger.v(LOG_TAG, "file is apk: " + file.name)
        }

        //val intent = Intent("UPDATE_SERVICE")
        val intent = Intent(context, UpdateService::class.java)
        intent.putExtra(UpdateService.EXTRA_FILEPATH, file.path)
        context.startService(intent)
    }

    /*override fun onPostExecute(result : Object) {
        super.onPostExecute(result);
        Context context = (Context)this.contextRef.get();

        Logger.v(LOG_TAG, "onPostExecute");
    }*/

    override fun onCancelled() {
        super.onCancelled()
        //Context context = (Context)this.contextRef.get();

        Logger.v(LOG_TAG, "onCancelled")
    }

    override fun onCancelled(result: Boolean) {
        super.onCancelled(result)
        //Context context = (Context)this.contextRef.get();

        Logger.v(LOG_TAG, "onCancelled(Void result)")
    }
}