package br.com.i9algo.taxiadv.v2.views.slideshow.maintenance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class MaintenanceFragment extends BaseFragment implements MaintenanceViewInterface {

    private static final String TAG = MaintenanceFragment.class.getSimpleName();

    @BindView(R.id.config_layout)
    RelativeLayout configLayout;

    @BindView(R.id.desligar_layout)
    RelativeLayout desligarLayout;

    private String m_Text = "";

    private Delegate delegate;

    MaintenancePresenter presenter;

    @SuppressLint("ValidFragment")
    @Inject
    public MaintenanceFragment(Delegate delegate) {
        super();
        presenter = new MaintenancePresenter();
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        ButterKnife.bind(MaintenanceFragment.this, view);
        presenter.bindView(this);

        return view;
    }

    @OnClick(R.id.config_layout)
    public void onConfigTapped() {
        delegate.sendToAndroidConfigMenu();
    }

    @OnClick(R.id.desligar_layout)
    public void onLightsOffTapped() {
        showLightsOff();
    }

    @SuppressLint("ResourceAsColor")
    private void showLightsOff() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Insira quantos minutos de espera (MAX: 120 minutos)");
        final EditText input = new EditText(delegate.getContext());
        input.setTextColor(R.color.black);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            private boolean validateTime(String time) {
                if (time != null && !time.isEmpty() && Integer.parseInt(time) <= 120) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validateTime(input.getText().toString())) {
                    presenter.sleep(Integer.parseInt(input.getText().toString()));
                    delegate.setDialogFlag(false);
                    dialog.dismiss();
                } else {;
                    dialog.cancel();
                    showInvalidTime();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        delegate.setDialogFlag(true);
    }

    private void showInvalidTime() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tempo de espera inválido!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delegate.setDialogFlag(false);
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.cancelTimer();
    }

    public interface Delegate {
        void sendToAndroidConfigMenu();
        Context getContext();
        void setDialogFlag(boolean flag);

    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }
}
