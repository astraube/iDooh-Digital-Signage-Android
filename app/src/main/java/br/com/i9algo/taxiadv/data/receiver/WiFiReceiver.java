package br.com.i9algo.taxiadv.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.List;

/**
 * Created by aStraube on 23/05/2016.
 */
public class WiFiReceiver extends BroadcastReceiver {

    /**
     * Listener responsável para notificar a consulta
     */
    public static interface WiFiListener {
        void onResultScan(Context arg0, Intent arg1, List<ScanResult> results);
    }

    public static WiFiReceiver startScanWIFI(Context context, WiFiListener wiFiListener) {
        WiFiReceiver wifi = new WiFiReceiver();
        wifi.wiFiListener = wiFiListener;
        wifi.wifiManager = wifi.getWifiManager(context);

        //Se o wi-fi estiver desabilitado, envia um comando para habilitar e retorna null para tratar.
        if (wifi.wifiManager.isWifiEnabled() == false) {
            try {
                Toast.makeText(context, "Habilitando Wi-Fi...", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            wifi.wifiManager.setWifiEnabled(true);
            return null;
        }

        //Registra o broadcast para scaniar as redes wi-fi
        context.registerReceiver(wifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        //Inicia o rastramento
        wifi.wifiManager.startScan();

        return wifi;
    }


    private WifiManager wifiManager;
    private WiFiListener wiFiListener;

    private WiFiReceiver() {
    }

    /**
     * Pega o adaptador Wirelles
     *
     * @param context
     * @return
     */
    public WifiManager getWifiManager(Context context) {
        if (wifiManager == null) {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }
        return wifiManager;
    }

    /**
     * Método responsavel em receber o broadcast do scan wi-fi
     */
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        List<ScanResult> results = wifiManager.getScanResults();

        // Se tiver listener, envia os dados.
        if (wiFiListener != null) {
            wiFiListener.onResultScan(arg0, arg1, results);
        }

        //Cancela o Broadcast e desregistra.
        clearAbortBroadcast();
        arg0.unregisterReceiver(this);
    }
}