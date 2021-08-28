package br.com.i9algo.taxiadv.ui.views.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.v2.utils.time.CountUITimer;

/**
 * Created by aStraube on 03/05/2016.
 */
public class BaseDialogAutoDismiss extends AlertDialog.Builder implements
        DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    private AlertDialog mDialog = null;
    private CountUITimer mTimer = null;

    private Runnable runMethod = new Runnable()
    {
        public void run()
        {
            //Log.v("@@@", "run");
            if (getDialog().isShowing())
                getDialog().dismiss();
        }
    };

    public final AlertDialog getDialog() {
        if (mDialog == null) {
            this.mDialog = this.create();
            this.mDialog.setOnCancelListener(this);
            this.mDialog.setOnDismissListener(this);

        }
        return mDialog;
    }

    public BaseDialogAutoDismiss(final Context context) {
        super(context);

        mTimer = new CountUITimer(new Handler(), runMethod, Constants.INTERVAL_HIDE_DIALOG);
        mTimer.start();
    }

    /**
     * Serve para nao deixar fechar a Dialog sozinha, enquanto este campo estiver sendo utilizado
     * @param editText
     */
    public void addEditText(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //Log.v("@@@", "onTextChanged: ");
                onResetTimer();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                //Log.v("@@@", "beforeTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Log.v("@@@", "afterTextChanged: ");
            }
        });
    }

    public void onResetTimer() {
        if (mTimer != null)
            mTimer.reset();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //Log.v("@@@", "onCancel: ");
        if (mTimer != null)
            mTimer.stop();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //Log.v("@@@", "onDismiss: ");
        if (mTimer != null)
            mTimer.stop();
    }
}
