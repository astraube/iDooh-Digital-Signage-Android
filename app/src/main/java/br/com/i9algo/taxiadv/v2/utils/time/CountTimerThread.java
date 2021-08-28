package br.com.i9algo.taxiadv.v2.utils.time;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Taxi ADV on 21/03/2016.
 *
 * @author andre
 * @usage:

startThread();

public Thread startThread() {

    // Caso seja necessario enviar e receber uma "Message"
    Message msg = Message.obtain();
    msg.what = 10;

    Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;

            if (msg.what == 10)
                Log.i("localHandler", "what: " + what);
        }
    };

    Runnable localComplete = new Runnable()  {
        @Override
        public void run() {
            Log.i("localHandler", "complete!!! ");

            thread.resetTimer();
        }
    };

    // Iniciar Timer
    thread = new CountTimerThread(messageHandler, localComplete, 5000, false, msg);
    //thread = new CountTimerThread(new Handler(), localComplete, 5000, false, msg);
    thread.start();

    return thread;
}

 *
 **/
public class CountTimerThread extends Thread implements Runnable {

    private Handler mHandler = null;
    private Runnable mRunnable = null;
    private Message mMsg = null;
    private long mIntervalMS;
    private boolean enabled = false;
    private boolean mRepeat = false;


    public CountTimerThread(Handler handler, Runnable runnable, long intervalMS) {
        this(handler, runnable, intervalMS, false, null);
    }
    public CountTimerThread(Handler handler, Runnable runnable, long intervalMS, boolean repeat) {
        this(handler, runnable, intervalMS, repeat, null);
    }
    public CountTimerThread(Handler handler, Runnable runnable, long intervalMS, boolean repeat, Message msg) {
        super(new ThreadGroup("iDoohThreadGroup"), runnable);
        mHandler = handler;
        mRunnable = runnable;
        mIntervalMS = intervalMS;
        mRepeat = repeat;
        mMsg = msg;
    }

    public boolean isEnabled()  { return enabled; }
    public boolean isRepeat()  { return mRepeat; }

    public void start(long intervalMS) {
        mIntervalMS = intervalMS;
        start();
    }

    @Override
    public void start() {
        enabled = true;
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this, mIntervalMS);
    }

    public void stopTimer()  {
        enabled = false;
        mHandler.removeCallbacks(this);
    }

    public void resetTimer() {
        this.finalize();
        this.stopTimer();
        this.start();
    }

    public void finalize() {
        try {
            enabled = true;
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Executar Runnable de acao
        if (mRunnable != null)
            mRunnable.run();

        // Enviar Messege de acao
        if (mMsg != null)
            mHandler.dispatchMessage(mMsg);

        if (mRepeat)  {
            mHandler.postDelayed(this, mIntervalMS);
        } else {
            enabled = false;
        }
    }
}
