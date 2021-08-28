package br.com.i9algo.taxiadv.v2.views.slideshow.maintenance;

import android.os.CountDownTimer;

import java.util.concurrent.TimeUnit;

import br.com.i9algo.taxiadv.v2.presenters.BasePresenter;
import br.com.i9algo.taxiadv.v2.utils.LightUtil;

public class MaintenancePresenter extends BasePresenter<MaintenanceFragment> {

    //Declare timer
    CountDownTimer cTimer = null;

    float soundLevel;
    float brightnessLevel;

    //start timer function
    void startTimer(int i) {
        cTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(i), 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                //SoundUtil.setSoundLevel(view.getActivity(), (int) soundLevel);
                LightUtil.setLightLevel(view.getActivity(), (int) brightnessLevel);
            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    public void sleep(int i) {
        brightnessLevel = LightUtil.getLightLevel(view.getActivity());
        LightUtil.setLightOff(view.getActivity());

        //soundLevel = SoundUtil.getSoundLevel(view.getActivity());
        //SoundUtil.setSoundOff(view.getActivity());

        startTimer(i);

    }

    private void awake(int i) {
        LightUtil.setLightOff(view.getActivity());
        //SoundUtil.setSoundOff(view.getActivity());
        startTimer(i);
    }
}
