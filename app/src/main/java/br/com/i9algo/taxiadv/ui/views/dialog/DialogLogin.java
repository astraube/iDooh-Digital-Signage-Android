package br.com.i9algo.taxiadv.ui.views.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.storage.firebase.RemoteConfigs;

public class DialogLogin extends BaseDialogAutoDismiss {

	private final int passwordArrayID = R.string.dialog_loginconfig_title;
	private OnDialogLoginListener mListener = null;


	interface OnDialogLoginListener {
		void onPassInsert(String pass);
	}

	public DialogLogin(final Context context, String title, final OnDialogLoginListener listener) {
		this(context, null, title);
		mListener = listener;
	}
	public DialogLogin(final Context context, final Runnable runResult) {
		this(context, runResult, context.getString(R.string.dialog_loginconfig_title));
	}
	public DialogLogin(final Context context, final Runnable runResult, String title) {
		super(context);
		
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		final LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_layout_config_login, null);
		final EditText txtPass = (EditText)layout.findViewById(R.id.txtPass);

		addEditText(txtPass);
		
		//setIcon(R.drawable.money);
        setView(layout);
        setTitle(title);
        setCancelable(true);
        setOnCancelListener(new AlertDialog.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				getDialog().dismiss();
			}
		});
        setPositiveButton("OK",
			new DialogInterface.OnClickListener() {
				@SuppressLint("ResourceType")
				public void onClick(DialogInterface dialog, int whichButton) {
					// TODO - senha de ADM desativada temporariamente
					/*
					if (runResult != null) {
						runResult.run();
					}
					*/

					String value = txtPass.getText().toString();

					if (TextUtils.isEmpty(value)) {
						setMessage(getContext().getString(R.string.dialog_loginconfig_input_pass));
					} else {
						if (runResult != null) {
							String pass;

							try {
								pass = RemoteConfigs.getPasswordMaintenance();
							} catch (Exception ex) {
								pass = "ratata";
							}
							/*if (Arrays.asList(pass).contains(value)) {
								runResult.run();
							}*/
							if (pass.equals(value)) {
								runResult.run();
							}
						}
						if (mListener != null) {
							mListener.onPassInsert(value);
						}
						getDialog().dismiss();
					}
				}
			}
		);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.AppBaseTheme;
		getDialog().show();
	}
}
