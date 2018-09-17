package com.example.feng.xgs.main.find.broadcast;

/**
 * Created by Administrator on 2018/8/21.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyChronometer extends AppCompatTextView {

    private static final String TAG = "MyChronometer";

    /**
     * A callback that notifies when the MyChronometer has incremented on its
     * own.
     */
    public interface OnMyChronometerTickListener {

        /**
         * Notification that the MyChronometer has changed.
         */
        void onMyChronometerTick(int time);

    }

    public interface OnMyChronometerTimeListener {

        /**
         * Notification that the MyChronometer has changed.
         */
        void OnMyChronometerTimeListener(int time);

    }

    private OnMyChronometerTimeListener OnMyChronometerTimeListener;

    private long mBase;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private OnMyChronometerTickListener mOnMyChronometerTickListener;
    private long now_time;

    private static final int TICK_WHAT = 2;

    /**
     * Initialize this MyChronometer object. Sets the base to the current time.
     */
    public MyChronometer(Context context) {
        this(context, null, 0);
    }

    /**
     * Initialize with standard view layout information. Sets the base to the
     * current time.
     */
    public MyChronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Initialize with standard view layout information and style. Sets the base
     * to the current time.
     */
    public MyChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    /**
     * Set the time that the count-up timer is in reference to.
     *
     * @param base Use the {@link SystemClock#elapsedRealtime} time base.
     */
    public void setBase(long base) {
        mBase = base;
        updateText(SystemClock.elapsedRealtime());
    }

    /**
     * Sets the listener to be called when the MyChronometer changes.
     *
     * @param listener The listener.
     */
    public void setOnMyChronometerTickListener(OnMyChronometerTickListener listener) {
        mOnMyChronometerTickListener = listener;
    }

    public void setOnMyChronometerTimeListener(OnMyChronometerTimeListener listener) {
        OnMyChronometerTimeListener = listener;
    }

    /**
     * Start counting up. This does not affect the base as set from
     * {@link #setBase}, just the view display.
     * <p/>
     * MyChronometer works by regularly scheduling messages to the handler, even
     * when the Widget is not visible. To make sure resource leaks do not occur,
     * the user should make sure that each start() call has a reciprocal call to
     * {@link #stop}.
     */
    public void start() {
        mStarted = true;
        updateRunning();
    }

    /**
     * Stop counting up. This does not affect the base as set from
     * {@link #setBase}, just the view display.
     * <p/>
     * This stops the messages to the handler, effectively releasing resources
     * that would be held as the MyChronometer is running, via {@link #start}.
     */
    public void stop() {
        mStarted = false;
        updateRunning();
        now_time /= 10;
        if (OnMyChronometerTimeListener != null) {
            OnMyChronometerTimeListener.OnMyChronometerTimeListener((int) now_time);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    private synchronized void updateText(long now) {

        long seconds = now - mBase;
        seconds /= 10;
        now_time = seconds;

        int time_m = (int) (seconds / 100);
        if (mOnMyChronometerTickListener != null) {
            mOnMyChronometerTickListener.onMyChronometerTick(time_m);
        }
        int time_s = (int) (seconds % 100);
        setText(time_m + "");

    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 1000);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), 1000);
            }
        }
    };

    @SuppressLint("NewApi")
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(MyChronometer.class.getName());
    }

    @SuppressLint("NewApi")
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(MyChronometer.class.getName());
    }

}