package br.com.i9algo.taxiadv.ui.views.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.data.receiver.WiFiReceiver;

/**
 * Created by aStraube on 23/05/2016.
 */
public class DialogWifiConfigure extends BaseDialogAutoDismiss implements
        WiFiReceiver.WiFiListener,
        DialogLogin.OnDialogLoginListener {

    private Activity mActivity;
    private ProgressDialog mProgressDialog = null;
    private WiFiReceiver wiFi = null;
    private ArrayAdapter<String> arrayAdapter;
    private List<ScanResult> scans;
    private WifiConfiguration newWifiConfig;
    private int idWifiSelected;

    public DialogWifiConfigure(final Activity activity) {
        super(activity);
        mActivity = activity;

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        final Handler handler = new Handler(Looper.getMainLooper());
        final int tempoDeEspera = 3000;
        final int tentativas = 10;
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < tentativas; i++) {
                    SystemClock.sleep(tempoDeEspera);

                    //Inicia a Busca...
                    wiFi = WiFiReceiver.startScanWIFI(activity, DialogWifiConfigure.this);

                    if (wiFi != null) {
                        i = tentativas;

                        // Desconectar da rede atual
                        wiFi.getWifiManager(getContext()).disconnect();
                        return;
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (wiFi != null) {
                            Toast.makeText(getContext(), "Wifi nao pode ser ativada.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();



        setTitle("Selecione uma rede");
        setNegativeButton(
            "cancel",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getDialog().dismiss();
                }
            });

        arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_singlechoice);
        setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onResetTimer();
                        idWifiSelected = id;
                        new DialogLogin(mActivity, "Digite a senha da rede WIFI", DialogWifiConfigure.this);

                    }
                });

        setCancelable(true);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AppBaseTheme;
        getDialog().show();

        showProgress("Buscando...");
    }

    public void onPassInsert(String pass) {
        onResetTimer();
        onWifiSelected(pass);
    }
    Runnable runResult = new Runnable() {
        public void run() {

        }
    };

    private void showProgress(String title) {
        if(mProgressDialog != null) {
            //mProgressDialog.setTitle("Por Favor Aguarde");
            mProgressDialog.setMessage(title);
            mProgressDialog.show();
        }
    }

    private void hideProgress() {
        if(mProgressDialog != null)
            mProgressDialog.hide();
    }

    @Override
    public void onResultScan(Context arg0, Intent arg1, List<ScanResult> results) {
        hideProgress();

        scans = results;
        arrayAdapter.clear();
        for (ScanResult scanResult : results) {
            //arrayAdapter.add(scanResult.SSID + " - " + scanResult.BSSID);
            arrayAdapter.add(scanResult.SSID);
        }
    }

    /**
     * Exemplo de conexão Wi-Fi
     *
     * Ao clicar em um item crio um WifiConfiguration com os dados do item e
     * realizo a conexão Wi-Fi.
     *
     */
    public void onWifiSelected(String pass) {
        //Configuro uma rede baseada nos dados encontrados.
        newWifiConfig = new WifiConfiguration();
        newWifiConfig.BSSID = scans.get(idWifiSelected).BSSID;
        newWifiConfig.SSID = "\"" + scans.get(idWifiSelected).SSID + "\"";
        newWifiConfig.preSharedKey = "\"" + pass + "\"";

        //Log.v("WifiConnect", "newWifiConfig: " + newWifiConfig);
        //Log.v("WifiConnect", "Rede: " + newWifiConfig.SSID + " - senha: " + newWifiConfig.preSharedKey);

        //Conecto na nova rede criada.
        WifiManager wifiManager = wiFi.getWifiManager(getContext());
        int netId = wifiManager.addNetwork(newWifiConfig);
        wifiManager.saveConfiguration();
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

        onConnecting();
    }

    private void onConnecting() {
        final WifiManager wifiManager = wiFi.getWifiManager(getContext());
        final Handler handler = new Handler(Looper.getMainLooper());
        final int tempoDeEspera = 5000;
        final int tentativas = 3;

        showProgress("Conectando...");

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < tentativas; i++) {
                    SystemClock.sleep(tempoDeEspera);

                    SupplicantState s = wifiManager.getConnectionInfo().getSupplicantState();
                    NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(s);

                    if (state.equals(NetworkInfo.DetailedState.OBTAINING_IPADDR)) {
                        i = tentativas;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                                Toast.makeText(getContext(), "Conectou em " + newWifiConfig.SSID, Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    } else if (i == tentativas-1){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                                onErrorConnected();
                            }
                        });
                    }
                }

            }
        }).start();
    }

    public void onErrorConnected() {
        AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
        builderInner.setTitle(mActivity.getString(R.string.error_input_pass));
        builderInner.setMessage(mActivity.getString(R.string.error_message_connect) + " em" + newWifiConfig.SSID);
        builderInner.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new DialogLogin(mActivity, "Digite a senha da rede WIFI", DialogWifiConfigure.this);
                    }
                });
        builderInner.show();
    }
}