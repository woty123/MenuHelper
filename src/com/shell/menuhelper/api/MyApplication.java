package com.shell.menuhelper.api;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.shell.menuhelper.R;

public class MyApplication extends Application {

    private static final boolean DEBUG = true;
    private static final String TAG = "MyApplication";

    private Context mContext;
    private WindowManager mWindowManager;
    private static WindowManager.LayoutParams mWmParams = null;
    private ImageView mMenuView;
    private int mScreenHeight = 0;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
//        mWmParams = new WindowManager.LayoutParams();
//        mMenuView = new ImageView(mContext);
//        mMenuView.setImageResource(R.drawable.menu_selector);
//
//        initWmParams();
//
//        addMenuView();
    }

    public WindowManager getWindowManager() {
        if(mWindowManager == null) {
            mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


    /**
     *
     * <p>Title: initWmParams</p>
     * <p>Description: init WindowManager LayoutParams</p>
     */
    public WindowManager.LayoutParams getWmParams() {

        if(mWmParams == null) {
            mWmParams = new WindowManager.LayoutParams();
            // TODO Auto-generated method stub
            mWmParams.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;     // 系统提示类型,重要
            mWmParams.format=PixelFormat.RGBA_8888;
            mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制

            // 以屏幕左上角为原点，设置x、y初始值
            mWmParams.x = 0;
            mWmParams.y = mScreenHeight / 2 - 65;
            // 设置悬浮窗口长宽数据
            mWmParams.width = 129;
            mWmParams.height = 129;
            // 调整悬浮窗口位置
            mWmParams.gravity = Gravity.LEFT | Gravity.TOP;
        }
        return mWmParams;

    }

    private void addMenuView() {
        // TODO Auto-generated method stub

        mMenuView.setAlpha(255);

        mWindowManager.addView(mMenuView, mWmParams);

        mMenuView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(DEBUG) Log.i(TAG, "mMenuView onClick invoked!");
                new Thread(){
                    public void run() {
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
                        return;
                    };
                }.start();
            }
        });

        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if(DEBUG) Log.i(TAG, "mMenuView is Down");
                        break;
                    case MotionEvent.ACTION_UP:
                        if(DEBUG) Log.i(TAG, "mMenuView is UP");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(DEBUG) Log.i(TAG, "mMenuView is MOVE");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if(DEBUG) Log.i(TAG, "mMenuView is CANCEL");
                        break;

                    default:
                        break;
                }
                return false;
            }
        });


        mMenuView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                if(DEBUG) Log.i(TAG, "mMenuView onLongClick invoked!");
                return false;
            }
        });

    }

    /**
    *
    * <p>Title: updateMenuView</p>
    * <p>Description: 更新MenuView的位置</p>
    * @param x
    * @param y
    */
   public void updateMenuView(int x, int y)
   {
     mWmParams.x = x;
     mWmParams.y = y;
     mWindowManager.updateViewLayout(mMenuView, mWmParams);
   }

}
