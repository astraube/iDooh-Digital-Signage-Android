package br.com.i9algo.taxiadv.v2.presenters;


import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.v2.helpers.AnalyticsTrackerProvider;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import br.com.i9algo.taxiadv.v2.network.taxiadv.AdvService;
import br.com.i9algo.taxiadv.v2.storage.SidebarDAO;
import br.com.i9algo.taxiadv.v2.views.slideshow.SideBarAdvertViewInterface;
import rx.Observable;

public class SideBarPresenter extends BasePresenter<SideBarAdvertViewInterface> {

    public SideBarPresenter(AdvService advService, SidebarDAO sidebarDAO) {
        super();
        this.advService = advService;
        this.sidebarDAO = sidebarDAO;
    }

    AdvService advService;

    SidebarDAO sidebarDAO;

    public void shareButtonAction(int category, int id) {
        if (click()) {
            AnalyticsTrackerProvider.sendShareTap(view.getContext(), category, id);
            view.showSharePopUpBox();
        }
    }

    public void shareViaEmailAction(int category, int id) {
        if (click()) {
            AnalyticsTrackerProvider.sendShareMethodTap(view.getContext(), category, id, "email");
            view.showShareButtons(false);
            view.updateDialogBoxHeader(R.string.share_input_title, R.string.share_input_emails);
            view.showEmailSharePanel(true);
        }
    }

    public void sendEmailAction(String recipients, int category, int id) {
        if (click()) {
            /*AnalyticsTrackerProvider.sendShareConfirmTap(view.getContext(), category, id, "email");
            view.showEmailSharePanel(false);
            view.showShareButtons(false);
            view.showSpinner(true);
            List<Bitmap> bmps = BitmapUtils.toBitmapList(view.getViewPager(), view.getmContentPanel());
            // Juntar Bitmaps
            Bitmap bitSave = BitmapUtils.combineBitmaps(LinearLayout.VERTICAL, bmps.get(0), bmps.get(1));

            Map<String, String> params = new HashMap<String, String>();
            //TODO validar emails
            params.put("emails", recipients);
            //TODO desabilitado pq nao funfa
            this.callback(true);
            //new UploadTask(params, this).execute(bitSave);
            */
            // TODO provisorio, ate arrumar a parte de cima
            shareCallback(true);
        }
    }


    public void callback(Boolean uploadWasSucessfull) {
        view.showShareButtons(false);
        view.showEmailSharePanel(false);
        view.showSpinner(false);

        String msg = (uploadWasSucessfull) ?
                view.getContext().getString(R.string.reserve_msg_send_success) :
                view.getContext().getString(R.string.reserve_msg_send_error);

        view.showStatusMessage(uploadWasSucessfull, msg);
    }

    public void shareCallback(Boolean uploadWasSucessfull) {
        view.showShareButtons(false);
        view.showEmailSharePanel(false);
        view.showSpinner(false);

        String msg = (uploadWasSucessfull) ?
                view.getContext().getString(R.string.share_msg_send_success) :
                view.getContext().getString(R.string.share_msg_send_error);

        view.showStatusMessage(uploadWasSucessfull, msg);
    }

    // database stuff
    public Observable<SidebarItem> getSidebarItem(int id) {
        return sidebarDAO.getSidebarById(id);
    }
}
