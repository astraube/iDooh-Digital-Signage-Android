package br.com.i9algo.taxiadv.v2.presenters;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import br.com.i9algo.taxiadv.v2.views.BaseViewInterface;

public abstract class BasePresenter<V extends BaseViewInterface> {

    protected V view;

    protected long mLastClickTime;
    protected String mLastClickModel;
    protected int mLastClickModelId;

    public BasePresenter() {
        this.resetModel();
    }


    public void resetModel() {
        mLastClickModel = "MODEL";
        mLastClickModelId = -2;
    }

    public boolean click() {
        return this.click(null, -1);
    }
    public boolean click(String model, int modelId) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 400) {
            return false;
        }
        if (mLastClickModel.equals(model) && mLastClickModelId == modelId) {
            return false;
        }
        if (model != null && modelId > -1) {
            mLastClickModel = model;
            mLastClickModelId = modelId;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    public void bindView(@NonNull V view) {
        this.view = view;
    }

    public void unbindView() {
        this.view = null;
    }

}
