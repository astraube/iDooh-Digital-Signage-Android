package br.com.i9algo.taxiadv.v2.views.slideshow;

import android.widget.LinearLayout;

import androidx.annotation.StringRes;
import androidx.viewpager.widget.ViewPager;
import br.com.i9algo.taxiadv.v2.views.BaseViewInterface;

public interface SideBarAdvertViewInterface extends BaseViewInterface {

    void showSharePopUpBox();

    void delegateEmailTouchEvent();

    void delegateSendMailEvent();

    void updateDialogBoxHeader(@StringRes int titleResId, @StringRes int descriptionResId);

    LinearLayout getmContentPanel();

    void showStatusMessage(boolean success, String message);

    void showSpinner(boolean show);

    void showShareButtons(boolean show);

    void showEmailSharePanel(boolean show);

    void delegateDismissShareDialogue();

    //void showReservePanel();

    //String getReserveName();

    //String getReservePhone();

    //String getReserveDate();
}
