package br.com.i9algo.taxiadv.ui.views.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.utils.Pref;

public class DialogCarConfig extends BaseDialogAutoDismiss {

	public DialogCarConfig(final Activity activity, final DialogCarDelegate delegate) {
		super(activity);
		
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		final LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_layout_config_device, null);
		final EditText txtCarBoard = (EditText)layout.findViewById(R.id.txt_car_board);

		addEditText(txtCarBoard);
		
		//setIcon(R.drawable.money);
        setView(layout);
        setTitle(R.string.dialog_devicenconfig_title);
        setCancelable(true);
        setOnCancelListener(new AlertDialog.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				getDialog().dismiss();
			}
		});
        setPositiveButton("OK",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String placaCarro = txtCarBoard.getText().toString();

					// Validar numero do carro
					if (!TextUtils.isEmpty(placaCarro)) {
						Pref.setCarNumber(placaCarro);
						delegate.handleCarPlateAboutFragment(placaCarro);
						getDialog().dismiss();
					}
				}
			}
		);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.AppBaseTheme;
		getDialog().show();
	}

	public interface DialogCarDelegate {

		void handleCarPlateAboutFragment(String placa);
	}
}
