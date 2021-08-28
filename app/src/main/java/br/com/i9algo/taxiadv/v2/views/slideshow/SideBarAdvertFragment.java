package br.com.i9algo.taxiadv.v2.views.slideshow;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.jorgecastillo.State;
import com.github.jorgecastillo.listener.OnStateChangeListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.Timer;

import javax.inject.Inject;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.libs.SweetAlert.ProgressHelper;
import br.com.i9algo.taxiadv.ui.components.SheetLayout;
import br.com.i9algo.taxiadv.v2.components.QRGen.QRCode;
import br.com.i9algo.taxiadv.v2.components.QRGen.scheme.GeoInfo;
import br.com.i9algo.taxiadv.v2.helpers.DialogUtil;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarProperties;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarPropertiesItem;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarTicket;
import br.com.i9algo.taxiadv.v2.network.taxiadv.AdvService;
import br.com.i9algo.taxiadv.v2.presenters.SideBarPresenter;
import br.com.i9algo.taxiadv.v2.storage.SidebarDAO;
import br.com.i9algo.taxiadv.v2.utils.GlideLoaderUtil;
import br.com.i9algo.taxiadv.v2.views.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SideBarAdvertFragment extends BaseFragment implements
        SideBarAdvertViewInterface
        ,SheetLayout.OnFabAnimationEndListener
        ,OnStateChangeListener {

    private static final String LOG_TAG = SideBarAdvertFragment.class.getSimpleName();


    SideBarPresenter presenter;

    @BindView(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;

    @BindView(R.id.main_image)
    ImageView mMainImage;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.txtDescription)
    TextView tvDescription;
    @BindView(R.id.fab_share)
    FloatingActionButton mFabShare;
    @BindView(R.id.contentScrollingPanel)
    LinearLayout mContentPanel;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    MaterialStyledDialog dialog;
    ViewHolder holder;

    private ProgressWheel mProgressWheel;
    private ProgressHelper mProgressHelper;

    private int category;
    private int id;

    private Timer initTimer;

    @SuppressLint("ValidFragment")
    @Inject
    public SideBarAdvertFragment(AdvService advService, SidebarDAO sidebarDAO) {
        super();
        presenter = new SideBarPresenter(advService, sidebarDAO);
    }

    public SideBarAdvertFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Logger.v("SideBarAdvertFragment", LOG_TAG" + "ID category: " + this.category + " - ID item: " + this.id);
        final View view = inflater.inflate(R.layout.content_sidebar_item, container, false);
        ButterKnife.bind(SideBarAdvertFragment.this, view);
        presenter.bindView(this);

        mProgressWheel = (ProgressWheel) view.findViewById(R.id.progressWheel);
        mProgressHelper = new ProgressHelper(getContext());
        mProgressHelper.setProgressWheel((ProgressWheel) view.findViewById(R.id.progressWheel));
        mProgressHelper.setRimWidth(mProgressHelper.getRimWidth() + 15);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //((ProgressWheel) view.findViewById(R.id.progressWheel)).setVisibility(View.GONE);
                //onStateChange(State.FINISHED);
                stopLoader();
            }
        }, 2000);


        //mSheetLayout.setFabAnimationEndListener(SideBarAdvertFragment.this);

        presenter.getSidebarItem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<SidebarItem>() {
                    @Override
                    public void onNext(SidebarItem sidebarItem) {
                        super.onNext(sidebarItem);

                        Logger.v(LOG_TAG, "Has a sidebar");

                        Logger.i(LOG_TAG, "Type - " + sidebarItem.getType());
                        Logger.i(LOG_TAG, "Title - " + sidebarItem.getTitle());

                        /*AdvType type = (!TextUtils.isEmpty(sidebarItem.getType())) ? AdvType.valueOf(sidebarItem.getType().toUpperCase()) : AdvType.DEFAULT;
                        if (type.equals(AdvType.HTML)) {
                            viewSwitcher.showNext();

                            WebView mWebView = (WebView) view.findViewById(R.id.webview);
                            WebSettings webSettings = mWebView.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                            webSettings.setAppCachePath(getContext().getCacheDir().getAbsolutePath() );
                            webSettings.setAppCacheEnabled(true);
                            webSettings.setDomStorageEnabled(true);
                            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                            webSettings.setUseWideViewPort(true);
                            webSettings.setEnableSmoothTransition(true);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                mWebView.setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_BOUND, true);
                            }
                            //myWebView.loadUrl("https://globoesporte.globo.com/futebol/brasileirao-serie-a/");
                            mWebView.loadUrl("https://esporte.uol.com.br/futebol/campeonatos/brasileirao/");
                        } else {
                            assembleTemplateV2(sidebarItem);
                        }*/
                        assembleTemplateV2(sidebarItem);
                        stopLoader();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.v(LOG_TAG, "finished seeSidebar");
                    }
                });

        //mSheetLayout.setFabAnimationEndListener(SideBarAdvertFragment.this);

        return view;
    }

    private int currentState = 0;
    private void stopLoader() {
        if (currentState == State.FINISHED)
            return;

        onStateChange(State.FINISHED);

        if (mProgressWheel != null)
            mProgressWheel.setVisibility(View.GONE);

        if (initTimer != null)
            initTimer.cancel();
    }

    @Override
    public void onStateChange(int state) {
        currentState = state;

        switch (state) {
            case State.NOT_STARTED:
                //Logger.v(LOG_TAG", "State: NOT_STARTED");
                break;
            case State.STROKE_STARTED:
                //Logger.v(LOG_TAG", "State: STROKE_STARTED");
                break;
            case State.FILL_STARTED:
                //Logger.v(LOG_TAG", "State: FILL_STARTED");
                break;
            case State.FINISHED:
                //Logger.v(LOG_TAG", "State: FINISHED");
                viewSwitcher.showNext();
                break;
        }
    }

    public void assembleTemplateV2(SidebarItem sidebarItem) {
        /*
        Logger.v(LOG_TAG, "assembleTemplateV2  " + "\n" +
                " - getTitle: " + sidebarItem.getTitle() + "\n" +
                " - getType: " + sidebarItem.getType() + "\n" +
                " - getUrl: " + sidebarItem.getUrl() + "\n" +
                " - getFlatImages: " + sidebarItem.getFlatImages() + "\n" +
                " - getFlatProperties: " + sidebarItem.getFlatProperties() + "\n" +
                " - getLatitude: " + sidebarItem.getGeoJson().getGeometry().getLatitude());
        */

        // Titulo da pagina
        mToolbar.setTitle(sidebarItem.getTitle());
        //Descricao
        tvDescription.setText(sidebarItem.getDescription());

        // geojson -> propriedades
        if (sidebarItem.getTicket() != null && sidebarItem.getTicket().size() > 0) {
            Logger.v(LOG_TAG, "assembleTemplateV2 - sidebarItem.getTicket() != null");

            // Criar a linha de divisao
            Logger.v(LOG_TAG, "assembleTemplateV2 - newLineDivider");
            newLineDivider();

            TextView tvTitle = newTextViewTitle();
            tvTitle.setText("Ticket");

            for (SidebarTicket ticket : sidebarItem.getTicket()) {
                Logger.v(LOG_TAG, "assembleTemplateV2 - ticket.getTitle(): " + ticket.getTitle());

                // Cria as propriedades
                StringBuilder sb1 = new StringBuilder();
                if (!TextUtils.isEmpty(ticket.getTitle())) {
                    sb1.append(ticket.getTitle());
                    sb1.append("  -  ");
                }
                sb1.append(ticket.getSymbol());
                sb1.append(ticket.getPrice());
                if (!TextUtils.isEmpty(sb1.toString())) {
                    TextView tv = newTextViewProperties(30, 0, 30, 0);
                    tv.setText(sb1.toString());
                }

                if (!TextUtils.isEmpty(ticket.getDescription())) {
                    TextView tvSub = newTextViewPropertiesSub(30, 0, 30, 0);
                    tvSub.setText(ticket.getDescription());
                    tvSub.setBackgroundResource(R.drawable.bg_textview_line_bottom);
                }
            }
        }

        // geojson -> propriedades
        if (sidebarItem.getProperties() != null && sidebarItem.getProperties().size() > 0) {
            Logger.v(LOG_TAG, "assembleTemplateV2 - sidebarItem.getProperties() != null");

            for (SidebarProperties group : sidebarItem.getProperties()) {
                if (group != null) {
                    Logger.v(LOG_TAG, "assembleTemplateV2 - group.getTitle(): " + group.getTitle());

                    // Criar a linha de divisao
                    Logger.v(LOG_TAG, "assembleTemplateV2 - newLineDivider");
                    newLineDivider();

                    // Criar TextView de titulo do grupo
                    if (!group.getTitle().isEmpty()) {
                        TextView tvTitle = newTextViewTitle();
                        tvTitle.setText(group.getTitle());
                    }

                    // Cria as propriedades
                    Logger.v(LOG_TAG, "assembleTemplateV2 - props: group.getProperties()");
                    for (SidebarPropertiesItem props : group.getProperties()) {
                        TextView tv = newTextViewProperties(30, 0, 30, 0);
                        tv.setBackgroundResource(R.drawable.bg_textview_line_bottom);
                        tv.setText(props.getContent());
                        if (props.getIcon() != null && !props.getIcon().equals("")) {
                            //handle icon
                        }
                    }
                }
            }
        }

        //pagerAdapter = new AdvPagerAdapter(sidebarItem, this.getActivity());
        //viewpager.setAdapter(pagerAdapter);

        GlideLoaderUtil.loadImageView(this.getActivity(), sidebarItem.getMainImage(), this.mMainImage);

        // Adiciona o endereco ao container
        newLineDivider();
        TextView addresTxt = newTextViewTitle();
        addresTxt.setText(R.string.sidebar_title_address);
        if (sidebarItem.getGeoJson() != null && sidebarItem.getGeoJson().getProperties() != null)
            newTextViewProperties(30, 0, 30, 0).setText(sidebarItem.getGeoJson().getProperties().getAddress());

        // Adiciona o QRCode
        int qrSize = 300;
        Bitmap qr = getQrcode(sidebarItem, qrSize);
        if (qr != null) {
            newTextViewProperties(30, 20, 30, 0).setText(R.string.sidebar_title_location_qrcode);

            ImageView imgQrcode = newImageView(qr, 300);
        }
    }

    @Override
    public void showSharePopUpBox() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.share_selector_dialog_box_layout, null);
        holder = new ViewHolder(view, this);
        view.setTag(holder);
        dialog = DialogUtil.showCustomMaterialDialog(getActivity(), view);
        dialog.show();
    }

    @OnClick(R.id.fab_share)
    public void onShareButtonTapped() {
        presenter.shareButtonAction(category, id);
    }

    @Override
    public void delegateEmailTouchEvent() {
        presenter.shareViaEmailAction(category, id);
    }

    @Override
    public void delegateSendMailEvent() {
        presenter.sendEmailAction(holder.getRecipients(), category, id);
    }


    @Override
    public void updateDialogBoxHeader(@StringRes int titleResId, @StringRes int descriptionResId) {
        dialog.setTitle(getString(titleResId));
    }

    @Override
    public LinearLayout getmContentPanel() {
        return mContentPanel;
    }

    @Override
    public void showStatusMessage(boolean success, String message) {
        delegateDismissShareDialogue();

        String title = (success) ? getString(R.string.msg_congratulations) : getString(R.string.msg_ops);

        MaterialStyledDialog dialog = DialogUtil.showMaterialDialogMessage(getActivity(), message, title, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                /*if (mSheetLayout != null && mSheetLayout.isFabExpanded()) {

                }*/
                dialog.dismiss();
            }
        });
    }


    @Override
    public void showSpinner(boolean show) {
        if (holder != null) {
            if (show) {
                holder.mProgressbarPanel.setVisibility(View.VISIBLE);
            } else {
                holder.mProgressbarPanel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showShareButtons(boolean show) {
        if (holder != null) {
            if (show) {
                holder.mShareAdvertPanel.setVisibility(View.VISIBLE);
            } else {
                holder.mShareAdvertPanel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showEmailSharePanel(boolean show) {
        if (holder != null) {
            if (show) {
                holder.mEmailPanel.setVisibility(View.VISIBLE);
            } else {
                holder.mEmailPanel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void delegateDismissShareDialogue() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fab_share:
                //Logger.v(LOG_TAG, "fab_share");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.v(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.v(LOG_TAG, "onDestroy");
    }

    // Criar a linha de divisao
    private View newLineDivider() {
        View line = new View(mContentPanel.getContext());
        LinearLayout.LayoutParams lpLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        lpLine.setMargins(0, 20, 0, 10);
        line.setLayoutParams(lpLine);
        line.setBackgroundResource(R.color.white_opaque_50);
        mContentPanel.addView(line);

        return line;
    }

    // Criar TextView de titulo do grupo
    private TextView newTextViewTitle() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30, 10, 30, 10);

        TextView tvTitle = new TextView(mContentPanel.getContext());
        tvTitle.setTextSize(22);
        tvTitle.setAllCaps(true);
        tvTitle.setTextColor(getResources().getColor(R.color.main_navigation_button_selected));
        tvTitle.setLayoutParams(lp);
        //tvTitle.setText(str);
        mContentPanel.addView(tvTitle);
        return tvTitle;
    }

    // Cria as propriedades
    private TextView newTextViewProperties(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(left, top, right, bottom);

        TextView tv = new TextView(mContentPanel.getContext());
        tv.setTextSize(18);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setLayoutParams(lp);
        //tv.setText(str);
        mContentPanel.addView(tv);
        return tv;
    }

    // Cria sub propriedades
    private TextView newTextViewPropertiesSub(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(left, top, right, bottom);

        TextView tv = new TextView(mContentPanel.getContext());
        tv.setTextSize(14);
        tv.setTextColor(getResources().getColor(R.color.half_black));
        tv.setLayoutParams(lp);
        //tv.setText(str);
        mContentPanel.addView(tv);
        return tv;
    }


    private ImageView newImageView(Bitmap image) {
        return this.newImageView(image, 100);
    }
    private ImageView newImageView(Bitmap image, int height) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(height, height);
        lp.setMargins(0, 20, 0, 50);

        ImageView imgView = new ImageView(mContentPanel.getContext());
        imgView.setImageBitmap(image);
        imgView.setLayoutParams(lp);
        //imgView.setMinimumHeight(height);
        mContentPanel.addView(imgView);

        return imgView;
    }

    /*
    private Bitmap getQrcodeVCard(SidebarItem item) {
        // encode contact data as vcard using defaults
        VCard qr = new VCard("John Doe")
                .setEmail("john.doe@example.org")
                .setAddress("John Doe Street 1, 5678 Doestown")
                .setTitle("Mister")
                .setCompany("John Doe Inc.")
                .setPhoneNumber("1234")
                .setWebsite("www.example.org");
        Bitmap qrcodeBmp = QRCode.from(qr).bitmap();

        return qrcodeBmp;
    }
    private Bitmap getQrcodeMeCard(SidebarItem item) {
        // encode MeCard data
        MeCard qr = new MeCard("John Doe");
        qr.setEmail("john.doe@example.org");
        qr.setAddress("John Doe Street 1, 5678 Doestown");
        qr.setTelephone("1234");
        Bitmap qrcodeBmp = QRCode.from(qr).bitmap();

        return qrcodeBmp;
    }*/
    private Bitmap getQrcode(SidebarItem item, int qrSize) {
        QRCode qr = null;

        if (!TextUtils.isEmpty(item.getQrcode())) {
            qr = QRCode.from(item.getQrcode());

        } else if (!TextUtils.isEmpty(item.getUrl())) {
            qr = QRCode.from(item.getUrl());

        } else if( item.getGeoJson() != null && item.getGeoJson().getGeometry() != null ) {
            String geoStr = "geo:" + item.getGeoJson().getGeometry().getLatitude() + "," + item.getGeoJson().getGeometry().getLongitude();
            GeoInfo geoInfo = new GeoInfo();
            geoInfo.parseSchema(geoStr);
            qr = QRCode.from(geoInfo);
        }
        if (qr != null)
            return qr.withSize(qrSize, qrSize).bitmap();

        return null;
    }

    @Override
    public void onFabAnimationEnd() {
        //Toast.makeText(getActivity(), "FabAnimationEnd", Toast.LENGTH_SHORT);
    }

    public static SideBarAdvertFragment newInstance() {
        return new SideBarAdvertFragment();
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    static class ViewHolder {

        @BindView(R.id.bt_share_email)
        ImageButton mEmailShareButton;
        @BindView(R.id.bt_share_facebook)
        ImageButton mFacebookShareButton;
        @BindView(R.id.bt_share_whatsapp)
        ImageButton whatsappShareButton;
        @BindView(R.id.bt_share_twitter)
        ImageButton mTwitterShareButton;
        @BindView(R.id.bt_share_gplus)
        ImageButton mGplusShareButton;

        @BindView(R.id.email_share_panel)
        View mEmailPanel;
        @BindView(R.id.share_advert_panel)
        View mShareAdvertPanel;

        @BindView(R.id.recipient_edittext)
        EditText mRecipientEditText;
        @BindView(R.id.send_email_button_layout)
        Button mSendEmailButtonLayout;

        @BindView(R.id.advert_share_upload_spinner_panel)
        RelativeLayout mProgressbarPanel;
        @BindView(R.id.advert_share_upload_progressbar)
        ProgressBar mProgressBar;

        SideBarAdvertViewInterface parent;

        ViewHolder(View view, Fragment parent) {
            ButterKnife.bind(this, view);
            this.parent = (SideBarAdvertViewInterface) parent;

            // Set Icon Button Email
            mEmailShareButton.setImageResource(R.drawable.ic_email_white_36dp);
        }

        @OnClick((R.id.bt_share_email))
        public void onEmailButtonTapped() {
            parent.delegateEmailTouchEvent();
            //Logger.v("ADV", "AdvNavigationActivity:onEmailButtonTapped()");
        }

        @OnClick(R.id.bt_share_facebook)
        public void onFacebookButtonTapped() {
//            parent.delegateFacebookTouchEvent();
            //Logger.v("ADV", "AdvNavigationActivity:onFacebookButtonTapped()");
        }

        @OnClick(R.id.bt_share_whatsapp)
        public void onWhatsappButtonTapped() {
//            parent.delegateWhatsappTouchEvent();
            //Logger.v("ADV", "AdvNavigationActivity:onWhatsappButtonTapped()");
        }

        @OnClick(R.id.bt_share_twitter)
        public void onTwitterButtonTapped() {
//            parent.delegateTwitterTouchEvent();
            //Logger.v("ADV", "AdvNavigationActivity:onTwitterButtonTapped()");
        }

        @OnClick(R.id.bt_share_gplus)
        public void onGplusButtonTapped() {
//            parent.delegateGplusTouchEvent();
            //Logger.v("ADV", "AdvNavigationActivity:onGplusButtonTapped()");
        }

        @OnClick(R.id.send_email_button_layout)
        public void onSendMailButtontapped() {
            parent.delegateSendMailEvent();
            //Logger.v("ADV", "AdvNavigationActivity:onGplusButtonTapped()");
        }

        public String getRecipients() {
            return mRecipientEditText.getText().toString();
        }
    }
}

