package br.com.i9algo.taxiadv.v2.utils.time;

import java.util.Timer;
import java.util.TimerTask;

/**
 * long timeTimer = TimeUnit.MINUTES.toMillis(2);
 * idleTimer = new IdleTimer(TEMPO_MEDIO_NOVO_PASSAGEIRO, new IIdleCallback() {
 *      @Override
 *      public void inactivityDetected() {
 *          Log.d(LOG_TAG, "-----> finaly");
 *      }
 * });
 * idleTimer.startIdleTimer();
 *
 *      @Override
 *     public void onResume() {
 *         super.onResume();
 *         idleTimer.restartIdleTimer();
 *     }
 *
 *     @Override
 *     public void onStop() {
 *         super.onStop();
 *         idleTimer.stopIdleTimer();
 *     }
 */
public class IdleTimer
{
    private Boolean isTimerRunning;
    private IIdleCallback idleCallback;
    private long maxIdleTime;
    private Timer timer;

    public IdleTimer(long maxInactivityTime, IIdleCallback callback)
    {
        maxIdleTime = maxInactivityTime;
        idleCallback = callback;
    }

    /*
     * creates new timer with idleTimer params and schedules a task
     */
    public void startIdleTimer()
    {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                idleCallback.inactivityDetected();
            }
        }, maxIdleTime);
        isTimerRunning = true;
    }

    /*
     * schedules new idle timer, call this to reset timer
     */
    public void restartIdleTimer()
    {
        stopIdleTimer();
        startIdleTimer();
    }

    /*
     * stops idle timer, canceling all scheduled tasks in it
     */
    public void stopIdleTimer()
    {
        timer.cancel();
        isTimerRunning = false;
    }

    /*
     * check current state of timer
     * @return boolean isTimerRunning
     */
    public boolean checkIsTimerRunning()
    {
        return isTimerRunning;
    }
}