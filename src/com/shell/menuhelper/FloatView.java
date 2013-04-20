
package com.shell.menuhelper;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.shell.menuhelper.api.MyApplication;

public class FloatView extends ImageView {

    private static final boolean DEBUG = true;
    private static final String TAG = "FloatView";

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams = null;
    private int mStatusBarHeight;
    private int mImageId = 0;
    private int mImageDownId = 0;
    private boolean mIsDown = false;
    // private boolean mIsMove = false;
    private int mMoveTimes = 0;
    private long mDownTime = 0L;
    private long mUpTime = 0L;

    private int mScreenWidth;
    private int mScreenHeight;
    private float mRawX = 0.0f;
    private float mRawY = 0.0f;
    private float mTouchX = 0.0f;
    private float mTouchY = 0.0f;
    private float mMoveX = 0.0f;
    private float mMoveY = 0.0f;

    private View.OnClickListener mOnClickListener;
    private Handler mHandler = new Handler();

    public FloatView(Context context) {
        this(context, 0);
    }

    public FloatView(Context context, int statusBarHeight) {
        super(context);
        mWindowManager = ((MyApplication) context).getWindowManager();
        this.mStatusBarHeight = Math.max(0, statusBarHeight);
        this.mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public WindowManager.LayoutParams getmWmParams() {
        return mWmParams;
    }

    public void setmWmParams(WindowManager.LayoutParams mWmParams) {
        this.mWmParams = mWmParams;
    }

    /**
     * <p>
     * Title: updateMenuView
     * </p>
     * <p>
     * Description: 更新MenuView的位置
     * </p>
     *
     * @param x
     * @param y
     */
    public void updateViewPosition(int x, int y)
    {
        mWmParams.x = x;
        mWmParams.y = y;
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub

        mRawX = event.getRawX();
        mRawY = event.getRawY();

        final int action = event.getAction();
        changeRes(action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (DEBUG)
                    Log.i(TAG, "mMenuView is Down");
                mIsDown = true;
                mDownTime = System.currentTimeMillis();

                mTouchX = event.getX();
                mTouchY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (DEBUG)
                    Log.i(TAG, "mMenuView is UP");
                mUpTime = System.currentTimeMillis();
                onUpEvent();
                break;
            case MotionEvent.ACTION_MOVE:
                if (DEBUG)
                    Log.i(TAG, "mMenuView is MOVE");
                // mIsMove = true;
                mMoveTimes++;

                mMoveX = mRawX - 65;
                mMoveY = mRawY - 65;
                if (mRawX < 65) {
                    mMoveX = 0;
                }
                if (mRawX > mScreenWidth - 65) {
                    mMoveX = mScreenWidth - 129;
                }
                if (mRawY < mStatusBarHeight + 65) {
                    mMoveY = mStatusBarHeight;
                }
                if (mRawY > mScreenHeight - 65) {
                    mMoveY = mScreenHeight - 129;
                }

                updateViewPosition((int) mMoveX, (int) mMoveY);

                break;
            case MotionEvent.ACTION_CANCEL:
                if (DEBUG)
                    Log.i(TAG, "mMenuView is CANCEL");
                mUpTime = System.currentTimeMillis();
                onUpEvent();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void changeRes(int action) {
        // TODO Auto-generated method stub
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                this.setImageResource(mImageDownId);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setImageResource(mImageId);
                break;
            default:
                break;
        }
    }

    public void resetAlpha()
    {
        this.setAlpha(255);
        invalidate();
    }

    public void setResId(int upId, int downId)
    {
        this.mImageId = upId;
        this.mImageDownId = downId;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        // TODO Auto-generated method stub
        super.setOnClickListener(l);
        mOnClickListener = l;
    }

    private void onUpEvent() {
        long timeSpace = mUpTime - mDownTime;
        if (this.mIsDown && timeSpace <= 800 && this.mMoveTimes <= 3) {
            mOnClickListener.onClick(FloatView.this);
        }
        this.mIsDown = false;
        this.mDownTime = 0;
        this.mUpTime = 0;
        this.mMoveTimes = 0;
        // this.mIsMove = false;
    }
}
