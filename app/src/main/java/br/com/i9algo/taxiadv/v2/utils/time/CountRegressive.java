package br.com.i9algo.taxiadv.v2.utils.time;

import android.os.CountDownTimer;
import android.os.Handler;

/**
 * CountRegressive countDownTimer = new CountRegressive(new Handler(), tickMethod, finishMethod, startTime, interval);
 * 
 * 
 * countDownTimer.cancel();
 * countDownTimer.start();
 * 
 * 
 * private Runnable tickMethod = new Runnable()
    {
        public void run()
        {
        	print("TICK - " + countDownTimer.getTickSecond());
        }
    };
	
	private Runnable finishMethod = new Runnable()
    {
        public void run()
        {
        	print("FIM");
        }
    };
    
 * @author andre
 *
 */
public class CountRegressive extends CountDownTimer {

    private Handler handler;
    private Runnable tickMethod = null;
    private Runnable fishMethod = null;
    private long millisTick;
    
    public CountRegressive(Handler handler, Runnable tickMethod, Runnable fishMethod, long startTime, long interval) {
        super(startTime, interval);
        this.handler = handler;
        this.tickMethod = tickMethod;
        this.fishMethod = fishMethod;
    }
    
    public long getTickMillis() {
    	return (millisTick);
    }
    
    public long getTickSecond() {
    	return (millisTick/1000);
    }
    
    @Override
    public void onFinish() {
    	if (this.handler != null && this.fishMethod != null)
    		this.handler.post(this.fishMethod);
    }

    @Override
    public void onTick(long millisUntilFinished) {
    	this.millisTick = millisUntilFinished;
    	
    	if (this.handler != null && this.tickMethod != null)
    		this.handler.post(this.tickMethod);
    }
}