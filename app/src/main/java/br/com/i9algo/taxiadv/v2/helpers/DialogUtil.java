package br.com.i9algo.taxiadv.v2.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.DialogBase;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.libs.utilcode.util.DeviceUtils;

public class DialogUtil {

    public interface DialogUtilInterface {
        String doSomething(int param1, String param2);
    }

    public static void showTimerRebootDialog(Activity activity, int seconds, String title, String text) {

        MaterialDialog dialog = null;

        if (activity != null) {
            dialog = new MaterialDialog.Builder(activity)
                    .title(title)
                    .content(text)
                    .build();

            dialog.show();
        }
        //final MaterialStyledDialog dialog = DialogUtil.showMaterialDialogMessage(context, "Auto-closing Dialog", "teste...", onCompleteTimerCallback);

        final Timer t = new Timer();
        final MaterialDialog finalDialog = dialog;
        t.schedule(new TimerTask() {
            public void run() {
                try {
                    if (finalDialog != null)
                        finalDialog.dismiss();

                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report

                    DeviceUtils.reboot();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, TimeUnit.SECONDS.toMillis(seconds));
    }

    public static MaterialStyledDialog showMaterialDialogMessage(Context context, String message, String title, MaterialDialog.SingleButtonCallback buttonCallback) {
        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(context)
                .setIcon(new IconicsDrawable(context).icon(MaterialDesignIconic.Icon.gmi_mail_send).color(Color.WHITE))
                .withDialogAnimation(true)
                .setHeaderColor(R.color.colorAccent)
                .setTitle(title)
                .setDescription(message)
                .setPositive("OK", buttonCallback)
                .build();

        dialog.show();

        return dialog;
    }

    public static MaterialStyledDialog showCustomMaterialDialog(Context context, View customview) {
        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(context)
                .setIcon(new IconicsDrawable(context).icon(MaterialDesignIconic.Icon.gmi_email_open).color(Color.WHITE))
                .withDialogAnimation(true)
                .setHeaderColor(R.color.colorAccent)
                .setTitle(context.getResources().getString(R.string.share_dialog_title))
                .setCustomView(customview)
                .build();
        dialog.show();
        return dialog;
    }

    public static MaterialStyledDialog showBadInternetMessage(Context context) {
        return new MaterialStyledDialog.Builder(context)
                .setTitle("Problemas na rede")
                .setDescription("Tente novamente mais tarde")
                .show();
    }

    public static AlertDialog showCustomDialog(Context context, View customview) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setTitle("Compartilhe essa campanha!")
                //.setDescription("Escolha o método de compartilhamento:")
                .setView(customview);

        AlertDialog d = dialog.create();

        return d;
    }
}
