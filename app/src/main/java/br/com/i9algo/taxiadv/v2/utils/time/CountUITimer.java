package br.com.i9algo.taxiadv.v2.utils.time;

import android.os.Handler;
import android.util.Log;

/**
 * 
 * @author andre
 * @usage
 * 	private Runnable runMethod = new Runnable()
    {
        public void run()
        {
              // do something
        }
    };

    CountUITimer timer = new CountUITimer(new Handler(), runMethod, timeoutSeconds*1000);

    // Iniciar Timer
    timer.start();

    // Parar Timer
    timer.stop();
        	
 */
public class CountUITimer
{
    private Handler handler;
    private Runnable runMethod;
    private long intervalMs;
    private boolean enabled = false;
    private boolean repeat = false;

    public CountUITimer(Handler handler, Runnable runMethod, long intervalMs)  {
        this.handler = handler;
        this.runMethod = runMethod;
        this.intervalMs = intervalMs;
    }

    public CountUITimer(Handler handler, Runnable runMethod, long intervalMs, boolean repeat)  {
        this(handler, runMethod, intervalMs);
        this.repeat = repeat;
    }

    public void start()  {
        if (intervalMs < 1)  {
            Log.e("timer start", "Invalid interval:" + intervalMs);
            return;
        }
        stop();
        enabled = true;
        handler.postDelayed(timer_tick, intervalMs);
    }

    public void stop()  {
        enabled = false;
        handler.removeCallbacks(runMethod);
        handler.removeCallbacks(timer_tick);
    }

    public void reset()  {
        stop();
        start();
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    private Runnable timer_tick = new Runnable()
    {
        public void run()
        {
            handler.post(runMethod);

            if (!repeat)  {
                enabled = false;
                return;
            }
            handler.postDelayed(timer_tick, intervalMs);
        }
    };
}