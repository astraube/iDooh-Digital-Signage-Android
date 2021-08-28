package br.com.i9algo.taxiadv.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import br.com.i9algo.taxiadv.BuildConfig;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.domain.enums.AdvType;
import br.com.i9algo.taxiadv.domain.enums.Analytics;
import br.com.i9algo.taxiadv.libs.utilcode.util.PhoneUtils;
import br.com.i9algo.taxiadv.ui.views.dialog.DialogCarConfig;
import br.com.i9algo.taxiadv.ui.views.dialog.DialogLogin;
import br.com.i9algo.taxiadv.ui.views.dialog.DialogWifiConfigure;
import br.com.i9algo.taxiadv.v2.helpers.AnalyticsTrackerProvider;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;
import br.com.i9algo.taxiadv.v2.utils.BatteryUtil;
import br.com.i9algo.taxiadv.v2.utils.IDManagement;
import br.com.i9algo.taxiadv.v2.utils.Pref;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import br.com.i9algo.taxiadv.libs.utilcode.util.NetworkUtils;

public class AboutFragment extends BaseFragment implements DialogCarConfig.DialogCarDelegate{

	private final String LOG_TAG = AboutFragment.class.getSimpleName();

    @BindView(R.id.about_fragment_playlist_viewer_scrollview)
    ScrollView mScrollview;

    @BindView(R.id.view_content)
	ImageView imgView;

    @BindView(R.id.view_list_of_slides)
    TextView playlistList;

    @BindView(R.id.txt_serial)
    TextView txtSerial;

    @BindView(R.id.txt_car_number)
    TextView txtCarNumber;

    @BindView(R.id.txt_status)
    TextView textViewStatus;

    @BindView(R.id.txt_version)
    TextView txtVersion;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.btn_wifi)
    ImageView btnWifi;

    @BindView(R.id.btn_config)
    ImageView btnConfig;

    @BindView(R.id.demoButton)
    RelativeLayout demoButton;

    @BindView(R.id.demoStatus)
    TextView demoStatus;
    
    private AboutFragmentDelegate delegate;

    private int count = 0;
    private long startMillis=0;

    private boolean isShowingSlideshowList = false;

    @Inject
    public AboutFragment () {
        super();
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(AboutFragment.this, view);

        if (!Pref.isDemo()){
            demoStatus.setText(getResources().getText(R.string.running_mode_normal));
        }else{
            demoStatus.setText(getResources().getText(R.string.running_mode_demo));
        }
        demoStatus.setVisibility(View.GONE);

        changeWifiIcon(NetworkUtils.getWifiEnabled());

        // Versao do Programa
        txtVersion.setText("Versão: " + BuildConfig.VERSION_NAME);

        // Status de Internet
        String statusNet = (NetworkUtils.isConnected()) ? "ON" : "OFF";
        textViewStatus.setText("Internet: " + statusNet);

        // Status Bateria
        textViewStatus.setText(textViewStatus.getText() + "\nBateria: " + BatteryUtil.getBatteryLevel(getActivity()) + "%");

        // Status Carregando
        textViewStatus.setText(textViewStatus.getText() + "\nCarregando: " + (BatteryUtil.getHasCharging(getActivity()) ? "Sim" : "Nao"));

        // Versao do Programa
        txtSerial.setText("S/N: " + PhoneUtils.getSerial().toUpperCase());

        // Versao do Programa
        txtCarNumber.setText("Carro: " + Pref.getCarNumber());

        load();

        // Analytics
        AnalyticsTrackerProvider.sendTap(getContext(), Analytics.CategoryName.UI, Analytics.ActionsName.VISUALIZOU_CREDITOS, getName(), 1);

        return view;
    }

    private void changeWifiIcon(boolean enabled) {
        if(enabled) {
            btnWifi.setImageResource(R.drawable.ic_wifi_white_48dp);
            return;
        }
        btnWifi.setImageResource(R.drawable.ic_wifi_off_white_48dp);
    }
    @OnClick(R.id.btn_wifi)
    public void onClickWifi() {
        Runnable runResult = new Runnable() {
            public void run() {
                boolean enabled = NetworkUtils.getWifiEnabled();
                boolean newState = !enabled;

                NetworkUtils.setWifiEnabled(newState);
                changeWifiIcon(newState);

                if (newState)
                    new DialogWifiConfigure(getActivity());
            }
        };
        new DialogLogin(getActivity(), runResult);
    }

    @OnClick(R.id.demoButton)
    public void onDemoButtonTapped(){
        Runnable runResult = new Runnable() {
            public void run() {
                delegate.switchDemoDisplayMode();
            }
        };
        new DialogLogin(getActivity(), runResult);
    }

    @OnClick(R.id.txt_version)
    public void onClickVersionUpdate() {
        Runnable runResult = new Runnable() {
            public void run() {
                if (NetworkUtils.isConnected()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(getContext().getString(R.string.dl_message_title));
                    alertDialogBuilder.setMessage(getContext().getString(R.string.dl_message_content));
                    /* libraryAndroidUpdateChecker
                    alertDialogBuilder.setPositiveButton(com.madx.updatechecker.lib.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });*/
                    alertDialogBuilder.show();
                    // libraryAndroidUpdateChecker
                    // CustomApplication.get(getContext()).forceUpdate();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(getContext().getString(R.string.error_message_oops));
                    alertDialogBuilder.setMessage(getContext().getString(R.string.error_message_network));
                    /* libraryAndroidUpdateChecker
                    alertDialogBuilder.setPositiveButton(com.madx.updatechecker.lib.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });*/
                    alertDialogBuilder.show();
                }
            }
        };
        new DialogLogin(getActivity(), runResult);
    }

    @OnClick(R.id.btn_config)
    public void onClickConfig() {
        delegate.showMaintenanceFragment();
    }

    @OnClick(R.id.txt_car_number)
    public void onClickConfigCar() {
        Runnable runResult = new Runnable() {
            public void run() {
                new DialogCarConfig(getActivity(), AboutFragment.this);
            }
        };
        new DialogLogin(getActivity(), runResult);
    }

    @OnClick(R.id.btn_refreshPlaylist)
    public void onClickRenewPlaylist() {
        Runnable runResult = new Runnable() {
            public void run() {
                delegate.handlePlaylistRefresh();
            }
        };
        new DialogLogin(getActivity(), runResult);
    }

    public void load() {
    	progressBar.setVisibility(View.VISIBLE);

        /*
        String mUrl = "file:///android_asset/cover_sidebar.jpg"; // Carregar com glide

        RequestOptions glideOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.placeholder_loop);

        Glide.with(this.getContext())
                .load(mUrl)
                .apply(glideOptions)
                .into(imgView);*/

        imgView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cover_sidebar));


        playlistList.setText("");
        for (SlideshowItem item : delegate.getPlaylistFromPlaylistView()){
            AdvType type = (!TextUtils.isEmpty(item.getType())) ? AdvType.valueOf(item.getType().toUpperCase()) : AdvType.DEFAULT;

            //Logger.i(LOG_TAG, "Type - " + type.toString());

            if (type.equals(AdvType.NEWS)) {
                playlistList.setText(playlistList.getText() + "Noticia" + "\r\n");
            }else{
                playlistList.setText(playlistList.getText() + "Conteúdo: " + item.getTitle() + "\r\n");
            }
        }

        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.view_content)
    public void onViewContentTapped(){
        //get system current milliseconds
        long time= System.currentTimeMillis();

        //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
        if (startMillis==0 || (time-startMillis> 3000) ) {
            startMillis=time;
            count=1;
        }
        //it is not the first, and it has been  less than 3 seconds since the first
        else{ //  time-startMillis< 3000
            count++;
        }

        if (count==5) {
            mScrollview.setVisibility(View.VISIBLE);
            imgView.setVisibility(View.GONE);
            isShowingSlideshowList = true;
        }

    }

    public AboutFragmentDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(AboutFragmentDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handleCarPlateAboutFragment(String placa) {
        txtCarNumber.setText(placa);
        delegate.handleCarPlate(placa);
    }

    public boolean isShowingSlideshowList() {
        return isShowingSlideshowList;
    }

    public void hideSlideshowViewer() {
        mScrollview.setVisibility(View.GONE);
        imgView.setVisibility(View.VISIBLE);
        isShowingSlideshowList = false;
    }

    public interface AboutFragmentDelegate {

        void switchDemoDisplayMode();
        void handleCarPlate(String placa);
        List<SlideshowItem> getPlaylistFromPlaylistView();
        void handlePlaylistRefresh();
        void showMaintenanceFragment();
        void showFloat();
    }
}
